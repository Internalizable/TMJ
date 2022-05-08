package me.internalizable.tmj.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SocketResponse<T> {

    private int responseCode;
    private T response;

}
