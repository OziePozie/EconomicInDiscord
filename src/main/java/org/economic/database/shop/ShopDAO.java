package org.economic.database.shop;

import java.util.List;

public interface ShopDAO {

    public List<Shop> getListRoles();

    public void addRoleToShop(Shop role);

    public void removeRoleToShop(Shop role);

    public Shop getRoleFromShop(Shop shop);


}
