package com.example.hoge;

import org.apache.http.Header;

/**
 * <p>
 * HTTPレスポンス<code>Dto</code>クラスです。
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
public class HttpResponseDto {

    /** ステータスコード */
    private int statusCode;

    /** レスポンスヘッダ */
    private Header[] headers;

    /** レスポンスボディ */
    private String body;

    /** ファイル名 */
    private String fileName;

    /**
     * ステータスコードを設定します。
     * @param statusCode ステータスコード
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * ステータスコードを返却します。
     * @return ステータスコード
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * レスポンスヘッダを設定します。
     * @param headers レスポンスヘッダ
     */
    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    /**
     * レスポンスヘッダを返却します。
     * @return レスポンスヘッダ
     */
    public Header[] getHeaders() {
        return this.headers;
    }

    /**
     * レスポンスボディを設定します。
     * @param body レスポンスボディ
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * レスポンスボディを返却します。
     * @return レスポンスボディ
     */
    public String getBody() {
        return this.body;
    }

    /**
     * ファイル名を設定します。
     * @param fileName ファイル名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * ファイル名を返却します。
     * @return ファイル名
     */
    public String getFileName() {
        return this.fileName;
    }
}
