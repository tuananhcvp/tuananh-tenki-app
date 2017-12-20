package com.example.anh.itenki.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.anh.itenki.R;
import com.example.anh.itenki.adapter.ListAlarmNoteAdapter;
import com.example.anh.itenki.model.AlarmNote;
import com.example.anh.itenki.model.Note;
import com.example.anh.itenki.utils.MyDatabaseHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anh on 2017/12/18.
 */

public class ListAlarmNoteFragment extends Fragment {
    private ListView lvAlarmNote;
    private List<AlarmNote> alarmList = new ArrayList<AlarmNote>();
    private ListAlarmNoteAdapter alarmAdapter;
    private boolean isInitialize = false;
    OnListAlarmNoteListener mCallback;

    /**
     * ListAlarmNoteFragment initialize
     *
     * @return ListAlarmNoteFragment
     */
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
            if (isInitialize) {
                updateListNote();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isInitialize = true;
        View v = inflater.inflate(R.layout.fragment_list_alarm_note, container, false);

        lvAlarmNote = (ListView) v.findViewById(R.id.lvAlarmNote);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        alarmList.clear();
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        alarmList.addAll(db.getListAlarmNote());

        alarmAdapter = new ListAlarmNoteAdapter(getActivity(), alarmList);
        lvAlarmNote.setAdapter(alarmAdapter);

        lvAlarmNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.sendAlarmNoteInfo(alarmList.get(position));
            }
        });

        lvAlarmNote.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setMessage(getResources().getString(R.string.check_delete))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.txt_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlarmNote note = alarmList.get(position);

                                MyDatabaseHelper db = new MyDatabaseHelper(getContext());
                                db.deleteAlarmNote(note);
                                alarmList.remove(note);
                                alarmAdapter.notifyDataSetChanged();

                                Intent almIntent = new Intent("com.example.anh.itenki.activity.alertalarmnoteActivity");
                                PendingIntent operation = PendingIntent.getActivity(getActivity().getApplicationContext(), note.getPendingId(), almIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
                                AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                                alarmManager.cancel(operation);

                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.txt_no), null)
                        .show();

                return true;
            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInitialize = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isInitialize = false;
    }

    private void updateListNote() {
        alarmList.clear();
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        alarmList.addAll(db.getListAlarmNote());
        alarmAdapter.notifyDataSetChanged();
    }

    public interface OnListAlarmNoteListener {
        void sendAlarmNoteInfo(AlarmNote note);
    }

    public void setAlarmNoteCallbackListener(OnListAlarmNoteListener callback) {
        this.mCallback = callback;
    }

}
