package com.kamesh.movieapp_v1.Activities;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.kamesh.movieapp_v1.R;

public class SplitCalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_calculator);

        EditText numPeopleInput = findViewById(R.id.numPeopleInput);
        EditText ticketRateInput = findViewById(R.id.ticketRateInput);
        EditText snacksRateInput = findViewById(R.id.snacksRateInput);
        Button calculateButton = findViewById(R.id.calculateButton);
        TextView resultTextView = findViewById(R.id.resultTextView);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numPeopleText = numPeopleInput.getText().toString();
                String ticketRateText = ticketRateInput.getText().toString();
                String snacksRateText = snacksRateInput.getText().toString();

                if (!numPeopleText.isEmpty() && !ticketRateText.isEmpty() && !snacksRateText.isEmpty()) {
                    int numPeople = Integer.parseInt(numPeopleText);
                    double ticketRate = Double.parseDouble(ticketRateText);
                    double snacksRate = Double.parseDouble(snacksRateText);

                    if (numPeople > 0) {
                        double totalCost = (numPeople * ticketRate) + snacksRate;
                        double costPerPerson = totalCost / numPeople;

                        resultTextView.setText(String.format("Cost per person: ₹%.2f", costPerPerson));
                    } else {
                        resultTextView.setText("Number of people must be greater than 0.");
                    }
                } else {
                    resultTextView.setText("Please fill all fields.");
                }
            }
        });
    }
}
