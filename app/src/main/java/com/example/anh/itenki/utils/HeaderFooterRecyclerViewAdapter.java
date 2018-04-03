package com.example.anh.itenki.utils;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * ヘッダーフッターを設定できるRecyclerViewのViewAdapter
 * ヘッダーフッターそれぞれ１つの種類しか登録できません。
 */

abstract public class HeaderFooterRecyclerViewAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Integer> footers = new ArrayList<>();
    List<Integer> headers = new ArrayList<>();
    ViewDataBinding headerBinding;

    @Override
    final public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.FOOTER.type) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(footers.get(0), parent, false));
        }
        if (viewType == ViewType.HEADER.type) {
            View headerView = LayoutInflater.from(parent.getContext()).inflate(headers.get(0), parent, false);
            headerBinding = DataBindingUtil.bind(headerView);
            onHeaderBinding();
            return new ViewHolder(headerView);
        }
        return onCreateViewHolderContent(parent, viewType);
    }

    @Override
    final public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isContentsPosition(position)) {
            onBindViewHolderContent((T) holder, position - headers.size());
        }
    }

    @Override
    final public int getItemCount() {
        return getContentsCount() + headers.size() + footers.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooter(position)) {
            return ViewType.FOOTER.type;
        }
        if (isHeader(position)) {
            return ViewType.HEADER.type;
        }
        return getContentItemViewType(position - headers.size());
    }

    /**
     * ヘッダーのBindingを取得
     *
     * @param <T>
     * @return
     */
    protected <T extends ViewDataBinding> T getHeaderBinding() {
        return (T) headerBinding;
    }

    protected abstract T onCreateViewHolderContent(ViewGroup parent, int viewType);

    protected abstract void onBindViewHolderContent(T holder, int position);

    protected abstract int getContentItemViewType(int position);

    protected abstract void onHeaderBinding();

    /**
     * ヘッダーの追加
     *
     * @param resourceId
     */
    public void addHeader(int resourceId) {
        headers.add(resourceId);
    }

    /**
     * フッターの追加
     *
     * @param resourceId
     */
    public void addFooter(int resourceId) {
        footers.add(resourceId);
    }

    /**
     * ヘッダーをすべて削除
     */
    public void clearHeader() {
        headers.clear();
    }

    /**
     * フッターをすべて削除
     */
    public void clearFooter() {
        footers.clear();
    }

    /**
     * 指定されたpositionがHeader/Footer以外のコンテンツの領域かどうか
     *
     * @param position
     * @return
     */
    public boolean isContentsPosition(int position) {
        if (isHeader(position) || isFooter(position)) {
            return false;
        }
        return true;
    }

    /**
     * ヘッダー領域かどうか
     *
     * @param position
     * @return
     */
    private boolean isHeader(int position) {
        return headers.size() > position;
    }

    /**
     * フッター領域かどうか
     *
     * @param position
     * @return
     */
    private boolean isFooter(int position) {
        return getContentsCount() + headers.size() <= position;
    }

    /**
     * Header/Footerを除いたコンテンツのindex
     *
     * @param position
     * @return
     */
    public int getContentsPosition(int position) {
        if (isContentsPosition(position)) {
            return position - headers.size();
        }
        return -1;
    }

    /**
     * コンテンツ用のフォルダー
     *
     * @param holder
     * @param <T>
     * @return
     */
    public <T> T getViewHolder(RecyclerView.ViewHolder holder) {
        return (T) holder;
    }

    /**
     * コンテンツの要素数
     *
     * @return
     */
    abstract public int getContentsCount();

    enum ViewType {
        HEADER(998),
        FOOTER(999);
        private int type;

        ViewType(int type) {
            this.type = type;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
