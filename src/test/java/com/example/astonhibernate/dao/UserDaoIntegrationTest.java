/**Интеграционные тесты для UserDao с Testcontainers
Testcontainers запускает реальную PostgreSQL в Docker контейнере для тестов.*/

package com.example.astonhibernate.dao;

import org.junit.jupiter.api.*;
import com.example.astonhibernate.entity.UserEntity;
import com.example.astonhibernate.hibernate.HibernateUtil;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers; // библиотека для запуска Docker-контейнеров в тестах

import java.util.List;

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

    @BeforeAll
    static void beforeAll() {
        // Настройка Hibernate на использование тестовой БД
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());

        userDao = new UserDao();
    }

    @AfterAll
    static void afterAll() {
        // Закрытие сессии Hibernate
        HibernateUtil.shutdown();
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
        assertFalse(users.isEmpty(), "Список пользователей не должен быть пустым");
        assertEquals(1, users.size(), "В базе данных пока 1 пользователь");
    }

    @Test
    @Order(4)
    void testUpdateUser() {
        // Given
        UserEntity userToUpdate = userDao.getUserById(1L);

        // When
        userToUpdate.setName("Светлана Кузнецова");
        userDao.updateUser(userToUpdate);

        // Then
        UserEntity updatedUser = userDao.getUserById(1L);
        assertEquals("Светлана Кузнецова", updatedUser.getName(), "Пользователь должен обновиться");
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
