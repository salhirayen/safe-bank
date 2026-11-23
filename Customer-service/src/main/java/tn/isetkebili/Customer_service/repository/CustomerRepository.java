package tn.isetkebili.Customer_service.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.isetkebili.Customer_service.entity.Customer;
import java.util.Optional;
public interface CustomerRepository
        extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
}
