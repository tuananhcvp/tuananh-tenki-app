package com.example.anh.itenki.view.helper;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * DataBindingHelper
 * カスタムバインディングを定義する
 */
public class DataBindingHelper {
    @BindingAdapter({"imageUrl", "error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        if (url == null || url.isEmpty()) {
            view.setImageDrawable(error);
            return;
        }
        Glide.with(view.getContext()).load(url).error(error).into(view);
    }
}
