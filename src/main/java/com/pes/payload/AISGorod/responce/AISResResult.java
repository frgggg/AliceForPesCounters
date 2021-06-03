package com.pes.payload.AISGorod.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AISResResult {
    public static final long OK_CODE = 0L;
    private Long code;
    private String message;
}
