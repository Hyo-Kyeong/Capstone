package com.example.spp_client;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PaymentHistory extends AppCompatActivity {
    private Member member;

    private TextView start, end;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private DatePickerDialog.OnDateSetListener callbackMethod2;

    private String yearStart, monthStart, dayStart;
    private String yearEnd, monthEnd, dayEnd;

    LocalDate nowDate;
    LocalDate startDate;
    LocalDate endDate;

    ListView listView;
    ListViewAdapter adapter;

    Response.Listener<String> responseListener;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        initialize();
        initializeListener();

        adapter = new ListViewAdapter();

        listView = (ListView) findViewById(R.id.listview1);
        listView.setAdapter(adapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initialize(){
        member = Member.getInstance();

        TextView userID = (TextView)findViewById(R.id.textUserID4);
        userID.setText(member.getID()+"님");

        nowDate = LocalDate.now();
        startDate = LocalDate.now();
        endDate = LocalDate.now();

        yearStart = Integer.toString(nowDate.getYear());
        monthStart = Integer.toString(nowDate.getMonthValue());
        dayStart = Integer.toString(nowDate.getDayOfMonth());

        yearEnd = Integer.toString(nowDate.getYear());
        monthEnd = Integer.toString(nowDate.getMonthValue());
        dayEnd = Integer.toString(nowDate.getDayOfMonth());

        start = (TextView)findViewById(R.id.startDate);
        end = (TextView)findViewById(R.id.endDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        start.setText(nowDate.format(formatter));
        end.setText(nowDate.format(formatter));
    }

    public void initializeListener(){
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int _year, int monthOfYear, int dayOfMonth)
            {
                monthOfYear+=1;
                startDate = LocalDate.of(_year, monthOfYear, dayOfMonth);
                String _month;
                String _day;
                if(monthOfYear<10) _month = "0"+Integer.toString(monthOfYear);
                else _month = Integer.toString(monthOfYear);
                if(dayOfMonth<10) _day ="0"+Integer.toString(dayOfMonth);
                else _day = Integer.toString(dayOfMonth);
                yearStart = Integer.toString(_year);
                monthStart = _month;
                dayStart = _day;
                start.setText(_year + "-" + _month + "-" + _day);
            }
        };
        callbackMethod2 = new DatePickerDialog.OnDateSetListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int _year, int monthOfYear, int dayOfMonth)
            {
                monthOfYear+=1;
                endDate = LocalDate.of(_year, monthOfYear, dayOfMonth);
                String _month;
                String _day;
                if(monthOfYear<10) _month = "0"+Integer.toString(monthOfYear);
                else _month = Integer.toString(monthOfYear);
                if(dayOfMonth<10) _day ="0"+Integer.toString(dayOfMonth);
                else _day = Integer.toString(dayOfMonth);
                yearEnd = Integer.toString(_year);
                monthEnd = _month;
                dayEnd = _day;
                end.setText(_year + "-" + _month + "-" + _day);
            }
        };

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    //JSONObject jsonResponse = new JSONObject(response);
                    //String log = jsonResponse.getString("result");
                    //System.out.println(log);

                    int length= jsonArray.length();
                    System.out.println("length : "+length);
                    System.out.println(jsonArray.getString(length-1));
                    for(int i=0;i<length;i++){
                        String date = jsonArray.getJSONArray(length-1).getString(4*i);
                        date = date.substring(0,10);
                        String price = jsonArray.getJSONArray(length-1).getString(4*i+1);
                        String store = jsonArray.getJSONArray(length-1).getString(4*i+3);
                        adapter.addItem(date,store,price);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void onStartClick(View v){
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, Integer.parseInt(yearStart), Integer.parseInt(monthStart)-1, Integer.parseInt(dayStart));
        dialog.show();
    }
    public void onEndClick(View v){
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod2, Integer.parseInt(yearEnd), Integer.parseInt(monthEnd)-1, Integer.parseInt(dayEnd));
        dialog.show();
    }

    public void onClickLogout(View v){
        member = null;
        Intent myIntent = new Intent(getApplicationContext(), Login.class);
        myIntent.addFlags(myIntent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickSearch(View v){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        start.setText(startDate.atStartOfDay().format(formatter));
        end.setText(endDate.atStartOfDay().with(LocalTime.MAX).format(formatter));

        PaymentHistoryRequest registerRequest = new PaymentHistoryRequest(member.getID(), start.getText().toString(), end.getText().toString(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(PaymentHistory.this);
        queue.add(registerRequest);

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        start.setText(startDate.atStartOfDay().format(formatter));
        end.setText(endDate.atStartOfDay().with(LocalTime.MAX).format(formatter));

        adapter.deleteAllItems();
        adapter.notifyDataSetChanged();
    }
}