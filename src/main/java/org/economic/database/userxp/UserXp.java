package org.economic.database.userxp;

import jakarta.persistence.*;

@Entity
@Table(name = "user_xp")
public class UserXp {
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "experience")
    public int experience;

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
