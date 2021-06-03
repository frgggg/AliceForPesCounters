package com.pes.service.impl;

import com.pes.client.AISGext01Client;
import com.pes.component.AISReqFabric;
import com.pes.payload.AISGorod.responce.AISAccountCountersRes;
import com.pes.payload.AISGorod.responce.AISSetCounterIndicationRes;
import com.pes.service.AISGext01Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@Qualifier("rel")
public class AISGext01ServiceImpl implements AISGext01Service {
    public static final String WRONG_RESPONSE_STATUS_EXC_MSG = "Error: wrong response status!";

    private AISGext01Client aisGext01Client;

    @Autowired
    public AISGext01ServiceImpl(AISGext01Client aisGext01Client) {
        this.aisGext01Client = aisGext01Client;
    }

    @Override
    public AISAccountCountersRes accountCounters(String account) {
        ResponseEntity<AISAccountCountersRes> resEntity = aisGext01Client.accountCounters(AISReqFabric.accountCountersReq(account));
        if(resEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException(WRONG_RESPONSE_STATUS_EXC_MSG);
        }
        return resEntity.getBody();
    }

    @Override
    public AISSetCounterIndicationRes setCounterIndication(String account, Double counterId, String val, LocalDateTime valTime) {
        ResponseEntity<AISSetCounterIndicationRes> resEntity = aisGext01Client.setCounterIndication(AISReqFabric.setCounterReq(account, counterId, val, valTime));
        if(resEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException(WRONG_RESPONSE_STATUS_EXC_MSG);
        }
        return resEntity.getBody();
    }
}
