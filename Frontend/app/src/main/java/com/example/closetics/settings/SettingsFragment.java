package com.example.closetics.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.example.closetics.DeleteUserActivity;
import com.example.closetics.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

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
                return true; // Return true if the event is handled.
            });
        }

        Preference changepasswordPref = findPreference("change_password");
        if (changepasswordPref != null) {
            changepasswordPref.setOnPreferenceClickListener((preference) -> {
                Log.d("Preferences", "Change password was clicked");
                return true; // Return true if the event is handled.
            });
        }

        Preference logoutPref = findPreference("log_out");
        if (logoutPref != null) {
            logoutPref.setOnPreferenceClickListener((preference) -> {
                Log.d("Preferences", "Log out was clicked");
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