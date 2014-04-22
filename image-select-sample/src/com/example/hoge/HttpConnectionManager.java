package com.example.hoge;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;

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

    /** リクエストパラメータリスト */
    private List<List<NameValuePair>> paramsList;

    /** HTTPメソッド */
    private HttpMethod httpMethod;

    /**
     * コンストラクタです。
     *
     * @param context コンテキスト
     * @param uri URI
     * @param httpMethod HTTPメソッド
     */
    public HttpConnectionManager(Context context, String uri, HttpMethod httpMethod) {
        super(context);

        this.setProperties(context, uri, null, httpMethod);
    }

    /**
     * コンストラクタです。
     *
     * @param context コンテキスト
     * @param uri URI
     * @param params リクエストパラメータ
     * @param httpMethod HTTPメソッド
     */
    public HttpConnectionManager(Context context, String uri, List<NameValuePair> params,
            HttpMethod httpMethod) {
        super(context);

        this.setProperties(context, uri, params, httpMethod);
    }

    /**
     * コンストラクタです。
     *
     * @param context コンテキスト
     * @param uris URIリスト
     * @param httpMethod HTTPメソッド
     */
    public HttpConnectionManager(Context context, List<String> uris, HttpMethod httpMethod) {
        super(context);

        this.setProperties(context, uris, null, httpMethod);
    }

    /**
     * プロパティに従い{@link HttpClient}を実行し、{@link HttpResponseDto}のリストを返却します。
     */
    @Override
    public List<HttpResponseDto> loadInBackground() {

        HttpClient httpClient = new DefaultHttpClient();

        this.sendTotalReceiveCount();

        List<HttpResponseDto> dtos = new ArrayList<HttpResponseDto>();
        HttpResponseDto dto = null;

        try {

            for (int i = 0; i < this.uris.size(); i++) {

                dto = httpClient.execute(this.getRequest(this.uris.get(i), i),
                        new HttpResponseHandler(this.context, this.uris.get(i)));

                if (dto.getStatusCode() != HttpStatus.SC_OK) {

                    throw new Exception("StatusCode:" + String.valueOf(dto.getStatusCode()));
                }
                dtos.add(dto);
            }

        } catch (Exception e) {

            if (dto == null) {
                dto = new HttpResponseDto();
            }
            dto.setException(e);
            dtos.add(dto);
        }
        return dtos;

    }

    /**
     * プロパティを設定します。
     * @param context コンテキスト
     * @param uri URI
     * @param params リクエストパラメータ
     * @param httpMethod HTTPメソッド
     */
    private void setProperties(Context context, String uri, List<NameValuePair> params,
            HttpMethod httpMethod) {

        List<String> uris = new ArrayList<String>();
        uris.add(uri);

        List<List<NameValuePair>> paramsList = new ArrayList<List<NameValuePair>>();
        paramsList.add(params);

        this.setProperties(context, uris, paramsList, httpMethod);
    }

    /**
     * プロパティを設定します。
     * @param context コンテキスト
     * @param uri URI
     * @param paramsList リクエストパラメータリスト
     * @param httpMethod HTTPメソッド
     */
    private void setProperties(Context context, List<String> uris,
            List<List<NameValuePair>> paramsList, HttpMethod httpMethod) {

        this.context = context;
        this.uris = uris;
        this.paramsList = paramsList;
        this.httpMethod = httpMethod;
    }


    /**
     * 総受信数を{@link HttpBroadCastReceiver}に送信します。
     */
    private void sendTotalReceiveCount() {

        Intent intent = new Intent();

        intent.putExtra("totalReceiveCount", this.uris.size());
        intent.setAction("HTTP_BROAD_CAST");

        this.context.sendBroadcast(intent);
    }

    /**
     * HTTPメソッドに従い{@link HttpUriRequest}インスタンスを返却します。
     *
     * @param uri リクエストURI
     * @param idx リクエストパラメータリストの添え字
     * @return {@link HttpUriRequest}インスタンス
     * @throws UnsupportedEncodingException
     */
    private HttpUriRequest getRequest(String uri, int idx) throws UnsupportedEncodingException {

        switch (this.httpMethod) {
            case GET:
                return new HttpGet(uri);
            case POST:
                HttpPost request = new HttpPost(uri);
                if (this.paramsList != null && this.paramsList.size() > idx) {
                    request.setEntity(new UrlEncodedFormEntity(this.paramsList.get(idx), HTTP.UTF_8));
                }
                return request;
            default:
                return null;
        }
    }
}
