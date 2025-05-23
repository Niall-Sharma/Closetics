package com.example.closetics.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.example.closetics.DeleteUserActivity;
import com.example.closetics.MainActivity;
import com.example.closetics.R;
import com.example.closetics.UserManager;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // disable Account when not logged in
        PreferenceCategory accountCategory = findPreference("account_category");
        accountCategory.setVisible(
                UserManager.getUsername(getActivity().getApplicationContext()) != null);

        // set preference listeners
        SwitchPreferenceCompat notificationsPref = findPreference("notifications");
        if (notificationsPref != null) {
            notificationsPref.setOnPreferenceChangeListener((preference, newValue) -> {
                Log.d("Preferences", String.format("Notifications enabled: %s", newValue));
                return true; // Return true if the event is handled.
            });
        }

        ListPreference themePref = findPreference("theme");
        if (themePref != null) {
            themePref.setOnPreferenceChangeListener((preference, newValue) -> {
                Log.d("Preferences", String.format("Theme changed: %s", newValue));
                if ((newValue + "").equals("light")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (((newValue + "").equals("dark"))) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else { // system
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
                return true; // Return true if the event is handled.
            });
        }

        Preference changePasswordPref = findPreference("change_password");
        if (changePasswordPref != null) {
            changePasswordPref.setOnPreferenceClickListener((preference) -> {
                Log.d("Preferences", "Change password was clicked");
                return true; // Return true if the event is handled.
            });
        }

        Preference logoutPref = findPreference("log_out");
        if (logoutPref != null) {
            logoutPref.setOnPreferenceClickListener((preference) -> {
                Log.d("Preferences", "Log out was clicked");

                UserManager.clearSavedData(getActivity().getApplicationContext());

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("OPEN_FRAGMENT", 3); // open fragment Profile
                startActivity(intent);

                return true; // Return true if the event is handled.
            });
        }

        Preference feedbackPref = findPreference("feedback");
        if (feedbackPref != null) {
            feedbackPref.setOnPreferenceClickListener((preference) -> {
                Log.d("Preferences", "Feedback was clicked");
                // Address of a random Olive Garden
                Toast.makeText(getActivity(), "Please, mail your feedback to 3600 Westown Pkwy, West Des Moines, IA", Toast.LENGTH_LONG).show();
                return true; // Return true if the event is handled.
            });
        }

        Preference deleteAccPref = findPreference("delete_account");
        if (deleteAccPref != null) {
            deleteAccPref.setOnPreferenceClickListener((preference) -> {
                Log.d("Preferences", "Delete account was clicked");
                // open delete user activity
                Intent intent = new Intent(getActivity(), DeleteUserActivity.class);
                startActivity(intent);
                return true; // Return true if the event is handled.
            });
        }
    }
}