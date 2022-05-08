package me.internalizable.tmj.persistence;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class MessageObject {

    private String message;
    private UUID senderId;
    private UUID receiverId;
    private boolean read;

}
