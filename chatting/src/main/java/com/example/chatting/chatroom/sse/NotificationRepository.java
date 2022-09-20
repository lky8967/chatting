package com.example.chatting.chatroom.sse;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
//    @Query("select n from Notification n where n.receiver.id = :userId order by n.id desc")
//    List<Notification> findAllByUserId(@Param("userId") Long userId);

//    @Query("select count(n) from Notification n where n.receiver.id = :userId and n.isRead = false")
//    Long countUnReadNotifications(@Param("userId") Long userId);

    Optional<Notification> findById(Long NotificationsId);

//    void deleteAllByReceiverId(Long receiverId);
    void deleteById(Long notificationId);
}

