package com.example.anh.itenki.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anh.itenki.R;

/**
 * Created by anh on 2017/12/11.
 */

public class NextDaysWeatherAdapter extends BaseAdapter {
    private Activity context;
    private String[] arrIcon;
    private String[] arrDay;
    private String[] arrDate;
    private String[] arrTemp;

    public NextDaysWeatherAdapter(Activity context, String[] arrDay, String[] arrDate, String[] arrIcon, String[] arrTemp){
        this.context = context;
        this.arrDay = arrDay;
        this.arrDate = arrDate;
        this.arrIcon = arrIcon;
        this.arrTemp = arrTemp;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_next_days_weather, null);
        }
        TextView detailDay = (TextView)convertView.findViewById(R.id.detailDay);
        TextView detailDayMont = (TextView)convertView.findViewById(R.id.detailDayMonth);
        TextView dtMinMaxTemp = (TextView)convertView.findViewById(R.id.dtMinMaxTemp);
        ImageView dtImgViewSky = (ImageView)convertView.findViewById(R.id.dtImgViewSky);

        detailDay.setText(arrDay[i]);
        detailDayMont.setText(arrDate[i]);
        dtMinMaxTemp.setText(arrTemp[i]);
        Glide.with(context).load(context.getString(R.string.base_icon_url)+arrIcon[i]+".png").into(dtImgViewSky);

        return convertView;
    }

}
