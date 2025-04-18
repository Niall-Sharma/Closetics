package cloestics;

import closetics.MainApplication;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class BraydenSystemTest {
    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void outfitAndOutfitStatsTest() throws JSONException {
        // Test createOutfit endpoint TEST 1
        Response response1 = RestAssured.given().
                header("Content-Type", "application/json").
                body("{\n" +
                        "  \"userId\": 13,\n" + //User meant for testing only
                        "  \"outfitName\": \"Test Outfit\",\n" +
                        "  \"favorite\": true\n" +
                        "}").
                post("/createOutfit");

        // Check status code
        assertEquals(200, (int) response1.getStatusCode());

        // Parse response as JSON
        JSONObject responseJson = new JSONObject(response1.getBody().asString());
        long testOutfitId = responseJson.getLong("outfitId");

        assertTrue(testOutfitId > 0);

        assertEquals(13, responseJson.getJSONObject("user").getInt("userId"));
        assertEquals("TestCaseOnly", responseJson.getJSONObject("user").getString("name"));
        assertEquals("TestCaseOnly", responseJson.getJSONObject("user").getString("username"));

        assertEquals(0, responseJson.getJSONObject("outfitStats").getInt("timesWorn"));
        assertEquals(-1000.0, responseJson.getJSONObject("outfitStats").getDouble("avgHighTemp"));
        assertEquals(-1000.0, responseJson.getJSONObject("outfitStats").getDouble("avgLowTemp"));

        assertEquals(0, responseJson.getJSONArray("outfitItems").length());
        assertEquals("Test Outfit", responseJson.getString("outfitName"));
        String expectedDate = java.time.LocalDate.now().toString();
        assertEquals(expectedDate, responseJson.getString("creationDate"));
        assertEquals(true, responseJson.getBoolean("favorite"));



        // Test swapFavoriteOnOutfit endpoint TEST 2
        Response response2 = RestAssured.given().
                header("Content-Type", "application/json").
                put(String.format("/swapFavoriteOnOutfit/%s", testOutfitId));

        // Check status code
        assertEquals(200, response2.getStatusCode());

        JSONObject tempJsonObj = new JSONObject(response2.getBody().asString());

        // Main test
        assertEquals(false, tempJsonObj.getBoolean("favorite"));

        responseJson = tempJsonObj; // Update responseJson to new expected values



        // Test addItemToOutfit endpoint TEST 3
        Response response3 = RestAssured.given().
                header("Content-Type", "application/json").
                put(String.format("/addItemToOutfit/%s/23", testOutfitId));

        // Check status code
        assertEquals(200, response3.getStatusCode());
        tempJsonObj = new JSONObject(response3.getBody().asString());

        JSONArray outfitItems = tempJsonObj.getJSONArray("outfitItems");


        // Main test
        JSONObject firstItem = outfitItems.getJSONObject(0);
        assertEquals(1, outfitItems.length());
        assertEquals(23, firstItem.getInt("clothesId"));

        responseJson = tempJsonObj; // Update responseJson to new expected values



        // Test getAllOutfitItems endpoint TEST 4
        Response response4 = RestAssured.given().
                header("Content-Type", "application/json").
                get(String.format("/getAllOutfitItems/%s", testOutfitId));

        // Check status code
        assertEquals(200, response4.getStatusCode());

        JSONArray responseArray = new JSONArray(response4.getBody().asString());
        JSONObject clothingItem = responseArray.getJSONObject(0);

        // Main tests
        assertEquals(1, responseArray.length()); // one for clothes, user and clothingStats
        assertEquals(23, clothingItem.getInt("clothesId"));



        // Test getAllUsersOutfits endpoint TEST 5
        Response response5 = RestAssured.given().
                header("Content-Type", "application/json").
                get(String.format("/getAllUserOutfits/%s", responseJson.getJSONObject("user").getInt("userId")));

        // Check status code
        assertEquals(200, response5.getStatusCode());

        responseArray = new JSONArray(response5.getBody().asString());
        JSONObject outfit = responseArray.getJSONObject(0);

        // Main tests
        assertEquals(1, responseArray.length());
        assertEquals(testOutfitId, outfit.getLong("outfitId"));



        // Test removeItemFromOutfit endpoint TEST 6
        Response response6 = RestAssured.given().
                header("Content-Type", "application/json").
                put(String.format("/removeItemFromOutfit/%s/23", testOutfitId));

        // Check status code
        assertEquals(200, response6.getStatusCode());

        tempJsonObj = new JSONObject(response6.getBody().asString());

        // Main test
        assertEquals("[]", tempJsonObj.getJSONArray("outfitItems").toString());



        // Test updateOutfit endpoint TEST 7
        Response response7 = RestAssured.given().
                header("Content-Type", "application/json").
                body(String.format("{\n" +
                        "  \"outfitId\":%s,\n" +
                        "  \"userId\": 13,\n" +
                        "  \"outfitName\": \"Test Outfit Updated\",\n" +
                        "  \"favorite\": true,\n" +
                        "  \"outfitItems\": [23]\n" +
                        "}", responseJson.getInt("outfitId"))).
                put("/updateOutfit");

        // Check status code
        assertEquals(200, response7.getStatusCode());

        responseJson = new JSONObject(response7.getBody().asString());

        // Main tests
        JSONArray itemsArray = responseJson.getJSONArray("outfitItems");
        assertEquals(1, itemsArray.length());

        JSONObject item = itemsArray.getJSONObject(0);
        assertEquals(23, item.getInt("clothesId"));

        assertEquals("Test Outfit Updated", responseJson.getString("outfitName"));
        assertEquals(true, responseJson.getBoolean("favorite"));


        // Test getOutfit endpoint TEST 8
        Response response8 = RestAssured.given().
                header("Content-Type", "application/json").
                get(String.format("/getOutfit/%s", testOutfitId));

        responseJson = new JSONObject(response8.getBody().asString());
        outfitItems = responseJson.getJSONArray("outfitItems");
        firstItem = outfitItems.getJSONObject(0);

        // Check status code
        assertEquals(200, response8.getStatusCode());

        // Main tests
        assertEquals(testOutfitId, responseJson.getLong("outfitId"));
        assertEquals(13, responseJson.getJSONObject("user").getInt("userId"));
        assertEquals("TestCaseOnly", responseJson.getJSONObject("user").getString("name"));
        assertEquals("TestCaseOnly", responseJson.getJSONObject("user").getString("username"));

        assertEquals(0, responseJson.getJSONObject("outfitStats").getInt("timesWorn"));
        assertEquals(-1000.0, responseJson.getJSONObject("outfitStats").getDouble("avgHighTemp"));
        assertEquals(-1000.0, responseJson.getJSONObject("outfitStats").getDouble("avgLowTemp"));

        assertEquals(1, outfitItems.length());
        assertEquals(23, firstItem.getInt("clothesId"));
        assertEquals("Test Outfit Updated", responseJson.getString("outfitName"));
        assertEquals(expectedDate, responseJson.getString("creationDate"));
        assertEquals(true, responseJson.getBoolean("favorite"));



        // Test getOutfit endpoint TEST 9
        Response response9 = RestAssured.given()
                .header("Content-Type", "application/json")
                .get(String.format("/getOutfitStats/%s", testOutfitId));

        // Check status code
        assertEquals(200, response9.getStatusCode());

        JSONObject responseJsonStats = new JSONObject(response9.getBody().asString());
        JSONArray datesWorns = responseJsonStats.optJSONArray("datesWorn");

        // Assert if the array exists, then check length
        if (datesWorns != null) {
            assertEquals(0, datesWorns.length());
        } else {
            // Fails the test and shows a clear message
            fail("Expected 'datesWorn' array to be present in the response");
        }

        assertEquals(0, responseJsonStats.getLong("timesWorn"));
        assertEquals(-1000.0, responseJsonStats.getDouble("avgHighTemp"));
        assertEquals(-1000.0, responseJsonStats.getDouble("avgLowTemp"));



        // Test wornOutfitToday endpoint TEST 10
        Response response10 = RestAssured.given().
                header("Content-Type", "application/json").
                put(String.format("/wornOutfitToday/%s", testOutfitId));

        // Check status code
        assertEquals(200, response10.getStatusCode());

        responseJsonStats = new JSONObject(response10.getBody().asString());

        // Main tests
        JSONArray datesWorn = responseJsonStats.getJSONArray("datesWorn");
        assertEquals(1, datesWorn.length());

        JSONObject dateEntry = datesWorn.getJSONObject(0);
        assertTrue(dateEntry.has("dateWorn"));
        assertTrue(dateEntry.has("highTemp"));
        assertTrue(dateEntry.has("lowTemp"));

        double highTemp = dateEntry.getDouble("highTemp");
        double lowTemp = dateEntry.getDouble("lowTemp");
        assertNotEquals(-1000.0, highTemp);
        assertNotEquals(-1000.0, lowTemp);

        String dateWorn = dateEntry.getString("dateWorn");
        String today = java.time.LocalDate.now().toString();
        assertEquals(today, dateWorn);

        assertEquals(highTemp, responseJsonStats.getDouble("avgHighTemp"), 0.001);
        assertEquals(lowTemp, responseJsonStats.getDouble("avgLowTemp"), 0.001);


        // Test deleteOutfit endpoint TEST 11
        Response response11 = RestAssured.given().
                header("Content-Type", "application/json").
                delete(String.format("/deleteOutfit/%s", testOutfitId));

        // Check status code
        assertEquals(200, response11.getStatusCode());
        // Checks that the body returns empty
        assertEquals("", response11.getBody().asString().trim());


        // Test getOutfit endpoint TEST 12
        Response response12 = RestAssured.given().
                header("Content-Type", "application/json").
                get(String.format("/getOutfit/%s", testOutfitId));

        // Check status code
        assertEquals(404, response12.getStatusCode());

    }

}

