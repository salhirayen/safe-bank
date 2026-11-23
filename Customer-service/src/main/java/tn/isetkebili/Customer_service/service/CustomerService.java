package tn.isetkebili.Customer_service.service;

import org.springframework.stereotype.Service;
import tn.isetkebili.Customer_service.entity.Customer;
import tn.isetkebili.Customer_service.repository.CustomerRepository;
import java.util.List;
@Service
public class CustomerService {
    private final CustomerRepository repository;
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }
    public Customer create(Customer customer) {
        return repository.save(customer);
    }
    public List<Customer> findAll() {
        return repository.findAll();
    }
    public Customer findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Client introuvable : " + id));
    }
    public Customer update(Long id, Customer data) {
        Customer customer = findById(id);
        customer.setFirstName(data.getFirstName());
        customer.setLastName(data.getLastName());
        customer.setEmail(data.getEmail());
        customer.setPhone(data.getPhone());
        return repository.save(customer);
    }
    public void delete(Long id) {
        // Vérifie d'abord si le client existe (optionnel mais bien pour les erreurs)
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Client introuvable : " + id);
        }
        // Supprime directement
        repository.deleteById(id);
    }
}
