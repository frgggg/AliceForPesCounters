package com.pes.payload.AISGorod.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AISReq {
    private String typeRequest;
    private String metodRequest;
    private List<AISReqParam> params;
}
