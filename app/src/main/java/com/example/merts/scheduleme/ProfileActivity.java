package com.example.merts.scheduleme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    TextView profiletview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_draweer_open, R.string.navigation_draweer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {


            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AddalarmFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_addalarm);
        }


        // mAuth = FirebaseAuth.getInstance();
        // mUser = mAuth.getCurrentUser();
        // profiletview = (TextView) findViewById(R.id.textView);
        //profiletview.setText(mUser.getEmail());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_addalarm:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddalarmFragment()).commit();
                break;
            case R.id.nav_addlocation:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddlocationFragment()).commit();
                break;
            case R.id.nav_takenote:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TakenoteFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfilFragment()).commit();
                break;

            case R.id.nav_localactivity:
                break;
            case R.id.nav_signout:

                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {  //açıp kapamayı left side da yapıyor. Sağ taraf için start yerine end
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void signOut(View v) {
        mAuth.signOut();
        finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
