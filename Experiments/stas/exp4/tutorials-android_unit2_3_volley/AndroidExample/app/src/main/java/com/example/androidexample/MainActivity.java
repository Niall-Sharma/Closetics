package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String url = "https://jsonplaceholder.typicode.com/users/1";

    private Spinner spMethod;
    private EditText etUrl;
    private EditText etRequest;
    private TextView tvResponse;
    private Button btnSend;

    private String method;
    private String requestBody;
    private String responseBody;

    private TextView messageText;   // define message textview variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* initialize UI elements */
        etUrl = findViewById(R.id.etUrl);
        etRequest = findViewById(R.id.etRequest);
        tvResponse = findViewById(R.id.tvResponse);
        btnSend = findViewById(R.id.sendBtn);

        etUrl.setText(url);

        // method spinner
        Spinner spMethod = findViewById(R.id.spMethod);
        String[] methods = new String[]{"GET", "POST"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, methods);
        spMethod.setAdapter(adapter);
        spMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                method = (String) parent.getItemAtPosition(position);
                if (method.equals("GET")) etRequest.setText("Leave this field empty");
                else etRequest.setText("Enter JSON object here");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                method = "GET";
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                url = etUrl.getText().toString();
                requestBody = etRequest.getText().toString();
                if (method.equals("GET")) getRequest();
                else postRequest();
            }
        });
    }

    private void getRequest() {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // String response can be converted to JSONObject via
                        // JSONObject object = new JSONObject(response);
                        tvResponse.setText("Response is: "+ response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvResponse.setText("That didn't work!" + error.toString());
                    }
                }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
    //                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
    //                headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
    //                params.put("param1", "value1");
    //                params.put("param2", "value2");
                    return params;
                }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void postRequest() {

        // Convert input to JSONObject
        JSONObject postBody = null;
        try{
            // etRequest should contain a JSON object string as your POST body
            // similar to what you would have in POSTMAN-body field
            // and the fields should match with the object structure of @RequestBody on sb
            postBody = new JSONObject(etRequest.getText().toString());
        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        tvResponse.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvResponse.setText(error.getMessage());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
                //                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //                params.put("param1", "value1");
                //                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}