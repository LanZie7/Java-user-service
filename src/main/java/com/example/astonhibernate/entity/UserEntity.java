package com.example.astonhibernate.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "userdb")
public class UserEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name", nullable = false)
        private String name;

        @Column(name = "email", unique = true, nullable = false)
        private String email;

        @Column(name = "age")
        private int age;

        @Column(name = "created_at", nullable = false)
        private LocalDateTime createdAt;

        public UserEntity() {}

        public UserEntity(String name, String email, int age) {
            this.name = name;
            this.email = email;
            this.age = age;
            this.createdAt = LocalDateTime.now();
        }


        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", age=" + age +
                    ", createdAt=" + createdAt +
                    '}';
        }
}

