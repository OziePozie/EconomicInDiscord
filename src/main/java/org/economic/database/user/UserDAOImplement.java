package org.economic.database.user;

import org.economic.database.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class UserDAOImplement implements UserDAO {
    SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
    @Override
    public void topTenBalance() {

    }
    public List<User> topTen(String st) {
        Session session = sessionFactory.openSession();
        List<User> lst = session
                .createQuery(String.format("FROM User ORDER BY %s DESC", st), User.class)
                .getResultList();
        session.close();
        return lst;
    }
    public long getRank(User user, String option){
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String st = String.format("SELECT COUNT(*) From User\n" +
                    "WHERE %s > ( SELECT %s as ex FROM User WHERE id = :param1)",option, option);
            return (long) session.createQuery(st).setParameter("param1", user.getId()).list().get(0);
        } catch (Exception e){
            System.out.println(e);
        } finally {
            if (session !=null && session.isOpen()){
                session.close();
            }
        }
        return 1;
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
    public int getReputation(User user) {
        return user.getReputation();
    }

    @Override
    public void setReputation(User user) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            user.setReputation(user.getReputation() + 1);
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

    @Override
    public int getBalance(User user) {
        return user.getBalance();
    }

    @Override
    public User findByID(long id) {
        return sessionFactory.openSession().find(User.class, id);
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
            user.setBalance(user.getBalance() + newBalance);
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

    @Override
    public long getMessages(User user) {
        return user.getMessages();
    }

    @Override
    public void setMessages(User user, int count) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            user.setMessages(user.getMessages() + count);
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

    @Override
    public Duration getCooldown(User user) {
        Instant instant;
        try {
            instant = user.getReputationCooldown().toInstant();
        } catch (NullPointerException e){
            return Duration.ZERO;
        }

        Duration duration = Duration.between(instant, Instant.now());

        if (!duration.isNegative() || duration.isZero()) return Duration.ZERO;

        else return duration;
    }

    @Override
    public void setCooldown(User user) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            user.setReputationCooldown(Timestamp.valueOf(LocalDateTime.now().plusHours(12)));
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
