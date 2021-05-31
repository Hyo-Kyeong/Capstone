package com.example.spp_client;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MyInfoRequest extends StringRequest {
    final static private String URL = "http://spp.dothome.co.kr/MyInfoUpdate.php";
    private Map<String, String> parameters;

    public MyInfoRequest(String PW,String birth, String phone,String ID,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();



    }


}
