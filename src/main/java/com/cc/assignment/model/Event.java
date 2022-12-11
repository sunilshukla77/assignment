package com.cc.assignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Event {
    @JsonProperty("id")
    private String id;

    @JsonProperty("state")
    private State state;

    @JsonProperty("type")
    private String type;

    @JsonProperty("host")
    private String host;

    @JsonProperty("timestamp")
    private long timestamp;
}
