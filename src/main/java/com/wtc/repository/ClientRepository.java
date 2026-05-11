package com.wtc.repository;

import com.wtc.entity.Client;
import com.wtc.enums.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUserId(Long userId);
    List<Client> findBySegmentId(Long segmentId);
    List<Client> findByStatus(ClientStatus status);

    @Query("SELECT c FROM Client c WHERE " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:segmentId IS NULL OR c.segment.id = :segmentId) AND " +
           "(:tag IS NULL OR c.tags LIKE %:tag%)")
    List<Client> findWithFilters(@Param("status") ClientStatus status,
                                 @Param("segmentId") Long segmentId,
                                 @Param("tag") String tag);

    @Query("SELECT c FROM Client c WHERE " +
           "LOWER(c.user.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(c.user.email) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Client> search(@Param("q") String q);
}
