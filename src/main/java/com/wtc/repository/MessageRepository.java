package com.wtc.repository;

import com.wtc.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.id = :userId OR m.receiver.id = :userId) " +
           "ORDER BY m.sentAt DESC")
    List<Message> findConversation(@Param("userId") Long userId);

    List<Message> findByReceiverIdOrderBySentAtDesc(Long receiverId);

    List<Message> findBySegmentIdOrderBySentAtDesc(Long segmentId);
}
