package com.example.hoge.sprite;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.ui.activity.BaseGameActivity;

import com.example.hoge.util.RenderUtil;

public class BtnGo extends Sprite {

    public BtnGo(BaseGameActivity activity) {

        super(0, 0, RenderUtil.getTexture(activity, "btn-go.png"), activity
                .getVertexBufferObjectManager());
        this.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        Camera camera = activity.getEngine().getCamera();
        this.setPosition((camera.getWidth() / 2) - this.getWidth() - 20,
                camera.getHeight() - this.getHeight());
    }
}
