package com.devabhi.circlecue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class RegistrationActivity extends AppCompatActivity {

    Button addSocialMediaButton, signUpButton;

    LinearLayout addSocialMediaLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fetchViews();
        setOnCLickListeners();

    }

    void fetchViews(){

        addSocialMediaButton = findViewById(R.id.socialMediaAccountButton);
        signUpButton = findViewById(R.id.signUpButton);
        addSocialMediaLayout = findViewById(R.id.addSocialMediaLayout);

    }

    void setOnCLickListeners(){

        addSocialMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addSocialMediaLayout.getVisibility() == View.VISIBLE)
                    addSocialMediaLayout.setVisibility(View.GONE);
                else
                    addSocialMediaLayout.setVisibility(View.VISIBLE);

            }
        });

    }
}
