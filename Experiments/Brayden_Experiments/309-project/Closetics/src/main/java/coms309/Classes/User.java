package coms309.Classes;

import okhttp3.*;
import java.util.UUID;
import lombok.*;


@AllArgsConstructor
public class User extends ConvertsToJSON {

    @Getter
    @NonNull
    private UUID userId;

    @Getter
    @NonNull
    private String username;

    @Getter
    @NonNull
    private String password;

    @Getter
    @NonNull
    private String firstName;

    @Getter
    @NonNull
    private String lastName;

    public void updateUsername(String newUsername) {
        String oldUsername = this.username;
        this.username = newUsername;
        if (!updateUser()) {this.username = oldUsername;}
    }
    public void updatePassword(String newPassword) {
        String oldPassword = this.password;
        this.password = newPassword;
        if (!updateUser()) {this.password = oldPassword;}
    }
    public void updateFirstName(String newFirstName) {
        String oldFirstName = this.firstName;
        this.firstName = newFirstName;
        if (!updateUser()) {this.firstName = oldFirstName;}
    }
    public void updateLastName(String newLastName) {
        String oldLastName = this.lastName;
        this.lastName = newLastName;
        if (!updateUser()) {this.lastName = oldLastName;}
    }

    @Override
    public String toJSON() {
        return "{" +
                "\"userId\": \"" + escapeJSON(userId.toString()) + "\"," +
                "\"username\": \"" + escapeJSON(username) + "\"," +
                "\"password\": \"" + escapeJSON(password) + "\"," +
                "\"firstName\": \"" + escapeJSON(firstName) + "\"," +
                "\"lastName\": \"" + escapeJSON(lastName) + "\"" +
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