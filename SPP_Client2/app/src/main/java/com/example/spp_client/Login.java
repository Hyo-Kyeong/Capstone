package com.example.spp_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    EditText id;
    EditText pw;

    Response.Listener<String> responseListener;
    Response.Listener<String> responseListener2;

    String name, birth, phone, car, card, cvc, exp_year, exp_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText)findViewById(R.id.editTextID);
        pw = (EditText)findViewById(R.id.editTextPW);

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Integer login = jsonResponse.getInt("login");
                    if (login==1) {

                        Member member = Member.getInstance();
                        member.setID(id.getText().toString());
                        member.setPW(pw.getText().toString());

                        responseListener2 = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Intent myIntent = new Intent(getApplicationContext(), MainPage.class);
                                    JSONObject jsonResponse = new JSONObject(response);
                                    name = jsonResponse.getString("name");
                                    birth = jsonResponse.getString("birth");
                                    phone = jsonResponse.getString("phone");
                                    car = jsonResponse.getString("car");
                                    card = jsonResponse.getString("card");
                                    cvc = jsonResponse.getString("cvc");
                                    exp_year = jsonResponse.getString("exp_year");
                                    exp_month = jsonResponse.getString("exp_month");

                                    Member member = Member.getInstance();
                                    member.setName(name);
                                    member.setBirth(birth);
                                    member.setPhone(phone);
                                    if(car.matches("null")) car="";
                                    if(card.matches("null")) card="";
                                    if(cvc.matches("null")) cvc="";
                                    member.setCarNo(car);
                                    member.setCardNo(card);
                                    if(exp_month.matches("null")&&exp_year.matches("null")) member.setValidDate("");
                                    else member.setValidDate(exp_month+"/"+exp_year);
                                    member.setCVC(cvc);

                                    startActivity(myIntent);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        SelectInfoRequest request2 = new SelectInfoRequest(id.getText().toString(), responseListener2);
                        RequestQueue queue2 = Volley.newRequestQueue(Login.this);
                        queue2.add(request2);
                    }
                    else {
                        Toast.makeText(Login.this, "아이디 혹은 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
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
        LoginRequest request = new LoginRequest(id.getText().toString(), pw.getText().toString(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(Login.this);
        queue.add(request);
    }

    public void onSignUpButtonClicked(View v){
        Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(myIntent);
    }
}