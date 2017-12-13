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

import es.dmoral.toasty.Toasty;

/**
 * Created by anh on 2017/12/06.
 */

public class SelectLocationFragment extends Fragment {
    private Button btnSeeWeather;
    private AutoCompleteTextView actvAddress;
    private ListView lvJapanCity;

    private ArrayAdapter<String> adapterCity = null;
    private ArrayAdapter<String> adapterJapanCity = null;
    private View mainView;

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
        mainView = inflater.inflate(R.layout.fragment_select_location, container, false);

        actvAddress = (AutoCompleteTextView)mainView.findViewById(R.id.actvAddress);
        btnSeeWeather = (Button)mainView.findViewById(R.id.btnSeeWeather);
        lvJapanCity = (ListView)mainView.findViewById(R.id.lvJapanCity);

        return mainView;
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
//                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                in.hideSoftInputFromWindow(mainView.getApplicationWindowToken(), 0);
            }
        });

        actvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
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
                    Toasty.error(getActivity(), "Please input an address!", Toast.LENGTH_SHORT, true).show();
                    return;
                } else {
                    Intent mIntent = new Intent(getActivity(), SelectedLocationWeatherActivity.class);
                    mIntent.putExtra("SelectedAddress", actvAddress.getText().toString());
                    startActivity(mIntent);
                }
            }
        });

    }

}
