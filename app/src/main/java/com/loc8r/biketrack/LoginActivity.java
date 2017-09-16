package com.loc8r.biketrack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private boolean checkPassword() {
        return true;
    }

    public void loginWithGoogle(View v) {

    }

    public void loginWithEmail(View v) {

    }
}
