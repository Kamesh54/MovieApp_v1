package com.kamesh.movieapp_v1.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.kamesh.movieapp_v1.databinding.ActivityIntroBinding;

public class IntroActivity extends AppCompatActivity {
    ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Make the window edge-to-edge
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // On Start button click, decide the next activity based on login status
        binding.startBtn.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                // Navigate to MainActivity
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                String username = preferences.getString("username", "");
                String email = preferences.getString("email", "");
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);
            } else {
                // Navigate to LoginActivity
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            // Finish IntroActivity to prevent back navigation
            finish();
        });
    }
}
