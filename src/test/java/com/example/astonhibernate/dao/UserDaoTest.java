//package com.example.astonhibernate.dao;
//
//import com.example.astonhibernate.entity.UserEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class UserDaoTest {
//    private UserDao userDao;
//
//    @BeforeEach
//    void setUp() {
//        userDao = new UserDao();
//    }
//
//    @Test
//    void testUserEntityCreation() {
//        UserEntity user = new UserEntity("Test User", "test@test.com", 35);
//        assertNull(user.getId()); // ID должен быть null до сохранения
//        assertNotNull(user.getCreatedAt()); // Дата создания должна быть установлена
//    }
//}
