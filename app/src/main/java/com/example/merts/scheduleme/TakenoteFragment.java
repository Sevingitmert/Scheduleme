package com.example.merts.scheduleme;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by merts on 24.04.2018.
 */

public class TakenoteFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TakenoteFragment";
    String docid;

    private static final String KEY_TITLE="Title";
    private static final String KEY_DESCRIPTION="Description";
    private String emailString;
    FirebaseAuth mAuth;
    private EditText Edittexttitle;
    private EditText Edittextdescription;

    private TextView textViewData;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference notebookRef= db.collection("Notebook");
    private DocumentReference noteRef=db.document("Notebook/My First Note");






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_takenote, container, false);
        mAuth = FirebaseAuth.getInstance();
        emailString = mAuth.getCurrentUser().getEmail();
        Edittexttitle= view.findViewById(R.id.edit_text_title);
        Edittextdescription=view.findViewById(R.id.edit_text_description);
        textViewData=view.findViewById(R.id.text_view_data);
        docid=null;

        Button bsave= view.findViewById(R.id.savenote);
        bsave.setOnClickListener(this);
        Button bload= view.findViewById(R.id.loadnote);
        bload.setOnClickListener(this);
        Button bupdatedescription=view.findViewById(R.id.updatedescription);
        bupdatedescription.setOnClickListener(this);

        Button bdeletenote=view.findViewById(R.id.deletenote);
        bdeletenote.setOnClickListener(this);


        return view;



    }

    @Override
    public void onStart() {
        super.onStart();
        notebookRef.whereEqualTo("email",emailString)

                .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if(e !=null){
                    return;
                }
                String data="";
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    Note note=documentSnapshot.toObject(Note.class);
                    note.setDocumentId(documentSnapshot.getId());
                    String documentId=note.getDocumentId();
                    emailString=mAuth.getCurrentUser().getEmail();
                    String title=note.getTitle();
                    String description=note.getDescription();
                    data+= "\nTitle: "+ title +"\nDescription: "+description
                            +" \n\n";
                    //notebookRef.document(documentId)

                }
                textViewData.setText(data);

            }
        });
    }
    /*public void saveNote(View v){}
    public void updateDescription(View v){}

    public void deleteNote(View v){}
    public void loadNote(View v){}*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.savenote:
        String title = Edittexttitle.getText().toString();
        final String description = Edittextdescription.getText().toString();


        emailString=mAuth.getCurrentUser().getEmail();
        System.out.println(emailString);
        Note note=new Note(emailString,title,description);
        docid=note.getDocumentId();
        notebookRef.add(note);



            break;
            case R.id.loadnote:
                notebookRef.whereEqualTo("email",emailString)


                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                String data="";
                                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                    Note note=documentSnapshot.toObject(Note.class);
                                    note.setDocumentId(documentSnapshot.getId());

                                    String documentId=note.getDocumentId();
                                    emailString=mAuth.getCurrentUser().getEmail();
                                    String title=note.getTitle();
                                    String description=note.getDescription();
                                    data+= "\nTitle: "+ title +"\nDescription: "+description
                                            +" \n\n";



                                }
                                textViewData.setText(data);


                            }
                        });

            break;
            case R.id.updatedescription:
                String updatetitle=Edittexttitle.getText().toString();
                final String updatedesc=Edittextdescription.getText().toString();

                notebookRef.whereEqualTo("email",emailString).whereEqualTo("title",updatetitle)


                        .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                                if(e !=null){
                                    return;
                                }

                                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                                    Note note=documentSnapshot.toObject(Note.class);
                                    note.setDocumentId(documentSnapshot.getId());
                                    String documentId=note.getDocumentId();
                                    //emailString=mAuth.getCurrentUser().getEmail();
                                    String title=note.getTitle();


                                    notebookRef.document(documentId).update("description",updatedesc);

                                }


                            }
                        });




                        break;
            case R.id.deletenote:
                String xtitle = Edittexttitle.getText().toString();

                notebookRef.whereEqualTo("email",emailString).whereEqualTo("title",xtitle)

                        .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                                if(e !=null){
                                    return;
                                }

                                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                                    Note note=documentSnapshot.toObject(Note.class);
                                    note.setDocumentId(documentSnapshot.getId());
                                    String documentId=note.getDocumentId();
                                   // emailString=mAuth.getCurrentUser().getEmail();
                                    String title=note.getTitle();


                                    notebookRef.document(documentId).delete();

                                }


                            }
                        });

            break;
        }
    }
}
