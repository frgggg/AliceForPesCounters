package com.pes.payload.AISGorod.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AISAccountCountersRes {
    private AISResResult result;
    private List<AISAccountCountersResCounter> responseObject;
}
