package com.example.eadproject.QueueController;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadproject.DBHelper.DBHelper;
import com.example.eadproject.R;
import com.example.eadproject.UserDashboard.Dashboard;
import com.example.eadproject.fuelController.ViewFuelDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/*
 *  This is User View Queue All details Page
 * */

public class UserViewQueueAllDetails extends AppCompatActivity {

    private TextView name,vehicleType, vehicleNo, fuelType,fuelStation,fuelStationNo,QueueArrivalTime,QueueLength;
    private SQLiteDatabase sqLiteDatabaseObj;
    private Cursor cursor;
    private String email, id,queueId,stationId,arrivalTime;
    DBHelper DB;
    private SwipeRefreshLayout layout;
    private Button exit,back;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_queue_all_details);

        email = getIntent().getStringExtra("email");
        queueId = getIntent().getStringExtra("queueId");
        stationId = getIntent().getStringExtra("stationId");
        arrivalTime = getIntent().getStringExtra("arrivalTime");
        id = getIntent().getStringExtra("id");

        name = findViewById(R.id.textViewQueueUserName);
        vehicleType = findViewById(R.id.textViewQueueVehicleType);
        vehicleNo = findViewById(R.id.textViewQueueVehicleNo);
        fuelType = findViewById(R.id.textViewQueueFuelType);
        fuelStation = findViewById(R.id.textViewQueueStation);
        fuelStationNo = findViewById(R.id.textViewQueueStationNo);
        QueueArrivalTime = findViewById(R.id.textViewQueueStationArrivalTime);
        QueueLength = findViewById(R.id.textViewAllQueueLength);
        exit = findViewById(R.id.btnViewStop);
        back = findViewById(R.id.btnViewQueueAllBack);
        layout = findViewById(R.id.viewQueueRefresh);

        DB = new DBHelper(this);

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                layout.setRefreshing(false);
            }
        });
        loadData();

        String time = OffsetDateTime.parse(arrivalTime).toLocalTime().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm a");
        LocalTime ltObject4 = LocalTime.parse(time);;
        String arrivalTime = ltObject4.format(formatter);

        QueueArrivalTime.setText(arrivalTime);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://192.168.202.134:44323/api/station/FuelStation/GetOne/" + stationId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    String name = jsonResponse.getString("name");
                    String stationNo = jsonResponse.getString("stationNo");
                    fuelStation.setText(name);
                    fuelStationNo.setText(stationNo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

        RequestQueue queue1 = Volley.newRequestQueue(this);
        String url1 = "https://192.168.202.134:44323/api/queue/Queue/GetOneQueueLength?id=" + stationId;
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                QueueLength.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue1.add(stringRequest1);

        exit.setOnClickListener(new View.OnClickListener() {
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

                JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(Request.Method.PATCH, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        Toast.makeText(UserViewQueueAllDetails.this, "Departure time update successfully", Toast.LENGTH_SHORT).show();
                        logoff(email);
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


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }

    private void logoff(String email) {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    @SuppressLint("Range")
    private void loadData() {
        sqLiteDatabaseObj = DB.getWritableDatabase();
        // Adding search email query to cursor.
        cursor = sqLiteDatabaseObj.query(DBHelper.TABLE_NAME, null, " " + DBHelper.Table_Column_2_Email + "=?", new String[]{email}, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                // Storing Password associated with entered email.
                String name1 = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_1_Name));
                String vehicleType1 = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_10_VehicleType));
                String vehicleNo11 = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_4_VehicleNo1));
                String vehicleNo21 = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_5_VehicleNo2));
                String fuelType1 = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_11_FuelType));

                name.setText(name1);
                vehicleType.setText(vehicleType1);
                vehicleNo.setText(vehicleNo11 + "-" + vehicleNo21);
                fuelType.setText(fuelType1);
                // Closing cursor.
                cursor.close();
            }
        }
    }
}