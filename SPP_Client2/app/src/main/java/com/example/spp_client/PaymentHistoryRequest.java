package com.example.spp_client;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PaymentHistoryRequest extends StringRequest {
    final static private String URL = "http://spp.dothome.co.kr/PaymentHistory.php";
    private Map<String, String> parameters;

    public PaymentHistoryRequest(String id, String start_date, String end_date, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("start_date", start_date);
        parameters.put("end_date", end_date);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
