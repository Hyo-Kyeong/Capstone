package com.example.spp_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class PersonalInfoConsent extends AppCompatActivity {
    Button m_OkBtn;
    boolean b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_consent);

        m_OkBtn = (Button)findViewById(R.id.okBtn);
        m_OkBtn.setEnabled(false);
        m_OkBtn.getBackground().setAlpha(80);


        b1=false;
        b2=false;

        final CheckBox box1 = (CheckBox)findViewById(R.id.imgChkButton);
        final CheckBox box2 = (CheckBox)findViewById(R.id.imgChkButton2);
        box1.setOnClickListener(new CheckBox.OnClickListener(){
            @Override
            public void onClick(View v){
                if(box1.isChecked()&&box2.isChecked()){
                    m_OkBtn.setEnabled(true);
                    m_OkBtn.getBackground().setAlpha(200);
                }
                else{
                    m_OkBtn.setEnabled(false);
                    m_OkBtn.getBackground().setAlpha(80);
                }
            }
        });
        box2.setOnClickListener(new CheckBox.OnClickListener(){
            @Override
            public void onClick(View v){
                if(box2.isChecked()&&box1.isChecked()){
                    m_OkBtn.setEnabled(true);
                    m_OkBtn.getBackground().setAlpha(200);
                }
                else{
                    m_OkBtn.setEnabled(false);
                    m_OkBtn.getBackground().setAlpha(80);
                }
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        CheckBox box1 = (CheckBox)findViewById(R.id.imgChkButton);
        CheckBox box2 = (CheckBox)findViewById(R.id.imgChkButton2);

        if(box1.isChecked()&&box2.isChecked()){
            m_OkBtn.setEnabled(true);
        }
    }

    public void onCancel(View v){
        finish();
    }

    public void onOk(View v){
        Intent myIntent = new Intent(getApplicationContext(), Register.class);
        startActivity(myIntent);
        finish();
    }
}