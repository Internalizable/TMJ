package me.internalizable.tmj.persistence;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SocketMessage {
    private AuthObject authObject;
    private MessageObject messageObject;
}
