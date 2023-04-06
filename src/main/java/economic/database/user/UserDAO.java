package economic.database.user;

import economic.database.user.User;

public interface UserDAO {
    void topTenBalance();
    void giveToOtherUser();
    void awardUser();
    void awardUsers();
    int getBalance(User user);
    User findByID(long id);
    void addUser(User user);
    void setBalance(User user, int newBalance);
}
