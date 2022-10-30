package com.example.eadproject.fuelController;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadproject.OwnerController.Adapter;
import com.example.eadproject.OwnerController.AddFuelDetailsOwner;
import com.example.eadproject.OwnerController.Fuel;
import com.example.eadproject.OwnerController.ViewAllFuelDetails;
import com.example.eadproject.R;
import com.example.eadproject.UserDashboard.Dashboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/*
 *  This is  view Fuel station details page
 * */

public class ViewFuelStationDetails extends AppCompatActivity {

    private Spinner spinnerFuelStation, spinnerFuelType, spinnerFuelCity;
    private String fuelType, city, station, email, id, stationId, stationNo;
    private Button backButton;
    ArrayList<String> spinnerArray = new ArrayList<>();
    ArrayList<String> spinnerArray2 = new ArrayList<>();
    ArrayAdapter<String> adapter1, adapter3;
    private TextView stationTextViewNo, arrivalTextViewTime, fuelFinishTextView;
    private LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fuel_station_details);

        spinnerFuelStation = findViewById(R.id.spinnerViewUserFuelStation);
        spinnerFuelType = findViewById(R.id.spinnerViewUserFuelType);
        spinnerFuelCity = findViewById(R.id.spinnerViewUserFuelCity);
        backButton = findViewById(R.id.btnbackViewUserFuelDetails);
        stationTextViewNo = findViewById(R.id.textViewFuelStationNo);
        arrivalTextViewTime = findViewById(R.id.textViewFuelStationArrivalTime);
        fuelFinishTextView = findViewById(R.id.textViewFuelStationFinishFuel);
        layout = findViewById(R.id.viewFuellayer);

        layout.setVisibility(View.GONE);

        email = getIntent().getStringExtra("email");

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.fuelType, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFuelType.setAdapter(adapter2);

        //this is the city type spinner adapter
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        //this is the station type spinner adapter
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray2);

        handleSSLHandshake();

        System.out.println("inside on click");
        String url = "https://192.168.202.134:44323/api/station/FuelStation";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("inside res");
                System.out.println(response.toString());

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String obj = object.getString("city");
                        spinnerArray.add(obj);
                        //System.out.println(spinnerArray);
                    }
                    // convert the arraylist into a set
                    Set<String> set = new LinkedHashSet<>();
                    set.addAll(spinnerArray);

                    // delete al elements of arraylist
                    spinnerArray.clear();

                    // add element from set to arraylist
                    spinnerArray.addAll(set);
                    //newList = removeDuplicates(spinnerArray);

                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerFuelCity.setAdapter(adapter1);

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

        spinnerFuelCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city = adapterView.getItemAtPosition(i).toString();
                spinnerArray2.clear();
                System.out.println(city);
                System.out.println("inside on click");
                String url1 = "https://192.168.202.134:44323/api/station/FuelStation/GetStationCity?city=" + city;
                JsonArrayRequest jsonArrayRequestStation = new JsonArrayRequest(Request.Method.GET, url1, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("Station res");
                        //System.out.println(response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                String obj = object.getString("name");
                                spinnerArray2.add(obj);
                                System.out.println(spinnerArray2);
                            }
                            Set<String> set = new LinkedHashSet<>();
                            set.addAll(spinnerArray2);

                            // delete al elements of arraylist
                            spinnerArray2.clear();

                            // add element from set to arraylist
                            spinnerArray2.addAll(set);
                            //newList = removeDuplicates(spinnerArray);
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerFuelStation.setAdapter(adapter3);

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

                RequestQueue requestQueueStation = Volley.newRequestQueue(getApplicationContext());
                requestQueueStation.add(jsonArrayRequestStation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        spinnerFuelStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                station = adapterView.getItemAtPosition(i).toString();
                System.out.println("inside on click");
                String url2 = "https://192.168.202.134:44323/api/station/FuelStation";
                JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(Request.Method.GET, url2, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("inside res");
                        System.out.println(response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                String obj = object.getString("name");
                                if (station.equals(obj)) {
                                    stationId = object.getString("stationId");
                                    stationNo = object.getString("stationNo");
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

                RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
                requestQueue2.add(jsonArrayRequest2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerFuelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fuelType = adapterView.getItemAtPosition(i).toString();
                LoadFuelDetails(stationId, stationNo, fuelType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void LoadFuelDetails(String stationId, String stationNo, String fuel) {
        String url3 = "https://192.168.202.134:44323/api/fuel/FuelDetails/GetFuelDetailsFromStationAndFuel?sId=" + stationId + "&fName=" + fuel;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url3, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("inside res");
                System.out.println(response.toString());

                if (response.length() == 0) {
                    layout.setVisibility(View.GONE);
                    stationTextViewNo.setText("");
                    arrivalTextViewTime.setText("");
                    fuelFinishTextView.setText("");

                    if(fuel.matches("Choose")){

                    }else{
                        Toast.makeText(ViewFuelStationDetails.this, "Not Fuel Type", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject object = response.getJSONObject(i);
                            String obj = object.getString("fuelName");
                            System.out.println(fuel);
                            if (!fuel.equals(obj)) {
                                stationTextViewNo.setText("");
                                arrivalTextViewTime.setText("");
                                fuelFinishTextView.setText("");
                                layout.setVisibility(View.GONE);
                            }
                            if (fuel.equals(obj)) {
                                layout.setVisibility(View.VISIBLE);
                                System.out.println(response.toString());
                                String arrivalTime = object.getString("fuelArrivalTime");
                                Boolean fuelFinish = object.getBoolean("fuelFinish");

                                String fuelStatus1 = "";

                                if (fuelFinish == true) {
                                    fuelStatus1 = "Available";
                                    fuelFinishTextView.setTextColor(Color.GREEN);
                                } else {
                                    fuelStatus1 = "Not Available";
                                    fuelFinishTextView.setTextColor(Color.RED);
                                }
                                String time = OffsetDateTime.parse(arrivalTime).toLocalTime().toString();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm a");
                                LocalTime ltObject4 = LocalTime.parse(time);
                                String timeString = ltObject4.format(formatter);

                                System.out.println(timeString);

                                stationTextViewNo.setText(stationNo);
                                arrivalTextViewTime.setText(timeString);
                                fuelFinishTextView.setText(fuelStatus1);

                                Toast.makeText(ViewFuelStationDetails.this, "Display Fuel Type", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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