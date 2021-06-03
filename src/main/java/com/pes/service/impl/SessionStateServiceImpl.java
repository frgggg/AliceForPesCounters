package com.pes.service.impl;

import com.pes.model.SessionCounter;
import com.pes.model.SessionState;
import com.pes.payload.AISGorod.responce.AISAccountCountersRes;
import com.pes.service.AISGext01Service;
import com.pes.service.SessionStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.pes.payload.AISGorod.responce.AISResResult.OK_CODE;

@Slf4j
@Service
public class SessionStateServiceImpl implements SessionStateService {
    private HashMap<String, SessionState> tmpRepo = new HashMap<>();
    private AISGext01Service aisGext01Service;

    @Autowired
    public SessionStateServiceImpl(@Qualifier("test") AISGext01Service aisGext01Service) {
        this.aisGext01Service = aisGext01Service;
    }

    @Override
    public SessionState find(String aliceSessionId) {
        return tmpRepo.get(aliceSessionId);
    }

    @Override
    public SessionState create(String aliceSessionId) {
        tmpRepo.put(aliceSessionId, new SessionState(aliceSessionId));
        return tmpRepo.get(aliceSessionId);
    }

    @Override
    public SessionState update(SessionState sessionState) {
        tmpRepo.put(sessionState.getAliceSessionId(), sessionState);
        return tmpRepo.get(sessionState.getAliceSessionId());
    }

    @Override
    public void delete(String aliceSessionId) {
        tmpRepo.remove(aliceSessionId);
    }
}
