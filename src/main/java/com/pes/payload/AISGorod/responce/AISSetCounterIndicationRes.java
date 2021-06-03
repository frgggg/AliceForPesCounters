package com.pes.payload.AISGorod.responce;

import lombok.Data;

@Data
public class AISSetCounterIndicationRes extends AISResResult {
    public AISSetCounterIndicationRes(Long code, String message) {
        super(code, message);
    }

    public AISSetCounterIndicationRes() {
    }
}
