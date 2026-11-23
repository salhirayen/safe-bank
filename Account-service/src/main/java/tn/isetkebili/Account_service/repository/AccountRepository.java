package tn.isetkebili.Account_service.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.isetkebili.Account_service.entity.Account;
import java.util.List;
import java.util.Optional;
public interface AccountRepository
        extends JpaRepository<Account, Long> {
    List<Account> findByCustomerId(Long customerId);

    Optional<Account> findByAccountNumber(String accountNumber);
}
