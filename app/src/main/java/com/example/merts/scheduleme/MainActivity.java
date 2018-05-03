package com.example.merts.scheduleme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        if (settings.contains("email") && settings.contains("password") || settings.contains("gmailstring")) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }

    public void openRegister(View v) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public void openLogin(View v) {
        System.out.println("BUTTON CLICKED");
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

}
