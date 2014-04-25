package com.example.hoge;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import android.util.DisplayMetrics;

import com.example.hoge.scene.ButtleScene;

public class MainActivity extends SimpleLayoutGameActivity {

    @Override
    public EngineOptions onCreateEngineOptions() {

        // 端末解像度取得
        DisplayMetrics metrics = new DisplayMetrics();
        super.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // 描画範囲指定
        final Camera camera = new Camera(0, 0, metrics.widthPixels, metrics.heightPixels);

        // オプション設定
        return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
                new RatioResolutionPolicy(metrics.widthPixels, metrics.heightPixels), camera);
    }

    @Override
    protected void onCreateResources() {
    }

    @Override
    protected Scene onCreateScene() {

        return new ButtleScene(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected int getRenderSurfaceViewID() {
        return R.id.renderSurfaceView;
    }
}
