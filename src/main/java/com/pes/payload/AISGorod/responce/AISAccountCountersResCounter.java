package com.pes.payload.AISGorod.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AISAccountCountersResCounter {
    private Long id;
    private String counterTypeName;
    private String serviceTypeName;
    private Double val;
}
