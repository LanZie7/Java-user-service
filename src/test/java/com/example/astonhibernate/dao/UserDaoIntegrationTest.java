/**Интеграционные тесты для UserDao с Testcontainers
Testcontainers запускает реальную PostgreSQL в Docker контейнере для тестов.*/

package com.example.astonhibernate;

import com.example.astonhibernate.dao.UserDao;
import com.example.astonhibernate.hibernate.HibernateUtil;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers; // библиотека для запуска Docker-контейнеров в тестах



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
        // Given - 
    }

}
