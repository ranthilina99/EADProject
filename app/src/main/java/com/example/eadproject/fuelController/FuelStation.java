package com.example.eadproject.fuelController;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadproject.QueueController.ViewLengthQueue;
import com.example.eadproject.R;
import com.example.eadproject.UserDashboard.Dashboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FuelStation extends AppCompatActivity {

    private Spinner spinnerFuelStation,spinnerFuelType,spinnerFuelCity;
    private String fuelType,city,station;
    private Button nextButton;
    private EditText time;
    String res = "";
    ArrayList<String> spinnerArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station);

        spinnerFuelStation = findViewById(R.id.spinnerViewStationUserFuelStation);
        spinnerFuelType = findViewById(R.id.spinnerViewStationUserFuelType);
        spinnerFuelCity = findViewById(R.id.spinnerViewStationUserCity);
        nextButton= findViewById(R.id.btnFuelStationNext);
        time= findViewById(R.id.textAddFuelStationArrivalTimeOwner);
//
//        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.fuelType, android.R.layout.simple_spinner_item);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerFuelType.setAdapter(adapter2);
//
//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
//                (this, android.R.layout.simple_spinner_item,
//                        spinnerArray); //selected item will look like a spinner set from XML
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
//                .simple_spinner_dropdown_item);
//        spinnerFuelCity.setAdapter(spinnerArrayAdapter);


        handleSSLHandshake();


        System.out.println("inside on click");
        String url = "https://192.168.202.134:44323/api/station/FuelStation";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("inside res");
                System.out.println(response.toString());
                res = response.toString();


                try {
                    for (int i = 0 ; i<response.length() ; i++){
                        JSONObject object =  response.getJSONObject(i);

                        System.out.println(object.get("city"));
//                        spinnerArray.add((String) object.get("city"));
//                        System.out.println(spinnerArray);
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

        spinnerFuelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fuelType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ViewLengthQueue.class);
//                startActivity(intent);



            }
        });

    }

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