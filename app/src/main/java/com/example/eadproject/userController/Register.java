package com.example.eadproject.userController;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.eadproject.DBHelper.DBHelper;
import com.example.eadproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/*
 *  This is  Register page
 * */

public class Register extends AppCompatActivity {

    //these are variable;
    private TextView textView, sigin;
    private EditText editTextName, editTextEmail, editTextMobile, editTextVehicleNo1, editTextVehicleNo2,
            editTextAddress, editTextStationName, editTextStationNo, editTextPassword, editTextRetypepassowrd;
    private Spinner spinnerRole, spinnerVehicelType, spinnerFuelType, editTextCity;
    private Button button;
    private String roleType, fuelType, vehicleType, cityType;
    private String name, email, mobile, vehicleNo1, vehicleNo2, city, address, stationName, stationNo, vehicleTypeAdd, fuelTypeAdd, password, rePassword, role;
    private LinearLayout layout1, layout2;
    SQLiteDatabase sqLiteDatabaseObj;
    DBHelper DB;
    String SQLiteDataBaseQueryHolder;
    Boolean EditTextEmptyHolder;
    String F_Result = "Not_Found";
    Cursor cursor;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //these are variable assigned ids
        editTextName = findViewById(R.id.registerfullName);
        editTextEmail = findViewById(R.id.registeremail);
        editTextMobile = findViewById(R.id.registermobile);
        editTextVehicleNo1 = findViewById(R.id.registervehicleNo1);
        editTextVehicleNo2 = findViewById(R.id.registervehicleNo2);
        editTextCity = findViewById(R.id.registerCity);
        editTextAddress = findViewById(R.id.registerAddress);
        editTextStationName = findViewById(R.id.registerStationName);
        editTextStationNo = findViewById(R.id.registerStationNo);
        editTextPassword = findViewById(R.id.registerpassword);
        editTextRetypepassowrd = findViewById(R.id.registerretypepassowrd);
        spinnerRole = findViewById(R.id.spinnerRole);
        spinnerVehicelType = findViewById(R.id.spinnerVehicleType);
        spinnerFuelType = findViewById(R.id.spinnerFuelType);
        layout1 = findViewById(R.id.customerLayer);
        layout2 = findViewById(R.id.ownerLayer);
        button = findViewById(R.id.btnregcreateaccount);
        sigin = findViewById(R.id.txtsigiin);

        //there are new Sql lite db helper created
        DB = new DBHelper(this);

        //this is request queue backend volley library
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //this is handle SSL handshake
        handleSSLHandshake();

        //this is assign the role user and owner in spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.role, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        //click the sign in button
        sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
        //this is assign the vehicle type in spinner
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.vehicleType, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicelType.setAdapter(adapter1);

        //vehicle type spinner select the items
        spinnerVehicelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vehicleType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //this is assign the fuel type in spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.fuelType, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFuelType.setAdapter(adapter2);

        //Fuel type spinner select the items
        spinnerFuelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fuelType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // edit text city spinner 
        editTextCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //this is assign the city in spinner
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.cityType, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editTextCity.setAdapter(adapter4);

        //according to select role empty fields
        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                roleType = adapterView.getItemAtPosition(i).toString();

