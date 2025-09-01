package com.example.astonhibernate.hibernate;

import com.example.astonhibernate.dao.UserDao;
import com.example.astonhibernate.entity.UserEntity;

import java.util.List;
import java.util.Scanner;

public class UserCLI {
    private final UserDao userDao = new UserDao();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- User Service ---");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Посмотреть всех пользователей");
            System.out.println("3. Найти пользователя по ID");
            System.out.println("4. Обновить пользователя");
            System.out.println("5. Удалить пользователя по ID");
            System.out.println("6. Выход");
            System.out.print("Выберите вариант: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    viewAllUsers();
                    break;
                case 3:
                    viewUserById();
                    break;
                case 4:
                    updateUser();
                    break;
                case 5:
                    deleteUser();
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        }
    }

    private void createUser() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите возраст: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        UserEntity user = new UserEntity(name, email, age);
        userDao.saveUser(user);
        System.out.println("Пользователь создан");
    }

    private void viewAllUsers() {
        List<UserEntity> users = userDao.getAllUsers();
        for (UserEntity user : users) {
            System.out.println(user);
        }
    }

    private void viewUserById() {
        System.out.print("Введите ID пользователя: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        UserEntity user = userDao.getUserById(id);
        if (user != null) {
            System.out.println(user);
        } else {
            System.out.println("Пользователь не найден");
        }
    }

    private void updateUser() {
        System.out.print("Введите ID пользователя: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        UserEntity user = userDao.getUserById(id);
        if (user == null) {
            System.out.println("Пользователь не найден");
            return;
        }

        System.out.print("Введите новое имя [" + user.getName() + "]: ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) user.setName(name);

        System.out.print("Введите новый email [" + user.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) user.setEmail(email);

        System.out.print("Введите новый возраст [" + user.getAge() + "]: ");
        String ageStr = scanner.nextLine();
        if (!ageStr.isEmpty()) user.setAge(Integer.parseInt(ageStr));

        userDao.updateUser(user);
        System.out.println("Пользователь обновлен");
    }

    private void deleteUser() {
        System.out.print("Введите ID пользователя: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        userDao.deleteUser(id);
        System.out.println("Пользователь удалён");
    }
}