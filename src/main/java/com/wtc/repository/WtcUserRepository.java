package com.wtc.repository;

import com.wtc.entity.WtcUser;
import com.wtc.enums.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WtcUserRepository extends MongoRepository<WtcUser, String> {
    Optional<WtcUser> findByEmail(String email);
    boolean existsByEmail(String email);
    List<WtcUser> findByRole(UserRole role);
}
