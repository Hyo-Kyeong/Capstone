package com.example.spp_client;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CarRegisterRequest extends StringRequest {
    final static private String URL = "http://spp.dothome.co.kr/CarRegister.php";
    private Map<String, String> parameters;

    public CarRegisterRequest(String id, String car, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("car", car);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}


