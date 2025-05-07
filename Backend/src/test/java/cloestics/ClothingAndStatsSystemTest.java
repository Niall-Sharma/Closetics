package cloestics;

import closetics.MainApplication;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // Ensure tests run in order
public class ClothingAndStatsSystemTest {
    @LocalServerPort
    int port;

    // Store IDs of created items to use across tests
    private static long testClothingId;
    private static long tempOutfitId;
    private static String expectedCreationDate;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        // Set expected creation date once
        if (expectedCreationDate == null) {
            expectedCreationDate = java.time.LocalDate.now().toString();
        }
    }

    @Test
    public void test00_CreateImage() throws JSONException{
        //Tests the add clothing endpoint
        Response response = RestAssured.given().header("Content-Type", "application/json").body(
                "{\n" +
                        ""
        ).post();
    }

    @Test
    public void test01_CreateClothing() throws JSONException {
        // Test createClothing endpoint
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                body("{\n" +
                        "    \"brand\": \"TestBrand\",\n" +
                        "    \"color\": \"TestColor\",\n" +
                        "    \"dateBought\": \"2025-01-01\",\n" +
                        "    \"imagePath\": \"./test_image.png\",\n" +
                        "    \"favorite\": true,\n" +
                        "    \"itemName\": \"Test Item\",\n" +
                        "    \"material\": \"TestMaterial\",\n" +
                        "    \"price\": 19.99,\n" +
                        "    \"size\": \"M\",\n" +
                        "    \"specialType\": 1,\n" +
                        "    \"clothingType\": 2,\n" +
                        "    \"userId\": 13\n" + // Use test user ID 13
                        "}").
                post("/createClothing");

        // Check status code
        assertEquals(200, response.getStatusCode());

        // Parse response
        JSONObject responseJson = new JSONObject(response.getBody().asString());

        // Assert basic fields
        assertTrue(responseJson.getLong("clothesId") > 0, "Expected a valid clothing ID");
        testClothingId = responseJson.getLong("clothesId"); // Store ID for later tests

        assertEquals("TestBrand", responseJson.getString("brand"));
        assertEquals("TestColor", responseJson.getString("color"));
        assertEquals("2025-01-01", responseJson.getString("dateBought"));
        assertEquals("./test_image.png", responseJson.getString("imagePath"));
        assertTrue(responseJson.getBoolean("favorite"));
        assertEquals("Test Item", responseJson.getString("itemName"));
        assertEquals("TestMaterial", responseJson.getString("material"));
        assertEquals(19.99, responseJson.getDouble("price"), 0.001);
        assertEquals("M", responseJson.getString("size"));
        assertEquals(1, responseJson.getInt("specialType"));
        assertEquals(2, responseJson.getInt("clothingType"));

        JSONObject userJson = responseJson.getJSONObject("user");
        assertEquals(13, userJson.getInt("userId"));
        assertEquals("TestCaseOnly", userJson.getString("username"));

        JSONObject statsJson = responseJson.getJSONObject("clothingStats");
        assertEquals(0, statsJson.getInt("timesWorn"));
        assertEquals(-1000.0, statsJson.getDouble("avgHighTemp"), 0.001);
        assertEquals(-1000.0, statsJson.getDouble("avgLowTemp"), 0.001);
    }

    @Test
    public void test02_GetClothingById() throws JSONException {
        assertNotEquals(0, testClothingId, "testClothingId not set");

        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                get("/getClothing/" + testClothingId);

        assertEquals(200, response.getStatusCode());
        JSONObject responseJson = new JSONObject(response.getBody().asString());

        assertEquals(testClothingId, responseJson.getLong("clothesId"));
        assertEquals("Test Item", responseJson.getString("itemName"));
        assertEquals(13, responseJson.getJSONObject("user").getInt("userId"));
    }

    @Test
    public void test03_GetClothingByUser() throws JSONException {
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                get("/getClothing/user/13");

        assertEquals(200, response.getStatusCode());
        JSONArray responseArray = new JSONArray(response.getBody().asString());

        // Find our test item in the list
        boolean found = false;
        for (int i = 0; i < responseArray.length(); i++) {
            if (responseArray.getJSONObject(i).getLong("clothesId") == testClothingId) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Created clothing item not found in user's list");
    }

    @Test
    public void test04_GetClothingByType() throws JSONException {
         Response response = RestAssured.given().
                header("Content-Type", "application/json").
                get("/getClothing/type/13/2"); // Type 2 used during creation

        assertEquals(200, response.getStatusCode());
        JSONArray responseArray = new JSONArray(response.getBody().asString());

        boolean found = false;
        for (int i = 0; i < responseArray.length(); i++) {
             if (responseArray.getJSONObject(i).getLong("clothesId") == testClothingId) {
                assertEquals(2, responseArray.getJSONObject(i).getInt("clothingType"));
                found = true;
                break;
            }
        }
        assertTrue(found, "Created clothing item not found when searching by type");
    }

     @Test
    public void test05_GetClothingBySpecialType() throws JSONException {
         Response response = RestAssured.given().
                header("Content-Type", "application/json").
                get("/getClothing/special_type/13/1"); // Special Type 1 used during creation

        assertEquals(200, response.getStatusCode());
        JSONArray responseArray = new JSONArray(response.getBody().asString());

        boolean found = false;
        for (int i = 0; i < responseArray.length(); i++) {
             if (responseArray.getJSONObject(i).getLong("clothesId") == testClothingId) {
                assertEquals(1, responseArray.getJSONObject(i).getInt("specialType"));
                found = true;
                break;
            }
        }
        assertTrue(found, "Created clothing item not found when searching by special type");
    }

    @Test
    public void test06_SwapFavoriteOnClothing() throws JSONException {
        assertNotEquals(0, testClothingId, "testClothingId not set");

        // Initial state should be favorite: true
        // Swap favorite status
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                put("/swapFavoriteOnClothing/" + testClothingId);

        assertEquals(200, response.getStatusCode());
        JSONObject responseJson = new JSONObject(response.getBody().asString());
        assertFalse(responseJson.getBoolean("favorite"), "Favorite should now be false");

        // Swap back to true for consistency in later tests
        Response responseSwapBack = RestAssured.given().
                header("Content-Type", "application/json").
                put("/swapFavoriteOnClothing/" + testClothingId);
        assertEquals(200, responseSwapBack.getStatusCode());
        JSONObject responseJsonSwapBack = new JSONObject(responseSwapBack.getBody().asString());
        assertTrue(responseJsonSwapBack.getBoolean("favorite"), "Favorite should be true again");
    }

    @Test
    public void test07_UpdateClothing() throws JSONException {
        assertNotEquals(0, testClothingId, "testClothingId not set");

        // Test updateClothing endpoint
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                body(String.format("{\n" +
                        "    \"clothingId\": %d,\n" +
                        "    \"brand\": \"UpdatedBrand\",\n" +
                        "    \"color\": \"UpdatedColor\",\n" +
                        "    \"dateBought\": \"2024-12-25\",\n" +
                        "    \"imagePath\": \"./updated_image.jpg\",\n" +
                        "    \"favorite\": false,\n" +
                        "    \"itemName\": \"Updated Item\",\n" +
                        "    \"material\": \"UpdatedMaterial\",\n" +
                        "    \"price\": 29.95,\n" +
                        "    \"size\": \"L\",\n" +
                        "    \"specialType\": 3,\n" +
                        "    \"clothingType\": 5,\n" +
                        "    \"userId\": 13\n" +
                        "}", testClothingId)).
                put("/updateClothing");

        assertEquals(200, response.getStatusCode());
        JSONObject responseJson = new JSONObject(response.getBody().asString());

        assertEquals(testClothingId, responseJson.getLong("clothesId"));
        assertEquals("UpdatedBrand", responseJson.getString("brand"));
        assertEquals("UpdatedColor", responseJson.getString("color"));
        assertEquals("2024-12-25", responseJson.getString("dateBought"));
        assertEquals("./updated_image.jpg", responseJson.getString("imagePath"));
        assertFalse(responseJson.getBoolean("favorite")); // Updated favorite
        assertEquals("Updated Item", responseJson.getString("itemName"));
        assertEquals("UpdatedMaterial", responseJson.getString("material"));
        assertEquals(29.95, responseJson.getDouble("price"), 0.001);
        assertEquals("L", responseJson.getString("size"));
        assertEquals(3, responseJson.getInt("specialType"));
        assertEquals(5, responseJson.getInt("clothingType"));
        assertEquals(13, responseJson.getJSONObject("user").getInt("userId"));
    }

    @Test
    public void test08_GetClothingStats() throws JSONException {
        assertNotEquals(0, testClothingId, "testClothingId not set");

        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                get("/getClothingStats/" + testClothingId);

        assertEquals(200, response.getStatusCode());
        JSONObject responseJson = new JSONObject(response.getBody().asString());

        int timesWorn = responseJson.getInt("timesWorn");
        assertTrue(timesWorn >= 0);

        // Only check default temps if unworn
        if (timesWorn == 0) {
             assertEquals(-1000.0, responseJson.getDouble("avgHighTemp"), 0.001);
             assertEquals(-1000.0, responseJson.getDouble("avgLowTemp"), 0.001);
             assertTrue(responseJson.getJSONArray("datesWorn").length() == 0);
        } else {
            assertNotEquals(-1000.0, responseJson.getDouble("avgHighTemp"), 0.001);
            assertNotEquals(-1000.0, responseJson.getDouble("avgLowTemp"), 0.001);
             assertFalse(responseJson.getJSONArray("datesWorn").length() == 0);
        }
    }

    @Test
    public void test09_WornClothingToday() throws JSONException {
        assertNotEquals(0, testClothingId, "testClothingId not set");

        // Get current wear count
        Response getStatsResponse = RestAssured.given().get("/getClothingStats/" + testClothingId);
        assertEquals(200, getStatsResponse.getStatusCode());
        int initialWearCount = new JSONObject(getStatsResponse.getBody().asString()).getInt("timesWorn");

        // Mark as worn
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                put("/wornClothingToday/" + testClothingId);

        assertEquals(200, response.getStatusCode());
        JSONObject responseJson = new JSONObject(response.getBody().asString());

        assertEquals(initialWearCount + 1, responseJson.getInt("timesWorn"));

        JSONArray datesWorn = responseJson.getJSONArray("datesWorn");
        assertEquals(initialWearCount + 1, datesWorn.length());
        JSONObject latestWearRecord = datesWorn.getJSONObject(datesWorn.length() - 1); // Get the latest record
        assertEquals(java.time.LocalDate.now().toString(), latestWearRecord.getString("dateWorn"));
        double highTemp = latestWearRecord.getDouble("highTemp");
        double lowTemp = latestWearRecord.getDouble("lowTemp");

        // Check that temps are fetched (not default -1000)
        // Note: This might fail if weather API is down or returns error codes
        assertNotEquals(-1000.0, highTemp, 0.001, "High temp should be updated");
        assertNotEquals(-1000.0, lowTemp, 0.001, "Low temp should be updated");

        // Average temps should reflect all valid wear records
        assertNotEquals(-1000.0, responseJson.getDouble("avgHighTemp"), 0.001);
        assertNotEquals(-1000.0, responseJson.getDouble("avgLowTemp"), 0.001);
    }

    @Test
    public void test10_GetStatsAfterWearing() throws JSONException {
        assertNotEquals(0, testClothingId, "testClothingId not set");

        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                get("/getClothingStats/" + testClothingId);

        assertEquals(200, response.getStatusCode());
        JSONObject statsJson = new JSONObject(response.getBody().asString());
        assertEquals(1, statsJson.getInt("timesWorn"), "Times worn should be 1 after calling /wornClothingToday");
    }

    @Test
    public void test10a_GetCostPerWear() {
        assertNotEquals(0, testClothingId, "testClothingId not set from previous tests.");
        // This test assumes test07_UpdateClothing set the price to 29.95
        // and test09_WornClothingToday was called once, making timesWorn = 1.

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .get("/getCostPerWear/" + testClothingId);

        assertEquals(200, response.getStatusCode(), "Expected HTTP 200 for getCostPerWear");

        // The response body should be a float or null
        String responseBody = response.getBody().asString();
        try {
            // Assuming price is 29.95 and timesWorn is 1 from previous tests
            float expectedCostPerWear = 29.95f / 1.0f;
            float actualCostPerWear = Float.parseFloat(responseBody);
            assertEquals(expectedCostPerWear, actualCostPerWear, 0.001, "Calculated cost per wear is incorrect.");
        } catch (NumberFormatException e) {
            fail("Response body is not a valid float: " + responseBody, e);
        }

        // Test case: clothing item not found
        Response notFoundResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .get("/getCostPerWear/999999"); // Non-existent ID
        assertEquals(404, notFoundResponse.getStatusCode(), "Expected HTTP 404 for non-existent clothing ID");
    }

    @Test
    public void test11_NumberOfOutfitsIn() throws JSONException {
        assertNotEquals(0, testClothingId, "testClothingId not set");

        // 1. Check initial count (should be 0)
        Response response1 = RestAssured.given()
                .put("/numberOfOutfitsIn/" + testClothingId);
        assertEquals(200, response1.getStatusCode());
        assertEquals("0", response1.getBody().asString(), "Initial numberOfOutfitsIn should be 0");

        // 2. Create a temporary outfit for user 13
        Response createOutfitResponse = RestAssured.given().
                header("Content-Type", "application/json").
                body("{\"userId\": 13, \"outfitName\": \"Temp Outfit For Clothing Test\", \"favorite\": false}").
                post("/createOutfit");
        assertEquals(200, createOutfitResponse.getStatusCode());
        tempOutfitId = new JSONObject(createOutfitResponse.getBody().asString()).getLong("outfitId");
        assertTrue(tempOutfitId > 0, "Failed to create temporary outfit");

        // 3. Add the test clothing item to the temporary outfit
        Response addItemResponse = RestAssured.given()
                .put("/addItemToOutfit/" + tempOutfitId + "/" + testClothingId);
        assertEquals(200, addItemResponse.getStatusCode(), "Failed to add item to outfit");

        // 4. Check count again (should be 1)
        Response response2 = RestAssured.given()
                .put("/numberOfOutfitsIn/" + testClothingId);
        assertEquals(200, response2.getStatusCode());
        assertEquals("1", response2.getBody().asString(), "numberOfOutfitsIn should be 1 after adding to outfit");

        Response deleteOutfitResponse = RestAssured.given()
                .delete("/deleteOutfit/" + tempOutfitId);
        assertEquals(200, deleteOutfitResponse.getStatusCode(), "Failed to delete temporary outfit");

        // 6. Check count one last time (should be 0 again)
         Response response3 = RestAssured.given()
                 .put("/numberOfOutfitsIn/" + testClothingId);
         assertEquals(200, response3.getStatusCode());
         assertEquals("0", response3.getBody().asString(), "numberOfOutfitsIn should be 0 after outfit deletion");
    }


    @Test
    public void test12_GetUsersMostExpensiveClothing() throws JSONException {
         Response response = RestAssured.given().
                header("Content-Type", "application/json").
                get("/getUsersMostExpensiveClothing/13");
        assertEquals(200, response.getStatusCode());

        if (!response.getBody().asString().equals("null") && !response.getBody().asString().isEmpty()) {
            JSONObject responseJson = new JSONObject(response.getBody().asString());
            assertTrue(responseJson.has("clothesId"));
        }
    }

    @Test
    public void test13_GetUsersMostWornClothing() throws JSONException {
         Response response = RestAssured.given().
                header("Content-Type", "application/json").
                get("/getUsersMostWornClothing/13");
        assertEquals(200, response.getStatusCode());

        if (!response.getBody().asString().equals("null") && !response.getBody().asString().isEmpty()) {
            JSONObject responseJson = new JSONObject(response.getBody().asString());
            assertTrue(responseJson.has("clothesId"), "Expected a clothing item object");
        }
    }

    @Test
    public void test13_GetUsersColdestAvgClothing() throws JSONException {
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                get("/getUsersColdestAvgClothing/13");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void test14_GetUsersWarmestAvgClothing() throws JSONException {
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                get("/getUsersWarmestAvgClothing/13");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void test15_DeleteClothing() {
        assertNotEquals(0, testClothingId, "testClothingId not set");

        // Delete the created clothing item
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                delete("/deleteClothing/" + testClothingId);

        assertEquals(200, response.getStatusCode());
        // Body should be empty for void return type in controller
        assertEquals("", response.getBody().asString().trim());


        // Verify stats deletion
        Response getStatsResponse = RestAssured.given().
                header("Content-Type", "application/json").
                get("/getClothingStats/" + testClothingId);
        assertEquals(500, getStatsResponse.getStatusCode());
    }
}