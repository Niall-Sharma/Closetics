package closetics.Statistics;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class WeatherFetcher {
    public static WornRecord fetchWeatherData(LocalDate date) {
        try {
            String API_KEY = "36e16da93d8e48219b123107250204";
            String LATITUDE = "42.0347";
            String LONGITUDE = "-93.6199";
            String urlString = String.format(
                    "https://api.weatherapi.com/v1/forecast.json?key=%s&q=%s,%s&days=1",
                    API_KEY, LATITUDE, LONGITUDE
            );

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                System.err.println("Error: " + conn.getResponseCode());
                return new WornRecord(date,-1000f, -1000f);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.lines().collect(Collectors.joining());
            reader.close();

            JSONObject jsonResponse = new JSONObject(response);
            JSONObject dayData = jsonResponse.getJSONObject("forecast")
                    .getJSONArray("forecastday")
                    .getJSONObject(0)
                    .getJSONObject("day");

            Float minTemp = dayData.getFloat("maxtemp_f"); // Not sure why but they come in swapped
            Float maxTemp = dayData.getFloat("mintemp_f");

            return new WornRecord(date, minTemp, maxTemp);
        } catch (Exception e) {
            e.printStackTrace();
            return new WornRecord(date,-1000f, -1000f);
        }
    }

}