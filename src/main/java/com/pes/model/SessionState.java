package com.pes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SessionState {
    private String aliceSessionId;

    private String aisAccountId;
    private Boolean isAisAccountIdConfirmed;

    private String currentCounterId;
    private List<SessionCounter> counters;

    private Boolean isSessionClose;

    public SessionState(String aliceSessionId) {
        this.aliceSessionId = aliceSessionId;
        isSessionClose = false;
        isAisAccountIdConfirmed = false;
        aisAccountId = null;
        counters = null;
        currentCounterId = null;
    }
}
