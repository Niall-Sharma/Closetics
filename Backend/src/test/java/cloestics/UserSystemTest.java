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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // Ensure tests run in order
public class UserSystemTest {

    @LocalServerPort
    int port;

    // Store state between tests
    private static long testUserId;
    private static String testUsername = "systest";
    private static String testEmail = "systest@iastate.edu";
    private static String currentPassword = "Password!123"; // Initial password
    private static final String securityAnswer = "TestAnswer";

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void test01_Signup() throws JSONException {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(String.format("{\n" +
                        "  \"email\":\"%s\",\n" +
                        "  \"name\":\"System Test User\",\n" +
                        "  \"password\":\"%s\",\n" +
                        "  \"userTier\":\"Free\",\n" +
                        "  \"username\":\"%s\",\n" +
                        "  \"sQA1\":\"%s\",\n" +
                        "  \"sQID1\":\"1\",\n" +
                        "  \"sQA2\":\"OtherAnswer\",\n" +
                        "  \"sQID2\":\"2\",\n" +
                        "  \"sQA3\":\"AnotherAnswer\",\n" +
                        "  \"sQID3\":\"3\"\n" +
                        "}", testEmail, currentPassword, testUsername, securityAnswer))
                .post("/signup");

        assertEquals(200, response.getStatusCode());
        JSONObject responseJson = new JSONObject(response.getBody().asString());

        assertTrue(responseJson.has("token"), "Response should contain token");
        assertTrue(responseJson.has("user_id"), "Response should contain user_id");
        assertEquals("Login successful", responseJson.getString("message"));

        testUserId = Long.parseLong(responseJson.getString("user_id"));
        assertTrue(testUserId > 0, "User ID should be positive");
    }

    @Test
    public void test02_LoginWithUsername() {
        assertNotEquals(0, testUserId, "testUserId not set");

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(String.format("{\n" +
                        "  \"username\":\"%s\",\n" +
                        "  \"password\":\"%s\"\n" +
                        "}", testUsername, currentPassword))
                .post("/login");

        assertEquals(200, response.getStatusCode());
        response.then().body("message", equalTo("Login successful"));
        response.then().body("user_id", equalTo(String.valueOf(testUserId)));
        assertNotNull(response.jsonPath().getString("token"));
    }

    @Test
    public void test03_LoginWithEmail() {
         assertNotEquals(0, testUserId, "testUserId not set");

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(String.format("{\n" +
                        "  \"email\":\"%s\",\n" +
                        "  \"password\":\"%s\"\n" +
                        "}", testEmail, currentPassword))
                .post("/login");

        assertEquals(200, response.getStatusCode());
        response.then().body("message", equalTo("Login successful"));
        response.then().body("user_id", equalTo(String.valueOf(testUserId)));
        assertNotNull(response.jsonPath().getString("token"));
    }

     @Test
    public void test04_GetUserById() throws JSONException {
        assertNotEquals(0, testUserId, "testUserId not set");

        Response response = RestAssured.given()
                .get("/users/" + testUserId);

        assertEquals(200, response.getStatusCode());
        JSONObject userJson = new JSONObject(response.getBody().asString());

        assertEquals(testUserId, userJson.getLong("userId"));
        assertEquals(testUsername, userJson.getString("username"));
        assertEquals(testEmail, userJson.getString("email"));
        assertEquals("System Test User", userJson.getString("name"));
        assertEquals("Free", userJson.getString("userTier"));
    }

     @Test
    public void test05_GetUserByUsername() throws JSONException {
        assertNotNull(testUsername, "testUsername not set");

        Response response = RestAssured.given()
                .get("/users/username/" + testUsername);

        assertEquals(200, response.getStatusCode());
         JSONObject userJson = new JSONObject(response.getBody().asString());

        assertEquals(testUserId, userJson.getLong("userId"));
        assertEquals(testUsername, userJson.getString("username"));
        assertEquals(testEmail, userJson.getString("email"));
    }

    @Test
    public void test06_SearchUserByUsername() throws JSONException {
        assertNotNull(testUsername, "testUsername not set");
        // Use a substring of the username
        String partialUsername = testUsername.substring(0, testUsername.length() - 2);

        Response response = RestAssured.given()
                .get("/searchUsersByUsername/" + partialUsername);

        assertEquals(200, response.getStatusCode());
        JSONArray responseArray = new JSONArray(response.getBody().asString());

        assertTrue(responseArray.length() > 0, "Search should return at least one result");

        // Verify our test user is in the results
        boolean found = false;
        for (int i = 0; i < responseArray.length(); i++) {
            if (responseArray.getJSONObject(i).getLong("userId") == testUserId) {
                assertEquals(testUsername, responseArray.getJSONObject(i).getString("username"));
                found = true;
                break;
            }
        }
        assertTrue(found, "Test user not found in search results");
    }

