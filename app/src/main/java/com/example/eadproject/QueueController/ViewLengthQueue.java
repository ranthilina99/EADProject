package com.example.eadproject.QueueController;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.eadproject.R;
import com.example.eadproject.UserDashboard.Dashboard;
import com.example.eadproject.fuelController.ViewFuelDetails;

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
/*
 *  This is  View Queue Length page
 * */

public class ViewLengthQueue extends AppCompatActivity {

    //create variables
    private Button enterButton, viewDetailButton;
    private String station, fuel, city, email, id, vehicleType, lengthQueue, QueueId, stationName;
    private EditText length, average;
    private SQLiteDatabase sqLiteDatabaseObj;
    private Cursor cursor;
    private RequestQueue requestQueue;
    DBHelper DB;
    String res = "";
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_length_queue);

        //assign the  variables ids
        enterButton = findViewById(R.id.btnViewEnterQueue1);
        viewDetailButton = findViewById(R.id.btnViewDetailsQueue2);
        average = findViewById(R.id.avgTimeQueue);
        length = findViewById(R.id.lengthQueue);
        back = findViewById(R.id.imageViewViewQueueBack);

        //get the  station,fuel,city values
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");

        average.setEnabled(false);
        length.setEnabled(false);

        //get the  station,fuel,city values
        station = getIntent().getStringExtra("station");
        stationName = getIntent().getStringExtra("stationName");
        fuel = getIntent().getStringExtra("fuelType");
        city = getIntent().getStringExtra("city");

        System.out.println("---------------------------------------------------");
        System.out.println(station);
        System.out.println(stationName);
        System.out.println(id);
        System.out.println("---------------------------------------------------");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                intent.putExtra("email", email);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        //this is new DBHelper in this page
        DB = new DBHelper(this);

        //this is request queue backend volley library
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        lengthQueue = length.getText().toString();
        //this is handle SSL handshake
        handleSSLHandshake();

        //this is the queue average time backend url
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://192.168.202.134:44323/api/queue/Queue/GetOneQueueAverage?id=" + station;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                average.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        //load the sal data
        loadData();

        //add queue string request
        queue.add(stringRequest);

        //this is the queue length time backend url
        String url1 = "https://192.168.202.134:44323/api/queue/Queue/GetOneQueueLength?id=" + station;
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                length.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        //add queue string request
        queue.add(stringRequest1);

        //click eneter button go to your length page
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue1 = Volley.newRequestQueue(ViewLengthQueue.this);
                String url2 = "https://192.168.202.134:44323/api/queue/Queue/CheckDepartureTime?id=" + id;
                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        JSONObject jsonResponse = null;
                        String myObjAsString = null;
                        try {
                            jsonResponse = new JSONObject(response.toString());
                            myObjAsString = jsonResponse.getString("queueId");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        System.out.println(myObjAsString);
                        if (myObjAsString != "null") {
                            Toast.makeText(ViewLengthQueue.this, "Already have Queue please exit the queue", Toast.LENGTH_SHORT).show();
                        } else {
                            EnterTheYourLoaction();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                //add queue string request
                queue1.add(stringRequest2);
            }
        });

        //click the view details and pass the  station,fuel,city values
        viewDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewFuelDetails.class);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void EnterTheYourLoaction() {
        System.out.println("inside on click");
        String url3 = "https://192.168.202.134:44323/api/queue/Queue";
        String obj = "{'StationId': '" + station + "', 'UserId': '" + id + "','VeihicleType': '" + vehicleType + "','ArrivalTime': '" + java.time.LocalDateTime.now() + "' }";

        System.out.println(obj);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url3, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    QueueId = response.getString("queueId").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(ViewLengthQueue.this, "Yor Time Added successfully", Toast.LENGTH_SHORT).show();
                enetertheYourQueueLocation(QueueId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println(volleyError.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void enetertheYourQueueLocation(String queueId) {
        Intent intent = new Intent(getApplicationContext(), AddUserQueueDetails.class);
        intent.putExtra("station", station);
        intent.putExtra("queueId", queueId);
        intent.putExtra("id", id);
        intent.putExtra("email", email);
        intent.putExtra("queueId", QueueId);
        startActivity(intent);
    }

    private void handleSSLHandshake() {
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

    //Load the user Sql Data
    @SuppressLint("Range")
    private void loadData() {
        sqLiteDatabaseObj = DB.getWritableDatabase();
        // Adding search email query to cursor.
        cursor = sqLiteDatabaseObj.query(DBHelper.TABLE_NAME, null, " " + DBHelper.Table_Column_2_Email + "=?", new String[]{email}, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                // Storing Password associated with entered email.
                vehicleType = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_10_VehicleType));
                // Closing cursor.
                cursor.close();
            }
        }
    }
}