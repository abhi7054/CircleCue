package com.devabhi.circlecue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText;
    TextInputEditText passwordEditText;

    Button loginButton, registerButton;

    TextView forgotPasswordTextView, aboutUsTextView, faqsTextView, showcaseTextView;

    String emailEntered, passwordEntered, picString;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setStatusBarColor(getColor(R.color.black));
        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        emailEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerPageButton);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        aboutUsTextView = findViewById(R.id.aboutUsTextView);
        faqsTextView = findViewById(R.id.faqsTextView);
        showcaseTextView = findViewById(R.id.showcaseTextView);

        preferences = getSharedPreferences(Constants.preference, MODE_PRIVATE);
        editor = preferences.edit();

        setOnClickListeners();


        if(!preferences.getString(Constants.id, "").equals("")){

            Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
            mainActivityIntent.putExtra("pic", picString);
            startActivity(mainActivityIntent);
            finish();
        }

    }


    void setOnClickListeners() {

        aboutUsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalLink("https://circlecue.com/about.html");
            }
        });

        faqsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalLink("https://circlecue.com/faq.html");
            }
        });

        showcaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalLink("https://circlecue.com/user/showcase");
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailEntered = emailEditText.getText().toString();
                passwordEntered = passwordEditText.getText().toString();

                if (emailEntered.equals(""))
                    Toast.makeText(LoginActivity.this, "Please enter the email", Toast.LENGTH_SHORT).show();
                if (passwordEntered.equals(""))
                    Toast.makeText(LoginActivity.this, "Please enter the password", Toast.LENGTH_SHORT).show();

                if (!emailEntered.equals("") && !passwordEntered.equals("")) {
                    Log.e("Email", emailEntered);
                    Log.e("Password", passwordEntered);

                    login(emailEntered, passwordEntered, "false");
                }


            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(registrationIntent);
            }
        });
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(i);
            }
        });

    }

    void openExternalLink(String link) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        startActivity(intent);

    }

    public void login(String id, String password, String type){

        final String loginURL = Constants.loginURL+"?id="+id+"&password="+password+"&type="+type;



        StringRequest loginRequest = new StringRequest(Request.Method.GET, loginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            Log.e("Login URL", loginURL);

                            JSONObject loginJSONObject = new JSONObject(response);

                            Log.e("Login response", String.valueOf(loginJSONObject));

                            Boolean status = loginJSONObject.getBoolean("Status");
                            String message = loginJSONObject.getString("Message");

                            if(status){

                                picString = loginJSONObject.getString("pic");

                                editor.putBoolean(Constants.status, status);
                                editor.putString(Constants.id, loginJSONObject.getString("Data"));
                                editor.putString(Constants.username, loginJSONObject.getString("username"));
                                editor.putString(Constants.pic, loginJSONObject.getString("pic"));
                                editor.putString(Constants.phone, loginJSONObject.getString("phone"));
                                editor.putString(Constants.message, loginJSONObject.getString("Message"));

                                editor.apply();



                                Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                                mainActivityIntent.putExtra("pic", picString);
                                startActivity(mainActivityIntent);
                                finish();



                            }else{

                                Toast.makeText(LoginActivity.this, "Please check the credentials", Toast.LENGTH_SHORT).show();

                            }


                        }catch (Exception e){
                            Log.e("Login Exception", String.valueOf(e));
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginActivity.this, String.valueOf(error), Toast.LENGTH_SHORT).show();

            }
        });



        loginRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue loginQueue = Volley.newRequestQueue(this);

        loginQueue.add(loginRequest);

    }
}
