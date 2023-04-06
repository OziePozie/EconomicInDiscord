package economic.database.user;

import economic.database.HibernateSessionFactoryUtil;
import economic.database.user.User;
import economic.database.user.UserDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class UserDAOImplement implements UserDAO {
    SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
    @Override
    public void topTenBalance() {

    }

    @Override
    public void giveToOtherUser() {

    }

    @Override
    public void awardUser() {

    }

    @Override
    public void awardUsers() {

    }

    @Override
    public int getBalance(User user) {
        return user.getBalance();
    }

    @Override
    public User findByID(long id) {
        return (User) sessionFactory.openSession().get(User.class, id);
    }

    @Override
    public void addUser(User user) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(user);
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
    public void setBalance(User user, int newBalance) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            user.setBalance(newBalance);
            session.update(user);
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
