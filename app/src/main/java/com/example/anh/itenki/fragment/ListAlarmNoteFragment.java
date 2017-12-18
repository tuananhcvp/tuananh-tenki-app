package com.example.anh.itenki.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anh.itenki.R;

/**
 * Created by anh on 2017/12/18.
 */

public class ListAlarmNoteFragment extends Fragment {

    public static ListAlarmNoteFragment newInstance() {
        Bundle args = new Bundle();
        ListAlarmNoteFragment fragment = new ListAlarmNoteFragment();
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

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            // called here
            Log.e("ListAlarmNoteFrag", "==> visible");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_alarm_note, container, false);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("AlarmNoteFrag", "==> onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("AlarmNoteFrag", "==> onResume");
    }
}
