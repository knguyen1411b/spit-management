package com.spring.backend.modules.notification;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationReceiverRepository extends JpaRepository<NotificationReceiver, Long> {
  Page<NotificationReceiver> findByReceiverId(Long receiverId, Pageable pageable);

  Optional<NotificationReceiver> findByNotificationIdAndReceiverId(
      Long notificationId, Long receiverId);

  long countByReceiverIdAndIsReadFalse(Long receiverId);
}
