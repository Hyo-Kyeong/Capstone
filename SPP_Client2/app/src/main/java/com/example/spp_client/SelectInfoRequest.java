package com.example.spp_client;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SelectInfoRequest extends StringRequest {
    final static private String URL = "http://spp.dothome.co.kr/SelectInfo.php";
    private Map<String, String> parameters;

    public SelectInfoRequest(String id, String pw,  Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("pw", pw);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}