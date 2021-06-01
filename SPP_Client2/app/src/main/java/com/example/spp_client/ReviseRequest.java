package com.example.spp_client;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReviseRequest extends StringRequest {
    final static private String URL = "http://spp.dothome.co.kr/MyInfoUpdate.php";
    private Map<String, String> parameters;


    public ReviseRequest(String pw,String birth,String phone,String id, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("pw", pw);
        parameters.put("birth", birth);
        parameters.put("phone", phone);


    }

    @Override
    protected Map<String, String> getParams()  {
        return parameters;
    }
}
