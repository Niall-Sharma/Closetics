package com.example.closetics.clothes;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.closetics.MainActivity;
import com.example.closetics.VolleyByteArrayRequest;
import com.example.closetics.VolleyMultipartRequest;
import com.example.closetics.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Clothes manager is a helper class with public static methods.
 * It takes care of adding endpoint requests to the volley request queue for the /clothes endpoint.
 *
 */
public class ClothesManager {

    private static final String URL_GET_CLOTHING_IMAGE = MainActivity.SERVER_URL + "/clothingImages/"; // + {{clothingId}}


    /*
    More work here adding more types and figuring out the best methods of input for them, currently just an edit text
     */

    /**
     * Sends a Volley POST request to save a piece of clothing to the backend controller.
     *
     * <p>This method collects clothing attributes from a list of MutableLiveData fragments,
     * builds a JSON object, and submits it via a POST request to the provided URL.</p>
     *
     * @param context the context used to access the Volley request queue
     * @param fragments a list of MutableLiveData<String> containing clothing data:
     *                  <ul>
     *                      <li>0 - favorite (yes/no)</li>
     *                      <li>1 - size</li>
     *                      <li>2 - color</li>
     *                      <li>3 - date bought</li>
     *                      <li>4 - price</li>
     *                      <li>5 - item name</li>
     *                      <li>6 - brand</li>
     *                      <li>7 - material</li>
     *                  </ul>
     * @param userId the ID of the user saving the clothing item
     * @param URL the base URL of the server endpoint (without "/createClothing")
     * @param responseListener callback for handling a successful server response
     * @param errorListener callback for handling a server error or network failure
     *
     */

    public static void saveClothingRequest(Context context, ArrayList<MutableLiveData<String>> fragments, long userId,
    String URL, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String favorite = fragments.get(1).getValue();
        String size = fragments.get(2).getValue();
        String color = fragments.get(3).getValue();
        String dateBought = fragments.get(4).getValue();
        String brand = fragments.get(7).getValue();
        String itemName = fragments.get(6).getValue();
        String material = fragments.get(8).getValue();
        String price = fragments.get(5).getValue();
        String nonParsed = fragments.get(9).getValue();
        String [] parsed = nonParsed.split(",");
        String clothingType = parsed[0];
        String specialType = parsed[1];



        //Create the json object of the saveClothing data
        JSONObject saveClothing = new JSONObject();



        //Use try catch blocks when creating JSON objects

        //Making sure a boolean is sent to the backend
        try {

            if (favorite.toLowerCase().trim().equals("yes")) {
                nullCheck("favorite", true, saveClothing);
            } else {
                nullCheck("favorite", false, saveClothing);
            }


            nullCheck("size", size, saveClothing);
            nullCheck("color", color, saveClothing);
            nullCheck("dateBought", dateBought, saveClothing);
            nullCheck("brand", brand, saveClothing);
            nullCheck("itemName", itemName, saveClothing);
            nullCheck("material", material, saveClothing);
            nullCheck("price", price, saveClothing);
            nullCheck("clothingType", clothingType,saveClothing);
            //nullCheck("specialType", ClothingItem.typeConnections[Integer.valueOf(clothingType) - 1][Integer.valueOf(specialType)], saveClothing);
            nullCheck("specialType", specialType, saveClothing);


            saveClothing.put("userId", userId);


        } catch (JSONException e) {
            Log.e("JSON Error", e.toString());
            return;
        }

        //The post request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL + "/createClothing",
                saveClothing, responseListener, errorListener);
        //Add request to the volley singleton request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


    /**
     *Sends a volley DELETE request to the backend controller deleting the clothing item.
     *
     * <p>This method collects the clothing ID of the item to be deleted along with the proper URL endpoint
     * and deletes the specified clothing item from the database using a by creating a String Request. </p>
     *
     *@param context the context used to access the Volley request queue
     *@param clothingId the ID of the piece of clothing being deleted
     * @param URL the URL of the server endpoint
     * @param responseListener callback for handling a successful server response
     * @param errorListener callback for handling a server error or network failure
     */

