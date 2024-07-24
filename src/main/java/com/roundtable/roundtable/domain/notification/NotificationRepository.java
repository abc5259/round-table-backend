package com.roundtable.roundtable.domain.notification;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select n from Notification n where n.receiver.id = :receiverId and n.id < :lastId order by n.id desc")
    List<Notification> findNextNotificationsByReceiverId(
            @Param("receiverId") Long receiverId,
            @Param("lastId") Long lastId,
            Pageable pageable);

    @Query("select n from Notification n where n.receiver.id = :receiverId order by n.id desc")
    List<Notification> findTopNotificationsByReceiverId(
            @Param("receiverId") Long receiverId,
            Pageable pageable);
}
