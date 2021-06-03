package com.pes.payload.Alice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pes.payload.Alice.request.AliceRequest;
import com.pes.payload.Alice.request.AliceRequestSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliceResponse {
    @JsonProperty("session")
    private AliceResponseSession aliceResponseSession;

    @JsonProperty("version")
    private String version;

    @JsonProperty("response")
    private AliceResponseResponse aliceResponseResponse;

    public void setAliceResponseSession(AliceRequestSession aliceRequestSession) {
        aliceResponseSession = new AliceResponseSession(
                aliceRequestSession.getSkillId(),
                aliceRequestSession.getUserId(),
                aliceRequestSession.getSessionId(),
                aliceRequestSession.getNewFlag(),
                aliceRequestSession.getMessageId(),
                new AliceResponseUser(aliceRequestSession.getAliceRequestUser())
        );
    }

    public AliceResponse(AliceRequest req) {
        setAliceResponseSession(req.getAliceRequestSession());
        setVersion(req.getVersion());
    }

    public AliceResponse(AliceRequest req, String msg) {
        this(req, msg, false);
    }

    public AliceResponse(AliceRequest req, String msg, Boolean isEnd) {
        this(req);
        setAliceResponseResponse(new AliceResponseResponse(msg, isEnd));
    }
}
