package com.example.hoge;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * <p>
 * HTTPコネクションマネージャクラスです。
 * </p>
 *
 * 作成日：2014/04/16<br>
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
 * <td>2014/04/16</td>
 * <td>－</td>
 * <td>HWS 鈴木</td>
 * <td>新規作成</td>
 * </tr>
 * </table>
 *
 * @author HWS 鈴木
 */
public class HttpConnectionManager extends AsyncTaskLoader<List<HttpResponseDto>> {

    /** コンテキスト */
    private Context context;

    /** URIリスト */
    private List<String> uris;

    /** HTTPメソッド */
    private HttpMethod httpMethod;

    /**
     * コンストラクタです。
     *
     * @param context コンテキスト
     * @param uri URI
     * @param httpMethod HTTPメソッド
     */
    public HttpConnectionManager(Context context, List<String> uris, HttpMethod httpMethod) {
        super(context);

        this.context = context;
        this.uris = uris;
        this.httpMethod = httpMethod;
    }

    /**
     * プロパティに従い{@link HttpClient}を実行し、{@link HttpResponseDto}のリストを返却します。
     */
    @Override
    public List<HttpResponseDto> loadInBackground() {

        HttpClient httpClient = new DefaultHttpClient();

        try {

            List<HttpResponseDto> dtos = new ArrayList<HttpResponseDto>();

            for (final String uri : this.uris) {

                HttpResponseDto dto = httpClient.execute(this.getRequest(uri), new HttpResponseHandler(context, uri));

                dtos.add(dto);
            }

            return dtos;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }

    /**
     * HTTPメソッドに従い{@link HttpUriRequest}インスタンスを返却します。
     *
     * @param uri リクエストURI
     * @return {@link HttpUriRequest}インスタンス
     */
    private HttpUriRequest getRequest(String uri) {

        switch (this.httpMethod) {
            case GET:
                return new HttpGet(uri);
            case POST:
                return new HttpPost(uri);
            default:
                return null;
        }
    }
}
