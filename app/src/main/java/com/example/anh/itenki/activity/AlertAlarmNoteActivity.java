package com.example.anh.itenki.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.anh.itenki.fragment.AlarmNoteDialogFragment;
import com.example.anh.itenki.model.Note;

/**
 * Created by anh on 2017/12/19.
 */

public class AlertAlarmNoteActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        Note note = (Note) extras.getSerializable("isSetNote");
        AlarmNoteDialogFragment alert = new AlarmNoteDialogFragment(note.getContent());
        alert.show(getSupportFragmentManager(), "AlertAlarmNote");

    }
}
