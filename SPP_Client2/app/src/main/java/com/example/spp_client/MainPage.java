package com.example.spp_client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainPage extends AppCompatActivity {
    private Member member;

    private TextView car;
    private TextView card;

    private ListView listView;
    private ListViewAdapter adapter;

    PaymentHistoryInMainRequest registerRequest;
    RequestQueue queue;
    Response.Listener<String> responseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        member = Member.getInstance();
        car = (TextView)findViewById(R.id.textCar);
        card = (TextView)findViewById(R.id.textCard);

        if(!member.getCarNo().toString().matches("")) car.setText(member.getCarNo());
        if(!member.getCardNo().toString().matches("")) card.setText(member.getCardNo());

        TextView userID = (TextView)findViewById(R.id.textUserID3);
        userID.setText(member.getID()+"님");

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.matches("null")) {
                        JSONArray jsonArray = new JSONArray(response);

                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            String date = jsonArray.getJSONArray(length - 1).getString(4 * i);
                            date = date.substring(0, 10);
                            String price = jsonArray.getJSONArray(length - 1).getString(4 * i + 1);
                            String store = jsonArray.getJSONArray(length - 1).getString(4 * i + 3);
                            adapter.addItem(date, store, price);

                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        adapter = new ListViewAdapter();

        listView = (ListView) findViewById(R.id.ListView2);
        listView.setAdapter(adapter);

        registerRequest = new PaymentHistoryInMainRequest(member.getID(), responseListener);
        queue = Volley.newRequestQueue(MainPage.this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!member.getCarNo().toString().matches("")) car.setText(member.getCarNo());
        if(!member.getCardNo().toString().matches("")) card.setText(member.getCardNo());

        adapter.deleteAllItems();
        queue.add(registerRequest);
    }

    public void onClickRegister(View v){
        if(member.getCarNo().matches("")||member.getCardNo().matches("")){
            Intent myIntent = new Intent(getApplicationContext(), PersonalInfoConsent.class);
            startActivity(myIntent);
        }
        else {
            Intent myIntent = new Intent(getApplicationContext(), Register.class);
            startActivity(myIntent);
        }
    }

    public void onClickPayment(View v){
        Intent myIntent = new Intent(getApplicationContext(), PaymentHistory.class);
        startActivity(myIntent);
    }

    public void onClickMyInformation(View v){
        Intent myIntent = new Intent(getApplicationContext(), MyInformation.class);
        startActivity(myIntent);
    }

    public void onClickLogout(View v){
        member = null;
        Intent myIntent = new Intent(getApplicationContext(), Login.class);
        myIntent.addFlags(myIntent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        finish();
    }
}