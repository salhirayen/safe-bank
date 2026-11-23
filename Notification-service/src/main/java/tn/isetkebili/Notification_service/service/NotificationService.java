package tn.isetkebili.Notification_service.service;

import org.springframework.stereotype.Service;
import tn.isetkebili.Notification_service.dto.NotificationRequest;
import tn.isetkebili.Notification_service.entity.Notification;
// CORRECTION ICI : Majuscule à NotificationStatus
import tn.isetkebili.Notification_service.entity.NotificationStatus;
import tn.isetkebili.Notification_service.repository.NotificationRepository;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public Notification create(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setCustomerId(request.customerId());
        notification.setMessage(request.message());
        notification.setType(request.type());

        // CORRECTION ICI : Majuscule à NotificationStatus.PENDING
        notification.setStatus(NotificationStatus.PENDING);

        return repository.save(notification);
    }

    public List<Notification> findAll() {
        return repository.findAll();
    }

    public List<Notification> findByCustomerId(Long customerId) {
        return repository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    public Notification findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification introuvable : " + id));
    }
}