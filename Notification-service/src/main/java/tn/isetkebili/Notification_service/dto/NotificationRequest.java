package tn.isetkebili.Notification_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tn.isetkebili.Notification_service.entity.NotificationType;
public record NotificationRequest(
        @NotNull Long customerId,
        @NotBlank @Size(max = 500) String message,
        @NotNull NotificationType type
) {
}
