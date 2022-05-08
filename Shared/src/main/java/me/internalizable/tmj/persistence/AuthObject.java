package me.internalizable.tmj.persistence;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AuthObject {

    private String username;
    private String password;
    private boolean login;

}
