package com.phoenix.amazon.AmazonBackend.repository;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface IUserRepository extends JpaRepository<Users, String> {
    /**
     * @param email    - exception code
     * @param userName - userName of user
     * @return Optional<Users>
     */
    Optional<Users> findByEmailOrUserName(final String email, final String userName);

    /**
     * @param userId   - id of user
     * @param userName - userName of user
     * @return Optional<Users>
     */
    Optional<Users> findByUserIdOrUserName(final String userId, final String userName);

    /**
     * @param userId   - id of user
     * @param userName - userName of user
     */
    void deleteByUserIdOrUserName(final String userId, final String userName);

    /**
     * @param userName - userName of user
     * @return Set<Users>
     **/
    Optional<Set<Users>> findAllByUserNameContaining(String userName);
}
