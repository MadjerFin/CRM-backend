package com.wtc.repository;

import com.wtc.entity.Annotation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnotationRepository extends MongoRepository<Annotation, String> {
    List<Annotation> findByClientIdOrderByCreatedAtDesc(String clientId);
}
