package com.example.anh.itenki.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.anh.itenki.R;
import com.example.anh.itenki.activity.MainActivity;
import com.example.anh.itenki.activity.SelectedLocationWeatherActivity;
import com.example.anh.itenki.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * Created by anh on 2017/12/06.
 */

public class SelectLocationFragment extends Fragment {
    private ArrayAdapter<String> adapterCity = null;
    private ArrayAdapter<String> adapterJapanCity = null;
    private Unbinder unbinder;

    @BindView(R.id.actvAddress)
    AutoCompleteTextView actvAddress;

    @BindView(R.id.btnSeeWeather)
    Button btnSeeWeather;

    @BindView(R.id.lvJapanCity)
    ListView lvJapanCity;


    /**
     * SelectLocationFragment initialize
     *
     * @return SelectLocationFragment
     */
    public static SelectLocationFragment newInstance() {
        Bundle args = new Bundle();
        SelectLocationFragment fragment = new SelectLocationFragment();
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
        ((MainActivity) getActivity()).setActionBarName(getResources().getString(R.string.title_select_location));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_location, container, false);

        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapterCity = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, MainActivity.cityArr);
        actvAddress.setAdapter(adapterCity);

        actvAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                adapterCity.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        actvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utils.hideSoftKeyboard(getContext(), view);
            }
        });

        adapterJapanCity = new ArrayAdapter<String>(getContext(), R.layout.simple_list_white_text, MainActivity.japanCityList);
        lvJapanCity.setAdapter(adapterJapanCity);

        lvJapanCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SelectedLocationWeatherActivity.class);
                intent.putExtra("SelectedAddress", MainActivity.japanCityList.get(position));
                startActivity(intent);
            }
        });

        btnSeeWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actvAddress.getText().toString().equals("")) {
                    Toasty.info(getActivity(), getResources().getString(R.string.input_address), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), SelectedLocationWeatherActivity.class);
                    intent.putExtra("SelectedAddress", actvAddress.getText().toString());
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unbind the view to free some memory
        unbinder.unbind();
    }

}
