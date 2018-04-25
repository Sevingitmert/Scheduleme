package com.example.merts.scheduleme;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by merts on 24.04.2018.
 */

public class TakenoteFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TakenoteFragment";

    private static final String KEY_TITLE="Title";
    private static final String KEY_DESCRIPTION="Description";

    private EditText Edittexttitle;
    private EditText Edittextdescription;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_takenote, container, false);

        Edittexttitle= view.findViewById(R.id.edit_text_title);
        Edittextdescription=view.findViewById(R.id.edit_text_description);

        Button b= view.findViewById(R.id.savenote);
        b.setOnClickListener(this);
        return view;

    }
    public void saveNote(View v){


    }


    @Override
    public void onClick(View v) {
        String title=Edittexttitle.getText().toString();
        String description=Edittextdescription.getText().toString();

        Map<String,Object> note=new HashMap<>();
        note.put(KEY_TITLE,title);
        note.put(KEY_DESCRIPTION,description);

        db.collection("Notebook").document("My First Note").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid){

                        Toast.makeText(getActivity(), "Note saved", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());

                    }
                });
    }
}
