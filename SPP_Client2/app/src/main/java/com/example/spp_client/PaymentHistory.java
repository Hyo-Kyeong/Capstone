package com.example.spp_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
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

    ListView listView;
    ListViewAdapter adapter;

    Response.Listener<String> responseListener;

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

    public void initialize(){
        member = Member.getInstance();

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
                    System.out.println(jsonArray.getString(2));
                    for(int i=0;i<length;i++){
                        String date = jsonArray.getJSONArray(2).getString(4*i);
                        date = date.substring(0,10);
                        String price = jsonArray.getJSONArray(2).getString(4*i+1);
                        String store = jsonArray.getJSONArray(2).getString(4*i+3);
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

    public void onClickSearch(View v){
        //member.getID()
        PaymentHistoryRequest registerRequest = new PaymentHistoryRequest("blue", start.getText().toString(), end.getText().toString(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(PaymentHistory.this);
        queue.add(registerRequest);
    }
}