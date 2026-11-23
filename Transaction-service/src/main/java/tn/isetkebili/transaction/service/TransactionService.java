package tn.isetkebili.transaction.service;



import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tn.isetkebili.transaction.dto.AccountDTO;
import tn.isetkebili.transaction.entity.Transaction;
import tn.isetkebili.transaction.entity.TransactionStatus;
import tn.isetkebili.transaction.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {
    private final TransactionRepository repository;
    private final RestClient accountClient;
    private final RestClient notificationClient;

    public TransactionService(TransactionRepository repository,
                              @Qualifier("accountRestClient") RestClient accountClient,
                              @Qualifier("notificationRestClient") RestClient notificationClient) {
        this.repository = repository;
        this.accountClient = accountClient;
        this.notificationClient = notificationClient;
    }

    public Transaction transfer(Long sourceAccountId, Long destinationAccountId, BigDecimal amount) {
        // 1. Validations de base
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être strictement positif");
        }
        if (sourceAccountId.equals(destinationAccountId)) {
            throw new IllegalArgumentException("Les comptes source et destination doivent être différents");
        }

        // 2. Vérification des comptes via Account Service
        AccountDTO source = accountClient.get().uri("/api/accounts/{id}", sourceAccountId).retrieve().body(AccountDTO.class);
        AccountDTO dest = accountClient.get().uri("/api/accounts/{id}", destinationAccountId).retrieve().body(AccountDTO.class);

        if (!"ACTIVE".equals(source.status()) || !"ACTIVE".equals(dest.status())) {
            throw new IllegalStateException("L'un des comptes n'est pas actif");
        }
        if (source.balance().compareTo(amount) < 0) {
            throw new IllegalStateException("Solde insuffisant");
        }

        // 3. Création de la transaction en statut PENDING
        Transaction tx = new Transaction();
        tx.setSourceAccountId(sourceAccountId);
        tx.setDestinationAccountId(destinationAccountId);
        tx.setAmount(amount);
        tx = repository.save(tx);

        try {
            // 4. Débit du compte source
            accountClient.post().uri("/api/accounts/{id}/debit?amount={amount}", sourceAccountId, amount).retrieve().toBodilessEntity();

            // 5. Crédit du compte destination
            accountClient.post().uri("/api/accounts/{id}/credit?amount={amount}", destinationAccountId, amount).retrieve().toBodilessEntity();

            // 6. Succès
            tx.setStatus(TransactionStatus.COMPLETED);
            repository.save(tx);

            // Envoi de la notification
            sendNotification(source.customerId(), "Virement de " + amount + " effectué avec succès.", "TRANSFER_COMPLETED");

        } catch (Exception e) {
            // 🚨 STRATÉGIE DE COMPENSATION 🚨
            // Si le crédit a échoué (par exemple, le service a planté entre le débit et le crédit),
            // on doit rembourser le compte source pour annuler le débit.
            try {
                accountClient.post().uri("/api/accounts/{id}/credit?amount={amount}", sourceAccountId, amount).retrieve().toBodilessEntity();
            } catch (Exception compensationError) {
                System.err.println("ERREUR CRITIQUE : Échec de la compensation ! " + compensationError.getMessage());
            }

            tx.setStatus(TransactionStatus.FAILED);
            repository.save(tx);

            sendNotification(source.customerId(), "Virement de " + amount + " échoué.", "TRANSFER_FAILED");
            throw new RuntimeException("Le virement a échoué : " + e.getMessage());
        }

        return tx;
    }

    public List<Transaction> findAll() {
        return repository.findAll();
    }

    public Transaction findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Transaction introuvable"));
    }

    public List<Transaction> findByAccountId(Long accountId) {
        return repository.findBySourceAccountIdOrDestinationAccountId(accountId, accountId);
    }

    private void sendNotification(Long customerId, String message, String type) {
        try {
            notificationClient.post().uri("/api/notifications")
                    .body(Map.of("customerId", customerId, "message", message, "type", type))
                    .retrieve().toBodilessEntity();
        } catch (Exception e) {
            System.err.println("Erreur d'envoi de notification (non bloquant) : " + e.getMessage());
        }
    }
}