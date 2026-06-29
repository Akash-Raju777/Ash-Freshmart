package com.supermarket.management.repository.impl;

import com.supermarket.management.model.UserAccount;
import com.supermarket.management.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
@Profile("aws & !rds")
public class DynamoUserRepository implements UserRepository {
    private final DynamoDbTable<UserAccount> table;

    public DynamoUserRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("Users", TableSchema.fromBean(UserAccount.class));
    }

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        try {
            UserAccount account = table.getItem(r -> r.key(k -> k.partitionValue(username.toLowerCase().trim())));
            return Optional.ofNullable(account);
        } catch (Exception e) {
            System.err.println("Error reading user from DynamoDB: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        try {
            userAccount.setUsername(userAccount.getUsername().toLowerCase().trim());
            table.putItem(userAccount);
            return userAccount;
        } catch (Exception e) {
            System.err.println("Error saving user to DynamoDB: " + e.getMessage());
            return userAccount;
        }
    }
}
