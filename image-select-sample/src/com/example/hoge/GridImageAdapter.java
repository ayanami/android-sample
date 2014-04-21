package com.example.hoge;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GridImageAdapter extends AbstractAdapter<Bitmap> {

    public GridImageAdapter(Context context, List<Bitmap> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView iv = (ImageView) convertView;

        if (iv == null) {

            iv = new ImageView(super.context);
            iv.setLayoutParams(super.getLayoutParams(super.getDisplayWidth() / 3,
                    super.getDisplayWidth() / 3));
        }

        iv.setImageBitmap((Bitmap) super.getItem(position));

        return iv;
    }

}
