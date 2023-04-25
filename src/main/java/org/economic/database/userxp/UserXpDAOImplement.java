package org.economic.database.userxp;

import org.economic.database.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class UserXpDAOImplement implements UserXpDAO {
    SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
    @Override
    public void setXp(UserXp userXp, int exp) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            userXp.setExperience(exp);
            session.update(userXp);
            session.getTransaction().commit();
        } catch (Exception e){
            System.out.println(e);
        } finally {
            if (session !=null && session.isOpen()){
                session.close();
            }
        }
    }

    @Override
    public int getXp(UserXp userXp) {
        return userXp.getExperience();
    }

    @Override
    public UserXp findById(long id) {
        return sessionFactory.openSession().get(UserXp.class, id);
    }

    @Override
    public void addUserXp(UserXp userXp) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(userXp);
            session.getTransaction().commit();
        } catch (Exception e){
            System.out.println(e);
        } finally {
            if (session !=null && session.isOpen()){
                session.close();
            }
        }
    }
    public long getRank(UserXp user){
        Session session = null;
        try {
            session = sessionFactory.openSession();
            return (long) session.createQuery("SELECT COUNT(*) From UserXp\n" +
                    "WHERE experience > ( SELECT experience as ex FROM UserXp WHERE id = :param1)").setParameter("param1", user.getId()).list().get(0);
        } catch (Exception e){
            System.out.println(e);
        } finally {
            if (session !=null && session.isOpen()){
                session.close();
            }
        }
        return 1;
    }
}
