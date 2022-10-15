package com.example.eadproject.userController;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.eadproject.R;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private TextView textView;
    private EditText editTextName,editTextEmail,editTextMobile,editTextVehicleNo1,editTextVehicleNo2,
            editTextCity,editTextAddress,editTextStationName,editTextStationNo,editTextPassword,editTextRetypepassowrd;
    private Spinner spinnerRole,spinnerVehicelType,spinnerFuelType,spinner4;
    private Button button;
    private String roleType,fuelType,vehicleType;
    private LinearLayout layout1, layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.role, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                roleType = adapterView.getItemAtPosition(i).toString();

                if( roleType.equals("User")){
                    layout2.setVisibility(View.GONE);
                    layout1.setVisibility(View.VISIBLE);
                    editTextName.setText(null);
                    editTextEmail.setText(null);
                    editTextMobile.setText(null);
                    editTextVehicleNo1.setText(null);
                    editTextVehicleNo2.setText(null);
                    editTextCity.setText(null);
                    editTextAddress.setText(null);
                    editTextStationName.setText(null);
                    editTextPassword.setText(null);
                    editTextStationNo.setText(null);
                }else if(roleType.equals("Owner")){
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    editTextName.setText(null);
                    editTextEmail.setText(null);
                    editTextMobile.setText(null);
                    editTextVehicleNo1.setText(null);
                    editTextVehicleNo2.setText(null);
                    editTextCity.setText(null);
                    editTextAddress.setText(null);
                    editTextStationName.setText(null);
                    editTextPassword.setText(null);
                    editTextStationNo.setText(null);
                }else if(roleType.equals("Choose")){
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.GONE);
                    editTextName.setText(null);
                    editTextEmail.setText(null);
                    editTextMobile.setText(null);
                    editTextVehicleNo1.setText(null);
                    editTextVehicleNo2.setText(null);
                    editTextCity.setText(null);
                    editTextAddress.setText(null);
                    editTextStationName.setText(null);
                    editTextPassword.setText(null);
                    editTextStationNo.setText(null);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.vehicleType, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicelType.setAdapter(adapter1);

        spinnerVehicelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vehicleType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.fuelType, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFuelType.setAdapter(adapter2);

        spinnerFuelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fuelType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = "ABCD";
                String email = "abc@gmail.com";
                String phone = "0777";
                String password = "123456";
                String station = "AAAA";

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("name", name);
                userInfo.put("email", email);
                userInfo.put("phone", phone);
                userInfo.put("password", password);
                userInfo.put("station", station);

                System.out.println(userInfo);
            }
        });
    }
}