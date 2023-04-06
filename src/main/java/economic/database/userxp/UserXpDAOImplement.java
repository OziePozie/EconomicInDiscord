package economic.database.userxp;

public interface UserXpDAOImplement {
    void setXp(UserXp userXp, int exp);
    int getXp(UserXp userXp);
    UserXp findById(long id);
    void addUserXp(UserXp userXp);
}
