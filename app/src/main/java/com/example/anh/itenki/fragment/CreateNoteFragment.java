package com.example.anh.itenki.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anh.itenki.R;
import com.example.anh.itenki.activity.MainActivity;
import com.example.anh.itenki.model.AlarmNote;
import com.example.anh.itenki.model.Note;
import com.example.anh.itenki.utils.MyDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/**
 * Created by anh on 2017/12/18.
 */

public class CreateNoteFragment extends Fragment {
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private Note note;
    private AlarmNote alarmNote;
    private int mode;

    private EditText edtContent;
    private TextView txtNoteTime;

    public static CreateNoteFragment newInstance() {
        Bundle args = new Bundle();
        CreateNoteFragment fragment = new CreateNoteFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_note, container, false);

        edtContent = (EditText) v.findViewById(R.id.edtContent);
        txtNoteTime = (TextView) v.findViewById(R.id.txtNoteTime);

        try {
            note = (Note) getArguments().getSerializable("NeedEditNote");
            alarmNote = (AlarmNote) getArguments().getSerializable("ShowAlarmNote");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("CreateNoteFrag", "==> onStart");

        if (note == null) {
            if (alarmNote != null) {
                edtContent.setText(alarmNote.getAlarmContent());
            } else {
                mode = MODE_CREATE;
                txtNoteTime.setText(getCurrentTime());
            }
        } else {
            mode = MODE_EDIT;
            if (note.getModifyTime().equalsIgnoreCase("")) {
                txtNoteTime.setText(note.getCreateTime());
            } else {
                txtNoteTime.setText(note.getModifyTime());
            }
            edtContent.setText(note.getContent());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("CreateNoteFrag", "==> onPause");
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        String content = edtContent.getText().toString();

        try {
            if (content.equalsIgnoreCase("")) return;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (mode == MODE_CREATE) {
            String createTime = getResources().getString(R.string.create_at) + ": " + getCurrentTime();
            Note mNote = new Note(content, createTime, "");
            db.addNote(mNote);

        } else if (mode == MODE_EDIT) {
            String modifyTime = getResources().getString(R.string.modify_at) + ": " + getCurrentTime();
            note.setContent(content);
            note.setModifyTime(modifyTime);
            db.updateNote(note);
        }
        Toasty.info(getContext(), getResources().getString(R.string.note_saved), Toast.LENGTH_SHORT).show();
    }

    public String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("CreateNoteFrag", "==> onResume");
    }
}