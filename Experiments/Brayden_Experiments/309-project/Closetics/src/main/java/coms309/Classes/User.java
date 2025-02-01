package coms309.Classes;

import java.util.Date;
import java.util.UUID;

public class User extends ConvertsToJSON {
    private UUID user_id;
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private Date user_created_date;

    public User(UUID user_id, String username, String password, String first_name,
                String last_name, Date user_created_date) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_created_date = user_created_date;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Date getUser_created_date() {
        return user_created_date;
    }

    @Override
    public String toJSON() {
        return "{" +
                "\"user_id\": \"" + escapeJSON(user_id.toString()) + "\"," +
                "\"username\": \"" + escapeJSON(username) + "\"," +
                "\"password\": \"" + escapeJSON(password) + "\"," +
                "\"first_name\": \"" + escapeJSON(first_name) + "\"," +
                "\"last_name\": \"" + escapeJSON(last_name) + "\"," +
                "\"user_created_date\": \"" + (user_created_date != null ? dateFormat.format(user_created_date) : null) + "\"" +
                "}";
    }

}


