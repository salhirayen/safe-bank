package tn.isetkebili.Notification_service.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.isetkebili.Notification_service.dto.NotificationRequest;
import tn.isetkebili.Notification_service.entity.Notification;
import tn.isetkebili.Notification_service.service.NotificationService;
import java.util.List;
@RestController
@RequestMapping("/api/notifications")
public class notificationcontroller {
    private final NotificationService service;
    public notificationcontroller(
            NotificationService service) {
        this.service = service;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification create(
            @Valid @RequestBody NotificationRequest request) {
        return service.create(request);
    }
    @GetMapping
    public List<Notification> findAll() {
        return service.findAll();
    }
    @GetMapping("/{id}")
    public Notification findById(@PathVariable Long id) {
        return service.findById(id);
    }
    @GetMapping("/customer/{customerId}")
    public List<Notification> findByCustomer(
            @PathVariable Long customerId) {
        return service.findByCustomerId(customerId);
    }
}

