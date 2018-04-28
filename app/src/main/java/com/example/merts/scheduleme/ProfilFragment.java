package com.example.merts.scheduleme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by merts on 24.04.2018.
 */

public class ProfilFragment extends Fragment implements View.OnClickListener {
    private TextView textViewData;
    private String emailString;
    FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        emailString = mAuth.getCurrentUser().getEmail();
        textViewData = view.findViewById(R.id.textView);

        Button button1a= view.findViewById(R.id.button_takenote);
        Button button2a= view.findViewById(R.id.button_addalarm);
        Button button3a= view.findViewById(R.id.button_addlocation);
        Button button4a= view.findViewById(R.id.button_activity);

        button1a.setOnClickListener(this);
        button2a.setOnClickListener(this);
        button3a.setOnClickListener(this);
        button4a.setOnClickListener(this);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        notebookRef.whereEqualTo("email", emailString);
        String data = "";
        emailString = mAuth.getCurrentUser().getEmail();
        data += emailString;
        textViewData.setText(data);

    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.button_takenote:
                fragment = new TakenoteFragment();
                replaceFragment(fragment);
                break;
            case R.id.button_addalarm:
                fragment = new AddalarmFragment();
                replaceFragment(fragment);
                break;
            case R.id.button_addlocation:
                fragment = new AddlocationFragment();
                replaceFragment(fragment);
                break;
            case R.id.button_activity:
                fragment = new AddactivityFragment();
                replaceFragment(fragment);
                break;


        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}


