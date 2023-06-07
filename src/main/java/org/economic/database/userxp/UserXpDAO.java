package org.economic.database.userxp;

public interface UserXpDAO {
    void setXp(UserXp userXp, int exp);

    int getXp(UserXp userXp);

    UserXp findById(long id);

    void addUserXp(UserXp userXp);
}