                if (roleType.equals("User")) {
                    layout2.setVisibility(View.GONE);
                    layout1.setVisibility(View.VISIBLE);
                    editTextVehicleNo1.setText(null);
                    editTextVehicleNo2.setText(null);
                    editTextAddress.setText(null);
                    editTextStationName.setText(null);
                    editTextStationNo.setText(null);
                    adapter1.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                    adapter4.notifyDataSetChanged();

                } else if (roleType.equals("Owner")) {
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    editTextVehicleNo1.setText(null);
                    editTextVehicleNo2.setText(null);
                    editTextAddress.setText(null);
                    editTextStationName.setText(null);
                    editTextStationNo.setText(null);
                    adapter1.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                    adapter4.notifyDataSetChanged();

                } else if (roleType.equals("Choose")) {
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.GONE);
                    editTextVehicleNo1.setText(null);
                    editTextVehicleNo2.setText(null);
                    editTextAddress.setText(null);
                    editTextStationName.setText(null);
                    editTextPassword.setText(null);
                    editTextStationNo.setText(null);
                    adapter1.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                    adapter4.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //click the register button call the sqllite function and swagger function
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDataBaseBuild();
                // Creating SQLite table if dose n't exists.
                SQLiteTableBuild();
                // Checking EditText is empty or Not.
                CheckEditTextStatus();
                // Method to check Email is already exists or not.
                CheckingEmailAlreadyExistsOrNot();
                // Empty EditText After done inserting process.

            }
        });
    }

    // Method to check  table SQLiteDataBaseBuild
    public void SQLiteDataBaseBuild() {
        sqLiteDatabaseObj = openOrCreateDatabase(DBHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    // Method to check create a table sqlLite db
    public void SQLiteTableBuild() {
        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS "
                + DBHelper.TABLE_NAME
                + "(" + DBHelper.Table_Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBHelper.Table_Column_1_Name + " VARCHAR, "
                + DBHelper.Table_Column_2_Email + " VARCHAR, "
                + DBHelper.Table_Column_3_Mobile + " VARCHAR, "
                + DBHelper.Table_Column_4_VehicleNo1 + " VARCHAR, "
                + DBHelper.Table_Column_5_VehicleNo2 + " VARCHAR, "
                + DBHelper.Table_Column_6_CIty + " VARCHAR, "
                + DBHelper.Table_Column_7_Address + " VARCHAR, "
                + DBHelper.Table_Column_8_StationName + " VARCHAR, "
                + DBHelper.Table_Column_9_StationNo + " VARCHAR, "
                + DBHelper.Table_Column_10_VehicleType + " VARCHAR, "
                + DBHelper.Table_Column_11_FuelType + " VARCHAR, "
                + DBHelper.Table_Column_12_RoleType + " VARCHAR, "
                + DBHelper.Table_Column_13_Password + " VARCHAR);");
    }

    public void InsertDataIntoSQLiteDatabase() {
        // If editText is not empty then this block will executed.
        if (EditTextEmptyHolder == true) {
            // SQLite query to insert data into table.
            SQLiteDataBaseQueryHolder = "INSERT INTO " + DBHelper.TABLE_NAME + " (name,email,mobileNo,vehicleNo1,vehicleNo2,city,address,stationName,stationNo,vehicleType,fuelType,role,password) VALUES('" + name + "', '" + email + "', '" + mobile + "', '" + vehicleNo1 + "', '" + vehicleNo2 + "', '" + city + "', '" + address + "', '" + stationName + "', '" + stationNo + "', '" + vehicleType + "', '" + fuelType + "', '" + role + "', '" + password + "');";
            // Executing query.
            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);
            // Closing SQLite database object.
            sqLiteDatabaseObj.close();
            // Printing toast message after done inserting.
            Toast.makeText(Register.this, "User Registered Successfully", Toast.LENGTH_LONG).show();
            EmptyEditTextAfterDataInsert();
        }
        // This block will execute if any of the registration EditText is empty.
        else {
            // Printing toast message if any of EditText is empty.
            Toast.makeText(Register.this, "Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();

        }
    }

    // Method to check EditText is empty.
    public void EmptyEditTextAfterDataInsert() {
        editTextName.getText().clear();
        editTextEmail.getText().clear();
        editTextMobile.getText().clear();
        editTextVehicleNo1.getText().clear();
        editTextVehicleNo2.getText().clear();
        editTextAddress.getText().clear();
        editTextStationName.getText().clear();
        editTextStationNo.getText().clear();
        editTextPassword.getText().clear();
        editTextRetypepassowrd.getText().clear();
    }

    // Method to check EditText is empty or Not.
    public void CheckEditTextStatus() {
        // Getting value from All EditText and storing into String Variables.
        name = editTextName.getText().toString();
        email = editTextEmail.getText().toString();
        mobile = editTextMobile.getText().toString();
        vehicleNo1 = editTextVehicleNo1.getText().toString();
        vehicleNo2 = editTextVehicleNo2.getText().toString();
        city = cityType;
        address = editTextAddress.getText().toString();
        stationName = editTextStationName.getText().toString();
        stationNo = editTextStationNo.getText().toString();
        vehicleTypeAdd = vehicleType;
        fuelTypeAdd = fuelType;
        password = editTextPassword.getText().toString();
        rePassword = editTextRetypepassowrd.getText().toString();
        role = roleType;

        if (roleType.equals("Choose")) {
            EditTextEmptyHolder = false;
            Toast.makeText(this, "Please select the role", Toast.LENGTH_SHORT).show();
        } else if (roleType.equals("User")) {
            String passwordVal = "^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$";
            if (name.isEmpty()) {
                EditTextEmptyHolder = false;
                Toast.makeText(this, "Please fill the name", Toast.LENGTH_SHORT).show();
            }else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                EditTextEmptyHolder = false;
                Toast.makeText(this, "Please enter correct email", Toast.LENGTH_SHORT).show();
            } else if (!mobile.matches("[0-9]{10}")) {
                EditTextEmptyHolder = false;
                Toast.makeText(this, "Please enter correct mobile", Toast.LENGTH_SHORT).show();
            }  else if (vehicleType.equals("Choose")) {
                EditTextEmptyHolder = false;
                Toast.makeText(this, "Please select vehicle Type", Toast.LENGTH_SHORT).show();
            } else if (fuelType.equals("Choose")) {
                EditTextEmptyHolder = false;
                Toast.makeText(this, "Please select fuel type", Toast.LENGTH_SHORT).show();
            } else if (!password.matches(passwordVal)) {
                EditTextEmptyHolder = false;
                Toast.makeText(this, "Please enter correct password", Toast.LENGTH_SHORT).show();
            } else if (!rePassword.matches(password)) {
                EditTextEmptyHolder = false;
                Toast.makeText(this, "Please enter correct password", Toast.LENGTH_SHORT).show();
            } else {
                EditTextEmptyHolder = true;
            }
        } else if (roleType.equals("Owner")) {
            String passwordVal1 = "^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$";
            if (name.isEmpty()) {
                Toast.makeText(this, "Please fill the name", Toast.LENGTH_SHORT).show();
                EditTextEmptyHolder = false;
            } else if (cityType.equals("Choose")) {
                EditTextEmptyHolder = false;
                Toast.makeText(this, "Please select city", Toast.LENGTH_SHORT).show();
            } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                EditTextEmptyHolder = false;
                Toast.makeText(this, "Please enter correct email", Toast.LENGTH_SHORT).show();
            } else if (!mobile.matches("[0-9]{10}")) {
                EditTextEmptyHolder = false;
                Toast.makeText(this, "Please enter correct mobile", Toast.LENGTH_SHORT).show();
            } else if (!password.matches(passwordVal1)) {
                EditTextEmptyHolder = false;
                Toast.makeText(this, "Please enter correct password", Toast.LENGTH_SHORT).show();
            } else if (!rePassword.matches(password)) {
                EditTextEmptyHolder = false;
                Toast.makeText(this, "Please enter correct password", Toast.LENGTH_SHORT).show();
            } else {
                EditTextEmptyHolder = true;
            }
        }
    }

    // Checking Email is already exists or not.
    public void CheckingEmailAlreadyExistsOrNot() {
        // Opening SQLite database write permission.
        sqLiteDatabaseObj = DB.getWritableDatabase();
        // Adding search email query to cursor.
        cursor = sqLiteDatabaseObj.query(DBHelper.TABLE_NAME, null, " " + DBHelper.Table_Column_2_Email + "=?", new String[]{email}, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                // If Email is already exists then Result variable value set as Email Found.
                F_Result = "Email Found";
                // Closing cursor.
                cursor.close();
            }
        }
        // Calling method to check final result and insert data into SQLite database.
        CheckFinalResult();
    }

    // Checking result
    public void CheckFinalResult() {
        // Checking whether email is already exists or not.
        if (F_Result.equalsIgnoreCase("Email Found")) {
            // If email is exists then toast msg will display.
            Toast.makeText(Register.this, "Email Already Exists", Toast.LENGTH_LONG).show();
        } else {
            // If email already dose n't exists then user registration details will entered to SQLite database.
            InsertDataIntoSQLiteDatabase();
            RegisterMongodb();
        }
        F_Result = "Not_Found";
    }

    //create a mongodb post api call
    private void RegisterMongodb() {
        System.out.println("inside on click");
        String url = "https://192.168.202.134:44323/api/station/FuelStation";
        String obj = "{'name': '" + stationName + "', 'address': '" + address + "','ownerId': '" + email + "','stationNo': '" + stationNo + "' ,'city': '" + cityType + "'}";

        System.out.println(obj);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println(volleyError.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
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