package com.example.spp_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.android.volley.Response;

import org.json.JSONObject;


public class MyInformationRevise extends AppCompatActivity {


    private EditText password;
    private EditText birth;
    private EditText PN;
    private Button revise;
    private Member member;

    String Revisedbirth;
    String Revisedpassword;
    String RevisedPN;
    String ID;




    Response.Listener<String> responseListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information_revise);


        revise = (Button) findViewById(R.id.cardInfoBtn2);


        member = Member.getInstance();
        TextView userID = (TextView) findViewById(R.id.textUserID4);
        userID.setText(member.getID() + "님");



        password = (EditText) findViewById(R.id.editTextPWRevise);
        password.setText(member.getPW());

        birth = (EditText) findViewById(R.id.editTextBirthRevise);
        birth.setText(member.getBirth());

        PN = (EditText) findViewById(R.id.editTextPNRevise);
        PN.setText(member.getPhone());


    }


    public void onClickLogout(View v){
        member = null;
        finish();
    }

    public void onClickRevise(View v){

        password.setEnabled(false);
        PN.setEnabled(false);
        birth.setEnabled(false);

        member.setPW(password.getText().toString());
        member.setPhone(PN.getText().toString());
        member.setBirth(birth.getText().toString());



        RevisedPN=PN.getText().toString();
        Revisedbirth=birth.getText().toString();
        Revisedpassword=password.getText().toString();

        ID=member.getID();
        ReviseRequest reviseRequest = new ReviseRequest(Revisedpassword,Revisedbirth,RevisedPN,ID,responseListener);
        RequestQueue queue = Volley.newRequestQueue(MyInformationRevise.this);
        queue.add(reviseRequest);




    }


}