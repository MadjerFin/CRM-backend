package com.wtc.repository;

import com.wtc.entity.Segment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SegmentRepository extends MongoRepository<Segment, String> {
    boolean existsByName(String name);
}
