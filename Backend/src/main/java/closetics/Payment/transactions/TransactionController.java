package closetics.Payment.transactions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/transactions")
@RestController
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;

    @Operation(summary = "Retrieve all transaction histories from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all transactions",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/history")
    public ResponseEntity<List<TransactionHistory>> GetAllTransaction(){
        List<TransactionHistory> history = transactionRepository.findAll();
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Retrieve all transactions made by a specific user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user's transaction history",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "User transactions not found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/history/{id}")
    public ResponseEntity<?> GetUserTransactions(@PathVariable long id){
        try {
            List<TransactionHistory> history = transactionRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Transactions Not Found"));
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error occurred: " + e.getMessage());
        }
    }

    @Operation(summary = "Delete a specific transaction by its transaction ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted transaction",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Transaction not found",
                    content = @Content(mediaType = "application/json"))
    })
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
