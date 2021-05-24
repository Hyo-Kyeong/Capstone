package com.example.spp_client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatActivity;

public class MyInformation extends AppCompatActivity {
    private Member member;
    private EditText carNo;
    private Button carInfoBtn;

    private EditText cardNo;
    private EditText cardCompany;
    private EditText cvc;
    private EditText validDate;
    private Button cardInfoBtn;

    Intent intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);


        intent2 = new Intent();

        Intent intent = getIntent();
        member = (Member) intent.getSerializableExtra("Member");
        TextView userID = (TextView)findViewById(R.id.textUserID2);
        userID.setText(member.getID()+"님");
        userID = (TextView)findViewById(R.id.TextprintID);
        userID.setText(member.getID());
        TextView userBirth = (TextView)findViewById(R.id.TextprintBirth);
        userBirth.setText(member.getBirth());
        TextView userPassword = (TextView)findViewById(R.id.TextprintPassword);
        userPassword.setText(member.getPW());
        TextView userPhoneNumber = (TextView)findViewById(R.id.TextprintPN);
        userPhoneNumber.setText(member.getPhone());
        TextView userCarNum = (TextView)findViewById(R.id.TextprintCarNum);
        userCarNum.setText(member.getCarNo());

        //TextView userCardCompany = (TextView)findViewById(R.id.TextprintCC);
        //userCardCompany.setText(member.);

        TextView userCardNumber = (TextView)findViewById(R.id.TextprintCardNum);
        userCardNumber.setText(member.getCardNo());
        TextView userCVC = (TextView)findViewById(R.id.TextprintCVC);
        userCVC.setText(member.getCVC());
        TextView userCardValid = (TextView)findViewById(R.id.TextprintCV);
        userCardValid.setText(member.getValidDate());









    }
}