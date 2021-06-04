package com.pes.service.impl;

import com.pes.component.SessionStateStep;
import com.pes.model.SessionCounter;
import com.pes.model.SessionState;
import com.pes.payload.AISGorod.responce.AISAccountCountersRes;
import com.pes.payload.Alice.request.AliceRequest;
import com.pes.payload.Alice.response.AliceResponse;
import com.pes.service.AISGext01Service;
import com.pes.service.AliceService;
import com.pes.service.SessionStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.pes.payload.AISGorod.responce.AISResResult.OK_CODE;

@Slf4j
@Service
public class AliceServiceImpl implements AliceService {
    private AISGext01Service aisGext01Service;
    private SessionStateService sessionStateService;

    @Autowired
    public AliceServiceImpl(@Qualifier("test") AISGext01Service aisGext01Service, SessionStateService sessionStateService) {
        this.aisGext01Service = aisGext01Service;
        this.sessionStateService = sessionStateService;
    }

    @Override
    public AliceResponse say(AliceRequest request) {
        String aliceSessionId = request.getAliceRequestSession().getSessionId();
        SessionState session;

        if(aliceSessionId == null) {
            throw new IllegalArgumentException("Null session id! What are you?");
        }

        // if it's new session
        if((session = sessionStateService.find(aliceSessionId)) == null) {
            return procSayNeedSetAisAccount(request);
        }

        // if user want close
        if(wantClose(request)) {
            return procWantClose(request);
        }

        try {
            SessionStateStep step = SessionStateStep.fromSessionState(session);
            log.info("step = " + step);
            switch (step) {
                case NEED_SET_AIS_ACCOUNT:
                    return procSetAisAccountAndNeedConfirmIt(request);
                case NEED_CONFIRM_AIS_ACCOUNT:
                    return procConfirmAisAccountNeedSetFirsCounterVal(request);
                case NEED_SET_COUNTER_VAL:
                    return procNeedSetCounterVal(request);
                case NEED_CONFIRM_COUNTER_VAL:
                    return procConfirmCounterValGoNextOrSeyConfirmSend(request);
                case NEED_RESTART:
                    return procNeedRestart(request);
                case NEED_CONFIRM_SEND:
                    break;
            }
        } catch (Exception e) {
            return procNeedRestart(request);
        }
        return procNeedRestart(request);
    }

    // start of setting
    public static final String NEED_SET_AIS_ACCOUNT_ID = "Укажите индивидуальный номер или скажите завершить для завершения.";
    // without msg
    protected AliceResponse procSayNeedSetAisAccount(AliceRequest req) {
        return procSayNeedSetAisAccount(req, "");
    }
    // with msg
    protected AliceResponse procSayNeedSetAisAccount(AliceRequest req, String prefix) {
        sessionStateService.create(req.getAliceRequestSession().getSessionId());
        return new AliceResponse(req, prefix + NEED_SET_AIS_ACCOUNT_ID);
    }

    // set ais account
    public static final String NEED_CONFIRM_AIS_ACCOUNT_ID = "Подтвердить индивидуальный номер %s? Ответьте да или нет.";
    protected AliceResponse procSetAisAccountAndNeedConfirmIt(AliceRequest req) {
        StringBuilder aisAccountNumber = new StringBuilder();
        try {
            for(String token: req.getAliceRequestRequest().getAliceRequestNlu().getTokens()) {
                aisAccountNumber.append(token);
            }
            SessionState ss = sessionStateService.find(req.getAliceRequestSession().getSessionId());
            ss.setAisAccountId(aisAccountNumber.toString());
            sessionStateService.update(ss);
        } catch (Exception e) {
            return procNeedRestart(req);
        }
        return new AliceResponse(req, String.format(NEED_CONFIRM_AIS_ACCOUNT_ID, aisAccountNumber.toString()));
    }

    // confirm ais account
    public static final String CONFIRM_AIS_ACCOUNT_REG = "[Дд]а\\.*";
    public static final String CONFIRM_AIS_ACCOUNT_CONNECT_ERROR = "Не могу получить список ваших ссчетчиков.";
    public static final String CONFIRM_AIS_ACCOUNT_COUNTERS_ERROR = "У вас нет приборов.";
    protected AliceResponse procConfirmAisAccountNeedSetFirsCounterVal(AliceRequest req) {
        SessionState ss = sessionStateService.find(req.getAliceRequestSession().getSessionId());
        if(Pattern.matches(CONFIRM_AIS_ACCOUNT_REG, req.getAliceRequestRequest().getOriginalUtterance())) {

            AISAccountCountersRes aisRes;
            try { aisRes = aisGext01Service.accountCounters(ss.getAisAccountId()); }
            // no connection
            catch (Exception e) { return procNeedClose(req, CONFIRM_AIS_ACCOUNT_CONNECT_ERROR); }

            // error in AIS
            if(aisRes.getResult().getCode() != OK_CODE) { return procSayNeedSetAisAccount(req, aisRes.getResult().getMessage()); }

            // no counters
            if(aisRes.getResponseObject() == null || aisRes.getResponseObject().size() == 0) { return procSayNeedSetAisAccount(req, CONFIRM_AIS_ACCOUNT_COUNTERS_ERROR); }

            // set counters
            ss.setCounters(aisRes.getResponseObject().stream().map(SessionCounter::new).collect(Collectors.toList()));

            //set first counter for set value
            ss.setCurrentCounterId(ss.getCounters().get(0).getCounterId());
            ss.setIsAisAccountIdConfirmed(true);
            sessionStateService.update(ss);
            return procSayNeedSetCounterVal(req, "");
        } else {
            // back to setting of ais account
            ss.setAisAccountId(null);
            sessionStateService.update(ss);
            return procSayNeedSetAisAccount(req);
        }
    }

