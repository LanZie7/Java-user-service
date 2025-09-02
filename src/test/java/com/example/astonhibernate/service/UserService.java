package com.example.astonhibernate.service;

import com.example.astonhibernate.dao.UserDao;
import com.example.astonhibernate.entity.UserEntity;

import java.util.List;


public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserEntity createUser(String name, String email, int age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email введен некорректно");
        }
        UserEntity newUser = new UserEntity(name, email, age);
        userDao.saveUser(newUser);
        return newUser;
    }

    public UserEntity getUserById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Введен некорректный id");
        }
        return userDao.getUserById(id);
    }

    public List<UserEntity> getAllUsers() {
        return userDao.getAllUsers();
    }

    public void updateUser(UserEntity userToUpdate) {
        if (userToUpdate == null || userToUpdate.getId() == null) {
            throw new IllegalArgumentException("Имя пользователя или id не могут быть null");
        }
        userDao.updateUser(userToUpdate);
    }

    public void deleteUser(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Введен некорректный id");
        }
        userDao.deleteUser(id);
    }
}
