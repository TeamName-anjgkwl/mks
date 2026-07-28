package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;
import memo.example.demo.domain.Notification;
import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponseDto {
    private Long notificationId;
    private String type;     // V10 반영: 알림 유형
    private Long targetId;   // V10 반영: 라우팅용 리소스 ID
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationResponseDto from(Notification notification) {
        return NotificationResponseDto.builder()
                .notificationId(notification.getNotificationId())
                .type(notification.getType() != null ? notification.getType().name() : null)
                .targetId(notification.getTargetId())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}