    @Test
    public void test07_UpdateUserDetails() throws JSONException {
        assertNotEquals(0, testUserId, "testUserId not set");
        String newName = "System Test User Updated";
        String newEmail = "updated_" + testEmail;

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(String.format("{\n" +
                        "  \"userId\": %d,\n" +
                        "  \"name\": \"%s\",\n" +
                        "  \"email\": \"%s\"\n" + // Also update email
                        "}", testUserId, newName, newEmail))
                .put("/updateUser");

        assertEquals(200, response.getStatusCode());
        JSONObject updatedUserJson = new JSONObject(response.getBody().asString());

        assertEquals(newName, updatedUserJson.getString("name"));
        assertEquals(newEmail, updatedUserJson.getString("email")); // Verify email update
        assertEquals(testUsername, updatedUserJson.getString("username")); // Username shouldn't change here

        testEmail = newEmail;

        // Verify by getting the user again
        Response getResponse = RestAssured.given().get("/users/" + testUserId);
        assertEquals(200, getResponse.getStatusCode());
        JSONObject fetchedUserJson = new JSONObject(getResponse.getBody().asString());
        assertEquals(newName, fetchedUserJson.getString("name"));
         assertEquals(newEmail, fetchedUserJson.getString("email"));
    }


    @Test
    public void test08_UpdatePasswordWithOldPassword() {
        assertNotEquals(0, testUserId, "testUserId not set");
        String newPassword = currentPassword + "_new";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(String.format("{\n" +
                        "  \"userId\": %d,\n" +
                        "  \"oldPassword\": \"%s\",\n" +
                        "  \"newPassword\": \"%s\"\n" +
                        "}", testUserId, currentPassword, newPassword))
                .put("/updatePassword");

        assertEquals(200, response.getStatusCode());
        response.then().body("message", equalTo("Password change successful"));

        currentPassword = newPassword;
    }

    @Test
    public void test09_LoginWithNewPassword() {
         assertNotEquals(0, testUserId, "testUserId not set");

        // Try logging in with the updated password
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(String.format("{\n" +
                        "  \"username\":\"%s\",\n" +
                        "  \"password\":\"%s\"\n" +
                        "}", testUsername, currentPassword)) // Use the updated currentPassword
                .post("/login");

        assertEquals(200, response.getStatusCode());
        response.then().body("message", equalTo("Login successful"));
    }

    @Test
    public void test10_UpdatePasswordWithSecurityQuestion() {
         assertNotEquals(0, testUserId, "testUserId not set");
         String finalPassword = currentPassword + "_final";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(String.format("{\n" +
                        "  \"userId\": %d,\n" +
                        "  \"securityQuestionId\": \"1\",\n" + // Matches SQ set during signup
                        "  \"securityQuestionAnswer\": \"%s\",\n" + // Matches SQ answer set during signup
                        "  \"newPassword\": \"%s\"\n" +
                        "}", testUserId, securityAnswer, finalPassword))
                .put("/updatePassword");

        assertEquals(200, response.getStatusCode());
        response.then().body("message", equalTo("Password change successful"));

        currentPassword = finalPassword; // Update for final login test
    }

     @Test
    public void test11_LoginWithFinalPassword() {
         assertNotEquals(0, testUserId, "testUserId not set");

        // Try logging in with the final password set via SQ
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(String.format("{\n" +
                        "  \"username\":\"%s\",\n" +
                        "  \"password\":\"%s\"\n" +
                        "}", testUsername, currentPassword))
                .post("/login");

        assertEquals(200, response.getStatusCode());
        response.then().body("message", equalTo("Login successful"));
    }


    @Test
    public void test12_DeleteUser() {
        assertNotEquals(0, testUserId, "testUserId not set");

        Response response = RestAssured.given()
                .delete("/users/" + testUserId);

        assertEquals(200, response.getStatusCode()); // Assuming delete returns 200 OK on success

        // Verify deletion
        Response getResponse = RestAssured.given()
                .get("/users/" + testUserId);

        assertTrue(getResponse.getStatusCode() == 404 || getResponse.getStatusCode() == 500,
                 "Expected 404 or 500 after user deletion, but got: " + getResponse.getStatusCode());
    }
}
