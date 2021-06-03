package com.pes.component;

import com.pes.model.SessionCounter;
import com.pes.model.SessionState;

public enum SessionStateStep {
    NEED_SET_AIS_ACCOUNT, NEED_CONFIRM_AIS_ACCOUNT,
    NEED_SET_COUNTER_VAL_ACCOUNT, NEED_CONFIRM_COUNTER_VAL_ACCOUNT,
    NEED_CONFIRM_SEND,
    NEED_RESTART;

    public static SessionStateStep fromSessionState(SessionState session) {
        if(session == null) {
            throw new IllegalArgumentException("Null session");
        }
        if(session.getAliceSessionId() == null) {
            throw new IllegalArgumentException("Null session id");
        }

        if(checkForNeedSetAISAccount(session)) {
            return NEED_SET_AIS_ACCOUNT;
        }

        if(checkForNeedConfirmAISAccount(session)) {
            return NEED_CONFIRM_AIS_ACCOUNT;
        }

        if(checkForNeedSetCounterVal(session)) {
            return NEED_SET_COUNTER_VAL_ACCOUNT;
        }

        if(checkForNeedConfirmCounterVal(session)) {
            return NEED_CONFIRM_COUNTER_VAL_ACCOUNT;
        }

        if(checkForNeedConfirmSend(session)) {
            return NEED_CONFIRM_SEND;
        }

        return NEED_RESTART;
    }

    protected static boolean checkForNeedSetAISAccount(SessionState session) {
        return session.getCurrentCounterId() == null && session.getCounters() == null && session.getAisAccountId() == null && !session.getIsAisAccountIdConfirmed();
    }

    protected static boolean checkForNeedConfirmAISAccount(SessionState session) {
        return session.getCurrentCounterId() == null && session.getCounters() == null && session.getAisAccountId() != null && !session.getIsAisAccountIdConfirmed();
    }

    protected static boolean checkForNeedConfirmSend(SessionState session) {
        // AISAcountId is not set
        if(checkForAisAccountIsNotSet(session)) { return false; }
        // empty counters list
        if(checkForCountersNotExists(session)) { return false; }

        for(SessionCounter sc: session.getCounters()) {
            // wrong counter
            if(sc.getCounterId() == null && sc.getNewCounterValue() == null || sc.getCurCounterValue() == null) {
                return false;
            }

            // wrong value or not confirmed
            if(sc.getIsNewCounterValueConfirmed() || sc.getNewCounterValue() > sc.getCurCounterValue()) {
                return false;
            }
        }

        return true;
    }

    protected static boolean checkForNeedSetCounterVal(SessionState session) {
        // AISAcountId is not set
        if(checkForAisAccountIsNotSet(session)) { return false; }
        // empty counters list
        if(checkForCountersNotExists(session)) { return false; }

        for(SessionCounter sc: session.getCounters()) {
            if(sc.getCounterId() == null) {
                return false;
            }

            if(sc.getCounterId().equals(session.getCurrentCounterId())) {
                // check that current counter was not set or confirmed
                if(sc.getIsNewCounterValueConfirmed() || sc.getNewCounterValue() != null) {
                    return false;
                }
            } else {
                // check that not current counter was not set or set
                if(
                        (sc.getIsNewCounterValueConfirmed() && sc.getNewCounterValue() == null) ||
                        (!sc.getIsNewCounterValueConfirmed() && sc.getNewCounterValue() != null)
                ) {
                    return false;
                }
            }
        }

        return true;
    }
    protected static boolean checkForNeedConfirmCounterVal(SessionState session) {
        // AISAcountId is not set
        if(checkForAisAccountIsNotSet(session)) { return false; }
        // empty counters list
        if(checkForCountersNotExists(session)) { return false; }

        for(SessionCounter sc: session.getCounters()) {
            if(sc.getCounterId() == null) {
                return false;
            }

            if(sc.getCounterId().equals(session.getCurrentCounterId())) {
                // check that current counter was set and not confirmed
                if(!sc.getIsNewCounterValueConfirmed() || sc.getNewCounterValue() != null) {
                    return false;
                }
            } else {
                // check that not current counter was not set or set
                if(
                        (sc.getIsNewCounterValueConfirmed() && sc.getNewCounterValue() == null) ||
                        (!sc.getIsNewCounterValueConfirmed() && sc.getNewCounterValue() != null)
                ) {
                    return false;
                }
            }
        }

        return true;
    }

    protected static boolean checkForAisAccountIsNotSet(SessionState session) {
        return session.getAisAccountId() == null || !session.getIsAisAccountIdConfirmed();
    }

    protected static boolean checkForCountersNotExists(SessionState session) {
        return session.getCounters() == null || session.getCounters().size() == 0;
    }
}
