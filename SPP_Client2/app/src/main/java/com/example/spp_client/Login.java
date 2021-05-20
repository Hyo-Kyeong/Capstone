package com.example.spp_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    private String tmp_id = "root";
    private String tmp_pw = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) { // 빈공간 누르면 keyboard 내리기
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void onLoginButtonClicked(View v){
        EditText id = (EditText)findViewById(R.id.editTextID);
        EditText pw = (EditText)findViewById(R.id.editTextPW);
        if(id.getText().toString().matches(tmp_id)&&pw.getText().toString().matches(tmp_pw)) {
            Intent myIntent = new Intent(getApplicationContext(), MainPage.class);
            Member member = new Member(id.getText().toString(),pw.getText().toString(),"19991111","01011111111");
            myIntent.putExtra("Member", member);
            startActivity(myIntent);
        }
    }

    public void onSignUpButtonClicked(View v){
        Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(myIntent);
    }
}