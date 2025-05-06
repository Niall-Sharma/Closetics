package com.example.closetics.payment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.MainActivity;
import com.example.closetics.R;

import com.example.closetics.UserManager;
import com.example.closetics.outfits.OutfitManager;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.*;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity {
    private final String STRIPE_PUBLISHABLE_KEY = "pk_test_51RBJo6PBUCiZ3v4PB9Mg76Yxtmg9NCpNaFHQWPjLL9VSNbPw4MmY5aQ3XZmLN7tiAKc2XKz3PbCGPT0l7hXrZW2Z00R4dNM1Su";

    private ImageButton backButton;
    private Button basicBuyButton, premiumBuyButton;
    private TextView currentTierText, currentTierDescriptionText;
    private TextView basicTierText, basicTierDescriptionText;
    private TextView premiumTierText, premiumTierDescriptionText;

    private PaymentSheet paymentSheet;
    private String paymentIntentClientSecret;
    private String paymentIntentId;
    private PaymentSheet.CustomerConfiguration customerConfig;

    private String userTier;
    private boolean isBuyingBasic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        backButton = findViewById(R.id.payment_back_button);
        basicBuyButton = findViewById(R.id.payment_basic_buy_button);
        premiumBuyButton = findViewById(R.id.payment_premium_buy_button);
        currentTierText = findViewById(R.id.payment_current_tier_name_text);
        currentTierDescriptionText = findViewById(R.id.payment_current_tier_description_text);
        basicTierText = findViewById(R.id.payment_basic_tier_text);
        basicTierDescriptionText = findViewById(R.id.payment_basic_tier_description_text);
        premiumTierText = findViewById(R.id.payment_premium_tier_text);
        premiumTierDescriptionText = findViewById(R.id.payment_premium_tier_description_text);

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        PaymentConfiguration.init(getApplicationContext(), STRIPE_PUBLISHABLE_KEY);

        backButton.setOnClickListener(v -> {
            // return to Main
            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            intent.putExtra("OPEN_FRAGMENT", 3); // open fragment Profile
            startActivity(intent);
        });

        if (UserManager.getUsername(getApplicationContext()) != null) {
            basicBuyButton.setOnClickListener(v -> {
                buyBasic(UserManager.getUserID(getApplicationContext()));
            });

            premiumBuyButton.setOnClickListener(v -> {
                buyPremium(UserManager.getUserID(getApplicationContext()));
            });
        }

        // get and populate current tier data
        getUserTier(UserManager.getUserID(getApplicationContext()));
    }

    private void buyBasic(long userId) {
        PaymentManager.createPaymentRequest(getApplicationContext(), userId, 499, "Basic",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Success creating payment: " + response.toString());

                        try {
                            paymentIntentClientSecret = response.getString("clientSecret");
                            paymentIntentId = response.getString("paymentIntentId");

                            isBuyingBasic = true;
                            presentPaymentSheet();
                        } catch (JSONException e) {
                            Log.e("JSON Error", "Error parsing payment clientSecret: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error creating payment: " + error.toString());
                    }
                });
    }

    private void buyPremium(long userId) {
        PaymentManager.createPaymentRequest(getApplicationContext(), userId, 699, "Premium",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Success creating payment: " + response.toString());

                        try {
                            paymentIntentClientSecret = response.getString("clientSecret");
                            paymentIntentId = response.getString("paymentIntentId");

                            isBuyingBasic = false;
                            presentPaymentSheet();
                        } catch (JSONException e) {
                            Log.e("JSON Error", "Error parsing payment clientSecret: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error creating payment: " + error.toString());
                    }
                });
    }

    private void presentPaymentSheet() {
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Closetics, Inc.")
                // .customer(customerConfig)
                // Set `allowsDelayedPaymentMethods` to true if your business handles payment methods
                // delayed notification payment methods like US bank accounts.
                // .allowsDelayedPaymentMethods(true)
                .build();
        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret//,
                //configuration
        );
    }

    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d("PaymentSheetResult", "Canceled");

            confirmPayment();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e("PaymentSheetResult", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());

            confirmPayment();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            Log.d("PaymentSheetResult", "Completed");

            confirmPayment();
            //updateUserTier();

            // save userTier
            UserManager.saveUserTier(getApplicationContext(), isBuyingBasic ? 1 : 2);

            // return to Main
            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            intent.putExtra("OPEN_FRAGMENT", 3); // open fragment Profile
            startActivity(intent);
        }
    }

    private void getUserTier(long userId) {
        UserManager.getUserByIdRequest(getApplicationContext(), userId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Success getting user for tier: " + response);

                        try {
                            userTier = null;
                            if (response.has("userTier")) {
                                userTier = response.getString("userTier");
                            }

                            populateUserTier();
                        }
                        catch (JSONException e) {
                            Log.e("JSON Error", "Error getting user object for tier: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error getting user for tier: " + error.toString());
                    }
                });
    }

    private void populateUserTier() {
        if (userTier == null) userTier = "Free";

        if (userTier.equals("Basic")) {
            currentTierText.setText("Basic");
            currentTierDescriptionText.setText("+   Clothes limit: 30\n+   Outfits limit: 30\n+   All Free features\n+   Wearing statistics");
            // Hide buying Basic UI
            basicTierText.setVisibility(TextView.GONE);
            basicTierDescriptionText.setVisibility(TextView.GONE);
            basicBuyButton.setVisibility(TextView.GONE);
        } else if (userTier.equals("Premium")) {
            currentTierText.setText("Premium");
            currentTierDescriptionText.setText("+   Clothes limit: Unlimited\n+   Outfits limit: Unlimited\n+   All Basic features\n+   Leaderboard access\n+   Cosmetic badge");
            // Hide buying Basic UI
            basicTierText.setVisibility(TextView.GONE);
            basicTierDescriptionText.setVisibility(TextView.GONE);
            basicBuyButton.setVisibility(TextView.GONE);
            // Hide buying Premium UI
            premiumTierText.setVisibility(TextView.GONE);
            premiumTierDescriptionText.setVisibility(TextView.GONE);
            premiumBuyButton.setVisibility(TextView.GONE);
        }
        // No changes needed if Free
    }

    private void confirmPayment() {
        PaymentManager.confirmPaymentRequest(getApplicationContext(), paymentIntentId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", "Success confirming payment: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error confirming payment: " + error.toString());
                    }
                });
    }

    private void updateUserTier() {
        UserManager.updateUserTierRequest(getApplicationContext(), UserManager.getUserID(getApplicationContext()),
                isBuyingBasic ? "Basic" : "Premium",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Success changing user tier: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error changing user tier: " + error.toString());
                    }
                });
    }
}