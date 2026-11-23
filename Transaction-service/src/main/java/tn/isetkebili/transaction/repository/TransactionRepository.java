package tn.isetkebili.transaction.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.isetkebili.transaction.entity.Transaction;
import java.util.List;
public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceAccountIdOrDestinationAccountId(
            Long sourceAccountId,
            Long destinationAccountId);
}