package org.economic.database.shop;

import jakarta.persistence.*;

@Entity
@Table(name = "shop")
public class Shop {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "price")
    public int price;

    public Shop(Long roleId, int price) {
        this.roleId = roleId;
        this.price = price;
    }

    public Shop(Long id) {
        this.id = id;
    }

    public Shop() {
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
