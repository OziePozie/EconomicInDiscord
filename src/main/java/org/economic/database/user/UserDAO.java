package org.economic.database.user;

import java.time.Duration;

public interface UserDAO {
    void topTenBalance();

    void giveToOtherUser();

    void awardUser();

    void awardUsers();

    int getBalance(User user);

    User findByID(long id);

    void addUser(User user);

    void setBalance(User user, int newBalance);

    int getReputation(User user);

    void setReputation(User user);

    long getMessages(User user);

    void setMessages(User user, int count);

    Duration getCooldown(User user);

    void setCooldown(User user);

}
