package cloestics.TransactionTest;

import closetics.Payment.transactions.TransactionController;
import closetics.Payment.transactions.TransactionHistory;
import closetics.Payment.transactions.TransactionRepository;
import closetics.Users.User;
import closetics.Users.UserProfile.UserProfileController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TransactionController.class)
@ContextConfiguration(classes = closetics.MainApplication.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository transactionRepository;

    private TransactionHistory sampleTransaction;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserId(1);
        sampleTransaction = new TransactionHistory(user, "USD", "succeeded", 1L, "1", "Basic" );
        sampleTransaction.setId(1L);
    }

    @Test
    void testGetAllTransaction() throws Exception {
        List<TransactionHistory> transactions = Collections.singletonList(sampleTransaction);
        Mockito.when(transactionRepository.findAll()).thenReturn(transactions);

        mockMvc.perform(get("/transactions/history"))
                .andExpectAll(
                    status().isOk(),
                    jsonPath("$[0].currency").value("USD"),
                        jsonPath("$[0].amount").value(1L),
                        jsonPath("$[0].paymentIntentId").value("1"),
                        jsonPath("$[0].status").value("succeeded"),
                        jsonPath("$[0].tierName").value("Basic")
                );

    }

    @Test
    void testGetUserTransactionsSuccess() throws Exception {
        Mockito.when(transactionRepository.findByUserId(1L))
                .thenReturn(Optional.of(Collections.singletonList(sampleTransaction)));

        mockMvc.perform(get("/transactions/history/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(1));
    }

    @Test
    void testGetUserTransactionsNotFound() throws Exception {
        Mockito.when(transactionRepository.findByUserId(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/transactions/history/999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error occurred: Transactions Not Found"));
    }

    @Test
    void testDeleteTransactionHistorySuccess() throws Exception {
        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.of(sampleTransaction));
        doNothing().when(transactionRepository).deleteById(1L);

        mockMvc.perform(delete("/transactions/history/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("succeeded"));
    }

    @Test
    void testDeleteTransactionHistoryNotFound() throws Exception {
        Mockito.when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/transactions/history/999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error occurred: Transaction Not Found"));
    }
}