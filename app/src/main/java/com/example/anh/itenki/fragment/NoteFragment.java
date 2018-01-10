package com.example.anh.itenki.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.anh.itenki.R;
import com.example.anh.itenki.activity.MainActivity;
import com.example.anh.itenki.model.AlarmNote;
import com.example.anh.itenki.model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anh on 2017/12/14.
 */

public class NoteFragment extends Fragment implements SearchView.OnQueryTextListener, ListNoteFragment.OnListNoteFragmentListener,
        ListAlarmNoteFragment.OnListAlarmNoteListener {
    private ViewPager pagerNote;
    private TabLayout tabsNote;
    private ViewPagerAdapter pagerAdapter;

    /**
     * NoteFragment initialize
     *
     * @return NoteFragment
     */
    public static NoteFragment newInstance() {
        Bundle args = new Bundle();
        NoteFragment fragment = new NoteFragment();
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
        ((MainActivity) getActivity()).setActionBarName(getResources().getString(R.string.title_tenki_note));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note, container, false);

        pagerNote = (ViewPager) v.findViewById(R.id.pagerNote);
        tabsNote = (TabLayout) v.findViewById(R.id.tabsNote);

        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(CreateNoteFragment.newInstance(), getResources().getString(R.string.create_note));
        pagerAdapter.addFragment(ListNoteFragment.newInstance(), getResources().getString(R.string.list_note));
        pagerAdapter.addFragment(ListAlarmNoteFragment.newInstance(), getResources().getString(R.string.alarm_note));
        pagerNote.setAdapter(pagerAdapter);

        ((ListNoteFragment) pagerAdapter.getItem(1)).setNoteCallbackListener(this);
        ((ListAlarmNoteFragment) pagerAdapter.getItem(2)).setAlarmNoteCallbackListener(this);

        tabsNote.setupWithViewPager(pagerNote);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        pagerNote.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("onPageSelected", "=> " + position + "--" + pagerNote.getCurrentItem());
//                pagerNote.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void sendNoteInfo(Note note) {
        pagerNote.setCurrentItem(0);
        ((CreateNoteFragment) pagerAdapter.getItem(0)).showNoteEdit(note);
    }

    @Override
    public void sendAlarmNoteInfo(AlarmNote note) {
        pagerNote.setCurrentItem(0);
        ((CreateNoteFragment) pagerAdapter.getItem(0)).showNoteAlarm(note);
    }

    /**
     * Adapter for the viewpager using FragmentPagerAdapter
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_search_note, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);
    }

}
