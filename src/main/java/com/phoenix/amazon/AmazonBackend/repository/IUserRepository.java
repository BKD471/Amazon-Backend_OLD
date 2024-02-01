package com.phoenix.amazon.AmazonBackend.repository;

import com.phoenix.amazon.AmazonBackend.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
public interface IUserRepository extends JpaRepository<Users, String> {
    /**
     * @param email    - exception code
     * @param userName - userName of user
     * @return Optional<Users> - optional users
     */
    Optional<Users> findByPrimaryEmailAndUserName(final String email, final String userName);

    /**
     * @param userId   - id of user
     * @param userName - userName of user
     * @return Optional<Users> - optional users
     */
    Optional<Users> findByUserIdOrUserName(final String userId, final String userName);

    /**
     * @param userId   - id of user
     * @param userName - userName of user
     */
    void deleteByUserIdOrUserName(final String userId, final String userName);

    /**
     * @param userName - userName of user
     * @param pageable - pageable object
     * @return Set<Users> - set of users
     **/
    Optional<Page<Users>> findAllByUserNameContaining(final String userName, final Pageable pageable);

    /**
     * @param value    - value of user fields
     * @param pageable - pageable object
     * @return Set<Users> - set of users
     **/
    @Query(value = "SELECT * FROM Users WHERE first_name=?1", nativeQuery = true)
    Optional<Page<Users>> searchUserByFirstName(final String value, final Pageable pageable);

    /**
     * @param value    - value of user fields
     * @param pageable - pageable object
     * @return Set<Users> - set of users
     **/
    @Query(value = "SELECT * FROM Users WHERE last_name=?1", nativeQuery = true)
    Optional<Page<Users>> searchUserByLastName(final String value, final Pageable pageable);

    /**
     * @param value    - value of user fields
     * @param pageable - pageable object
     * @return Set<Users> - set of users
     **/
    @Query(value = "SELECT * FROM Users WHERE gender=?1", nativeQuery = true)
    Optional<Page<Users>> searchUserByGender(final String value, final Pageable pageable);

    /**
     * @param value    - value of user fields
     * @param pageable - pageable object
     * @return Set<Users> - set of users
     **/
    @Query(value = "SELECT * FROM Users WHERE user_name=?1", nativeQuery = true)
    Optional<Page<Users>> searchUserByUserName(final String value, final Pageable pageable);

    /**
     * @param value    - value of user fields
     * @param pageable - pageable object
     * @return Set<Users> - set of users
     **/
    @Query(value = "SELECT * FROM Users WHERE user_primary_email=?1", nativeQuery = true)
    Optional<Page<Users>> searchUserByPrimaryEmail(final String value, final Pageable pageable);
}
