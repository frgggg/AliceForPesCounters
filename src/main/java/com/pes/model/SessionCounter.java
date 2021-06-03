package com.pes.model;


import com.pes.payload.AISGorod.responce.AISAccountCountersResCounter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionCounter {
    private String counterId;

    private Double curCounterValue;
    private String counterTypeName;
    private String serviceTypeName;

    private Double newCounterValue;
    private Boolean isNewCounterValueConfirmed;

    public SessionCounter(AISAccountCountersResCounter rc) {
        this(rc.getId().toString(), rc.getVal(), rc.getCounterTypeName(), rc.getServiceTypeName());
    }

    public SessionCounter(String counterId, Double curCounterValue, String counterTypeName, String serviceTypeName) {
        this.counterId = counterId;
        this.curCounterValue = curCounterValue;
        this.counterTypeName = counterTypeName;
        this.serviceTypeName = serviceTypeName;

        isNewCounterValueConfirmed = false;
        newCounterValue = null;
    }
}
