package com.example.hoge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphObject;

public class MainActivity extends Activity implements LoaderCallbacks<List<HttpResponseDto>> {

    private ViewGroup container;

    private ListView menu;

    private GridView gallery;

    private ProgressDialog dialog;

    private Map<String, Bitmap> datas;

    private List<String> menuItems;

    private List<Bitmap> thumbnails;

    private ImageMediaManager imageMediaManager;

    private LoaderManager loaderManager;

    private Session.StatusCallback callback = new Session.StatusCallback() {

        @Override
        public void call(Session session, SessionState state, Exception exception) {

            setFacebookImages();
        }
    };

    private OnItemClickListener menuClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view,
                int position, long id) {

            String key = ((TextView) view).getText().toString();

            if ("facebook".equals(key)) {

                setFacebookImages();
            }

            if ("instagram".equals(key)) {

                return;
            }

            setDeviceImages(key);

        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.container = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(this.container);

        this.init();
        this.initFacebook(savedInstanceState);

        this.loaderManager = getLoaderManager();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar isf it is
        // present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(callback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(callback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    private void init() {

        this.imageMediaManager = new ImageMediaManager(this);

        this.menuItems = new ArrayList<String>();
        this.menu = new ListView(this);
        this.menu.setAdapter(new ListAdapter(this, this.menuItems));
        this.menu.setOnItemClickListener(this.menuClickListener);

        this.thumbnails = new ArrayList<Bitmap>();
        this.gallery = new GridView(this);
        this.gallery.setNumColumns(3);
        this.gallery.setAdapter(new GridImageAdapter(this, this.thumbnails));

        this.dialog = new ProgressDialog(this);
        HttpBroadCastReceiver receiver = new HttpBroadCastReceiver(this.dialog);
        IntentFilter filter = new IntentFilter();
        filter.addAction("HTTP_BROAD_CAST");
        registerReceiver(receiver, filter);

        this.setMenu();
    }

    private void setMenu() {

        this.container.removeAllViews();

        this.datas = imageMediaManager.getAllImages();

        this.createMenuItems();
        this.menuItems.add("facebook");
        this.menuItems.add("instagram");

        this.container.addView(this.menu);
    }

    private void createMenuItems() {

        List<String> folders = this.imageMediaManager.getImageFolders(this.datas);

        for (String folder : folders) {

            this.menuItems.add(folder);
        }
    }

    private void setDeviceImages(String key) {

        this.container.removeAllViews();

        List<Bitmap> images = this.imageMediaManager.getImages(this.datas, key);

        for (Bitmap image : images) {

            this.thumbnails.add(image);
        }

        this.container.addView(gallery);
    }

    private void initFacebook(Bundle bundle) {

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();

        if (session == null) {

            if (bundle != null) {

                session = Session.restoreSession(this, null, callback, bundle);
            }

            if (session == null) {

                session = new Session(this);
            }

            Session.setActiveSession(session);

            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {

                Session.OpenRequest request = new Session.OpenRequest(this).setCallback(callback);
                request.setPermissions(new String[] { "user_photos" });
                session.openForRead(request);
            }

        }
    }

    private void setFacebookImages() {

        Session session = Session.getActiveSession();

        if (session.isOpened()) {

            Request.newGraphPathRequest(session, "me/photos", new Request.Callback() {

                @Override
                public void onCompleted(Response response) {

                    GraphObject g = response.getGraphObject();
                    JSONObject json = g.getInnerJSONObject();

                    try {

                        String imgUri = json.getJSONArray("data").getJSONObject(0)
                                .getString("picture");

                        ArrayList<String> uris = new ArrayList<String>();
                        for (int i = 0; i < 1000; i++) {
                            uris.add(imgUri);
                        }
                        Bundle args = new Bundle();
                        args.putStringArrayList("uris", uris);
                        loaderManager.initLoader(0, args, MainActivity.this);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).executeAsync();

        } else {

            if (!session.isClosed()) {

                session.openForRead(new Session.OpenRequest(this).setCallback(callback));
            } else {

                Session.openActiveSession(this, true, callback);
            }
        }
    }

    @Override
    public Loader<List<HttpResponseDto>> onCreateLoader(int id, Bundle args) {

        HttpConnectionManager loader = new HttpConnectionManager(this,
                args.getStringArrayList("uris"), HttpMethod.GET);
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<HttpResponseDto>> loader, List<HttpResponseDto> data) {

        this.dialog.dismiss();
        this.container.removeAllViews();

        try {
            for (HttpResponseDto dto : data) {
                File file = getFileStreamPath(dto.getFileName());
                FileInputStream fis = new FileInputStream(file);
                this.thumbnails.add(BitmapFactory.decodeStream(fis));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.container.addView(gallery);

    }

    @Override
    public void onLoaderReset(Loader<List<HttpResponseDto>> loader) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
