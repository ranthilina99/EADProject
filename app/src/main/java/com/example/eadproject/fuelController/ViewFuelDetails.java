package com.example.eadproject.fuelController;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadproject.DBHelper.DBHelper;
import com.example.eadproject.OwnerController.AddFuelDetailsOwner;
import com.example.eadproject.QueueController.ViewLengthQueue;
import com.example.eadproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/*
 *  This is  view Details page
 * */
public class ViewFuelDetails extends AppCompatActivity {

    private String station, fuel, city,stationName,email,id;
    private TextView textViewStation, textViewFuel, textViewCity, textViewArrivalTime, textViewFinishTime;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fuel_details);

        station = getIntent().getStringExtra("station");
        fuel = getIntent().getStringExtra("fuelType");
        city = getIntent().getStringExtra("city");
        stationName = getIntent().getStringExtra("stationName");
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");

        textViewStation = findViewById(R.id.textViewFuelStation);
        textViewCity = findViewById(R.id.textViewFuelCity);
        textViewFuel = findViewById(R.id.textViewFuelDetailsFuelType);
        textViewArrivalTime = findViewById(R.id.textViewFuelStationArrivalTime);
        textViewFinishTime = findViewById(R.id.textViewFuelStationFinishFuel);
        back = findViewById(R.id.btnViewDetailsBack);

        textViewStation.setText(stationName);
        textViewCity.setText(city);
        textViewFuel.setText(fuel);

        String url3 = "https://192.168.202.134:44323/api/fuel/FuelDetails/GetFuelDetailsFromStationAndFuel?sId=" + station + "&fName=" + fuel;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url3, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("inside res");
                System.out.println(response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String obj = object.getString("fuelName");
                        if (fuel.equals(obj)) {
                            System.out.println(response.toString());
                            String arrivalTime = object.getString("fuelArrivalTime");
                            Boolean fuelFinish = object.getBoolean("fuelFinish");

                            String fuelStatus1 = "";

                            if (fuelFinish == true) {
                                fuelStatus1 = "Available";
                                textViewFinishTime.setTextColor(Color.GREEN);
                            } else {
                                fuelStatus1 = "Not Available";
                                textViewFinishTime.setTextColor(Color.RED);
                            }
                            String time = OffsetDateTime.parse(arrivalTime).toLocalTime().toString();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm a");
                            LocalTime ltObject4 = LocalTime.parse(time);
                            String timeString = ltObject4.format(formatter);

                            System.out.println(timeString);

                            textViewArrivalTime.setText(timeString);
                            textViewFinishTime.setText(fuelStatus1);

                            Toast.makeText(ViewFuelDetails.this, "View Fuel Details", Toast.LENGTH_SHORT).show();

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println(volleyError.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewLengthQueue.class);
                intent.putExtra("station", station);
                intent.putExtra("stationName", stationName);
                intent.putExtra("fuelType", fuel);
                intent.putExtra("city", city);
                intent.putExtra("email", email);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }
}