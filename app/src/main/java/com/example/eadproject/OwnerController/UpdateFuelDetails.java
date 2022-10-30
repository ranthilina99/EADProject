package com.example.eadproject.OwnerController;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/*
 *  This is update fuel details page
 * */

public class UpdateFuelDetails extends AppCompatActivity {

    private EditText editName, editStationNo, editArrivalTime, FuelType;
    private Spinner spinnerFinish;
    private Button button, back;
    private RequestQueue requestQueue1;
    private String fuelType, finishStatus;
    private Boolean status;
    private String email, StationId, fuelId, fuelStatus, fuelName, fuelStatus1;
    private TextView textView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_fuel_details);

        //assign the ids
        FuelType = findViewById(R.id.spinnerUpdateFuelTypeOwner);
        spinnerFinish = findViewById(R.id.spinnerUpdateFuelFinishStatusOwner);
        button = findViewById(R.id.btnUpdateFuel);
        back = findViewById(R.id.btnBackUpdateFuel);
        editName = findViewById(R.id.textUpdateFuelStationOwner);
        editStationNo = findViewById(R.id.textUpdateFuelStationNoOwner);
        editArrivalTime = findViewById(R.id.textUpdateFuelStationArrivalTimeOwner);
        textView = findViewById(R.id.fuelStatusUpdate);

        StationId = getIntent().getStringExtra("StationId");
        fuelId = getIntent().getStringExtra("id");
        fuelStatus = getIntent().getStringExtra("fuelfinish");
        fuelName = getIntent().getStringExtra("fuelName");
        email = getIntent().getStringExtra("email");

        //this is request queue backend volley library
        requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        FuelType.setEnabled(false);
        //set finish details in spinner
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.finishType, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFinish.setAdapter(adapter1);

        //get finish details in spinner
        spinnerFinish.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                finishStatus = adapterView.getItemAtPosition(i).toString();
                System.out.println(finishStatus);
                if (finishStatus.equals("Yes")) {
                    status = true;
                } else {
                    status = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //this is handle SSL handshake
        handleSSLHandshake();

//        //backButton
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewAllFuelDetails.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        editName.setEnabled(false);
        editStationNo.setEnabled(false);

        editArrivalTime.setEnabled(false);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);
        editArrivalTime.setText(time);

        if (fuelStatus.equals("true")) {
            fuelStatus1 = "Available";
            textView.setTextColor(Color.GREEN);
        } else {
            fuelStatus1 = "Not Available";
            textView.setTextColor(Color.RED);
        }
        textView.setText(fuelStatus1);

        FuelType.setText(fuelName);

//        //get the all FuelStation Details
        System.out.println("inside on click");
        String url = "https://192.168.202.134:44323/api/station/FuelStation";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("inside res");
                System.out.println(response.toString());
//                res = response.toString();

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String obj = object.getString("ownerId");
                        if (email.equals(obj)) {
                            String name = object.getString("ownerId");
                            String stationNo = object.getString("stationNo");
                            editName.setText(name);
                            editStationNo.setText(stationNo);
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
        //update fuelDetails
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finishStatus.matches("Choose")){
                    Toast.makeText(UpdateFuelDetails.this, "Please Select the items", Toast.LENGTH_SHORT).show();
                }else {
                    updateFuelDetails();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateFuelDetails() {
        System.out.println("inside on click");
        String url = "https://192.168.202.134:44323/api/fuel/FuelDetails/" + fuelId;
        String obj = "{'StationId': '" + StationId + "', 'FuelName': '" + fuelName + "','FuelArrivalTime': '" + java.time.LocalDateTime.now() + "','FuelFinish': " + status + " }";

        System.out.println(obj);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                Toast.makeText(UpdateFuelDetails.this, "Update Successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println(volleyError.toString());
            }
        });

        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        requestQueue1.add(jsonObjectRequest1);
    }


    //This is the handle  SSL Handshake Function
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
}