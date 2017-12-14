package com.example.anh.itenki.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.anh.itenki.R;
import com.example.anh.itenki.activity.MainActivity;
import com.example.anh.itenki.utils.MyNotifyReceiver;
import com.example.anh.itenki.utils.SharedPreference;

import java.util.Calendar;

/**
 * Created by anh on 2017/12/14.
 */

public class SettingFragment extends Fragment {
    private Switch swtNotify;

    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarName(getResources().getString(R.string.title_setting));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        swtNotify = (Switch) v.findViewById(R.id.swtNotify);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        swtNotify.setChecked(SharedPreference.getInstance(getContext()).getBoolean("notifyState", false));

        swtNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreference.getInstance(getContext()).putBoolean("notifyState", true);
                    setNotifyAlarmManager("On");

                } else {
                    SharedPreference.getInstance(getContext()).putBoolean("notifyState", false);
                    Intent receiverIntent = new Intent(getActivity(), MyNotifyReceiver.class);
                    receiverIntent.putExtra("Notify State", "Off");

                    Calendar calendar = Calendar.getInstance();
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), MainActivity.NOTIFY_REQUEST_CODE, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager manager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
                    manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                }
            }
        });
    }

    public void setNotifyAlarmManager(String n){
        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 8);
//        calendar.set(Calendar.MINUTE, 00);
//        calendar.set(Calendar.SECOND, 0);

//        Calendar now = Calendar.getInstance();
//        if (now.after(calendar)) {
//            calendar.add(Calendar.DATE, 1);
//        }

        Intent receiverIntent = new Intent(getActivity(), MyNotifyReceiver.class);
        receiverIntent.putExtra("Notify State", n);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), MainActivity.NOTIFY_REQUEST_CODE, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*3, pendingIntent);
//        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }
}
