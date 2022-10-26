package com.example.eadproject.QueueController;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadproject.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AddUserQueueDetails extends AppCompatActivity {

    private EditText text1,text2;
    private String station,id,queueId;
    private Button btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_queue_details);

        //assign the ids
        text1 = findViewById(R.id.addYouTimeQueue);
        text2 = findViewById(R.id.addYourLengthQueue);
        btn1 = findViewById(R.id.btnAddQueueRefresh);
        btn2 = findViewById(R.id.btnAddQueueExit);

        //get the length previous page using the putExtra
        station = getIntent().getStringExtra("station");
        id = getIntent().getStringExtra("id");
        queueId = getIntent().getStringExtra("queueId");

        // get the youtime value
        getYourTimeValue();

        //get the station length
        getStationLength();

        // refresh the data
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYourTimeValue();
                getStationLength();
            }
        });

        //update dipacher time
        btn2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                System.out.println("inside on click");
                String url = "https://192.168.202.134:44323/api/queue/Queue/" + queueId;
                String obj = "{'departureTime': '" + java.time.LocalDateTime.now() + "'}";

                System.out.println(obj);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println(volleyError.toString());
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(jsonObjectRequest);
            }
        });
    }

    private void getStationLength() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://192.168.202.134:44323/api/queue/Queue/GetOneQueueCustomer/" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                //set the your time value
                text1.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    private void getYourTimeValue() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url1 = "https://192.168.202.134:44323/api/queue/Queue/GetOneQueueLength?id=" + station;
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                //set the length value now
                text2.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //add queue string request
        queue.add(stringRequest1);
    }

}