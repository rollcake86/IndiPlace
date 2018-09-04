package com.rollcake.indi.indiplace.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018-05-18.
 */

public class ServerNetworking {

    public static final String TAG = ServerNetworking.class.getSimpleName();


    public interface getResult{
        void getResultText(String text);
        void fail(String Error);
    }

    public static void sendToMobileServer(Context context , int method , String url , final String[] keys , final String[] values , final getResult result) {

        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logs.e(TAG , "response : " +response);
                result.getResultText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result.fail("서버 실행 실패");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                if ( keys != null) {
                    for (int i = 0; i < keys.length; i++) {
                        params.put(keys[i], values[i]);
                    }
                }
                return params;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
    }
}
