package com.example.hoge.scene;

import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import com.example.hoge.sprite.BtnGigaGravity;
import com.example.hoge.sprite.BtnGo;

public class ButtleScene extends Scene {

    /**
     * コンストラクタです。
     */
    public ButtleScene(BaseGameActivity activity) {
        super();

        attachChild(new BtnGo(activity));
        attachChild(new BtnGigaGravity(activity));
    }

}
