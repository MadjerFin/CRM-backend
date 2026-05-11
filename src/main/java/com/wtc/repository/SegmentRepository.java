package com.wtc.repository;

import com.wtc.entity.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Long> {
    boolean existsByName(String name);
}
