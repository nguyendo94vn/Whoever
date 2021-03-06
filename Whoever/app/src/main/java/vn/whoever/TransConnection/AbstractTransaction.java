package vn.whoever.TransConnection;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import vn.whoever.models.dao.ConnDB;

/**
 * Created by Nguyen Van Do on 4/24/2016.
 * Class abstract for connect - transaction data between application and server
 */
public abstract class AbstractTransaction {

    protected Activity activity;
    protected Integer httpStatusCode = null;
    protected String address = "http://192.168.0.113:8080/mainserver/mobile";

    public AbstractTransaction(Activity activity) {
        this.activity = activity;
    }

    /***
     * Configuration for header HTTP packet
     */
    protected Map<String, String> onCreateHeaders (Map<String, String> headers) {
        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("User-agent", System.getProperty("http.agent"));
        SQLiteDatabase db = ConnDB.getConn().getWritableDatabase();
        Cursor cursor = db.rawQuery("Select token, expTime from Auth", null);
        String token = "";
        String expTime = "";
        // Load token and token expiration time from database
        while (cursor.moveToNext()) {
            token = cursor.getString(0);
            expTime = cursor.getString(1);
        }
        cursor.close();
        // Set data into header HTTP packet
        headers.put("Whoever-token", token);
        headers.put("Token-expiration", expTime);
        headers.put("User-agent", System.getProperty("http.agent"));
        return headers;
    }

    public synchronized Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    // Get extract error response from server  and volley API
    protected void exTractError(VolleyError error) {
        NetworkResponse networkResponse = error.networkResponse;
        if(networkResponse != null) {
            httpStatusCode = networkResponse.statusCode;
        }
        if (error instanceof TimeoutError) {
            httpStatusCode = HttpStatus.SC_REQUEST_TIMEOUT;
        } else if(error instanceof NoConnectionError) {
            httpStatusCode = HttpStatus.SC_SERVICE_UNAVAIABLE;
        }else if (error instanceof AuthFailureError) {
            httpStatusCode = HttpStatus.SC_UNAUTHORIZED;
        } else if (error instanceof ServerError) {
            httpStatusCode = HttpStatus.SC_SERVER_INTERNAL;
        } else if (error instanceof NetworkError) {
            httpStatusCode = HttpStatus.SC_BAD_REQUEST;
        } else if (error instanceof ParseError) {
            httpStatusCode = HttpStatus.SC_SERVER_INTERNAL;
        }
    }

}
