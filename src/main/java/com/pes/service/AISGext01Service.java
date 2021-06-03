package com.pes.service;

import com.pes.payload.AISGorod.responce.AISAccountCountersRes;
import com.pes.payload.AISGorod.responce.AISSetCounterIndicationRes;

import java.time.LocalDateTime;

public interface AISGext01Service {

    AISAccountCountersRes accountCounters(String account);
    AISSetCounterIndicationRes setCounterIndication(String account, Double counterId, String val, LocalDateTime valTime);

}
