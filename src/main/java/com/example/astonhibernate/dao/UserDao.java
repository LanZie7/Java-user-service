package com.example.astonhibernate.dao;

import com.example.astonhibernate.hibernate.HibernateUtil;
import com.example.astonhibernate.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class UserDao {

    private static final Logger logger = LogManager.getLogger(UserDao.class);
    private SessionFactory sessionFactory;

    // Конструктор по умолчанию для Mockito
    public UserDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Конструктор для тестов
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveUser(UserEntity user) {
        Transaction transaction = null;
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.info("Пользователь успешно сохранён: {}", user.getEmail());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при сохранении пользователя: {}", e.getMessage(), e);
        }
    }

    public List<UserEntity> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<UserEntity> users = session.createQuery("FROM UserEntity", UserEntity.class).list();
            logger.info("Получено {} пользователей", users.size());
            return users;
        } catch (Exception e) {
            logger.error("Ошибка при сохранении пользователя: {}", e.getMessage(), e);
            return List.of();
        }
    }

    public UserEntity getUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UserEntity user = session.get(UserEntity.class, id);
            if (user != null) {
                logger.info("Пользователь найден по ID {}: {}", id, user.getEmail());
            } else {
                logger.warn("Пользователь с ID {} не найден", id);
            }
            return user;
        } catch (Exception e) {
            logger.error("Error fetching user by ID {}: {}", id, e.getMessage(), e);
            return null;
        }
    }

    public void updateUser(UserEntity user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            logger.info("Пользователь успешно обновлён: {}", user.getEmail());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при обновлении пользователя: {}", e.getMessage(), e);
        }
    }

    public void deleteUser(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            UserEntity user = session.load(UserEntity.class, id);
            if (user != null) {
                session.remove(user);
                logger.info("Пользователь успешно удалён: ID {}", id);
            } else {
                logger.warn("Пользователь с ID {} не найден для удаления", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка удаления пользователя с ID {}: {}", id, e.getMessage(), e);
        }
    }
}