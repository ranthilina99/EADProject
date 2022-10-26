package com.example.eadproject.OwnerController;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadproject.DBHelper.DBHelper;
import com.example.eadproject.QueueController.AddUserQueueDetails;
import com.example.eadproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class AddFuelDetailsOwner extends AppCompatActivity {

    private Spinner spinnerFuelType, spinnerFinish;
    private String fuelType, finishStatus, name, stationNo, time, stationId;
    private Button btn1, btn2, btn3;
    private Cursor cursor;
    private String email, id;
    private Boolean status;
    DBHelper DB;
    private RequestQueue requestQueue1;
    private SQLiteDatabase sqLiteDatabaseObj;
    private EditText editTextStationName, getEditTextStationNo, arrivalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_queue_details_owner);

        spinnerFuelType = findViewById(R.id.spinnerFuelTypeOwner);
        spinnerFinish = findViewById(R.id.spinnerFuelFinishStatusOwner);
        btn1 = findViewById(R.id.btnAddFuel);
        btn2 = findViewById(R.id.btnBackAddFuel);
        btn3 = findViewById(R.id.btnUpdateFuelOwner);
        editTextStationName = findViewById(R.id.textAddFuelStationOwner);
        getEditTextStationNo = findViewById(R.id.textAddFuelStationNoOwner);
        arrivalTime = findViewById(R.id.textAddFuelStationArrivalTimeOwner);

        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");

//        name = editTextStationName.getText().toString();
//        stationNo = getEditTextStationNo.getText().toString();
//        time = arrivalTime.getText().toString();

        //new db controller
        DB = new DBHelper(this);

        //set fuel details in spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.fuelType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFuelType.setAdapter(adapter);

        //this is request queue backend volley library
        requestQueue1 = Volley.newRequestQueue(getApplicationContext());

        //this is handle SSL handshake
        handleSSLHandshake();

        //get fuel details in spinner
        spinnerFuelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fuelType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                if(finishStatus.equals("Yes")){
                    status = true;
                }else{
                    status = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //get the all FuelStation Details
        System.out.println("inside on click");
        String url = "https://192.168.202.134:44323/api/station/FuelStation";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("inside res");
                //System.out.println(response.toString());
//                res = response.toString();

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String obj = object.getString("ownerId");
                        if (email.equals(obj)) {
                            String name = object.getString("OwnerId");
                            String stationNo = object.getString("stationNo");
                            editTextStationName.setText(name);
                            getEditTextStationNo.setText(stationNo);
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

        //add the fuel details db
        btn1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                System.out.println("inside on click");
                String url3 = "https://192.168.202.134:44323/api/fuel/FuelDetails";
                String obj = "{'StationId': '" + editTextStationName.getText().toString() + "', 'FuelName': '" + fuelType + "','FuelArrivalTime': '" + java.time.LocalDateTime.now() + "','FuelFinish': " + status + " }";

                System.out.println(obj);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String stationIdnew = editTextStationName.getText().toString();
                checkData(stationIdnew);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url3, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            stationId = response.getString("stationId").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println(volleyError.toString());
                    }
                });
                requestQueue1.add(jsonObjectRequest);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OwnerDashboard.class);
                startActivity(intent);
            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateFuelDetails.class);
                startActivity(intent);
            }
        });

        editTextStationName.setEnabled(false);
        getEditTextStationNo.setEnabled(false);

        //loadData();
    }

    private void checkData(String stationIdNew) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String status;
        String url = "https://192.168.202.134:44323/api/fuel/FuelDetails/GetOneStation/" + stationIdNew;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                //check the details
                String res = response.toString();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
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

//    @SuppressLint("Range")
//    private void loadData() {
//        sqLiteDatabaseObj = DB.getWritableDatabase();
//        // Adding search email query to cursor.
//        cursor = sqLiteDatabaseObj.query(DBHelper.TABLE_NAME, null, " " + DBHelper.Table_Column_2_Email + "=?", new String[]{email}, null, null, null);
//        while (cursor.moveToNext()) {
//            if (cursor.isFirst()) {
//                cursor.moveToFirst();
//
//                String stName = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_8_StationName));
//                String stNo = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_9_StationNo));
//
//                editTextStationName.setText(stName);
//                getEditTextStationNo.setText(stNo);
//
//                // Closing cursor.
//                cursor.close();
//            }
//        }
//    }
}