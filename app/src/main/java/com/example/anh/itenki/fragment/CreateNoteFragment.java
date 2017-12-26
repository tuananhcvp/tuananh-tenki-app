package com.example.anh.itenki.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anh.itenki.R;
import com.example.anh.itenki.model.AlarmNote;
import com.example.anh.itenki.model.Note;
import com.example.anh.itenki.utils.MyDatabaseHelper;
import com.example.anh.itenki.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * Created by anh on 2017/12/18.
 */

public class CreateNoteFragment extends Fragment {
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private Note note;
    private int mode;
    private boolean isInitialize = false;

    @BindView(R.id.edtContent)
    EditText edtContent;

    @BindView(R.id.txtNoteTime)
    TextView txtNoteTime;

    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    private Unbinder unbinder;

    /**
     * CreateNoteFragment initialize
     *
     * @return CreateNoteFragment
     */
    public static CreateNoteFragment newInstance() {
        Bundle args = new Bundle();
        CreateNoteFragment fragment = new CreateNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("CreateNoteFrag", "==> onActivityCreated");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("CreateNoteFrag", "==> onCreate");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            // called here
            Log.e("CreateNoteFrag", "==> visible -- isInitialize = " + isInitialize);
            if (isInitialize) {
                if (note != null) {
                    note = null;
                }
                edtContent.setText("");
                String time = txtNoteTime.getText().toString();
                if (time.startsWith(getResources().getString(R.string.create_at))
                        || time.startsWith(getResources().getString(R.string.modify_at))
                        || time.startsWith(getResources().getString(R.string.alarm_at))) {
                    txtNoteTime.setText(getCurrentTime());
                }
                mode = MODE_CREATE;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("CreateNoteFrag", "==> onCreateView");
        isInitialize = true;
        View v = inflater.inflate(R.layout.fragment_create_note, container, false);
        unbinder = ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("CreateNoteFrag", "==> onStart");

        if (note == null) {
            mode = MODE_CREATE;
            txtNoteTime.setText(getCurrentTime());
            edtContent.setText("");
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideSoftKeyboard(getContext(), getActivity().getCurrentFocus());
                MyDatabaseHelper db = new MyDatabaseHelper(getContext());
                String content = edtContent.getText().toString();
                Log.e("Content", "=> " + content);

                if (content.equalsIgnoreCase("")) {
                    return;
                }

                if (mode == MODE_CREATE) {
                    String createTime = getCurrentTime();
                    Note note = new Note(content, createTime, "");
                    db.addNote(note);
                    Toasty.info(getContext(), getResources().getString(R.string.note_saved), Toast.LENGTH_SHORT).show();

                } else if (mode == MODE_EDIT) {
                    String modifyTime = getCurrentTime();
                    note.setContent(content);
                    note.setModifyTime(modifyTime);
                    db.updateNote(note);
                    Toasty.info(getContext(), getResources().getString(R.string.note_saved), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideSoftKeyboard(getContext(), getActivity().getCurrentFocus());
                mode = MODE_CREATE;
                edtContent.setText("");
                txtNoteTime.setText(getCurrentTime());
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("CreateNoteFrag", "==> onPause");

    }

    //Get time in current
    private String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        return df.format(c.getTime());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("CreateNoteFrag", "==> onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("CreateNoteFrag", "==> onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("CreateNoteFrag", "==> onDestroy");
        isInitialize = false;
    }

    /**
     * Show note's content
     */
    public void showNoteEdit(Note nodeEdit) {
        if (nodeEdit != null) {
            note = nodeEdit;
            mode = MODE_EDIT;
            if (nodeEdit.getModifyTime().equalsIgnoreCase("")) {
                txtNoteTime.setText(getResources().getString(R.string.create_at) + ": " + nodeEdit.getCreateTime());
            } else {
                txtNoteTime.setText(getResources().getString(R.string.modify_at) + ": " + nodeEdit.getModifyTime());
            }
            edtContent.setText(nodeEdit.getContent());
        }
    }

    /**
     * Show alarm note's content
     */
    public void showNoteAlarm(AlarmNote alarmNote) {
        if (alarmNote != null) {
            mode = 0;
            txtNoteTime.setText(getResources().getString(R.string.alarm_at) + ": " + alarmNote.getAlarmTime());
            edtContent.setText(alarmNote.getAlarmContent());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("CreateNoteFrag", "==> onDestroyView");
        // unbind the view to free some memory
        unbinder.unbind();
        isInitialize = false;
    }
}