package com.example.anh.itenki.fragment;

import android.content.DialogInterface;
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

    private static final int MENU_ITEM_DELETE = 111;
    private static final int MENU_ITEM_SETALARM = 222;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        menu.setHeaderTitle("Select the action");
        menu.add(0, MENU_ITEM_SETALARM, 0, "Set Alarm");
        menu.add(0, MENU_ITEM_DELETE, 1, "Delete Note");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Note selectedNote = (Note) lvNote.getItemAtPosition(info.position);
        Log.e("Note Selected", "==> " + new Gson().toJson(selectedNote));

        if (item.getItemId() == MENU_ITEM_SETALARM) {

        } else if (item.getItemId() == MENU_ITEM_DELETE) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Are you sure want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteNote(selectedNote);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        return true;
    }

    public void deleteNote(Note note) {
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        db.deleteNote(note);
        noteList.remove(note);
        adapter.notifyDataSetChanged();
    }
}
