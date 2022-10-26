package com.example.eadproject.fuelController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eadproject.DBHelper.DBHelper;
import com.example.eadproject.QueueController.ViewLengthQueue;
import com.example.eadproject.R;
/*
 *  This is  view Details page
 * */
public class ViewFuelDetails extends AppCompatActivity {

    private String station, fuel, city;
    private TextView textViewStation, textViewFuel, textViewCity, textViewArrivalTime, textViewFinishTime;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fuel_details);

        station = getIntent().getStringExtra("station");
        fuel = getIntent().getStringExtra("fuelType");
        city = getIntent().getStringExtra("city");

        textViewStation = findViewById(R.id.textViewFuelStation);
        textViewCity = findViewById(R.id.textViewFuelCity);
        textViewFuel = findViewById(R.id.textViewFuelDetailsFuelType);
        textViewArrivalTime = findViewById(R.id.textViewFuelStationArrivalTime);
        textViewFinishTime = findViewById(R.id.textViewFuelStationFinishFuel);
        back = findViewById(R.id.btnViewDetailsBack);

        textViewStation.setText(station);
        textViewCity.setText(city);
        textViewFuel.setText(fuel);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewLengthQueue.class);
                intent.putExtra("station",station);
                intent.putExtra("fuelType", fuel);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });
    }
}