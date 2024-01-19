package com.phoenix.amazon.AmazonBackend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.time.LocalDateTime;

import static  com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;

@Entity
@Table(name = "users")
public class Users {
    @Id
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "user_emal", unique = true)
    private String email;

    @Column(name = "user_password", length = 255, nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    private GENDER gender;

    @Column(name = "user_image_name")
    private String imageName;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    @Column(length = 1000)
    private String about;

    public Users(){}
    public Users(builder builder) {
        this.userId = builder.userId;
        this.userName = builder.userName;
        this.firstName=builder.firstName;
        this.lastName=builder.lastName;
        this.email = builder.email;
        this.gender = builder.gender;
        this.imageName = builder.imageName;
        this.password = builder.password;
        this.lastSeen=builder.lastSeen;
        this.about = builder.about;
    }
    public static final class builder {
        private String userId;
        private String userName;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private GENDER gender;
        private String imageName;
        private LocalDateTime lastSeen;
        private String about;

        public builder() {
        }

        public builder userId(final String userId) {
            this.userId = userId;
            return this;
        }

        public builder userName(final String userName) {
            this.userName = userName;
            return this;
        }

        public builder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public builder lastName(final String lastName){
            this.lastName=lastName;
            return this;
        }

        public builder email(final String email) {
            this.email = email;
            return this;
        }

        public builder password(final String password) {
            this.password = password;
            return this;
        }

        public builder gender(final GENDER gender) {
            this.gender = gender;
            return this;
        }

        public builder imageName(final String imageName) {
            this.imageName = imageName;
            return this;
        }

        public builder about(final String about) {
            this.about = about;
            return this;
        }

        public builder lastSeen(final LocalDateTime lastSeen){
            this.lastSeen=lastSeen;
            return this;
        }

        public Users build() {
            return new Users(this);
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public GENDER getGender() {
        return gender;
    }

    public String getImageName() {
        return imageName;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public String getAbout() {
        return about;
    }
}
