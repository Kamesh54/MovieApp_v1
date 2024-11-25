package com.kamesh.movieapp_v1.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kamesh.movieapp_v1.Database.DatabaseHelper;
import com.kamesh.movieapp_v1.R;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmailSignup, etPasswordSignup;
    private Button btnSignup;
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.etName);
        etEmailSignup = findViewById(R.id.etEmailSignup);
        etPasswordSignup = findViewById(R.id.etPasswordSignup);
        btnSignup = findViewById(R.id.btnSignup);
        dbHelper = new DatabaseHelper(this);

        btnSignup.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmailSignup.getText().toString().trim();
            String password = etPasswordSignup.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean isInserted = dbHelper.addUser(name, email, password);
                if (isInserted) {
                    Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();
                    finish(); // Return to login screen
                } else {
                    Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Window w=getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }
}
