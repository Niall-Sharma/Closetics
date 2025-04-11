package closetics.Payment.transactions;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TransactionRepository extends JpaRepository<TransactionHistory, Long> {

    @Query(value = "SELECT * FROM transaction_history_table WHERE id=?",nativeQuery = true)
    Optional<List<TransactionHistory>> findByUserId(long id);

    @Query(value = "SELECT * FROM transaction_history_table WHERE payment_intent_id=?", nativeQuery = true)
    Optional<TransactionHistory> findByPaymentIntentId(String id);

    @Transactional
    @Query(value = "DELETE FROM transaction_history_table WHERE payment_intent_id=?", nativeQuery = true)
    void deleteByPaymentIntentId(String Id);

}
