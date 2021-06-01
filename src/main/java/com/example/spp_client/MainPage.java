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

        TextView userCarNum = (TextView)findViewById(R.id.textprintCarNum2);
        userCarNum.setText(member.getCarNo());



        TextView userCardNumber = (TextView)findViewById(R.id.textprintCardNum2);
        userCardNumber.setText(member.getCardNo());

        TextView userPhoneNumber = (TextView)findViewById(R.id.textprintPN2);
        userPhoneNumber.setText(member.getPhone());
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
}