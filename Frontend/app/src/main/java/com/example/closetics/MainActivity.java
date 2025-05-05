package com.example.closetics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.closetics.recommendations.RecWebSocketService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity representing Closetics' main page.
 * Contains bottom navigation view.
 */
public class MainActivity extends AppCompatActivity {

    private static final String SERVER_ADDRESS = "10.0.2.2:8080";
    //public static final String SERVER_ADDRESS = "coms-3090-008.class.las.iastate.edu:8080";
    public static final String SERVER_URL = "http://" + SERVER_ADDRESS;
    public static final String SERVER_WS_URL = "ws://" + SERVER_ADDRESS;

    public static final Map<Integer, String> CLOTHING_TYPES = new HashMap<Integer, String>() {{
        put(1, "accessories");put(2, "activewear");put(3, "bottoms");put(4, "dresses");put(5, "footwear");put(6, "formalwear");put(7, "outerwear");put(8, "seasonal");put(9, "sleepwear");put(10, "tops");put(11, "undergarments");put(12, "workwear");
    }};
    public static final Map<Integer, String> CLOTHING_SPECIAL_TYPES = new HashMap<Integer, String>() {{
        put(81, "aprons");put(27, "a line dresses");put(13, "backpacks");put(10, "belts");put(51, "blazers");put(67, "blouses");put(28, "bodycon dresses");put(34, "boots");put(75, "boxers");put(73, "bras");put(76, "briefs");put(23, "capris");put(52, "cardigans");put(50, "coats");put(30, "cocktail dresses");put(6, "compression wear");put(72, "crop tops");put(45, "dress shirts");put(44, "evening gowns");put(38, "flats");put(36, "flip flops");put(48, "formal dresses");put(14, "glasses");put(11, "gloves");put(31, "gowns");put(5, "gym t shirts");put(12, "handbags");put(9, "hats");put(37, "heels");put(71, "hoodies");put(49, "jackets");put(17, "jeans");put(15, "jewelry");put(21, "leggings");put(39, "loafers");put(64, "loungewear");put(24, "maxi dresses");put(26, "midi dresses");put(25, "mini dresses");put(61, "nightgowns");put(7, "other");put(79, "overalls");put(40, "oxfords");put(60, "pajamas");put(77, "panties");put(56, "parkas");put(69, "polos");put(54, "ponchos");put(59, "rainwear");put(63, "robes");put(4, "running shorts");put(82, "safety gear");put(35, "sandals");put(8, "scarves");put(80, "scrubs");put(68, "shirts");put(19, "shorts");put(20, "skirts");put(62, "sleep shirts");put(41, "slippers");put(33, "sneakers");put(1, "sports bras");put(42, "suits");put(32, "sun dresses");put(22, "sweatpants");put(70, "sweatshirts");put(57, "swimwear");put(66, "tank tops");put(46, "ties");put(3, "tracksuits");put(18, "trousers");put(43, "tuxedos");put(65, "t shirts");put(74, "underwear");put(78, "uniforms");put(53, "vests");put(47, "waistcoats");put(16, "watches");put(55, "windbreakers");put(58, "winterwear");put(29, "wrap dresses");put(2, "yoga pants");
    }};

    private BottomNavigationView bottomNavView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // set saved theme
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String themeValue = preferences.getString("theme", "system");
        if (themeValue.equals("light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if ((themeValue.equals("dark"))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else { // system
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

        setContentView(R.layout.activity_main);             // link to Main activity XML

        bottomNavView = findViewById(R.id.bottom_nav_view);




        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_discover, R.id.navigation_profile)
//                .build();
        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavView, navController);

        // DEBUG CODE
//        UserManager.saveUsername(getApplicationContext(), "user1");
//        UserManager.saveUserID(getApplicationContext(), 1);

        // start websocket if person is logged in
        if (UserManager.getUsername(getApplicationContext()) != null) {
            startRecWebSocket();
        }

        // commented bc doesn't work properly
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            switch (extras.getInt("OPEN_FRAGMENT", 0)) {
//                case 1:
//                    navController.navigate(R.id.action_navigation_dashboard_to_navigation_recommendations);
//                    break;
//                case 2:
//                    navController.navigate(R.id.action_navigation_dashboard_to_navigation_clothes);
//                    break;
//                case 3:
//                    navController.navigate(R.id.action_navigation_dashboard_to_navigation_profile);
//                    break;
//            }
//        }

//        replaceFragment(new HomeFragment());

//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//
//            int itemId = item.getItemId();
//            if (itemId == R.id.home) {
//                replaceFragment(new HomeFragment());
//            }
//            else if (itemId == R.id.discover) {
//                replaceFragment(new DiscoverFragment());
//            }
//            else if (itemId == R.id.profile) {
//                replaceFragment(new ProfileFragment());
//            }
//
//            return true;
//        });
    }

    /**
     * Starts the Recommendations WebSocket if the user is logged in.
     * Should be called every time the app opens.
     */
    private void startRecWebSocket() {
        Intent serviceIntent = new Intent(this, com.example.closetics.recommendations.RecWebSocketService.class);
        serviceIntent.setAction("RecWebSocketConnect");
        startService(serviceIntent);
        Log.d("MainActivity", "RecWebSocket started");
    }

//    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayout, fragment);
//        fragmentTransaction.commit();
//    }
}

// What did 60 do when it get hungry?
// 68
// .