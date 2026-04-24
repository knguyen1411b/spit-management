package com.spring.backend.modules.notification;

import com.spring.backend.exception.AppException;
import com.spring.backend.modules.notification.dto.NotificationCreateDTO;
import com.spring.backend.modules.notification.dto.NotificationDTO;
import com.spring.backend.modules.user.User;
import com.spring.backend.modules.user.UserRepository;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {
  NotificationRepository notificationRepository;
  NotificationReceiverRepository receiverRepository;
  UserRepository userRepository;

  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'notification:create')")
  public void createNotification(NotificationCreateDTO request, Long senderId) {

    User sender = userRepository.findById(senderId).orElseThrow();

    Notification notification =
        notificationRepository.save(
            Notification.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .sender(sender)
                .build());

    List<NotificationReceiver> receivers =
        request.getReceiverIds().stream()
            .map(
                id ->
                    NotificationReceiver.builder()
                        .notification(notification)
                        .receiver(userRepository.getReferenceById(id))
                        .isRead(false)
                        .build())
            .toList();

    receiverRepository.saveAll(receivers);
  }

  @Transactional(readOnly = true)
  public Page<NotificationDTO> getMyNotifications(Long receiverId, Pageable pageable) {

    return receiverRepository
        .findByReceiverId(receiverId, pageable)
        .map(
            nr ->
                NotificationDTO.builder()
                    .id(nr.getNotification().getId())
                    .title(nr.getNotification().getTitle())
                    .content(nr.getNotification().getContent())
                    .type(nr.getNotification().getType())
                    .isRead(nr.getIsRead())
                    .createdAt(nr.getNotification().getCreatedAt())
                    .build());
  }

  @Transactional
  public void markAsRead(Long notificationId, Long receiverId) {
    NotificationReceiver receiver =
        receiverRepository
            .findByNotificationIdAndReceiverId(notificationId, receiverId)
            .orElseThrow(
                () ->
                    new AppException(
                        HttpStatus.NOT_FOUND, NotificationMessages.NOT_FOUND.getMessage()));

    if (!receiver.getIsRead()) {
      receiver.setIsRead(true);
      receiver.setReadAt(Instant.now());
    }
  }

  @Transactional(readOnly = true)
  public long getUnreadCount(Long receiverId) {
    return receiverRepository.countByReceiverIdAndIsReadFalse(receiverId);
  }
}
