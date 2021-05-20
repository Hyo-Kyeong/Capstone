package com.example.spp_client;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;






public class SignUpRequest extends StringRequest {

    final static private String URL = "http://spp.dothome.co.kr/UserRegister.php";
    private Map<String, String> parameters;


    public SignUpRequest(String id, String pw, String name, String birth, String phone, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("pw", pw);
        parameters.put("name", name);
        parameters.put("birth", birth);
        parameters.put("phone", phone);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;

    }
}

