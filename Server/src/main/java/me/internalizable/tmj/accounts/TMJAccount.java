package me.internalizable.tmj.accounts;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
public class TMJAccount {

    @Id
    private UUID id;

    private String username;
    private String password;
    private List<MessageHistory> messageHistoryList;

    public TMJAccount() {
        this.messageHistoryList = new ArrayList<>();
    }

}
