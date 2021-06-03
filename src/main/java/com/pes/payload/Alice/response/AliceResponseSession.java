package com.pes.payload.Alice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliceResponseSession {
    @JsonProperty("skill_id")
    private String skillId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("new")
    private Boolean newFlag;

    @JsonProperty("message_id")
    private Long messageId;

    @JsonProperty("user")
    private AliceResponseUser aliceResponseUser;
}
