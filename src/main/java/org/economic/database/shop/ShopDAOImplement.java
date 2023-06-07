package org.economic.database.shop;

import org.economic.database.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class ShopDAOImplement implements ShopDAO {
    SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();

    @Override
    public List<Shop> getListRoles() {
        Session session = sessionFactory.openSession();
        List<Shop> lst = session
                .createQuery("FROM Shop ORDER BY id ASC", Shop.class)
                .getResultList();
        session.close();
        return lst;
    }

    @Override
    public void addRoleToShop(Shop role) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            if (isRoleInShop(role)) {
                session.update(role);
            } else session.persist(role);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void removeRoleToShop(Shop role) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(role);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public Shop getRoleFromShop(Shop shop) {
        return null;
    }

    public boolean isRoleInShop(Shop role) {
        Session session = sessionFactory.openSession();
        List<Shop> lst = session
                .createQuery("FROM Shop", Shop.class)
                .getResultList();

        session.close();

        return lst.contains(role);
    }
}
