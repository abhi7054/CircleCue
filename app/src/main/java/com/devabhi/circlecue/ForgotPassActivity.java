package com.devabhi.circlecue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devabhi.circlecue.R;

import org.json.JSONObject;

public class ForgotPassActivity extends AppCompatActivity {

    EditText forgotPasswordEditText;
    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        forgotPasswordEditText = findViewById(R.id.forgotPasswordEmailEditText);
        confirmButton = findViewById(R.id.forgotPasswordButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(forgotPasswordEditText.getText().toString().equals("")){

                    Toast.makeText(ForgotPassActivity.this, "Enter an email", Toast.LENGTH_SHORT).show();

                }else{

                    login(forgotPasswordEditText.getText().toString(), "", "true");

                }

            }
        });


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

                            if(!status){

                                Toast.makeText(ForgotPassActivity.this, "Please check your email for new password", Toast.LENGTH_SHORT).show();

                            }


                        }catch (Exception e){
                            Log.e("Login Exception", String.valueOf(e));
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ForgotPassActivity.this, String.valueOf(error), Toast.LENGTH_SHORT).show();

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
