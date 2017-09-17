package com.loc8r.biketrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpUser extends AppCompatActivity {

    EditText mPhoneNumber;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        mPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void registerUser (View v) {
        String phoneNumber = mPhoneNumber.getText().toString();

        if (phoneNumber.length() != 10) {
            Toast.makeText(this, "Improper number. Please write the number in format xxxxxxxxxx without spaces!", Toast.LENGTH_SHORT)
                    .show();
            mPhoneNumber.setText("");
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit().putString("phone_number", phoneNumber).apply();
            returnToLogin();
        }
    }
    
    private void returnToLogin() {
        Toast.makeText(this, "New Account Made!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }
}
