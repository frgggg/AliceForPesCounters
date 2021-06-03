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
            return procNewSession(request);
        }

        // if user want close
        if(wantClose(request)) {
            return procWantClose(request);
        }

        try {
            SessionStateStep step = SessionStateStep.fromSessionState(session);
            switch (step) {
                case NEED_SET_AIS_ACCOUNT:
                    return procNeedSetAisAccount(request);
                case NEED_CONFIRM_AIS_ACCOUNT:
                    return procNeedConfirmAisAccount(request);
                case NEED_RESTART:
                    return procNeedRestart(request);
                case NEED_CONFIRM_SEND:
                    break;
                case NEED_SET_COUNTER_VAL_ACCOUNT:
                    break;
                case NEED_CONFIRM_COUNTER_VAL_ACCOUNT:
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
    protected AliceResponse procNewSession(AliceRequest req) {
        return procNewSession(req, "");
    }
    // with msg
    protected AliceResponse procNewSession(AliceRequest req, String prefix) {
        sessionStateService.create(req.getAliceRequestSession().getSessionId());
        return new AliceResponse(req, prefix + NEED_SET_AIS_ACCOUNT_ID);
    }

    // set ais account
    public static final String NEED_CONFIRM_AIS_ACCOUNT_ID = "Подтвердить индивидуальный номер %s? Ответьте да или нет.";
    protected AliceResponse procNeedSetAisAccount(AliceRequest req) {
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
    public static final String CONFIRM_AIS_ACCOUNT_RESPONSE = "Укажите показания для счетчика %s с номером %s.";
    protected AliceResponse procNeedConfirmAisAccount(AliceRequest req) {
        SessionState ss = sessionStateService.find(req.getAliceRequestSession().getSessionId());
        if(Pattern.matches(CONFIRM_AIS_ACCOUNT_REG, req.getAliceRequestRequest().getOriginalUtterance())) {

            AISAccountCountersRes aisRes;
            try { aisRes = aisGext01Service.accountCounters(ss.getAisAccountId()); }
            // no connection
            catch (Exception e) { return procNeedClose(req, CONFIRM_AIS_ACCOUNT_CONNECT_ERROR); }

            // error in AIS
            if(aisRes.getResult().getCode() != OK_CODE) { return procNewSession(req, aisRes.getResult().getMessage()); }

            // no counters
            if(aisRes.getResponseObject() == null || aisRes.getResponseObject().size() == 0) { return procNewSession(req, CONFIRM_AIS_ACCOUNT_COUNTERS_ERROR); }

            // set counters
            ss.setCounters(
                    aisRes.getResponseObject().stream()
                            .map(SessionCounter::new)
                            .collect(Collectors.toList())
            );

            //set first counter for set value
            ss.setCurrentCounterId(ss.getCounters().get(0).getCounterId());
            sessionStateService.update(ss);
            return new AliceResponse(req, String.format(CONFIRM_AIS_ACCOUNT_RESPONSE, ss.getCounters().get(0).getServiceTypeName(), ss.getCounters().get(0).getCounterId()));
        } else {
            // back to setting of ais account
            ss.setAisAccountId(null);
            sessionStateService.update(ss);
            return procNewSession(req);
        }
    }

    // restart session
    protected AliceResponse procNeedRestart(AliceRequest req) {
        sessionStateService.delete(req.getAliceRequestSession().getSessionId());
        return procNewSession(req);
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
