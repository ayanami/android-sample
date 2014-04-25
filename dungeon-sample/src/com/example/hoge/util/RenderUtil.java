package com.example.hoge.util;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.graphics.BitmapFactory;

public class RenderUtil {

    protected RenderUtil() {

    }

    public static ITextureRegion getTexture(BaseGameActivity activity, String assetsPath) {

        BitmapFactory.Options options = getOptions(activity, assetsPath);

        BitmapTextureAtlas bta = new BitmapTextureAtlas(activity.getTextureManager(),
                getLargerThanSize(options.outWidth), getLargerThanSize(options.outHeight),
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        activity.getEngine().getTextureManager().loadTexture(bta);

        return BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta, activity, assetsPath, 0,
                0);

    }

    private static BitmapFactory.Options getOptions(BaseGameActivity activity, String assetsPath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        InputStream is = null;

        try {
            is = activity.getResources().getAssets().open(assetsPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.decodeStream(is, null, options);

        return options;
    }

    private static int getLargerThanSize(float size) {

        int baseSize = 64;
        int compareSize = (int)(size + 1);

        while (baseSize < compareSize) {
            baseSize *= 2;
        }

        return baseSize;
    }

}
