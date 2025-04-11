package closetics.Payment;

import java.util.HashMap;
import java.util.Map;

import closetics.Users.User;
import closetics.Users.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
      long UID = Long.parseLong(request.get("userId").toString());
      String tier = request.get("tier").toString();

      Map<String, String> metadata = new HashMap<>();
      metadata.put("userId", String.valueOf(UID));
      metadata.put("tier", tier);

      PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setAmount(amount).setCurrency("usd").putAllMetadata(metadata).build();
      PaymentIntent intent = PaymentIntent.create(params);

      Map<String, Object> response = new HashMap<>();
      response.put("clientSecret", intent.getClientSecret());
      response.put("paymentIntentId", intent.getId());

      User user = userRepository.findById(UID).orElseThrow(() -> new RuntimeException("User Not Found"));


      TransactionHistory transactionHistory = new TransactionHistory(user, intent.getCurrency(), intent.getStatus(), intent.getAmount(), intent.getId(), tier);
      tRepository.save(transactionHistory);
      return ResponseEntity.ok(response);

    }
    catch(Exception e){
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }

  @PutMapping("/confirmPayment/{paymentIntentId}")
  public ResponseEntity<?> completePayment(@PathVariable("paymentIntentId") String paymentIntentId){
    try {
      PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
      Map<String,String> metadata = paymentIntent.getMetadata();
      User user = userRepository.findById(Long.parseLong(metadata.get("userId"))).orElseThrow(() -> new RuntimeException("User Not Found"));
      TransactionHistory transactionHistory = tRepository.findByPaymentIntentId(paymentIntent.getId()).orElseThrow(() -> new RuntimeException("Stripe Session Not Found"));

      System.out.println(paymentIntent.getStatus());
      if(paymentIntent.getStatus().equals("succeeded")){
        transactionHistory.setStatus(paymentIntent.getStatus());
        user.setUserTier(metadata.get("tier"));
        return ResponseEntity.ok("Payment Successfully Accepted");
      }
      else{
        tRepository.deleteByPaymentIntentId(paymentIntent.getId());
        return ResponseEntity.status(400).body("Payment Not Accepted");
      }
    }catch (StripeException e){
      return ResponseEntity.status(500).body("Stripe error: " + e.getMessage());
    } catch (RuntimeException e) {
      return  ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
    }
  }
}
