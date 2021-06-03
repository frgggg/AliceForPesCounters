package com.pes.payload.Alice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliceRequestNlu {
    private List<String> tokens;

    //TODO
    //"entities": [],
    //"intents": {}
}
