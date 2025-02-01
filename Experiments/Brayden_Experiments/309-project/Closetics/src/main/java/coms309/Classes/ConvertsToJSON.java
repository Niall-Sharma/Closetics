package coms309.Classes;

import java.text.SimpleDateFormat;

public abstract class ConvertsToJSON {
    private String objectAsJSON = null;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format the date

    // Escaping special characters in strings for JSON
    public String escapeJSON(String value) {
        if (value == null) {
            return null;
        }
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public abstract String toJSON();
}
