package com.example.hoge;


import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListAdapter extends AbstractAdapter<String> {

    protected ListAdapter(Context context, List<String> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView tv = (TextView)convertView;

        if (tv == null) {

            tv = new TextView(super.context);

            tv.setLayoutParams(super.getMatchParentLayoutParams());
            tv.setTextSize(30);
        }

        tv.setText((CharSequence)super.getItem(position));

        return tv;
    }

}
