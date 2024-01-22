package com.phoenix.amazon.AmazonBackend.repository;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Transactional
@Repository
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

    /**
     * @param value - value of user fields
     * @return Set<Users>
     **/
    @Query(value = "SELECT * FROM Users WHERE first_name=?1", nativeQuery = true)
    Optional<Set<Users>> searchUserByFirstName(final String value);

    /**
     * @param value - value of user fields
     * @return Set<Users>
     **/
    @Query(value = "SELECT * FROM Users WHERE last_name=?1", nativeQuery = true)
    Optional<Set<Users>> searchUserByLastName(final String value);

    /**
     * @param value - value of user fields
     * @return Set<Users>
     **/
    @Query(value = "SELECT * FROM Users WHERE gender=?1", nativeQuery = true)
    Optional<Set<Users>> searchUserByGender(final String value);

    /**
     * @param value - value of user fields
     * @return Set<Users>
     **/
    @Query(value = "SELECT * FROM Users WHERE user_name=?1", nativeQuery = true)
    Optional<Set<Users>> searchUserByUserName(final String value);

    /**
     * @param value - value of user fields
     * @return Set<Users>
     **/
    @Query(value = "SELECT * FROM Users WHERE user_email=?1", nativeQuery = true)
    Optional<Set<Users>> searchUserByEmail(final String value);
}
