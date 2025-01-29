package Classes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class User {
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

    // JSON conversion method
    public String toJson() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format the date
        return "{" +
                "\"user_id\": \"" + escapeJson(user_id.toString()) + "\"," +
                "\"username\": \"" + escapeJson(username) + "\"," +
                "\"password\": \"" + escapeJson(password) + "\"," +
                "\"first_name\": \"" + escapeJson(first_name) + "\"," +
                "\"last_name\": \"" + escapeJson(last_name) + "\"," +
                "\"user_created_date\": \"" + (user_created_date != null ? dateFormat.format(user_created_date) : null) + "\"" +
                "}";
    }
    // Escaping special characters in strings for JSON
    private String escapeJson(String value) {
        if (value == null) {
            return null;
        }
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
