package com.example.spp_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Register extends AppCompatActivity {
    private Member member;
    private EditText carNo;
    private Button carInfoBtn;

    private EditText cardNo;
    private EditText cvc;
    private EditText validMonth;
    private EditText validYear;
    private Button cardInfoBtn;

    Response.Listener<String> responseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        member = Member.getInstance();
        TextView userID = (TextView)findViewById(R.id.textUserID);
        userID.setText(member.getID()+"님");

        carNo = (EditText)findViewById(R.id.editTextCarNo);
        carInfoBtn = (Button)findViewById(R.id.carInfoBtn);
        if(!member.getCarNo().equals("")){
            carNo.setText(member.getCarNo());
            carNo.setEnabled(false);
            carInfoBtn.setText("수정");
        }

        cardNo = (EditText)findViewById(R.id.editTextCardNo);
        cvc = (EditText)findViewById(R.id.editTextCVC);
        validMonth = (EditText)findViewById(R.id.editTextMonth);
        validYear = (EditText)findViewById(R.id.editTextYear);
        cardInfoBtn = (Button)findViewById(R.id.cardInfoBtn);
        if(!member.getCardNo().equals("")){
            cardNo.setText(member.getCardNo());
            cvc.setText(member.getCVC());
            validMonth.setText(member.getValidDate().substring(0,2));
            validYear.setText(member.getValidDate().substring(3,5));
            cardNo.setEnabled(false);
            cvc.setEnabled(false);
            validMonth.setEnabled(false);
            validYear.setEnabled(false);
            cardInfoBtn.setText("수정");
        }

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String log = jsonResponse.getString("result");
                    System.out.println(log);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) System.out.println("success");
                    else System.out.println("fail");
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

    public void onClickLogout(View v){
        member = null;
        Intent myIntent = new Intent(getApplicationContext(), Login.class);
        myIntent.addFlags(myIntent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        finish();
    }

    public void onClickCarInfoBtn(View v){
        if(carInfoBtn.getText().toString().equals("등록")){
            CarRegisterRequest registerRequest = new CarRegisterRequest(member.getID(), carNo.getText().toString(), responseListener);
            RequestQueue queue = Volley.newRequestQueue(Register.this);
            queue.add(registerRequest);

            carNo.setEnabled(false);
            carInfoBtn.setText("수정");
            member.setCarNo(carNo.getText().toString());
        }
        else if(carInfoBtn.getText().toString().equals("수정")){
            carNo.setEnabled(true);
            carInfoBtn.setText("확인");
        }
        else if(carInfoBtn.getText().toString().equals("확인")){
            carNo.setEnabled(false);
            carInfoBtn.setText("수정");
            member.setCarNo(carNo.getText().toString());
        }
    }

    public void onClickCardInfoBtn(View v){
        if(cardInfoBtn.getText().toString().equals("등록")){
            CardRegisterRequest registerRequest = new CardRegisterRequest(member.getID(), cardNo.getText().toString(), validMonth.getText().toString().substring(0,2),
                    validYear.getText().toString().substring(3,5), cvc.getText().toString(), responseListener);
            RequestQueue queue = Volley.newRequestQueue(Register.this);
            queue.add(registerRequest);

            cardNo.setEnabled(false);
            cvc.setEnabled(false);
            validMonth.setEnabled(false);
            validYear.setEnabled(false);
            cardInfoBtn.setText("수정");
            member.setCardNo(cardNo.getText().toString());
            member.setCVC(cvc.getText().toString());
            member.setValidDate(validMonth.getText().toString()+"/"+validYear.getText().toString());
        }
        else if(cardInfoBtn.getText().toString().equals("수정")){
            cardNo.setEnabled(true);
            cvc.setEnabled(true);
            validMonth.setEnabled(true);
            validYear.setEnabled(true);
            cardInfoBtn.setText("확인");
        }
        else if(cardInfoBtn.getText().toString().equals("확인")){
            cardNo.setEnabled(false);
            cvc.setEnabled(false);
            validMonth.setEnabled(false);
            validYear.setEnabled(false);
            cardInfoBtn.setText("수정");
            member.setCardNo(cardNo.getText().toString());
            member.setCVC(cvc.getText().toString());
            member.setValidDate(validMonth.getText().toString()+"/"+validYear.getText().toString());
        }
    }
}