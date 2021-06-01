package com.example.spp_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainPage extends AppCompatActivity {
    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        member = Member.getInstance();

        TextView userID = (TextView)findViewById(R.id.textUserID3);
        userID.setText(member.getID()+"님");
    }

    public void onClickRegister(View v){
        Intent myIntent = new Intent(getApplicationContext(), Register.class);
        startActivity(myIntent);
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