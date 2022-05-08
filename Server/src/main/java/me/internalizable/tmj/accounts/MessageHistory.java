package me.internalizable.tmj.accounts;

import dev.morphia.annotations.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity

@Builder
@Getter
@Setter
public class MessageHistory {

    private UUID receiverId;
    private String message;
    private long timestamp;

}
