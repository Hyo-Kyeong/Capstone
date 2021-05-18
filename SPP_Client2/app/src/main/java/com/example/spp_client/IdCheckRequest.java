package com.example.spp_client;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class IdCheckRequest extends StringRequest {
    final static private String URL = "http://spp.dothome.co.kr/IdCheck.php";
    private Map<String, String> parameters;

    public IdCheckRequest(String id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", id);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
