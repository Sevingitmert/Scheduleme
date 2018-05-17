package com.example.merts.scheduleme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by merts on 24.04.2018.
 */

public class AddalarmFragment extends Fragment implements View.OnClickListener{
    static int requestCode = 1;

    String docid;
    Calendar calendar;
    FirebaseAuth mAuth;
    private String emailString;
    private EditText editTexttitle;
    private EditText editTextdescription;
    private Button save_alarm;
    private NotificationHelper mNotificationHelper;
    private TextView textViewAlarms;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference alarmsRef = db.collection("Alarms");
    ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
    private DatePicker pickerDate;
    private TimePicker pickerTime;
    private Button nextforpickday;
    private Button set_new_alarm;
    private Button cancelAlarm;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addalarm, container, false);
        mAuth = FirebaseAuth.getInstance();
        emailString = mAuth.getCurrentUser().getEmail();
        pickerDate = view.findViewById(R.id.pickerdate);
        pickerTime = view.findViewById(R.id.pickertime);
        calendar = Calendar.getInstance();
        set_new_alarm = view.findViewById(R.id.set_new_alarm);
        set_new_alarm.setOnClickListener(this);
        nextforpickday = view.findViewById(R.id.nextforpickday);
        nextforpickday.setOnClickListener(this);
        save_alarm = view.findViewById(R.id.save_alarm);
        save_alarm.setOnClickListener(this);
        cancelAlarm = view.findViewById(R.id.cancel_alarm);
        cancelAlarm.setOnClickListener(this);
        editTexttitle = view.findViewById(R.id.editText_title);
        editTextdescription = view.findViewById(R.id.editText_description);
        textViewAlarms = view.findViewById(R.id.text_view_Alarms);
        mNotificationHelper = new NotificationHelper(getActivity());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        alarmsRef.whereEqualTo("email", emailString)

                .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            AlarmClass alarmClass = documentSnapshot.toObject(AlarmClass.class);
                            alarmClass.setDocumentId(documentSnapshot.getId());
                            emailString = mAuth.getCurrentUser().getEmail();
                            String title = alarmClass.getTitle();
                            String description = alarmClass.getDescription();
                            String time = alarmClass.getTime();
                            String day = alarmClass.getDay();
                            data += "\nTitle: " + title + "\nDescription: " + description
                                    + "\nTime: " + time + " Day: " + day + " \n\n";

                        }
                        textViewAlarms.setText(data);

                    }
                });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.set_new_alarm:
                nextforpickday.setVisibility(View.VISIBLE);
                save_alarm.setVisibility(View.GONE);
                cancelAlarm.setVisibility(View.GONE);
                pickerTime.setVisibility(View.VISIBLE);
                set_new_alarm.setVisibility(View.GONE);


                break;
            case R.id.nextforpickday:
                pickerTime.setVisibility(View.GONE);
                pickerDate.setVisibility(View.VISIBLE);
                save_alarm.setVisibility(View.VISIBLE);
                nextforpickday.setVisibility(View.GONE);


                break;
            case R.id.save_alarm:
                cancelAlarm.setVisibility(View.VISIBLE);
                save_alarm.setVisibility(View.GONE);
                pickerDate.setVisibility(View.GONE);
                set_new_alarm.setVisibility(View.VISIBLE);
                calendar.set(pickerDate.getYear(), pickerDate.getMonth(), pickerDate.getDayOfMonth(),
                        pickerTime.getCurrentHour(), pickerTime.getCurrentMinute(), 0);

                updateTimeText(calendar);
                String title = editTexttitle.getText().toString();
                final String description = editTextdescription.getText().toString();
                String datetext = "";
                int x = pickerDate.getMonth() + 1;
                datetext += pickerDate.getDayOfMonth() + "/" + x + "/" + pickerDate.getYear();
                String timetext = "";
                timetext += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                AlarmClass alarmClass = new AlarmClass(emailString, title, description, timetext, datetext);
                docid = alarmClass.getDocumentId();
                alarmsRef.add(alarmClass);
                Toast.makeText(getActivity().getApplicationContext(), "alarm set for "+ timetext + " "+datetext , Toast.LENGTH_LONG).show();

                startAlarm(calendar);


                break;
            case R.id.cancel_alarm:
                String xtitle = editTexttitle.getText().toString();

                alarmsRef.whereEqualTo("email", emailString).whereEqualTo("title", xtitle)

                        .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                                if (e != null) {
                                    return;
                                }

                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    AlarmClass alarmClass = documentSnapshot.toObject(AlarmClass.class);
                                    alarmClass.setDocumentId(documentSnapshot.getId());
                                    String documentId = alarmClass.getDocumentId();


                                    alarmsRef.document(documentId).delete();

                                }


                            }
                        });


                cancelAlarm();


                break;
        }

    }


    private void updateTimeText(Calendar calendar) {
        String timetext = "AlarmClass set for: ";
        timetext += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        Toast.makeText(getActivity().getApplicationContext(), timetext, Toast.LENGTH_SHORT).show();
    }

    private void startAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);

        intent.putExtra("title", editTexttitle.getText().toString());
        intent.putExtra("description", editTextdescription.getText().toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode++, intent, 0);
        intentArray.add(pendingIntent);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode--, intent, 0);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(getActivity().getApplicationContext(), "alarm deleted", Toast.LENGTH_SHORT).show();
    }




}