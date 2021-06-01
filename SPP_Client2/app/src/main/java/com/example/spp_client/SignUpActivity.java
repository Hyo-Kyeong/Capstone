package com.example.spp_client;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
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

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "PhoneAuthActivity";
    //[START declare_auth]
    private FirebaseAuth mAuth;
    //[END declare_auth]

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private EditText editName;
    private EditText editID;
    private EditText editPW;
    private EditText editPhone;
    private EditText editAuthenticationNo;
    private TextView textState;
    private Button idCheckBtn;

    private static String yy,mm,dd;
    private String birth;
    private DatePicker datePicker;

    private boolean bId, bAuth;

    Response.Listener<String> responseListener;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        textState = (TextView)findViewById(R.id.textState);
        editName = (EditText)findViewById(R.id.editTextName);
        editID = (EditText)findViewById(R.id.editTextID);
        editPW = (EditText)findViewById(R.id.editTextPW);
        editAuthenticationNo = (EditText)findViewById(R.id.authenticationNo);
        editPhone = (EditText)findViewById(R.id.editTextPhone);
        idCheckBtn = (Button)findViewById(R.id.checkBtn);
        datePicker = findViewById(R.id.datepicker);
        birth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
        new DatePicker.OnDateChangedListener(){
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                yy=Integer.toString(i);
                if(i1+1<10) mm="0"+Integer.toString(i1+1);
                else mm=Integer.toString(i1+1);
                if(i2<10) dd="0"+Integer.toString(i2);
                else dd=Integer.toString(i2);
                birth = yy+mm+dd;
            }
        });
        bId = bAuth = false;

        editAuthenticationNo.setEnabled(false);

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

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Integer id_exist = jsonResponse.getInt("id_exist");
                    if (id_exist==1) {
                        bId = false;
                        Toast.makeText(SignUpActivity.this, "중복된 ID입니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        bId = true;
                        editID.setEnabled(false);
                        idCheckBtn.setEnabled(false);
                        idCheckBtn.setText("사용 가능");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
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
                            Button authenticationBtn = (Button)findViewById(R.id.authenticationBtn);
                            authenticationBtn.setEnabled(false);
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

    private boolean isValidInput(){
        String name = ((EditText)findViewById(R.id.editTextName)).getText().toString();
        String id = ((EditText)findViewById(R.id.editTextID)).getText().toString();
        String pw = ((EditText)findViewById(R.id.editTextPW)).getText().toString();
        String pwChk = ((EditText)findViewById(R.id.editTextPWChk)).getText().toString();

        if(name.matches("")){
            return false;
        }
        if(birth.matches("")){
            return false;
        }
        if(id.matches("")){
            return false;
        }
        if(pw.matches("")){
            return false;
        }
        if(!pwChk.equals(pw)){
            return false;
        }
        return true;
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

    //취소 버튼 눌렀을 때 이벤트 핸들링
    public void onClickCancel(View v){
        finish();
    }

    public void onClickReceive(View v){
        String regExp = "^01(?:0|1|[6-9])[-]?(\\d{4})[-]?(\\d{4})$";
        if(editPhone.getText().toString().matches(regExp)){
            Button receiveBtn = (Button)findViewById(R.id.receiveBtn);
            receiveBtn.setText("다시 보내기");
            startPhoneNumberVerification("+82"+editPhone.getText().toString());
            editAuthenticationNo.setEnabled(true);
            Toast.makeText(getApplicationContext(),"60초 이내에 인증번호를 입력하세요.",Toast.LENGTH_SHORT);
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

    public void onClickCheck(View v){
        //db 중복 확인
        IdCheckRequest registerRequest = new IdCheckRequest(editID.getText().toString(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
        queue.add(registerRequest);
    }

    public void onClickSignUp(View v){
        if(bAuth&&bId&&isValidInput()){
            SignUpRequest registerRequest = new SignUpRequest(editID.getText().toString(), editPW.getText().toString(),
                     birth, editName.getText().toString(), editPhone.getText().toString(), responseListener);
            RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
            queue.add(registerRequest);
            Toast.makeText(getApplicationContext() ,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show();
            finish();
        }
        else if(!bAuth){
            Toast.makeText(getApplicationContext(),"전화번호 인증에 실패했습니다. 다시 인증해 주세요.",Toast.LENGTH_SHORT).show();
        }
        else if (!bId){
            Toast.makeText(getApplicationContext(),"중복된 ID입니다. 다른 ID를 사용해주세요.",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"양식을 올바르게 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}