package com.example.spp_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.concurrent.TimeUnit;


public class MyInformationRevise extends AppCompatActivity {
    private static final String TAG = "PhoneAuthActivity";
    //[START declare_auth]
    private FirebaseAuth mAuth;
    //[END declare_auth]

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private EditText password;
    private EditText birth;
    private EditText PN;
    private EditText editAuthenticationNo;
    private TextView textState;
    private Button revise;
    private Member member;

    String Revisedbirth;
    String Revisedpassword;
    String RevisedPN;
    String ID;
    private boolean bAuth;




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
        editAuthenticationNo = (EditText)findViewById(R.id.authenticationNo2);
        textState = (TextView)findViewById(R.id.textState2);
        revise = (Button)findViewById(R.id.reviseBtn);

        bAuth=false;
        editAuthenticationNo.setEnabled(false);
        revise.setEnabled(false);

        //[START initialize_auth]
        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //[END initialize_auth]
        //Initialize phone auth callbacks
        //[START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                //signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
        //[END phone_auth_callbacks]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
        // [END verify_with_code]
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            textState.setText("인증에 성공했습니다.");
                            textState.setTextColor(Color.BLACK);
                            textState.setVisibility(View.VISIBLE);
                            Button authenticationBtn = (Button)findViewById(R.id.authenticationBtn2);
                            authenticationBtn.setEnabled(false);
                            revise.setEnabled(true);
                            bAuth=true;
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();
                            textState.setText("인증에 실패했습니다.");
                            textState.setTextColor(Color.RED);
                            textState.setVisibility(View.VISIBLE);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    private void updateUI(FirebaseUser user) {

    }

    @Override // 빈공간 누르면 keyboard 내리기
    public boolean dispatchTouchEvent(MotionEvent ev) {
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

    public void onClickReceive(View v){
        String regExp = "^01(?:0|1|[6-9])[-]?(\\d{4})[-]?(\\d{4})$";
        if(PN.getText().toString().matches(regExp)){
            Button receiveBtn = (Button)findViewById(R.id.receiveBtn2);
            receiveBtn.setText("다시 보내기");
            startPhoneNumberVerification("+82"+PN.getText().toString());
            editAuthenticationNo.setEnabled(true);
            Toast.makeText(getApplicationContext(),"60초 이내에 인증번호를 입력하세요.",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"전화번호를 정확히 입력해주세요.",Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickAuthentication(View v){
        if(!editAuthenticationNo.getText().toString().equals("")) {
            verifyPhoneNumberWithCode(mVerificationId, editAuthenticationNo.getText().toString());
        }
    }

    public void onClickRevise(View v){

        password.setEnabled(false);
        PN.setEnabled(false);
        birth.setEnabled(false);
        editAuthenticationNo.setEnabled(false);

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