package com.example.eadproject.userController;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eadproject.R;

public class Login extends AppCompatActivity {

    private TextView textView;
    private EditText editTextemail, editTextpassword;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.txtdonthaveacc);
        editTextemail= findViewById(R.id.loginemail);
        editTextpassword = findViewById(R.id.loginpassword);
        button = findViewById(R.id.btnlogin);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

    }
}