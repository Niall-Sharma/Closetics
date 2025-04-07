package closetics.Payment;

import java.util.HashMap;
import java.util.Map;

import closetics.Users.User;
import closetics.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import closetics.Payment.transactions.TransactionHistory;
import closetics.Payment.transactions.TransactionRepository;
import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/payments")
public class PaymentController{

  
  @Autowired
  TransactionRepository tRepository;

  @Autowired
  UserRepository userRepository;

  @Value("${stripe.api.key}")
  private String apiKey;

  @PostConstruct
  public void init(){
    Stripe.apiKey = apiKey;
  }

  @PostMapping("/createPayment")
  public ResponseEntity<Map<String,Object>> createPayment(@RequestBody Map<String,Object> request){
    try{
      long amount = Long.parseLong(request.get("amount").toString());
      long UID = Long.parseLong(request.get("uid").toString());

      PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setAmount(amount).setCurrency("usd").build();
      PaymentIntent intent = PaymentIntent.create(params);
      Map<String, Object> response = new HashMap<>();
      response.put("clientSecret", intent.getClientSecret());
      User user = userRepository.findById(UID).orElseThrow(() -> new RuntimeException("User Not Found"));
      TransactionHistory transactionHistory = new TransactionHistory(user, intent.getCurrency(), intent.getStatus(), intent.getAmount());
      tRepository.save(transactionHistory);
      return ResponseEntity.ok(response);

    }
    catch(Exception e){
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }
}
