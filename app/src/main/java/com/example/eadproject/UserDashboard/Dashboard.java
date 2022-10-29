package com.example.eadproject.UserDashboard;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadproject.DBHelper.DBHelper;
import com.example.eadproject.QueueController.NoDisplayQueue;
import com.example.eadproject.QueueController.UserViewQueueAllDetails;
import com.example.eadproject.R;
import com.example.eadproject.fuelController.FuelStation;
import com.example.eadproject.fuelController.ViewFuelDetails;
import com.example.eadproject.fuelController.ViewFuelStationDetails;
import com.example.eadproject.userController.Login;
import com.example.eadproject.userController.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/*
 *  This is  Dashboard page
 * */

public class Dashboard extends AppCompatActivity {

    private SwipeRefreshLayout layout;
    private CardView card1, card2, card3, card4;
    private SQLiteDatabase sqLiteDatabaseObj;
    private Cursor cursor;
    private String email, id;
    DBHelper DB;
    private TextView textViewMame;
    private ImageView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        email = getIntent().getStringExtra("email");

        card1 = findViewById(R.id.card1);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card2 = findViewById(R.id.card2);
        textViewMame = findViewById(R.id.textViewDashboardUser);
        layout = findViewById(R.id.dashboardRefresh);
        button = findViewById(R.id.userLogout);

        DB = new DBHelper(this);

        handleSSLHandshake();
        // this is the fuel view details card button
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ViewFuelStationDetails.class);
                intent.putExtra("email", email);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        // this is the queue details card button
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAllQueueDetails(id);
            }
        });
        // this is the fuel station card button
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, FuelStation.class);
                // Sending Email to Dashboard Activity using intent.
                intent.putExtra("email", email);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        // this is the profile card button
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, UserProfile.class);
                // Sending Email to Dashboard Activity using intent.
                intent.putExtra("email", email);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        //refresh data
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                layout.setRefreshing(false);
            }
        });
        //click the logout button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.putExtra("email", email);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        loadData();
    }

    private void loadAllQueueDetails(String userId) {
        System.out.println(userId);
        RequestQueue queue1 = Volley.newRequestQueue(Dashboard.this);
        String url2 = "https://192.168.202.134:44323/api/queue/Queue/CheckDepartureTime?id=" + userId;
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response.toString());
                    String myObjAsString = jsonResponse.getString("queueId");
                    String stationId = jsonResponse.getString("stationId");
                    String arrivalTime = jsonResponse.getString("arrivalTime");

                    if (myObjAsString != "null") {
                        Intent intent = new Intent(getApplicationContext(), UserViewQueueAllDetails.class);
                        intent.putExtra("queueId", myObjAsString);
                        intent.putExtra("stationId", stationId);
                        intent.putExtra("arrivalTime", arrivalTime);
                        intent.putExtra("email", email);
                        intent.putExtra("id", Dashboard.this.id);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), NoDisplayQueue.class);
                        intent.putExtra("email", email);
                        intent.putExtra("id", Dashboard.this.id);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    @SuppressLint("Range")
    private void loadData() {
        sqLiteDatabaseObj = DB.getWritableDatabase();
        // Adding search email query to cursor.
        cursor = sqLiteDatabaseObj.query(DBHelper.TABLE_NAME, null, " " + DBHelper.Table_Column_2_Email + "=?", new String[]{email}, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                // Storing Password associated with entered email.
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_1_Name));
                id = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_ID));

                textViewMame.setText("Hello!" + " " + name);
                // Closing cursor.
                cursor.close();
            }
        }
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
}