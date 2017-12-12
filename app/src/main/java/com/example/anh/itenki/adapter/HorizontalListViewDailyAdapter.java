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
 * Created by anh on 2017/12/06.
 */

public class HorizontalListViewDailyAdapter extends BaseAdapter {

    private Activity context = null;
    private String[] arrTime;
    private String[] arrTemp;
    private String[] arrIcon;

    public HorizontalListViewDailyAdapter(Activity context, String[] arrTime, String[] arrTemp, String[] arrIcon) {
        this.context = context;
        this.arrTime = arrTime;
        this.arrTemp = arrTemp;
        this.arrIcon = arrIcon;
    }

    @Override
    public int getCount() {
        return 8;
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
            convertView = inflater.inflate(R.layout.item_horizontal_daily_weather, null);
        }
        TextView txtDailyTime = (TextView)convertView.findViewById(R.id.txtDailyTime);
        TextView txtDailyTemp = (TextView)convertView.findViewById(R.id.txtDailyTemp);
        ImageView imgDailySky = (ImageView)convertView.findViewById(R.id.imgDailySky);

        txtDailyTime.setText(arrTime[i]);
        txtDailyTemp.setText(arrTemp[i]);
        Glide.with(context).load(context.getString(R.string.base_icon_url)+arrIcon[i]+".png").into(imgDailySky);

        return convertView;
    }
}
