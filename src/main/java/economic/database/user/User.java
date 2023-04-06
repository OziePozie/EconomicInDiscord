package economic.database.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "balance")
    public int balance;

    public User(Long id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public User() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance += balance;

    }
}
