package com.nshane.dualdialog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nshane.dualdialog.R;
import com.nshane.dualdialog.bean.ThemeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbl on 2017/3/30.
 */

public class ThemeItemAdapter extends BaseAdapter {
    private List<ThemeInfo> mThemeList = new ArrayList<>();

    private LayoutInflater mInflater;

    public ThemeItemAdapter(Context context, List<ThemeInfo> list) {
        if (list != null && list.size() > 0) {
            mThemeList.addAll(list);
        }
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mThemeList.size();
    }

    @Override
    public Object getItem(int position) {
        return mThemeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ThemeHolder holder;
        if (convertView == null) {
            holder = new ThemeHolder();
            convertView = mInflater.inflate(R.layout.theme_item, null);
            holder.mThemeName = (TextView) convertView.findViewById(R.id.tv_theme);
            holder.mThemeSample = (ImageView) convertView.findViewById(R.id.iv_theme);
            convertView.setTag(holder);
        } else {
            holder = (ThemeHolder) convertView.getTag();
        }
        holder.mThemeName.setText(mThemeList.get(position).themeName);
        holder.mThemeSample.setImageResource(mThemeList.get(position).themeSampleId);

        return convertView;
    }


    private class ThemeHolder {
        public TextView mThemeName;

        public ImageView mThemeSample;
    }

}
