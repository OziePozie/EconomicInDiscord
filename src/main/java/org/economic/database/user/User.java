package org.economic.database.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class User {

    @Column(name = "balance")
    public int balance;
    @Column(name = "reputation")
    public int reputation;
    @Column(name = "messages")
    public long messages;
    @Column(name = "rep_cooldown")
    public Timestamp reputationCooldown;
    @Id
    @Column(name = "user_id")
    private Long id;


    public User(Long id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;

    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public long getMessages() {
        return messages;
    }

    public void setMessages(long messages) {
        this.messages = messages;
    }

    public Timestamp getReputationCooldown() {
        return reputationCooldown;
    }

    public void setReputationCooldown(Timestamp reputationCooldown) {
        this.reputationCooldown = reputationCooldown;
    }
}
