package com.example.anh.itenki.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.anh.itenki.R;
import com.example.anh.itenki.activity.AlarmActivity;
import com.example.anh.itenki.adapter.ListNoteAdapter;
import com.example.anh.itenki.model.Note;
import com.example.anh.itenki.utils.MyDatabaseHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anh on 2017/12/18.
 */

public class ListNoteFragment extends Fragment {
    private List<Note> noteList = new ArrayList<Note>();
    private ListView lvNote;
    private ListNoteAdapter adapter;
    private boolean isInitialize = false;
    OnListNoteFragmentListener mCallback;

    private static final int MENU_ITEM_DELETE = 111;
    private static final int MENU_ITEM_SETALARM = 222;

    /**
     * ListNoteFragment initialize
     *
     * @return ListNoteFragment
     */
    public static ListNoteFragment newInstance() {
        Bundle args = new Bundle();
        ListNoteFragment fragment = new ListNoteFragment();
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
        setHasOptionsMenu(true);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            // called here
            Log.e("ListNoteFrag", "==> visible");
            if (isInitialize) {
                updateListNote();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isInitialize = true;
        View v = inflater.inflate(R.layout.fragment_list_note, container, false);

        lvNote = (ListView) v.findViewById(R.id.lvMainNote);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        noteList.clear();
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        noteList.addAll(db.getAllNotes());

        adapter = new ListNoteAdapter(getActivity(), noteList);
        lvNote.setAdapter(adapter);
        registerForContextMenu(lvNote);

        lvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.sendNoteInfo(noteList.get(position));
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("ListNoteFrag", "==> onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("ListNoteFrag", "==> onResume");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle(getResources().getString(R.string.select_action));
        menu.add(0, MENU_ITEM_SETALARM, 0, getResources().getString(R.string.set_alarm));
        menu.add(0, MENU_ITEM_DELETE, 1, getResources().getString(R.string.delete_note));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Note selectedNote = (Note) lvNote.getItemAtPosition(info.position);

        if (item.getItemId() == MENU_ITEM_SETALARM) {
            Intent intent = new Intent(getActivity(), AlarmActivity.class);
            intent.putExtra("SetAlarmNote", selectedNote);
            startActivity(intent);

        } else if (item.getItemId() == MENU_ITEM_DELETE) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(getResources().getString(R.string.check_delete))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.txt_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteNote(selectedNote);
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.txt_no), null)
                    .show();
        }

        return true;
    }

    private void deleteNote(Note note) {
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        db.deleteNote(note);
        noteList.remove(note);
        adapter.notifyDataSetChanged();
    }

    private void updateListNote() {
        noteList.clear();
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        noteList.addAll(db.getAllNotes());
        adapter.notifyDataSetChanged();
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

    public interface OnListNoteFragmentListener {
        void sendNoteInfo(Note note);
    }

    public void setNoteCallbackListener(OnListNoteFragmentListener callback) {
        this.mCallback = callback;
    }
}
