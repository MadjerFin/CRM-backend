package com.wtc.repository;

import com.wtc.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    @Query("{ $or: [ { 'senderId': ?0 }, { 'receiverId': ?0 } ] }")
    List<Message> findConversation(String userId);

    List<Message> findByReceiverIdOrderBySentAtDesc(String receiverId);
    List<Message> findBySegmentIdOrderBySentAtDesc(String segmentId);
}
