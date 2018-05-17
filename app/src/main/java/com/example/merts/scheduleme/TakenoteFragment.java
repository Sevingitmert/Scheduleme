package com.example.merts.scheduleme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class TakenoteFragment extends Fragment implements View.OnClickListener {

    String docid;
    private String emailString;
    FirebaseAuth mAuth;
    private EditText Edittexttitle;
    private EditText Edittextdescription;
    private TextView textViewData;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_takenote, container, false);
        mAuth = FirebaseAuth.getInstance();
        emailString = mAuth.getCurrentUser().getEmail();
        Edittexttitle = view.findViewById(R.id.edit_text_title);
        Edittextdescription = view.findViewById(R.id.edit_text_description);
        textViewData = view.findViewById(R.id.text_view_data);
        docid = null;

        Button bsave = view.findViewById(R.id.savenote);
        bsave.setOnClickListener(this);
        //Button bload = view.findViewById(R.id.loadnote);
        //bload.setOnClickListener(this);
        Button bupdatedescription = view.findViewById(R.id.updatedescription);
        bupdatedescription.setOnClickListener(this);

        Button bdeletenote = view.findViewById(R.id.deletenote);
        bdeletenote.setOnClickListener(this);


        return view;


    }

    @Override
    public void onStart() {
        super.onStart();
        notebookRef.whereEqualTo("email", emailString)

                .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            NoteClass noteClass = documentSnapshot.toObject(NoteClass.class);
                            noteClass.setDocumentId(documentSnapshot.getId());
                            //  String documentId = noteClass.getDocumentId();
                            emailString = mAuth.getCurrentUser().getEmail();
                            String title = noteClass.getTitle();
                            String description = noteClass.getDescription();
                            data += "\nTitle: " + title + "\nDescription: " + description
                                    + " \n\n";
                            //notebookRef.document(documentId)

                        }
                        textViewData.setText(data);

                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.savenote:
                String title = Edittexttitle.getText().toString();
                final String description = Edittextdescription.getText().toString();


                emailString = mAuth.getCurrentUser().getEmail();
                System.out.println(emailString);
                NoteClass noteClass = new NoteClass(emailString, title, description);
                docid = noteClass.getDocumentId();
                notebookRef.add(noteClass);


                break;

            case R.id.updatedescription:
                String updatetitle = Edittexttitle.getText().toString();
                final String updatedesc = Edittextdescription.getText().toString();

                notebookRef.whereEqualTo("email", emailString).whereEqualTo("title", updatetitle)


                        .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                                if (e != null) {
                                    return;
                                }

                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    NoteClass noteClass = documentSnapshot.toObject(NoteClass.class);
                                    noteClass.setDocumentId(documentSnapshot.getId());
                                    String documentId = noteClass.getDocumentId();


                                    notebookRef.document(documentId).update("description", updatedesc);

                                }


                            }
                        });


                break;
            case R.id.deletenote:
                String xtitle = Edittexttitle.getText().toString();

                notebookRef.whereEqualTo("email", emailString).whereEqualTo("title", xtitle)

                        .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                                if (e != null) {
                                    return;
                                }

                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    NoteClass noteClass = documentSnapshot.toObject(NoteClass.class);
                                    noteClass.setDocumentId(documentSnapshot.getId());
                                    String documentId = noteClass.getDocumentId();


                                    notebookRef.document(documentId).delete();

                                }


                            }
                        });

                break;
        }
    }
}
