package com.phoenix.amazon.AmazonBackend.repository;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<Users,String> {
    Optional<Users> findByEmailOrUserName(final String emailOrUserName);
    Optional<List<Users>> findAllByUserNameContaining(String userName);
}
