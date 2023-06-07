package org.economic.database.userxp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_xp")
public class UserXp {
    @Column(name = "experience")
    public int experience;
    @Id
    @Column(name = "user_id")
    private Long id;

    public UserXp(Long id, int experience) {
        this.id = id;
        this.experience = experience;
    }

    public UserXp() {

    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience += experience;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
