package com.pes.payload.AISGorod.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AISReqParam {
    private String name;
    private String value;
}
