/**
 * Mockito позволяет создавать "заглушки" (моки) для зависимостей.
 * Мы тестируем только сервис, не трогая реальную БД.
 * */

package com.example.astonhibernate.service;

import com.example.astonhibernate.dao.UserDao;
import com.example.astonhibernate.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock // Создание мока для UserDao
    private UserDao userDao;

    @InjectMocks // Внедрение моков в сервис
    private UserService userService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        // Подготовка тестовых данных перед каждым тестом
        testUser = new UserEntity("Test User", "test@gmail.com", 35);
        testUser.setId(1L);
    }

    @Test
    void testCreateUser_ValidData() {
        // Given - для void метода используем doAnswer, чтобы установить id
        doAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            user.setId(1L); // Эмулируем присвоение ID как в БД
            return null;
        }).when(userDao).saveUser(any(UserEntity.class));

        // When
        UserEntity createdUser = userService.createUser("Test User", "test@gmail.com", 35);

        // Then
        assertNotNull(createdUser.getId());
        assertEquals("Test User", createdUser.getName());
        verify(userDao, times(1))
                .saveUser(any(UserEntity.class)); // проверка, что метод вызван 1 раз
    }

    @Test
    void testCreateUser_InvalidName() {
        // When & Then - ожидаем исключение
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("", "test@gmail.com", 35);
        });

        // Проверяем, что saveUser не вызывался
        verify(userDao, never()).saveUser(any(UserEntity.class));
    }

    @Test
    void testCreateUser_InvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("Test User", "invalid-email", 35);
        });

        verify(userDao, never()).saveUser(any(UserEntity.class));
    }

    @Test
    void testGetAllUsers() {
        // Given
        when(userDao.getAllUsers()).thenReturn(List.of(testUser));

        // When
        List<UserEntity> users = userService.getAllUsers();

        // Then
        assertEquals(1, users.size());
        assertEquals("Test User", users.get(0).getName());
        verify(userDao, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById_ValidId() {
        // Given
        when(userDao.getUserById(1L)).thenReturn(testUser);

        // When
        UserEntity user = userService.getUserById(1L);

        // Then
        assertNotNull(user);
        assertEquals("Test User", user.getName());
        verify(userDao, times(1)).getUserById(1L);
    }

    @Test
    void testGetUserById_InvalidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(-1L);
        });

        verify(userDao, never()).getUserById(anyLong());
    }

    @Test
    void testUpdateUser_ValidData() {
        // When
        userService.updateUser(testUser);

        // Then
        verify(userDao, times(1)).updateUser(testUser);
    }

    @Test
    void testUpdateUser_NullUser() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(null);
        });

        verify(userDao, never()).updateUser(any(UserEntity.class));
    }

    @Test
    void testDeleteUser_ValidId() {
        // When
        userService.deleteUser(1L);

        // Then
        verify(userDao, times(1)).deleteUser(1L);
    }

    @Test
    void testDeleteUser_InvalidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(0L);
        });

        verify(userDao, never()).deleteUser(anyLong());
    }
}
