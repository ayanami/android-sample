package com.example.hoge;


import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

public abstract class AbstractAdapter<T> extends BaseAdapter {

    protected Context context;

    private List<T> items;

    protected AbstractAdapter(Context context, List<T> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected AbsListView.LayoutParams getWrapContentLayoutParams() {

        final int wrapContent = AbsListView.LayoutParams.WRAP_CONTENT;

        return new AbsListView.LayoutParams(wrapContent, wrapContent);
    }


    protected AbsListView.LayoutParams getMatchParentLayoutParams() {

        final int matchParent = AbsListView.LayoutParams.MATCH_PARENT;

        return new AbsListView.LayoutParams(matchParent, matchParent);
    }

    protected AbsListView.LayoutParams getLayoutParams(int w, int h) {

        return new AbsListView.LayoutParams(w, h);
    }

    protected Point getDisplaySize() {

        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);

        return point;
    }

    protected int getDisplayWidth() {

        return this.getDisplaySize().x;
    }

}
