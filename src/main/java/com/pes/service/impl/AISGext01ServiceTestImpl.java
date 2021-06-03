package com.pes.service.impl;

import com.pes.payload.AISGorod.responce.AISAccountCountersRes;
import com.pes.payload.AISGorod.responce.AISAccountCountersResCounter;
import com.pes.payload.AISGorod.responce.AISResResult;
import com.pes.payload.AISGorod.responce.AISSetCounterIndicationRes;
import com.pes.service.AISGext01Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Qualifier("test")
public class AISGext01ServiceTestImpl implements AISGext01Service {
    @Override
    public AISAccountCountersRes accountCounters(String account) {
        List<AISAccountCountersResCounter> counters = new ArrayList<>();
        counters.add(new AISAccountCountersResCounter(1L, "тип 1 счетчика", "сервис 1 счетчика", 1.0));
        counters.add(new AISAccountCountersResCounter(2L, "тип 2 счетчика", "сервис 2 счетчика", 2.0));
        return new AISAccountCountersRes(
                new AISResResult(0L, ""),
                counters
        );
    }

    @Override
    public AISSetCounterIndicationRes setCounterIndication(String account, Double counterId, String val, LocalDateTime valTime) {
        return new AISSetCounterIndicationRes(0L, "");
    }
}
