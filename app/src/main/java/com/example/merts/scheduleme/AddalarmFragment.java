package com.example.merts.scheduleme;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
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
import java.util.Date;

/**
 * Created by merts on 24.04.2018.
 */

public class AddalarmFragment extends Fragment {
    static int requestCode = 1;
    int hour, minute, day, month, year;
    String format, docid;
    Calendar calendar;
    TextView textViewtime, textViewdate;
    FirebaseAuth mAuth;
    private String emailString;
    private EditText editTexttitle;
    private EditText editTextdescription;

    private Button finish;
    private Button c2;
    private NotificationHelper mNotificationHelper;
    private TextView mTextview;
    private TextView textViewAlarms;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference alarmsRef = db.collection("Alarms");
    ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
    private DatePicker pickerDate;
    private TimePicker pickerTime;
    private Button next;
    private Button set;
    private Button cancelAlarm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addalarm, container, false);
        mAuth = FirebaseAuth.getInstance();
        emailString = mAuth.getCurrentUser().getEmail();
        editTexttitle = view.findViewById(R.id.editText_title);
        pickerDate=view.findViewById(R.id.pickerdate);
        pickerTime=view.findViewById(R.id.pickertime);
        next=view.findViewById(R.id.next);
        finish=view.findViewById(R.id.finish);
        cancelAlarm = view.findViewById(R.id.cancel_alarm);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickerTime.setVisibility(View.GONE);
                pickerDate.setVisibility(View.VISIBLE);
                finish.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
            }
        });
        set=view.findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.setVisibility(View.VISIBLE);
                finish.setVisibility(View.GONE);
                cancelAlarm.setVisibility(View.GONE);
                pickerTime.setVisibility(View.VISIBLE);
                set.setVisibility(View.GONE);


            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm.setVisibility(View.VISIBLE);
                finish.setVisibility(View.GONE);
                pickerDate.setVisibility(View.GONE);
                set.setVisibility(View.VISIBLE);
                calendar.set(pickerDate.getYear(),pickerDate.getMonth(),pickerDate.getDayOfMonth(),
                        pickerTime.getCurrentHour(),pickerTime.getCurrentMinute(),0);
                //textViewdate.setText(pickerDate.getYear() + "/" + pickerDate.getMonth() + "/" + pickerDate.getDayOfMonth());
                updateTimeText(calendar);
                String title = editTexttitle.getText().toString();
                final String description = editTextdescription.getText().toString();
                String datetext = "";
                int x=pickerDate.getMonth()+1;
                datetext +=pickerDate.getDayOfMonth()  + "/" + x +"/" +pickerDate.getYear() ;
                String timetext = "";
                timetext += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                Alarm alarm = new Alarm(emailString, title, description, timetext, datetext);
                docid = alarm.getDocumentId();
                alarmsRef.add(alarm);
                startAlarm(calendar);
            }
        });
        editTextdescription = view.findViewById(R.id.editText_description);
        //mTextview = view.findViewById(R.id.text_view_alarm);
        textViewAlarms = view.findViewById(R.id.text_view_Alarms);
        //c1 = view.findViewById(R.id.c1);
        //c2 = view.findViewById(R.id.c2);
        mNotificationHelper = new NotificationHelper(getActivity());
        //final Button timePicker = view.findViewById(R.id.time_picker);
        //final Button datePicker = view.findViewById(R.id.date_picker);

        //textViewtime = view.findViewById(R.id.text_view_time);
        //textViewdate = view.findViewById(R.id.text_view_date);
        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);

        minute = calendar.get(Calendar.MINUTE);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        //month++;
        //textViewdate.setText(day + "/" + month + "/" + year);
        //selectedTimeFormat(hour);
        //textViewtime.setText(hour + " :" + minute + " ");


      /*  timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        // selectedTimeFormat(i);
                        hour=i;
                        minute=i1;
                        calendar.set(Calendar.HOUR_OF_DAY, i);
                        calendar.set(Calendar.MINUTE, i1);
                        calendar.set(Calendar.SECOND, 0);

                        textViewtime.setText(i + ": " + i1 + " ");
                        // updateTimeText(calendar);
                        emailString = mAuth.getCurrentUser().getEmail();
                        String title = editTexttitle.getText().toString();
                        final String description = editTextdescription.getText().toString();
                        String timetext = "";
                        timetext += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                        Alarm alarm = new Alarm(emailString, title, description, timetext);
                        docid = alarm.getDocumentId();
                        alarmsRef.add(alarm);
                        startAlarm(calendar);

                    }
                }, hour, minute, true);
                timePickerDialog.show();

            }
        });
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        //i1++;

                        calendar.set(Calendar.HOUR_OF_DAY,hour);
                        calendar.set(Calendar.MINUTE,minute);
                        calendar.set(Calendar.DAY_OF_MONTH, i2);
                        calendar.set(Calendar.MONTH, i1);
                        calendar.set(Calendar.YEAR, i);
                        textViewdate.setText(i2 + "/" + i1 + "/" + i);
                        updateTimeText(calendar);
                        String title = editTexttitle.getText().toString();
                        final String description = editTextdescription.getText().toString();
                        String datetext = "";
                        datetext += textViewdate.getText().toString();
                        String timetext = "";
                        timetext += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                        Alarm alarm = new Alarm(emailString, title, description, timetext, datetext);
                        docid = alarm.getDocumentId();
                        alarmsRef.add(alarm);
                        startAlarm(calendar);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });*/
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String xtitle = editTexttitle.getText().toString();

                alarmsRef.whereEqualTo("email", emailString).whereEqualTo("title", xtitle)

                        .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                                if (e != null) {
                                    return;
                                }

                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Alarm alarm = documentSnapshot.toObject(Alarm.class);
                                    alarm.setDocumentId(documentSnapshot.getId());
                                    String documentId = alarm.getDocumentId();
                                    // emailString=mAuth.getCurrentUser().getEmail();
                                    //String title = note.getTitle();


                                    alarmsRef.document(documentId).delete();

                                }


                            }
                        });


                cancelAlarm();
            }
        });


        return view;
    }

    /*public void sendc1(String title, String description) {
        NotificationCompat.Builder nb = mNotificationHelper.getC1Notification();

        mNotificationHelper.getManager().notify(1, nb.build());

    }

    public void sendc2(String title, String description) {
        NotificationCompat.Builder nb = mNotificationHelper.getC2Notification(title, description);
        mNotificationHelper.getManager().notify(2, nb.build());
    }*/
    /*public void selectedTimeFormat(int hour) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
    }*/


    private void updateTimeText(Calendar calendar) {
        String timetext = "Alarm set for: ";
        timetext += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        Toast.makeText(getActivity().getApplicationContext(), timetext, Toast.LENGTH_SHORT).show();
        //mTextview.setText(timetext);
    }

    private void startAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);

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
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode--, intent, 0);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(getActivity().getApplicationContext(), "alarm deleted", Toast.LENGTH_SHORT).show();
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
                            Alarm alarm = documentSnapshot.toObject(Alarm.class);
                            alarm.setDocumentId(documentSnapshot.getId());
                            //  String documentId = note.getDocumentId();
                            emailString = mAuth.getCurrentUser().getEmail();
                            String title = alarm.getTitle();
                            String description = alarm.getDescription();
                            String time = alarm.getTime();
                            String day = alarm.getDay();
                            data += "\nTitle: " + title + "\nDescription: " + description
                                    + "\nTime: " + time + " Day: " + day + " \n\n";
                            //notebookRef.document(documentId)

                        }
                        textViewAlarms.setText(data);

                    }
                });
    }

}