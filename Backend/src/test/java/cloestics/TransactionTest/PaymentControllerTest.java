package cloestics.TransactionTest;

import closetics.Payment.PaymentController;
import closetics.Payment.transactions.TransactionController;
import closetics.Payment.transactions.TransactionHistory;
import closetics.Payment.transactions.TransactionRepository;
import closetics.Users.User;
import closetics.Users.UserProfile.UserProfile;
import closetics.Users.UserProfile.UserProfileController;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PaymentController.class)
@ContextConfiguration(classes = closetics.MainApplication.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository transactionRepository;

    private TransactionHistory sampleTransaction;
    private User user;

    @Value("${stripe.api.key}")
    private String apiKey;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);
        user.setUserTier("Basic");

        sampleTransaction = new TransactionHistory(user, "USD", "processing", 1L, "1", "Premium");

    }

    @Test
    void testCreatePaymentSuccess() throws Exception {
        Map<String, Object> request = Map.of(
                "amount", 1000,
                "userId", 1L,
                "tier", "Premium"
        );


    }

    @Test
    void testCompletePayment() throws Exception{
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(sampleTransaction));
        when(transactionRepository.save(any(TransactionHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String,Object> metadata = Map.of(
                "tier", "Premium",
                "userId", 1
        );
        Map<String,Object> mockIntent = Map.of(
                "status", "complete",
                "metadata", metadata
        );
        mockMvc.perform(put("/confirmPayment/1"))
                .andExpectAll(
        );

    }



}
