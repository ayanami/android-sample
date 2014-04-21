package com.example.hoge;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

import android.content.Context;

/**
 * <p>
 * HTTPレスポンスハンドラクラスです。
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
public class HttpResponseHandler implements ResponseHandler<HttpResponseDto> {

    /** コンテキスト */
    private Context context;

    /** URI */
    private String uri;

    /**
     * コンストラクタです。
     * @param uri URI
     */
    public HttpResponseHandler(Context context, String uri) {

        this.context = context;
        this.uri = uri;
    }

    @Override
    public HttpResponseDto handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

        HttpResponseDto dto = new HttpResponseDto();

        dto.setStatusCode(response.getStatusLine().getStatusCode());

        switch (dto.getStatusCode()) {

            case HttpStatus.SC_OK:

                dto.setHeaders(response.getAllHeaders());
                HttpEntity entity = response.getEntity();

                if (isImage(entity.getContentType().getValue())) {

                    String fileName = getFileName(uri);
                    dto.setFileName(fileName);

                    write(fileName, entity);

                } else {
                    dto.setBody(EntityUtils.toString(entity));
                }
            default:
                break;
        }

        return dto;
    }

    /**
     * コンテンツタイプがイメージかを判定します。
     *
     * @param contentType コンテンツタイプ
     * @return 判定結果
     */
    private boolean isImage(String contentType) {

        if (contentType.indexOf("image/") > -1) {
            return true;
        }

        return false;
    }

    /**
     * URIに含まれるファイル名を返却します。
     *
     * @param uri URI
     * @return ファイル名
     */
    private String getFileName(String uri) {

        String[] uriParts = uri.split("/");

        return uriParts[uriParts.length - 1];
    }

    /**
     * ファイルをプライベートモードでキャッシュします。
     * @param fileName ファイル名
     * @param entity
     * @throws IOException
     */
    private void write(String fileName, HttpEntity entity) throws IOException {

        FileOutputStream fos = this.context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
        InputStream is = (new BufferedHttpEntity(entity)).getContent();
        byte[] buffer = new byte[4096];
        int size = 0;

        while ((size = is.read(buffer)) > 0) {
            fos.write(buffer, 0, size);
        }

        fos.close();
        is.close();
    }

}
