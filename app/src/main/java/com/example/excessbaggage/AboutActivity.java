package com.example.excessbaggage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutActivity extends AppCompatActivity {

    private static final String APP_URL =
            "https://github.com/elisefeaa/Bag-Wise";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display About page layout
        setContentView(R.layout.activity_about);

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.aboutToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.about_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set up clickable GitHub repository link
        TextView tvAppWebsite = findViewById(R.id.tvAppWebsite);

        tvAppWebsite.setText(APP_URL);
        tvAppWebsite.setTextColor(
                getResources().getColor(android.R.color.holo_blue_dark)
        );

        tvAppWebsite.setOnClickListener(v -> {
            try {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(APP_URL)
                );

                startActivity(browserIntent);

            } catch (Exception e) {
                Toast.makeText(
                        AboutActivity.this,
                        "Unable to open the repository.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
