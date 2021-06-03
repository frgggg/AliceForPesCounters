package com.pes.controller;

import com.pes.payload.Alice.request.AliceRequest;
import com.pes.payload.Alice.response.AliceResponse;
import com.pes.service.AliceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/alice")
public class AliceController {
    private AliceService aliceService;

    @Autowired
    public AliceController(AliceService aliceService) {
        this.aliceService = aliceService;
    }

    @PostMapping("")
    public AliceResponse repeat(@RequestBody AliceRequest req, HttpServletRequest request) {
        log.info("Some say: " + req);
        return aliceService.say(req);
    }
}
