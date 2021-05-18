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

        Intent intent = getIntent();
        member = (Member) intent.getSerializableExtra("Member");

        TextView userID = (TextView)findViewById(R.id.textUserID3);
        userID.setText(member.getID()+"님");
    }

    public void onClickRegister(View v){
        Intent myIntent = new Intent(getApplicationContext(), Register.class);
        myIntent.putExtra("Member", member);
        startActivityForResult(myIntent,0);
    }

    public void onClickPayment(View v){
        Intent myIntent = new Intent(getApplicationContext(), PaymentHistory.class);
        //myIntent.putExtra("Member", member);
        startActivity(myIntent);
    }

    public void onClickMyInformation(View v){
        Intent myIntent = new Intent(getApplicationContext(), MyInformation.class);
        myIntent.putExtra("Member", member);
        startActivity(myIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if(resultCode==1){ finish();}
                else {
                    member = (Member) data.getSerializableExtra("Member");
                    break;
                }
        }
    }
}