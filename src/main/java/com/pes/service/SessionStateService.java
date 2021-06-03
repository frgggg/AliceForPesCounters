package com.pes.service;

import com.pes.model.SessionState;

public interface SessionStateService {
    SessionState find(String aliceSessionId);
    SessionState create(String aliceSessionId);
    SessionState update(SessionState sessionState);
    void delete(String aliceSessionId);
}
