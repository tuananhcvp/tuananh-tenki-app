package com.example.anh.itenki.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.anh.itenki.R;
import com.example.anh.itenki.activity.MainActivity;

/**
 * Created by anh on 2017/12/20.
 */

public class AboutAppFragment extends Fragment {

    /**
     * AboutAppFragment initialize
     *
     * @return AboutAppFragment
     */
    public static AboutAppFragment newInstance() {
        Bundle args = new Bundle();
        AboutAppFragment fragment = new AboutAppFragment();
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
        ((MainActivity) getActivity()).setActionBarName(getResources().getString(R.string.title_about));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about_app, container, false);

        TextView tvAppName = (TextView)v.findViewById(R.id.tvAppName);
        TextView tvVersion = (TextView)v.findViewById(R.id.tvVersion);

        tvAppName.setText(getResources().getString(R.string.txt_app_name) + ": " + getResources().getString(R.string.app_name));
        tvVersion.setText(getResources().getString(R.string.txt_version));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