    // proc set counter val
    public static final String COUNTER_VALUE_PARSE_ERROR = "Не могу считать ваши показания!";
    public static final String CRITICAL_ERROR = "Критическая ошибка!";
    public static final String CONFIRM_COUNTER_VAL_RESPONSE = "Подтвердите, что подали для счетчика %s с номером %s следующие показания:%s. Скажите да для подтверждения и нет для повторного ввода.";
    protected AliceResponse procNeedSetCounterVal(AliceRequest req) {
        SessionState ss = sessionStateService.find(req.getAliceRequestSession().getSessionId());
        Double newVal;
        StringBuilder newValStr = new StringBuilder();

        try {
            for(String token: req.getAliceRequestRequest().getAliceRequestNlu().getTokens()) {
                newValStr.append(token);
            }
            newVal = Double.valueOf(newValStr.toString());
        } catch (Exception e) {
            return procSayNeedSetCounterVal(req, COUNTER_VALUE_PARSE_ERROR);
        }

        SessionCounter curCounter = null;
        for(SessionCounter sc: ss.getCounters()) {
            if(sc.getCounterId().equals(ss.getCurrentCounterId())) {
                curCounter = sc;
                sc.setNewCounterValue(newVal);
                sessionStateService.update(ss);
                break;
            }
        }
        if(curCounter == null) {
            return procNeedClose(req, CRITICAL_ERROR);
        }
        return new AliceResponse(req, String.format(CONFIRM_COUNTER_VAL_RESPONSE, curCounter.getServiceTypeName(), curCounter.getCounterId(), newVal.toString()));
    }

    // proc confirm counter val
    public static final String CONFIRM_COUNTER_VAR_REG = "[Дд]а\\.*";
    protected AliceResponse procConfirmCounterValGoNextOrSeyConfirmSend(AliceRequest req) {
        SessionState ss = sessionStateService.find(req.getAliceRequestSession().getSessionId());
        SessionCounter curCounter = null;
        for(SessionCounter sc: ss.getCounters()) {
            if(sc.getCounterId().equals(ss.getCurrentCounterId())) {
                curCounter = sc;
                break;
            }
        }
        if(curCounter == null) {
            return procNeedClose(req, CRITICAL_ERROR);
        }

        if(!Pattern.matches(CONFIRM_COUNTER_VAR_REG, req.getAliceRequestRequest().getOriginalUtterance())) {
            curCounter.setIsNewCounterValueConfirmed(true);
            sessionStateService.update(ss);

            //check for another not set counter
            for(SessionCounter sc: ss.getCounters()) {
                if(sc.getNewCounterValue() == null) {
                    ss.setCurrentCounterId(sc.getCounterId());
                    sessionStateService.update(ss);
                    return procNeedSetCounterVal(req);
                }
            }

            return procNeedRestart(req);


        } else {
            curCounter.setNewCounterValue(null);
            sessionStateService.update(ss);
            return procNeedSetCounterVal(req);
        }
    }

    // send need set counter val
    public static final String SET_COUNTER_VAL_RESPONSE = "Укажите показания для счетчика %s с номером %s.";
    protected AliceResponse procSayNeedSetCounterVal(AliceRequest req, String prefix) {
        SessionState ss = sessionStateService.find(req.getAliceRequestSession().getSessionId());
        SessionCounter curCounter = null;
        for(SessionCounter sc: ss.getCounters()) {
            if(sc.getCounterId().equals(ss.getCurrentCounterId())) {
                curCounter = sc;
                break;
            }
        }
        if(curCounter == null) {
            return procNeedClose(req, CRITICAL_ERROR);
        }
        return new AliceResponse(req, prefix + String.format(SET_COUNTER_VAL_RESPONSE, curCounter.getServiceTypeName(), curCounter.getCounterId()));
    }

    // restart session
    protected AliceResponse procNeedRestart(AliceRequest req) {
        sessionStateService.delete(req.getAliceRequestSession().getSessionId());
        return procSayNeedSetAisAccount(req);
    }

    // want close
    public static final String NEED_CLOSE_WORD_REG = "[Зз]авершить\\.*";
    protected boolean wantClose(AliceRequest req) {
        return Pattern.matches(NEED_CLOSE_WORD_REG, req.getAliceRequestRequest().getOriginalUtterance());
    }
    // close without msg
    public static final String NEED_CLOSE = "До свидания!";
    protected AliceResponse procWantClose(AliceRequest req) {
        return procNeedClose(req, "");
    }
    // close with msg
    protected AliceResponse procNeedClose(AliceRequest req, String msg) {
        return  new AliceResponse(req, msg + NEED_CLOSE, true);
    }

}
