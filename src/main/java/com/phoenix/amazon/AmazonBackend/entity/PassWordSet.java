package com.phoenix.amazon.AmazonBackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;


@Entity
@Table(name = "user_password_set")
public class PassWordSet extends Audit {
    @Id
    private String password_id;
    @Column(name = "Passwords", nullable = false)
    private String passwords;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    protected PassWordSet() {
    }

    public PassWordSet(builder builder) {
        this.password_id = builder.password_id;
        this.passwords = builder.passwords;
        this.users = builder.users;
    }

    public static final class builder {
        private String password_id;
        private String passwords;
        private Users users;

        public builder() {
        }

        ;

        public builder password_id(final String password_id) {
            this.password_id = password_id;
            return this;
        }

        public builder passwords(final String passwords) {
            this.passwords = passwords;
            return this;
        }

        public builder users(final Users users) {
            this.users = users;
            return this;
        }

        public PassWordSet build() {
            return new PassWordSet(this);
        }
    }

    public String getPassword_id() {
        return password_id;
    }

    public String getPasswords() {
        return passwords;
    }

    public Users getUsers() {
        return users;
    }
}
