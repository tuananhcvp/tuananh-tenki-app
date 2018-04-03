package com.example.anh.itenki.utils;

/**
 * 追加読み込み用のViewAdapter
 */

import android.support.v7.widget.RecyclerView;

import com.example.anh.itenki.R;

abstract public class LoadMoreRecyclerViewAdapter<T extends RecyclerView.ViewHolder> extends HeaderFooterRecyclerViewAdapter<T> {
    public LoadMoreRecyclerViewAdapter() {
        // フッターを追加しておく
        addFooter(R.layout.progress_bar);
    }

    public LoadMoreRecyclerViewAdapter(int resId) {
        // フッターを追加しておく
        addFooter(resId);
    }
}
