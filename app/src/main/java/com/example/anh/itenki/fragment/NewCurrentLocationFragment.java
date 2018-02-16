package com.example.anh.itenki.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anh.itenki.R;
import com.example.anh.itenki.databinding.NewFragmentCurrentLocationBinding;
import com.example.anh.itenki.viewmodel.CurrentForecastViewModel;

import javax.inject.Inject;

/**
 * Created by anh on 2018/02/16.
 */

public class NewCurrentLocationFragment extends Fragment {
    private NewFragmentCurrentLocationBinding binding;

    @Inject
    CurrentForecastViewModel viewModel;

    /**
     * NewCurrentLocationFragment initialize
     *
     * @return NewCurrentLocationFragment
     */
    public static NewCurrentLocationFragment newInstance() {
        NewCurrentLocationFragment fragment = new NewCurrentLocationFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View layout = inflater.inflate(R.layout.new_fragment_current_location, container, false);

        return layout;
    }
}
