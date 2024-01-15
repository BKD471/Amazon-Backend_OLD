package com.phoenix.amazon.AmazonBackend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import java.time.LocalDateTime;

import static  com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;

@Getter
@Entity
@Table(name = "users")
public class Users {
    @Id
    private String userId;
    @Column(name = "user_name")
    private String name;
    @Column(name = "user_emal", unique = true)
    private String email;
    @Column(name = "user_password", length = 255, nullable = false)
    private String password;
    @Enumerated(value = EnumType.STRING)
    private GENDER gender;
    @Column(name = "user_image_name")
    private String imageName;
    private LocalDateTime lastSeen;
    @Column(length = 1000)
    private String about;

    public Users(){}
    public Users(builder builder) {
        this.userId = builder.userId;
        this.name = builder.name;
        this.email = builder.email;
        this.gender = builder.gender;
        this.imageName = builder.imageName;
        this.password = builder.password;
        this.lastSeen=builder.lastSeen;
        this.about = builder.about;
    }
    public static final class builder {
        private String userId;
        private String name;
        private String email;
        private String password;
        private GENDER gender;
        private String imageName;
        private LocalDateTime lastSeen;
        private String about;

        public builder() {
        }

        public builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public builder name(String name) {
            this.name = name;
            return this;
        }

        public builder email(String email) {
            this.email = email;
            return this;
        }

        public builder password(String password) {
            this.password = password;
            return this;
        }

        public builder gender(GENDER gender) {
            this.gender = gender;
            return this;
        }

        public builder imageName(String imageName) {
            this.imageName = imageName;
            return this;
        }

        public builder about(String about) {
            this.about = about;
            return this;
        }

        public builder lastSeen(LocalDateTime lastSeen){
            this.lastSeen=lastSeen;
            return this;
        }

        public Users build() {
            return new Users(this);
        }
    }
}
