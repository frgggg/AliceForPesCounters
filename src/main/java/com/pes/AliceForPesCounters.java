package com.pes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AliceForPesCounters {
    public static void main(String[] args) {
        SpringApplication.run(AliceForPesCounters.class, args);
    }
}
