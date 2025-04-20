package cloestics;

import closetics.MainApplication;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = MainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ClothingAndStatsSystemTest {
    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void clothingAndClothingStatsTest() throws JSONException {
        // Test createClothing endpoint TEST 1
        Response response1 = RestAssured.given().
                header("Content-Type", "application/json").
                body("{\n" +
                        "    \"brand\": \"Test\",\n" +
                        "    \"color\": \"Test\",\n" +
                        "    \"dateBought\": \"2025-04-18\",\n" +
                        "    \"imagePath\": \"./image1\",\n" +
                        "    \"favorite\": true,\n" +
                        "    \"itemName\": \"Test\",\n" +
                        "    \"material\": \"Test\",\n" +
                        "    \"price\": 9.99,\n" +
                        "    \"size\": \"Test\",\n" +
                        "    \"specialType\": 2,\n" +
                        "    \"clothingType\": 4,\n" +
                        "    \"userId\": 13\n" +
                        "}").
                post("/createClothing");

        // Check status code
        assertEquals(200, (int) response1.getStatusCode());
    }
}