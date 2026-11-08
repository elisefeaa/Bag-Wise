package com.example.excessbaggage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText etWeight, etRate;
    RadioGroup rgTicketClass;
    Button btnCalculate, btnReset;
    TextView tvResult;

    // Replace this with your actual GitHub repository URL later
    private static final String APP_URL =
            "https://github.com/elisefeaa/Bag-Wise";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        // Connect XML components
        etWeight = findViewById(R.id.etWeight);
        etRate = findViewById(R.id.etRate);
        rgTicketClass = findViewById(R.id.rgTicketClass);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        tvResult = findViewById(R.id.tvResult);

        // No ticket class selected when app starts
        rgTicketClass.clearCheck();

        // Calculate Button
        btnCalculate.setOnClickListener(v -> calculateFee());

        // Reset Button
        btnReset.setOnClickListener(v -> resetCalculator());
    }

    // Display Toolbar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handle Toolbar Menu Clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_share) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            shareIntent.putExtra(
                    Intent.EXTRA_SUBJECT,
                    "BagWise - Excess Baggage Calculator"
            );

            shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Check out BagWise!\nPack Smart. Fly Easy.\n" + APP_URL
            );

            startActivity(
                    Intent.createChooser(shareIntent, "Share BagWise")
            );

            return true;

        } else if (id == R.id.menu_about) {

            Intent intent = new Intent(
                    MainActivity.this,
                    AboutActivity.class
            );

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Calculate Excess Baggage Fee
    private void calculateFee() {

        String weightInput = etWeight.getText().toString().trim();
        String rateInput = etRate.getText().toString().trim();

        // Check empty weight
        if (weightInput.isEmpty()) {
            etWeight.setError("Please enter baggage weight.");
            etWeight.requestFocus();
            return;
        }

        // Check empty rate
        if (rateInput.isEmpty()) {
            etRate.setError("Please enter excess baggage rate.");
            etRate.requestFocus();
            return;
        }

        // Check ticket class selection
        if (rgTicketClass.getCheckedRadioButtonId() == -1) {
            Toast.makeText(
                    this,
                    "Please select a ticket class.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        double weight;
        double rate;

        try {
            weight = Double.parseDouble(weightInput);
            rate = Double.parseDouble(rateInput);

        } catch (NumberFormatException e) {
            Toast.makeText(
                    this,
                    "Please enter valid numbers.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // Validate weight and rate
        if (Double.isNaN(weight) || Double.isInfinite(weight)
                || weight <= 0) {

            etWeight.setError("Weight must be greater than 0.");
            etWeight.requestFocus();
            return;
        }

        if (Double.isNaN(rate) || Double.isInfinite(rate)
                || rate < 0) {

            etRate.setError("Rate cannot be negative.");
            etRate.requestFocus();
            return;
        }

        // Determine ticket class allowance
        int selectedClass = rgTicketClass.getCheckedRadioButtonId();

        double allowance;
        String ticketClass;

        if (selectedClass == R.id.rbBusiness) {
            allowance = 40;
            ticketClass = "Business";
        } else {
            allowance = 20;
            ticketClass = "Economy";
        }

        // Calculate excess weight
        double excessWeight = Math.max(weight - allowance, 0);

        // Calculate baggage charge
        double excessCharge = excessWeight * rate;

        // Calculate 8% service tax
        double serviceTax = excessCharge * 0.08;

        // Calculate total fee
        double totalFee = excessCharge + serviceTax;

        // Display results
        String result = String.format(
                Locale.getDefault(),
                "BAGWISE CALCULATION RESULTS\n\n" +
                        "Baggage Weight: %.2f kg\n" +
                        "Ticket Class: %s\n" +
                        "Baggage Allowance: %.2f kg\n" +
                        "Excess Weight: %.2f kg\n\n" +
                        "Excess Baggage Charge: RM %.2f\n" +
                        "Service Tax (8%%): RM %.2f\n\n" +
                        "TOTAL FEE: RM %.2f",
                weight,
                ticketClass,
                allowance,
                excessWeight,
                excessCharge,
                serviceTax,
                totalFee
        );

        tvResult.setText(result);
    }

    // Reset Calculator
    private void resetCalculator() {

        // Clear input fields
        etWeight.setText("");
        etRate.setText("");

        // Clear ticket class selection
        rgTicketClass.clearCheck();

        // Clear previous results
        tvResult.setText(
                "Your calculation results will appear here."
        );

        // Remove input error messages
        etWeight.setError(null);
        etRate.setError(null);

        // Show reset confirmation
        Toast.makeText(
                this,
                "Calculator has been reset.",
                Toast.LENGTH_SHORT
        ).show();
    }
}
