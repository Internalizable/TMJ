package me.internalizable.tmj.mongo;

import com.eatthepath.uuid.FastUUID;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.experimental.filters.Filters;
import me.internalizable.tmj.accounts.MessageHistory;
import me.internalizable.tmj.accounts.TMJAccount;
import org.bson.UuidRepresentation;

import java.util.UUID;

public class MongoStore {
    private final Datastore datastore;

    public MongoStore() {
        MongoClient client = MongoClients
                .create(MongoClientSettings.builder()
                        .uuidRepresentation(UuidRepresentation.STANDARD)
                        .credential(MongoCredential.createCredential("pickade", "mcl", "3Ynmu4gsjDDz44xe".toCharArray()))
                        .build());

        datastore = Morphia.createDatastore(client, "tmj");
        datastore.getMapper().map(TMJAccount.class, MessageHistory.class);
        datastore.ensureIndexes();
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public TMJAccount createAccount(String username, String password) {
        TMJAccount tmjAccount = getDatastore().find(TMJAccount.class)
                .filter(Filters.eq("username", username))
                .stream().findFirst().orElse(null);

        if(tmjAccount != null) // Username already exists
            return null;

        tmjAccount = TMJAccount.builder().id(UUID.randomUUID())
                .username(username)
                .password(password).build();

        return this.datastore.save(tmjAccount);
    }

    public TMJAccount login(String username, String password) {
        TMJAccount tmjAccount = getDatastore().find(TMJAccount.class)
                .filter(Filters.eq("username", username))
                .stream().findFirst().orElse(null);

        if(tmjAccount != null &&
                tmjAccount.getPassword().equals(password))
            return tmjAccount;

        return null;
    }

}
