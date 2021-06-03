package com.pes.service;

import com.pes.payload.Alice.request.AliceRequest;
import com.pes.payload.Alice.response.AliceResponse;

public interface AliceService {
    AliceResponse say(AliceRequest request);
}
