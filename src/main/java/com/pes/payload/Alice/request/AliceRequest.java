package com.pes.payload.Alice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliceRequest {
    @JsonProperty("session")
    private AliceRequestSession aliceRequestSession;

    @JsonProperty("version")
    private String version;

    @JsonProperty("request")
    private AliceRequestRequest aliceRequestRequest;
}
