package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StringReqActivity extends AppCompatActivity {

    private Button btnStringReq;
    private Button btnStringReqWithBody;
    private TextView msgResponse;

    /** Uncomment to use: if sending a [GET] StringRequest */
    /** Use public API on the Internet */
//    private static final String URL_STRING_REQ = "https://jsonplaceholder.typicode.com/users/1";

    /** Uncomment to use: if sending a [GET] StringRequest */
    /** Use API created with POSTMAN-mockserver */
//    public static final String URL_STRING_REQ = "https://2aa87adf-ff7c-45c8-89bc-f3fbfaa16d15.mock.pstmn.io/users/1";


    /** Uncomment to use: if sending a [GET] StringRequest */
    /** Use backend: https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit2_1_onetoone */
//    public static final String URL_STRING_REQ = "http://10.0.2.2:8080/users/1";

    /** Uncomment to use: if sending a [POST] StringRequest */
    /** Use backend: https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit2_1_onetoone */
    public static final String URL_STRING_REQ = "http://10.0.2.2:8080/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_req);

        btnStringReq = (Button) findViewById(R.id.btnStringReq);
        btnStringReqWithBody = (Button) findViewById(R.id.btnStringReqWithBody);
        msgResponse = (TextView) findViewById(R.id.msgResponse);

        btnStringReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeStringReq();
            }
        });

        btnStringReqWithBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    // fields should match the attributes of the User Object at:
                    // https://git.las.iastate.edu/cs309/tutorials/-/blob/springboot_unit2_1_onetoone/springboot_example/src/main/java/onetoone/Users/User.java
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("name", "MY NAME");
                    jsonBody.put("emailId", 1);
                    jsonBody.put("ifActive", true);
                    makeStringReqWithBody(jsonBody);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Sends a string request [GET] without a body.
     */
    private void makeStringReq() {

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL_STRING_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Volley Response", response);
                        msgResponse.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors that occur during the request
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    /**
     * Makes a string request [POST] with a JSON object body.
     *
     * @param jsonBody The JSON object containing the data to be sent with the request. This
     *                 object should be properly formatted and include all necessary fields
     *                 to match the attributes of the corresponding Object on the server.
     */
    private void makeStringReqWithBody(JSONObject jsonBody) {
        final String mRequestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_STRING_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Volley Response", response);
                        msgResponse.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors that occur during the request
                        Log.e("Volley Error", error.toString());
                        msgResponse.setText(error.toString());
                    }
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.d("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}