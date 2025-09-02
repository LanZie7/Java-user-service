/**Интеграционные тесты для UserDao с Testcontainers
Testcontainers запускает реальную PostgreSQL в Docker контейнере для тестов.*/

package com.example.astonhibernate.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import com.example.astonhibernate.entity.UserEntity;
import com.example.astonhibernate.hibernate.HibernateUtil;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers; // библиотека для запуска Docker-контейнеров в тестах
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers // Включение поддержки Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Порядок выполнения тестов
class UserDaoIntegrationTest {

    // Запуск контейнера PostgreSQL
    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("testuserdb")
                    .withUsername("admintest")
                    .withPassword("admintest");

    private static UserDao userDao;
    private static SessionFactory testSessionFactory;

    @BeforeAll
    static void beforeAll() {
        // Ждём, пока контейнер будет готов
        postgres.start();

        // Создание конфигурации Hibernate для Testcontainers
        Configuration configuration = new org.hibernate.cfg.Configuration();

        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop"); // создает и удаляет таблицы

        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");

        // Настройка Hibernate на использование тестовой БД
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());

        configuration.setProperties(properties);
        configuration.addAnnotatedClass(UserEntity.class);

//        userDao = new UserDao();
        testSessionFactory = configuration.buildSessionFactory();
        userDao = new UserDao(testSessionFactory);

        System.out.println("Test database URL: " + postgres.getJdbcUrl());
    }

    @AfterAll
    static void afterAll() {
//        // Закрытие сессии Hibernate
//        HibernateUtil.shutdown();

        if (testSessionFactory != null && !testSessionFactory.isClosed()) {
            testSessionFactory.close();
        }
        if (postgres != null && postgres.isRunning()) {
            postgres.stop();
        }
    }

    @Test
    @Order(1)
    void testSaveUser() {
        // Given - подготовка данных
        UserEntity user = new UserEntity("Светлана Бочкова", "sveta@gmail.com", 31);

        // When - выполнение действий
        userDao.saveUser(user);

        // Then - проверка результата
        assertNotNull(user.getId(), "id должен присваивается после сохранения пользователя");
        assertTrue(user.getId() > 0, "ID должен быть положительным числом");
    }

    @Test
    @Order(2)
    void testUserById() {
        // When
        UserEntity findUser = userDao.getUserById(1L);

        // Then
        assertNotNull(findUser, "Пользователь должен быть найден в базе данных");
        assertEquals("Светлана Бочкова", findUser.getName());
        assertEquals("sveta@gmail.com", findUser.getEmail());
        assertEquals(31, findUser.getAge());
    }

    @Test
    @Order(3)
    void testGetAllUsers() {
        // When
        List<UserEntity> users = userDao.getAllUsers();

        // Then
        assertNotNull(users, "Список пользователей не должен быть null");
        assertFalse(users.isEmpty(), "Список пользователей не должен быть пустым");
        assertEquals(1, users.size(), "В базе данных пока 1 пользователь");
    }

    @Test
    @Order(4)
    void testUpdateUser() {
        // Given
        UserEntity userToUpdate = userDao.getUserById(1L);
        assertNotNull(userToUpdate, "Пользователь для обновления должен существовать");

        // When
        userToUpdate.setName("Светлана Кузнецова");
        userToUpdate.setEmail("sveta.kuznetsova@gmail.com");
        userDao.updateUser(userToUpdate);

        // Then
        UserEntity updatedUser = userDao.getUserById(1L);
        assertNotNull(updatedUser, "Обновленный пользователь должен существовать");
        assertEquals("Светлана Кузнецова", updatedUser.getName());
        assertEquals("sveta.kuznetsova@gmail.com", updatedUser.getEmail());
    }

    @Test
    @Order(5)
    void testDeleteUser() {
        // When
        userDao.deleteUser(1L);

        // Then
        UserEntity deletedUser = userDao.getUserById(1L);
        assertNull(deletedUser, "Пользователь должен быть удален");
    }
}
