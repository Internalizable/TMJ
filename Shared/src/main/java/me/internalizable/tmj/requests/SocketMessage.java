package me.internalizable.tmj.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SocketMessage<T> {
    private T object;
}
