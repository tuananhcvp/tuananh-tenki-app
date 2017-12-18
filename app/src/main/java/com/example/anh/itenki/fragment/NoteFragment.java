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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anh on 2017/12/14.
 */

public class NoteFragment extends Fragment implements SearchView.OnQueryTextListener {
    private ViewPager pagerNote;
    private TabLayout tabsNote;
    private ViewPagerAdapter pagerAdapter;

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
        pagerAdapter.addFragment(CreateNoteFragment.newInstance(), "Create Note");
        pagerAdapter.addFragment(ListNoteFragment.newInstance(), "List Note");
        pagerAdapter.addFragment(ListAlarmNoteFragment.newInstance(), "Alarm Note");
        pagerNote.setAdapter(pagerAdapter);

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
                Log.e("onPageSelected", "=> " + position);
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

    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
//            return super.getItemPosition(object);
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
