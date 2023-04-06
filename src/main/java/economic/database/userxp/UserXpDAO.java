package economic.database.userxp;

import economic.database.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class UserXpDAO implements UserXpDAOImplement{
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
}
