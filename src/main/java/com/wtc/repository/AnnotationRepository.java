package com.wtc.repository;

import com.wtc.entity.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, Long> {
    List<Annotation> findByClientIdOrderByCreatedAtDesc(Long clientId);
}
