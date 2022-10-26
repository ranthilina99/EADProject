package com.example.eadproject.userController;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eadproject.DBHelper.DBHelper;
import com.example.eadproject.OwnerController.OwnerDashboard;
import com.example.eadproject.R;
import com.example.eadproject.UserDashboard.Dashboard;

/*
 *  This is  Login page
 * */

public class Login extends AppCompatActivity {

    //these are textview,editText, button and database variable;
    private TextView textView;
    private EditText editTextemail, editTextpassword;
    private Button button;
    DBHelper DB;
    private Boolean EditTextEmptyHolder;
    private SQLiteDatabase sqLiteDatabaseObj;
    private Cursor cursor;
    private String TempPassword = "NOT_FOUND";
    private String email, password,Role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //these are variable assigned ids
        textView = findViewById(R.id.txtdonthaveacc);
        editTextemail = findViewById(R.id.loginemail);
        editTextpassword = findViewById(R.id.loginpassword);
        button = findViewById(R.id.btnlogin);

        //there are new Sql lite db helper created
        DB = new DBHelper(this);

        // click register textview  go to the register page
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

        // click login button checkEditTextStatus and Login function Called there are
        // two role they are user and owner
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = editTextemail.getText().toString();
                String pass = editTextpassword.getText().toString();

                CheckEditTextStatus();
                // Calling login method.
                LoginFunction();

                EmptyEditTextAfterDataInsert();
            }
        });
    }

    //this is the email and password clear
    private void EmptyEditTextAfterDataInsert() {
        editTextemail.getText().clear();
        editTextpassword.getText().clear();
    }

    //this is the login function
    @SuppressLint("Range")
    private void LoginFunction() {
        if (EditTextEmptyHolder) {
            // Opening SQLite database write permission.
            sqLiteDatabaseObj = DB.getWritableDatabase();
            // Adding search email query to cursor.
            cursor = sqLiteDatabaseObj.query(DBHelper.TABLE_NAME, null, " " + DBHelper.Table_Column_2_Email + "=?", new String[]{email}, null, null, null);
            while (cursor.moveToNext()) {
                if (cursor.isFirst()) {
                    cursor.moveToFirst();
                    // Storing Password associated with entered email.
                    TempPassword = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_13_Password));
                    Role = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_12_RoleType));
                    // Closing cursor.
                    cursor.close();
                }
            }
            // Calling method check final result
            CheckFinalResult();
        } else {
            //If any of login EditText empty then this block will be executed.
            Toast.makeText(Login.this, "Please Enter UserName or Password.", Toast.LENGTH_LONG).show();
        }
    }

    //this is the check edit text status function
    public void CheckEditTextStatus() {

        //validation and get the value edit text in login page
        email = editTextemail.getText().toString();
        password = editTextpassword.getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        // Checking EditText is empty or no using TextUtils.
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            EditTextEmptyHolder = false;
        } else {
            EditTextEmptyHolder = true;
        }
    }

    // Checking entered password from SQLite database email associated password.
    public void CheckFinalResult() {
        if (TempPassword.equalsIgnoreCase(password)) {
            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
            System.out.println(Role);
            if(Role.equals("User")){
                // Going to Dashboard activity after login success message.
                Intent intent = new Intent(Login.this, Dashboard.class);
                // Sending Email to Dashboard Activity using intent.
                intent.putExtra("email", email);
                startActivity(intent);
            }else if(Role.equals("Owner")){
                // Going to Dashboard activity after login success message.
                Intent intent = new Intent(Login.this, OwnerDashboard.class);
                // Sending Email to Dashboard Activity using intent.
                intent.putExtra("email", email);
                startActivity(intent);
            }
        } else {
            Toast.makeText(Login.this, "UserName or Password is Wrong, Please Try Again.", Toast.LENGTH_LONG).show();
        }
        TempPassword = "NOT_FOUND";
    }
}