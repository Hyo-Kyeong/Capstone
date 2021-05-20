package com.example.spp_client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PaymentHistory extends AppCompatActivity {
    private Member member;

    private TextView start, end;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private DatePickerDialog.OnDateSetListener callbackMethod2;

    private Date currentTime;
    private SimpleDateFormat yearFormat;
    private SimpleDateFormat monthFormat;
    private SimpleDateFormat dayFormat;
    private String year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        initialize();
        initializeListener();
    }

    public void initialize(){
        currentTime = Calendar.getInstance().getTime();
        yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        dayFormat = new SimpleDateFormat("dd", Locale.getDefault());

        year = yearFormat.format(currentTime);
        month = monthFormat.format(currentTime);
        day = dayFormat.format(currentTime);

        start = (TextView)findViewById(R.id.startDate);
        end = (TextView)findViewById(R.id.endDate);

        start.setText(year+"-"+month+"-"+day);
        end.setText(year+"-"+month+"-"+day);
    }

    public void initializeListener(){
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                monthOfYear+=1;
                String _month, _;
                if(monthOfYear<10) _month = "0"+Integer.toString(monthOfYear);
                else _month = Integer.toString(monthOfYear);
                start.setText(year + "-" + _month + "-" + dayOfMonth);
            }
        };
        callbackMethod2 = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                monthOfYear+=1;
                String _month;
                if(monthOfYear<10) _month = "0"+Integer.toString(monthOfYear);
                else _month = Integer.toString(monthOfYear);
                end.setText(year + "-" + _month + "-" + dayOfMonth);
            }
        };
    }

    public void onStartClick(View v){
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
        dialog.show();
    }
    public void onEndClick(View v){
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod2, Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
        dialog.show();
    }

    public void onClickLogout(View v){
        member = null;
        finish();
    }
}