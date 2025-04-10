package closetics.Payment.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/transactions")
@RestController
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/history")
    public ResponseEntity<List<TransactionHistory>> GetAllTransaction(){
        List<TransactionHistory> history = transactionRepository.findAll();
        return ResponseEntity.ok(history);
    }
    @GetMapping("/history/{id}")
    public ResponseEntity<?> GetUserTransactions(@PathVariable long id){
        try {
            List<TransactionHistory> history = transactionRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Transactions Not Found"));
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error occurred: " + e.getMessage());
        }
    }
    @DeleteMapping("/history/{id}")
    public ResponseEntity<?> DeleteTransactionHistory(@PathVariable long id){
        try {
            TransactionHistory history = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction Not Found"));
            transactionRepository.deleteById(id);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error occurred: " + e.getMessage());
        }

    }
}
