package com.cs309.testing.Model;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cs309.testing.Presenter.MainActivityPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class RequestServerForService {
    IVolleyListener myListener;
    Context context;

    public RequestServerForService(Context c, IVolleyListener l) {
        this.context = c;
        this.myListener = l;
    }

    public void reverse(String str){
        String url = "http://10.0.2.2:8080/reverse";
        callServer(str, url);
    }

    public void capitalize(String str){
        String url = "http://10.0.2.2:8080/capitalize";
        callServer(str, url);
    }

    /**
     * Sends a string request with the given string to the given url
     * @param str
     * @param url
     */
    private void callServer(String str, String url){
        // Must be declared final to be used in inner class
        final String requestBody = str;

        // String post request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("VOLLEY", "SERVER RESPONSE: " + response);
                try {
                    JSONArray res = new JSONArray(response);
                    StringBuilder newText = new StringBuilder();
                    for (int i = 0; i < res.length() - 1; i++){
                        newText.append(res.getJSONObject(i).get("data") + ", ");
                    }
                    newText.append(res.getJSONObject(res.length()-1).get("data"));
                    myListener.onSuccess(newText.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if(error.networkResponse==null) return;
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                  //  Log.d(TAG, responseBody);
                    myListener.onError(responseBody);
                } catch (UnsupportedEncodingException e){
                    //Log.d(TAG,e.toString());
                }
            }
        }){
            @Override
            public String getBodyContentType() {
                return "text/plain; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }
}
