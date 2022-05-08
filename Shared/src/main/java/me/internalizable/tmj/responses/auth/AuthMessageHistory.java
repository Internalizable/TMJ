package me.internalizable.tmj.responses.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class AuthMessageHistory {

    private UUID receiverId;
    private String message;
    private long timestamp;

}
