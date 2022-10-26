package com.example.eadproject.userController;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eadproject.DBHelper.DBHelper;
import com.example.eadproject.OwnerController.OwnerDashboard;
import com.example.eadproject.R;
import com.example.eadproject.UserDashboard.Dashboard;
/*
 *  This is view user profile page
 * */

public class UserProfile extends AppCompatActivity {

    //create variables
    private EditText fullnameEditText,emailEditText,mobileEditText,
            roleEditText,vehicleNoEditText,vehicleTypeEditText,fuelTypeEditText;
    private SQLiteDatabase sqLiteDatabaseObj;
    private Cursor cursor;
    private String email,id;
    DBHelper DB;
    private Button update, delete;
    private AlertDialog.Builder builder;
    private SwipeRefreshLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //get the  station,fuel,city values
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");

        //assign the  variables ids
        fullnameEditText = findViewById(R.id.UserProfileName);
        emailEditText = findViewById(R.id.UserProfileEmail);
        mobileEditText = findViewById(R.id.UserProfileMobile);
        roleEditText = findViewById(R.id.UserProfileRole);
        vehicleNoEditText = findViewById(R.id.UserProfileVehicleNo);
        vehicleTypeEditText = findViewById(R.id.UserProfileVehicleType);
        fuelTypeEditText = findViewById(R.id.UserProfileFuelType);
        update = findViewById(R.id.btnUserProfileUpdate);
        delete = findViewById(R.id.btnUserProfileDelete);
        layout = findViewById(R.id.userProfileRefresh);

        //set the disable fields
        emailEditText.setEnabled(false);
        roleEditText.setEnabled(false);
        vehicleNoEditText.setEnabled(false);
        vehicleTypeEditText.setEnabled(false);
        fuelTypeEditText.setEnabled(false);
        builder = new AlertDialog.Builder(this);

        //this is new DBHelper in this page
        DB = new DBHelper(this);

        //click the delete button for delete user
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setTitle("Delete...!")
                        .setMessage("Do you want to delete the user")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean status = deleteData(id);
                                if(status == true){
                                    Toast.makeText(UserProfile.this, "Delete SuccessFully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(UserProfile.this, "Delete Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
            }
        });

        //refresh the layout
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                layout.setRefreshing(false);
            }
        });

        //update the user details
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = fullnameEditText.getText().toString();
                String mobile = mobileEditText.getText().toString();

                boolean status = updateData(name,mobile,id);
                if(status == true){
                    Toast.makeText(UserProfile.this, "Update SuccessFully", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
//                    startActivity(intent);
                }else{
                    Toast.makeText(UserProfile.this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadData();
    }

    //update the user Sql Data
    private boolean updateData(String name, String mobile, String id) {
        ContentValues contentValues =  new ContentValues();
        contentValues.put(DBHelper.Table_Column_1_Name,name);
        contentValues.put(DBHelper.Table_Column_3_Mobile, mobile);
        return sqLiteDatabaseObj.update(DBHelper.TABLE_NAME, contentValues, DBHelper.Table_Column_ID + "=?", new String[]{id})>0;
    }

    //Delete the user Sql Data
    private boolean deleteData(String id) {
        return sqLiteDatabaseObj.delete(DBHelper.TABLE_NAME, DBHelper.Table_Column_ID + "=?", new String[]{id})>0;
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
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_1_Name));
                String email = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_2_Email));
                String mobile = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_3_Mobile));
                String vehicleType = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_10_VehicleType));
                String vehicleNo1 = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_4_VehicleNo1));
                String vehicleNo2 = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_5_VehicleNo2));
                String fuelType = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_11_FuelType));
                String role = cursor.getString(cursor.getColumnIndex(DBHelper.Table_Column_12_RoleType));

                fullnameEditText.setText(name);
                emailEditText.setText(email);
                mobileEditText.setText(mobile);
                roleEditText.setText(role);
                vehicleNoEditText.setText(vehicleNo1 + "-" + vehicleNo2);
                vehicleTypeEditText.setText(vehicleType);
                fuelTypeEditText.setText(fuelType);

                // Closing cursor.
                cursor.close();
            }
        }
    }
}