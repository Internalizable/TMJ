package me.internalizable.tmj.responses.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class AuthResponse {

    private UUID uuid;
    private List<AuthMessageHistory> authMessageHistoryList;

    public AuthResponse() {
        this.authMessageHistoryList = new ArrayList<>();
    }

}