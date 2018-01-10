package com.example.anh.itenki.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.anh.itenki.R;
import com.example.anh.itenki.model.AlarmNote;
import com.example.anh.itenki.model.Note;
import com.example.anh.itenki.utils.MyDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/**
 * Created by anh on 2017/12/19.
 */

public class AlarmActivity extends AppCompatActivity {
    private DatePicker dpDate;
    private TimePicker tpTime;
    private Button btnSetAlarm;
    private Button btnQuitAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_dialog);

        dpDate = (DatePicker)findViewById(R.id.dp_date);
        tpTime = (TimePicker)findViewById(R.id.tp_time);
        btnSetAlarm = (Button)findViewById(R.id.btnSetAlarm);
        btnQuitAlarm = (Button)findViewById(R.id.btnQuitAlarm);

        final Note mNote = (Note) getIntent().getSerializableExtra("SetAlarmNote");

        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = dpDate.getYear();
                int month = dpDate.getMonth();
                int day = dpDate.getDayOfMonth();
                int hour = tpTime.getCurrentHour();
                int minute = tpTime.getCurrentMinute();

                MyDatabaseHelper db = new MyDatabaseHelper(AlarmActivity.this);
                Intent almIntent = new Intent("com.example.anh.itenki.activity.alertalarmnoteActivity");
                almIntent.putExtra("isSetNote", mNote);

                PendingIntent operation = PendingIntent.getActivity(getApplicationContext(), mNote.getId(), almIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
                AlarmManager alarmManager = (AlarmManager)getBaseContext().getSystemService(ALARM_SERVICE);

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hour, minute);
                long alarmTime = calendar.getTimeInMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
                String isSetTime = sdf.format(calendar.getTime());

                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);

                AlarmNote note = new AlarmNote(mNote.getContent(), isSetTime, mNote.getId());
                db.addAlarmNote(note);

                Toasty.info(getApplicationContext(), getResources().getString(R.string.alarm_success), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnQuitAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
