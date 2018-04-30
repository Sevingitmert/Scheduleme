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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by merts on 24.04.2018.
 */

public class AddalarmFragment extends Fragment {
    static int requestCode = 1;
    int hour, minute, day, month, year;
    String format;
    Calendar calendar;
    TextView textViewtime, textViewdate;
    private EditText editTexttitle;
    private EditText editTextdescription;
    private Button c1;
    private Button c2;
    private NotificationHelper mNotificationHelper;
    private TextView mTextview;
    ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addalarm, container, false);
        editTexttitle = view.findViewById(R.id.editText_title);
        editTextdescription = view.findViewById(R.id.editText_description);
        mTextview = view.findViewById(R.id.text_view_alarm);

        c1 = view.findViewById(R.id.c1);
        c2 = view.findViewById(R.id.c2);
        mNotificationHelper = new NotificationHelper(getActivity());
        Button timePicker = view.findViewById(R.id.time_picker);
        Button datePicker = view.findViewById(R.id.date_picker);
        final Button cancelAlarm = view.findViewById(R.id.cancel_alarm);
        textViewtime = view.findViewById(R.id.text_view_time);
        textViewdate = view.findViewById(R.id.text_view_date);
        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        month++;
        textViewdate.setText(day + "/" + month + "/" + year);
        //selectedTimeFormat(hour);
        textViewtime.setText(hour + " :" + minute + " " );
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendc1(editTexttitle.getText().toString(), editTextdescription.getText().toString());

            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendc2(editTexttitle.getText().toString(), editTextdescription.getText().toString());
            }
        });
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                       // selectedTimeFormat(i);
                        calendar.set(Calendar.HOUR_OF_DAY, i);
                        calendar.set(Calendar.MINUTE, i1);
                        calendar.set(Calendar.SECOND, 0);
                        textViewtime.setText(i + ": " + i1 + " ");
                        updateTimeText(calendar);
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
                        i1++;
                        textViewdate.setText(i2 + "/" + i1 + "/" + i);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm();
            }
        });


        return view;
    }

    public void sendc1(String title, String description) {
        NotificationCompat.Builder nb = mNotificationHelper.getC1Notification();

        mNotificationHelper.getManager().notify(1, nb.build());

    }

    public void sendc2(String title, String description) {
        NotificationCompat.Builder nb = mNotificationHelper.getC2Notification(title, description);
        mNotificationHelper.getManager().notify(2, nb.build());
    }
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
        String timetext="Alarm set for: ";
        timetext+= DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());

        mTextview.setText(timetext);
    }
    private void startAlarm(Calendar calendar){
        AlarmManager alarmManager =(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(getActivity(),AlertReceiver.class);

        intent.putExtra("title", editTexttitle.getText().toString());
        intent.putExtra("description", editTextdescription.getText().toString());

        PendingIntent pendingIntent=PendingIntent.getBroadcast(getActivity(),requestCode++,intent,0);
        intentArray.add(pendingIntent);
        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE,1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);



    }
    private void cancelAlarm(){
        AlarmManager alarmManager =(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(getActivity(),AlertReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getActivity(),requestCode--,intent,0);
        alarmManager.cancel(pendingIntent);
        mTextview.setText("Alarm canceled");
    }
}