    public static void deleteClothingRequest(Context context, Long clothingId, String URL,
                                             Response.Listener<String> responseListener,
                                             Response.ErrorListener errorListener) {
        String deleteUrl = URL + "/deleteClothing/" + clothingId;

        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                deleteUrl,
                responseListener,
                errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     *Sends a volley PUT request to the backend controller to update a clothing item.
     *
     * <p>
     *     This method collects a clothing object and updates the object in the backend at the same clothing ID
     *     to fit the attributes of the new object by creating a JSONObjectRequest.
     * </p>
     *
     *@param context the context used to access the Volley request queue
     * @param updateObject the updated JSON object to replace the current clothing item
     * @param URL the URL of the server endpoint
     * @param responseListener callback for handling a successful server response
     * @param errorListener callback for handling a server error or network failure
     */

    public static void updateClothingRequest(Context context, JSONObject updateObject, String URL,
                                             Response.Listener<JSONObject> responseListener,
                                             Response.ErrorListener errorListener) {
        String updateUrl = URL + "/updateClothing";


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                updateUrl,
                updateObject,
                responseListener,
                errorListener
        );
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


    /**
     * Volley GET request to retrieve a list of specific users clothing items
     * <p>
     *     This method collects a user ID to grab all clothing items belonging to that user
     *     via a JSONArrayRequest.
     * </p>
     *@param context the context used to access the Volley request queue
     *@param userId the ID of the user of who's clothes are to be viewed
     *@param URL the base URL of the server endpoint (without "/getClothing/user/")
     *@param responseListener callback for handling a successful server response
     *@param errorListener callback for handling a server error or network failure
     *
     *
     */
    public static void getClothingByUserRequest(Context context, long userId, String URL,
                                                Response.Listener<JSONArray> responseListener,
                                                Response.ErrorListener errorListener) {
        String getUrl = URL + "/getClothing/user/" + userId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                getUrl, null, responseListener, errorListener);

        //if (request.getBody() == null){
           // responseListener.onResponse(new JSONArray());
        //}



        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     *Sends a Volley GET request to retrieve one clothing item.
     *
     * <p>
     *     This method collects a clothing ID and returns the JSON object associated with that clothing ID
     *     via a JSONObjectRequest.
     * </p>
     *
     * @param context the context used to access the Volley request queue
     * @param clothingId the ID of the piece of clothing to retrieved
     * @param URL the URL of the server's endpoint
     * @param responseListener callback for handling a successful server response
     * @param errorListener callback for handling a server error or network failure
     */
    public static void getClothingRequest(Context context, long clothingId, String URL,
                                          Response.Listener<JSONObject> responseListener,
                                          Response.ErrorListener errorListener){

        String getUrl = URL + "/" + clothingId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                getUrl, null, responseListener, errorListener);





        VolleySingleton.getInstance(context).addToRequestQueue(request);

    }

    /**
     * Sends a Volley GET request to retrieve clothing items of a specific type for a user.
     *
     * <p>
     *     This method collects the user ID and the clothing type ID to fetch a list of clothing items
     *     that match the specified type, using a JsonArrayRequest.
     * </p>
     *
     * @param context the context used to access the Volley request queue
     * @param userId the ID of the user whose clothing items are to be retrieved
     * @param URL the base URL of the server endpoint (without "/type/{userId}/{type}")
     * @param type the type ID of the clothing items to retrieve
     * @param responseListener callback for handling a successful server response
     * @param errorListener callback for handling a server error or network failure
    */

    public static void getClothingByTypeRequest(Context context, long userId, String URL, long type, Response.Listener<JSONArray> responseListener,
                                                Response.ErrorListener errorListener){
        //clothes/type/{userId}/{type}
        String getUrl = URL + "/type/" + userId +"/"+type;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getUrl, null, responseListener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(request);


    }
    public static void addImage(Context context, Long clothingId, byte[] imageBytes, String URL,
                                Response.Listener<NetworkResponse> responseListener,
                                Response.ErrorListener errorListener) {
        String addImageUrl = URL + "/addImage/" + clothingId;


        //Create the responselistener and error listener in the clothes activity
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(
            Request.Method.PUT, addImageUrl, responseListener, errorListener);

        multipartRequest.addFile("imageFile", new VolleyMultipartRequest.DataPart("image.jpg", imageBytes,  "image/jpeg"));

        Volley.newRequestQueue(context).add(multipartRequest);
        

        /*
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                addImageUrl,
                URL,
                responseListener,
                errorListener
        );
        */

    //VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void getImageByClothing(Context context, long clothingId, String URL, Response.Listener<byte[]> responseListener,
                                          Response.ErrorListener errorListener){

        //clothingImages/{clothingId}
        String getUrl = URL + "/clothingImages/" + clothingId;
         //request = new JsonArrayRequest(Request.Method.GET, getUrl, null, responseListener, errorListener);
        VolleyByteArrayRequest request= new VolleyByteArrayRequest(Request.Method.GET, getUrl, responseListener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(request);

    }

    public static void getClothingImage(Context context, long clothingId, Response.Listener<Bitmap> responseListener,
                                          Response.ErrorListener errorListener){

        ImageRequest request = new ImageRequest(
                URL_GET_CLOTHING_IMAGE + clothingId,
                responseListener,
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config
                errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(request);

    }





    /**
     * Utility method to safely add a key-value pair to a JSONObject.
     *
     * <p>
     *     This method checks if the provided parameter is not null before inserting it into
     *     the given JSONObject under the specified header name. If the parameter is null,
     *     the key-value pair is not added.
     * </p>
     *
     * @param header the key under which the value should be stored
     * @param parameter the value to store in the JSONObject
     * @param object the JSONObject to which the key-value pair will be added
     * @throws JSONException if there is an error adding data to the JSONObject
     */

    public static void nullCheck(String header, Object parameter, JSONObject object) throws JSONException {
        if (parameter != null){
            object.put(header, parameter);
        }
    }


}

