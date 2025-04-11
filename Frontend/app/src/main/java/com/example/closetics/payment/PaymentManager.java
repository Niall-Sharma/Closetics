package com.example.closetics.payment;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.closetics.MainActivity;
import com.example.closetics.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentManager {
    private static final String URL_POST_CREATE_PAYMENT = MainActivity.SERVER_URL + "/payments/createPayment";
    private static final String URL_PUT_CONFIRM_PAYMENT = MainActivity.SERVER_URL + "/payments/confirmPayment/"; // + {{paymentIntentId}}


    public static void createPaymentRequest(Context context, long userId, long amount, String tier,
                                                Response.Listener<JSONObject> responseListener,
                                                Response.ErrorListener errorListener) {

        JSONObject paymentData = new JSONObject();
        try {
            paymentData.put("userId", userId);
            paymentData.put("amount", amount);
            paymentData.put("tier", tier);
        } catch (JSONException e) {
            Log.e("JSON Error", e.toString());
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL_POST_CREATE_PAYMENT,
                paymentData,
                responseListener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


    public static void confirmPaymentRequest(Context context, String paymentIntentId,
                                            Response.Listener<String> responseListener,
                                            Response.ErrorListener errorListener) {

        StringRequest request = new StringRequest(
                Request.Method.PUT,
                URL_PUT_CONFIRM_PAYMENT + paymentIntentId,
                responseListener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}
