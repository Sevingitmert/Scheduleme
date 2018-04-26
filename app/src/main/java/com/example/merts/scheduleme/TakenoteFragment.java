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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

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
    private TextView textViewData;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private DocumentReference noteRef=db.document("Notebook/My First Note");




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_takenote, container, false);

        Edittexttitle= view.findViewById(R.id.edit_text_title);
        Edittextdescription=view.findViewById(R.id.edit_text_description);
        textViewData=view.findViewById(R.id.text_view_data);

        Button bsave= view.findViewById(R.id.savenote);
        bsave.setOnClickListener(this);
        Button bload= view.findViewById(R.id.loadnote);
        bload.setOnClickListener(this);
        Button bupdatedescription=view.findViewById(R.id.updatedescription);
        bupdatedescription.setOnClickListener(this);
        Button bdeletedescription=view.findViewById(R.id.deletedescription);
        bdeletedescription.setOnClickListener(this);
        Button bdeletenote=view.findViewById(R.id.deletenote);
        bdeletenote.setOnClickListener(this);
        return view;



    }

    @Override
    public void onStart() {
        super.onStart();
        noteRef.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(e !=null){
                    Toast.makeText(getActivity(), "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,e.toString());
                    return;
                }

                if (documentSnapshot.exists()) {
                    String title = documentSnapshot.getString(KEY_TITLE);
                    String description = documentSnapshot.getString(KEY_DESCRIPTION);

                    textViewData.setText("Title: " + title + "\n" + "Description: " + description);
                    //Toast.makeText(getActivity(), "Note loaded", Toast.LENGTH_SHORT).show();


                }else{
                    textViewData.setText("");
                }

            }
        });
    }
    /*public void saveNote(View v){}
    public void updateDescription(View v){}
    public void deleteDescription(View v){}
    public void deleteNote(View v){}
    public void loadNote(View v){}*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.savenote:
        String title = Edittexttitle.getText().toString();
        String description = Edittextdescription.getText().toString();

        Map<String, Object> note = new HashMap<>();
        note.put(KEY_TITLE, title);
        note.put(KEY_DESCRIPTION, description);

            noteRef.set(note)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(getActivity(), "Note saved", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());

                        }
                    });
            break;
            case R.id.loadnote:
            noteRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String title = documentSnapshot.getString(KEY_TITLE);
                                String description = documentSnapshot.getString(KEY_DESCRIPTION);
                                //Map<String,Object> note=documentSnapshot.getData();

                                textViewData.setText("Title: " + title + "\n" + "Description: " + description);
                                Toast.makeText(getActivity(), "Note loaded", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(getActivity(), "Document does not exist", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });
            break;
            case R.id.updatedescription:
                String descriptionupdate= Edittextdescription.getText().toString();

                //Map<String,Object> note=new HashMap<>();
                //note.put(KEY_DESCRIPTION,description);
                //noteRef.set(note, SetOptions.merge());
                noteRef.update(KEY_DESCRIPTION ,descriptionupdate);
            break;
            case R.id.deletedescription:
                noteRef.update(KEY_DESCRIPTION, FieldValue.delete());
            break;
            case R.id.deletenote:
                noteRef.delete();
            break;
        }
    }
}
