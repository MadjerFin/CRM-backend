package com.wtc.repository;

import com.wtc.entity.Client;
import com.wtc.enums.ClientStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {

    Optional<Client> findByUserId(String userId);
    List<Client> findBySegmentId(String segmentId);
    List<Client> findByStatus(ClientStatus status);

    @Query("{ $and: [ " +
           "{ $or: [ { 'status': null }, { 'status': ?0 } ] }, " +
           "{ $or: [ { 'segmentId': null }, { 'segmentId': ?1 } ] }, " +
           "{ $or: [ { 'tags': null }, { 'tags': { $regex: ?2, $options: 'i' } } ] } " +
           "] }")
    List<Client> findWithFilters(ClientStatus status, String segmentId, String tag);

    @Query("{ $or: [ " +
           "{ 'userName': { $regex: ?0, $options: 'i' } }, " +
           "{ 'userEmail': { $regex: ?0, $options: 'i' } }, " +
           "{ 'company': { $regex: ?0, $options: 'i' } } " +
           "] }")
    List<Client> search(String q);
}
