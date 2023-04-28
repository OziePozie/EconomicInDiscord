package org.economic.database.shop;

import java.util.List;

public interface ShopDAO {

    List<Shop> getListRoles();

    void addRoleToShop(Shop role);

    void removeRoleToShop(Shop role);

    Shop getRoleFromShop(Shop shop);


}
