package tn.isetkebili.Notification_service.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.isetkebili.Notification_service.entity.Notification;
import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {
    List<Notification> findByCustomerIdOrderByCreatedAtDesc(
            Long customerId);
}