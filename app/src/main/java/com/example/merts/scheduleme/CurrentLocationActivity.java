package com.example.merts.scheduleme;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Address;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class CurrentLocationActivity extends AppCompatActivity {
    private final static int PLACE_PICKER_REQUEST = 999;
    TextView t1, t2, t3, textViewLocations;
    Location targetLocation = new Location("");
    double targetlatitude, targetlongitude;
    String address;
    EditText e1, e2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference locationsRef = db.collection("Locations");
    String docid;
    FirebaseAuth mAuth;
    private String emailString;
    static int requestcode=1;
    Button setdistance;
    float selecteddistance=500;
    NumberPicker numberPicker;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkPermissionOnActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PLACE_PICKER_REQUEST:
                    Place place = PlacePicker.getPlace(data, this);
                    //String placeName = String.format("Place: %s", place.getName());
                    targetlatitude = place.getLatLng().latitude;
                    targetlongitude = place.getLatLng().longitude;
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                        address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getAddressLine(1);
                        //String country = addresses.get(0).getAddressLine(2);
                        t3.setText(address);
                        System.out.println("");

                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                    targetLocation.setLatitude(targetlatitude);
                    targetLocation.setLongitude(targetlongitude);


                    t1.setText(String.valueOf(targetlatitude));
                    t2.setText(String.valueOf(targetlongitude));


            }
        }
    }

    private void checkPermissionOnActivityResult(int requestCode, int resultCode, Intent data) {


    }

    private static final int REQUEST_CODE = 1000;
    TextView lat, lon;
    Button getLocation, stopupdates;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    @Override
    protected void onStart() {
        super.onStart();
        locationsRef.whereEqualTo("email", emailString)

                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            LocationClass locationClass = documentSnapshot.toObject(LocationClass.class);
                            locationClass.setDocumentId(documentSnapshot.getId());
                            //  String documentId = note.getDocumentId();
                            emailString = mAuth.getCurrentUser().getEmail();
                            String title = locationClass.getTitle();
                            String description = locationClass.getDescription();
                            String address = locationClass.getAddress();
                            data += "\nTitle: " + title + "\nDescription: " + description
                                    + "\nAddress: " + address
                                    + " \n\n";
                            //notebookRef.document(documentId)

                        }
                        textViewLocations.setText(data);

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        emailString = mAuth.getCurrentUser().getEmail();
        setContentView(R.layout.activity_current_location);

        getSupportActionBar().setTitle("Add Location");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            // for activty
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            // for fragment
            //startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        textViewLocations = findViewById(R.id.text_view_Locations);
        e1 = findViewById(R.id.locationtitle);
        e2 = findViewById(R.id.locationdescription);
        t1 = findViewById(R.id.pickedlatitude);
        t2 = findViewById(R.id.pickedlongitude);
        t3 = findViewById(R.id.address);
        numberPicker=findViewById(R.id.numberpicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10000000);
        numberPicker.setWrapSelectorWheel(true);
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                int diff = value *10;
                return "" + diff;
            }
        };
        numberPicker.setFormatter(formatter);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                selecteddistance=i1;
            }
        });
        setdistance=findViewById(R.id.choosedistance);
        setdistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberPicker.setVisibility(View.VISIBLE);
                setdistance.setVisibility(View.GONE);
                getLocation.setVisibility(View.VISIBLE);
            }
        });

        stopupdates = findViewById(R.id.stopupdates);
        lat = findViewById(R.id.latitude);
        lon = findViewById(R.id.longitude);
        getLocation = findViewById(R.id.getLocation);
        //check permissions runtime
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            //if permission is granted
            buildLocationRequest();
            buildLocationCallback();
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());//this olabilir
            getLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numberPicker.setVisibility(View.GONE);
                    setdistance.setVisibility(View.VISIBLE);
                    String locationtitle = e1.getText().toString();
                    final String locationdescription = e2.getText().toString();
                    String locationaddress = address;
                    emailString = mAuth.getCurrentUser().getEmail();
                    LocationClass locationClass = new LocationClass(emailString, locationtitle, locationdescription, locationaddress);
                    docid = locationClass.getDocumentId();
                    locationsRef.add(locationClass);
                    if (ActivityCompat.checkSelfPermission(CurrentLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(CurrentLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CurrentLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

                        return;
                    }
                    //Intent intentt=new Intent(CurrentLocationActivity.this,LocationReceiver.class);
                    //PendingIntent pendingIntent=PendingIntent.getService(getApplicationContext(),1,intentt,0);
                    //fusedLocationProviderClient.requestLocationUpdates(locationRequest, pendingIntent);
                    //startService(intentt);


                    Intent locationReceiverIntent = new Intent(getApplicationContext(), LocationBroadcast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestcode, locationReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                    fusedLocationProviderClient.requestLocationUpdates(locationRequest,pendingIntent);
                    startService(locationReceiverIntent);
                    //fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    //change state of button
                    getLocation.setEnabled(!getLocation.isEnabled());
                    stopupdates.setEnabled(!stopupdates.isEnabled());
                    SharedPreferences settings = getSharedPreferences("preferences",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    // Edit and commit
                    editor.putFloat("selecteddistance",selecteddistance);
                    editor.putFloat("tlat", (float) targetlatitude);
                    editor.putFloat("tlon", (float) targetlongitude);
                    editor.putString("address", address);
                    editor.putString("title", e1.getText().toString());
                    editor.putString("description", e2.getText().toString());
                    editor.commit();

                }
            });
            stopupdates.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ActivityCompat.checkSelfPermission(CurrentLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(CurrentLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CurrentLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

                        return;
                    }
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    //change state of button
                    getLocation.setEnabled(!getLocation.isEnabled());
                    stopupdates.setEnabled(!stopupdates.isEnabled());
                }
            });

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                    }
                }
            }
        }
    }


    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    lat.setText(String.valueOf(location.getLatitude()));
                    lon.setText(String.valueOf(location.getLongitude()));


                    if (ActivityCompat.checkSelfPermission(CurrentLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(CurrentLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CurrentLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

                        return;
                    }


                    //Intent intentt = new Intent(getApplicationContext(), LocationReceiver.class);
                    //PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intentt, 0);
                    //fusedLocationProviderClient.requestLocationUpdates(locationRequest, pendingIntent);

                }
            }
        };
    }

    private void buildLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setSmallestDisplacement(10);
        locationRequest.setMaxWaitTime(7500);
    }


}