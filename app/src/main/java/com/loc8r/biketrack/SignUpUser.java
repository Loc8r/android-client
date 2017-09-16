package com.loc8r.biketrack;

import android.content.Intent;
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
    }

    public void registerUser (View v) {

        String phoneNumber = mPhoneNumber.getText().toString();
        if (phoneNumber.length() == 10) {
            updateToFirebase(makeUser(phoneNumber));
        } else {
            Toast.makeText(this, "This is an invalid phone number. Try again!", Toast.LENGTH_SHORT)
                    .show();
        }


    }

    private User makeUser(String s) {
        User newUser = new User();
        newUser.setUniqueID(s);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        newUser.setAcctEmail(user.getEmail());
        return newUser;
    }

    private void updateToFirebase(User user) {
        //TODO make this consistent with the rest of the LoginActivity Code. userlist is an arraylist of users.
        mDatabaseReference.child("userlist")
                .push()
                .setValue(user);
        returnToLogin();
    }

    private void returnToLogin() {
        Toast.makeText(this, "New Account Made!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }
}
