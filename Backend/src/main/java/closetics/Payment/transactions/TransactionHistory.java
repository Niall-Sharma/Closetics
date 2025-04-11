package closetics.Payment.transactions;

import java.time.LocalDateTime;

import closetics.Users.User;
import jakarta.persistence.*;

@Entity(name = "transaction_history_table")
public class TransactionHistory{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;


  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private String currency;

  private String status;

  private LocalDateTime localDateTime;

  private Long amount;

  private String paymentIntentId;

  private String tierName;



  public TransactionHistory(User user, String currency, String status, long amount, String paymentIntentId, String tierName){
    this.user = user;
    this.currency = currency;
    this.status = status;
    this.amount = amount;
    this.localDateTime = LocalDateTime.now();
    this.paymentIntentId = paymentIntentId;
    this.tierName = tierName;
  }

  public TransactionHistory(){
    
  }
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public void setLocalDateTime(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }

  public long getAmount() {
    return amount;
  }

  public void setAmount(long amount) {
    this.amount = amount;
  }

  public String getPaymentIntentId() {
    return paymentIntentId;
  }

  public void setPaymentIntentId(String paymentIntentId) {
    this.paymentIntentId = paymentIntentId;
  }

  public String getTierName() {
    return tierName;
  }

  public void setTierName(String tierName) {
    this.tierName = tierName;
  }
}
