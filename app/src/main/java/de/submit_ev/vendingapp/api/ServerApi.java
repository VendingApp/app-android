package de.submit_ev.vendingapp.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Igor on 28.05.2015.
 */
public class ServerApi {
    private static final String BASE_URL = "http://serverground.de:8080/";
    private static AsyncHttpClient client  = new AsyncHttpClient();

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        client.get(getAbsoluteUrl(url), params, handler);
    }

    public static void getVendors(double lat1, double lat2, double lng1, double lng2, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("lat1", lat1);
        params.put("lat2", lat2);
        params.put("long1", lng1);
        params.put("long2", lng2);

        get("vendor/getNear", params, handler);
    }

    public static void getProducts(Long vendorID, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("vendor", vendorID);

        get("vendorstorage/get", params, handler);
    }
}
