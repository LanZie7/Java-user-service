package com.example.astonhibernate;


import com.example.astonhibernate.hibernate.HibernateUtil;
import com.example.astonhibernate.hibernate.UserCLI;

public class Main {
    public static void main(String[] args) {
        try {
            new UserCLI().start();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
