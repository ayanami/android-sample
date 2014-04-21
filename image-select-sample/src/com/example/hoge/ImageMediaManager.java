package com.example.hoge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;

/**
 * 画像データマネージャクラスです。
 *
 * @author shimadev04
 *
 */
public class ImageMediaManager {

    /** コンテキスト */
    private Context context;

    /**
     * コンストラクタです。
     *
     * @param context
     *            コンテキスト
     */
    public ImageMediaManager(Context context) {

        this.context = context;
    }

    /**
     * デバイスに格納された、全画像データのマップを返却します。
     *
     * @return 画像データマップ(key:画像フルパス, value:Bitmap形式画像データ)
     */
    public Map<String, Bitmap> getAllImages() {

        Map<String, Bitmap> allImages = new HashMap<String, Bitmap>();

        MergeCursor cursor = new MergeCursor(
                new Cursor[] { this.getInternal(), this.getExternal() });

        int count = cursor.getCount();

        cursor.moveToFirst();

        for (int i = 0; i < count; i++) {

            cursor.moveToPosition(i);

            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            Bitmap thumbmail = MediaStore.Images.Thumbnails.getThumbnail(
                    this.context.getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND,
                    null);

            allImages.put(path, thumbmail);
        }

        cursor.close();

        return allImages;
    }

    /**
     * 画像データが格納されているフォルダのリストを返却します。
     *
     * @param images
     *            画像データマップ
     * @return フォルダリスト
     */
    public List<String> getImageFolders(Map<String, Bitmap> images) {

        Set<String> folders = new HashSet<String>();

        for (Map.Entry<String, Bitmap> e : images.entrySet()) {

            String[] splitPath = e.getKey().split("/");
            folders.add(splitPath[splitPath.length - 2]);
        }

        return new ArrayList<String>(folders);
    }

    /**
     * 指定フォルダに格納された画像データのリストを返却します。
     *
     * @param allImages
     *            画像データマップ
     * @param folder
     *            フォルダ
     * @return 画像データリスト
     */
    public List<Bitmap> getImages(Map<String, Bitmap> allImages, String folder) {

        List<Bitmap> images = new ArrayList<Bitmap>();

        for (Map.Entry<String, Bitmap> e : allImages.entrySet()) {

            String[] splitPath = e.getKey().split("/");

            if (folder.equals(splitPath[splitPath.length - 2])) {
                images.add(e.getValue());
            }
        }

        return images;
    }

    /**
     * デバイス内部画像データ取得用カーソルを返却します。
     *
     * @return
     */
    private Cursor getInternal() {

        return this.context.getContentResolver().query(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI, null, null, null, null);
    }

    /**
     * SDカード画像データ取得用カーソルを返却します。
     *
     * @return
     */
    private Cursor getExternal() {

        return this.context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
    }
}
