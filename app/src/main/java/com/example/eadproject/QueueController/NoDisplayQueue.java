package com.example.eadproject.QueueController;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eadproject.R;
import com.example.eadproject.UserDashboard.Dashboard;
import com.example.eadproject.fuelController.ViewFuelStationDetails;

/*
 *  This is No queue details page
 * */

public class NoDisplayQueue extends AppCompatActivity {

    private  Button button;
    private  String email,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_display_queue);

        id = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");

        button = findViewById(R.id.btnNotDisplayBack);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoDisplayQueue.this, Dashboard.class);
                intent.putExtra("email", email);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }
}