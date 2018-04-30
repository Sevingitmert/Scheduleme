package com.example.merts.scheduleme;



import android.content.Intent;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.webkit.WebView;

import android.webkit.WebViewClient;

import android.widget.Button;



public class LocalActivity extends AppCompatActivity {

    private WebView webView;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_local);

        webView = findViewById(R.id.webview);


        //webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("http://www.biletix.com/search/TURKIYE/tr?category_sb=-1&date_sb=-1&city_sb=Eski%C5%9Fehir#!city_sb:Eski%C5%9Fehir");


        Button button = findViewById(R.id.button_goback);

        button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent intent = new Intent(LocalActivity.this, ProfileActivity.class);

                startActivity(intent);

            }

        });


    }


    @Override

    public void onBackPressed() {

        if (webView.canGoBack()) {

            webView.goBack();

        } else {

            super.onBackPressed();

        }


    }
}