package com.example.hoge;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * <p>
 * HTTP{@link BroadcastReceiver}クラスです。
 * </p>
 *
 * 作成日：2014/04/21<br>
 *
 * <b>更新履歴</b><br>
 * <table border bgcolor="#ffffff">
 * <tr bgcolor="#ccccff">
 * <td>日付</td>
 * <td>欠陥管理番号</td>
 * <td>担当</td>
 * <td>変更点</td>
 * </tr>
 * <tr>
 * <td>2014/04/21</td>
 * <td>－</td>
 * <td>HWS 鈴木</td>
 * <td>新規作成</td>
 * </tr>
 * </table>
 *
 * @author HWS 鈴木
 */
public class HttpBroadCastReceiver extends BroadcastReceiver {

    /** {@link ProgressDialog} */
    private ProgressDialog dialog;

    /** 総受信数 */
    private int totalReceiveCount;

    /** 受信カウンター */
    private int receiveCounter;

    /** 初期化フラグ */
    private boolean isInit;

    /**
     * コンストラクタです。
     * @param dialog {@link ProgressDialog}
     */
    public HttpBroadCastReceiver(ProgressDialog dialog) {
        super();
        this.dialog = dialog;
    }

    /**
     * 受信処理です。
     * @param context コンテキスト
     * @param intent {@link Intent}
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        if (!this.isInit) {

            this.start(bundle);
            return;
        }

        int percentage = bundle.getInt("percentage");
        this.setProgress(percentage);

        if (percentage == 100 && this.totalReceiveCount > this.receiveCounter) {

            this.dialog.setProgress(this.receiveCounter);
            this.receiveCounter++;
        }

    }

    /**
     * 開始処理です。
     * @param bundle {@link Bundle}
     */
    private void start(Bundle bundle) {

        this.totalReceiveCount = bundle.getInt("totalReceiveCount");
        this.receiveCounter = 1;

        this.dialog.setTitle("hoge");
        this.dialog.setMessage("fuga .....");
        this.dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.dialog.setMax(this.totalReceiveCount);
        this.dialog.setProgress(0);
        this.dialog.show();
        this.isInit = true;
    }

    /**
     * 進捗を設定します。
     * @param bundle
     * @return
     */
    private void setProgress(int percentage) {

        int progress = (this.receiveCounter - 1) + (percentage / 100);
        this.dialog.setProgress(progress);
    }
}
