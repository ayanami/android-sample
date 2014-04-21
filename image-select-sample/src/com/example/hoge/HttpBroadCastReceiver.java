package com.example.hoge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

    private TextView tv;

    public HttpBroadCastReceiver(TextView tv) {
        super();
        this.tv = tv;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        int percentage = bundle.getInt("percentage");
        int receiveByte = bundle.getInt("receiveByte");

        this.tv.setText(receiveByte + " byte read. [" + percentage + "%]");
    }
}
