package com.wtc.repository;

import com.wtc.entity.WtcUser;
import com.wtc.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WtcUserRepository extends JpaRepository<WtcUser, Long> {
    Optional<WtcUser> findByEmail(String email);
    boolean existsByEmail(String email);
    List<WtcUser> findByRole(UserRole role);
}
