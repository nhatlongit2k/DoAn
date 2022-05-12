package com.drivinglicence.myapp.repository;

import com.drivinglicence.myapp.domain.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByActivationKey(String activationKey);
    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
    Optional<User> findOneByResetKey(String resetKey);
    Optional<User> findOneByEmailIgnoreCase(String email);
    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    @Query(value = "SELECT * FROM jhi_user inner join jhi_user_authority on jhi_user.id=jhi_user_authority.user_id WHERE authority_name=:role", nativeQuery = true)
    List<User> selectUsersWithRole(@Param("role")String role); //ROLE_ADMIN or ROLE_USER

    @Query(value = "SELECT * FROM jhi_user WHERE login Like %:strSearch% OR email Like %:strSearch% OR phone_number Like %:strSearch%", nativeQuery = true)
    List<User> searchMultioption(@Param("strSearch")String strSearch);
}
