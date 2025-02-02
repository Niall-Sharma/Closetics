package coms309.Classes;
//These 4 imports say there unused, but they are being used
import org.apache.catalina.connector.Request.*;
import org.apache.catalina.connector.Response.*;
import org.springframework.http.MediaType.*;
import org.springframework.web.bind.annotation.RequestBody.*;
import okhttp3.*;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.*;
@AllArgsConstructor
public class User extends ConvertsToJSON {

    @Getter
    @NonNull
    private UUID user_id;

    @Getter
    @NonNull
    private String username;

    @Getter
    @NonNull
    private String password;

    @Getter
    @NonNull
    private String first_name;

    @Getter
    @NonNull
    private String last_name;

    public void updateUsername(String new_username) {
        String old_username = this.username;
        this.username = new_username;
        if (!updateUser()) {this.username = old_username;}
    }
    public void updatePassword(String new_password) {
        String old_password = this.password;
        this.password = new_password;
        if (!updateUser()) {this.password = old_password;}
    }
    public void updateFirstName(String new_first_name) {
        String old_first_name = this.first_name;
        this.first_name = new_first_name;
        if (!updateUser()) {this.first_name = old_first_name;}
    }
    public void updateLastName(String new_last_name) {
        String old_last_name = this.last_name;
        this.last_name = new_last_name;
        if (!updateUser()) {this.last_name = old_last_name;}
    }

    @Override
    public String toJSON() {
        return "{" +
                "\"user_id\": \"" + escapeJSON(user_id.toString()) + "\"," +
                "\"username\": \"" + escapeJSON(username) + "\"," +
                "\"password\": \"" + escapeJSON(password) + "\"," +
                "\"first_name\": \"" + escapeJSON(first_name) + "\"," +
                "\"last_name\": \"" + escapeJSON(last_name) + "\"" +
                "}";
    }

    public void createUser() {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(toJSON(), mediaType);
            Request request = new Request.Builder().url("http://localhost:8080/addUser").post(body)
                    .addHeader("Content-Type", "application/json").build();
            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Response: " + response.body().string());
            } else {
                System.out.println("Error: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser() {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            String url = "http://localhost:8080/deleteUser/" + this.username;
            Request request = new Request.Builder().url(url).method("DELETE", body).build();
            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Response: " + response.body().string());
            } else {
                System.out.println("Error: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updateUser() {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(toJSON(), mediaType);
            Request request = new Request.Builder().url("http://localhost:8080/updateUser").put(body)
                    .addHeader("Content-Type", "application/json").build();
            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Response: " + response.body().string());
                return true;
            } else {
                System.out.println("Error: " + response.code());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}