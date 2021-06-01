package com.example.spp_client;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CardRegisterRequest extends StringRequest {
    final static private String URL = "http://spp.dothome.co.kr/CardRegister.php";
    private Map<String, String> parameters;

    public CardRegisterRequest(String id, String card, String exp_month, String exp_year, String CVC,  Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("card", card);
        parameters.put("expiration_month", exp_month);
        parameters.put("expiration_year", exp_year);
        parameters.put("cvc", CVC);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
