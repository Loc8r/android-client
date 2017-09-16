package com.loc8r.biketrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;


public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    GoogleApiClient mgac;
    GoogleSignInAccount mgsa;
    DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();

        mgac = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Oops! Something went wrong. Please restart program", Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void loginWithGoogle(View v) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mgac);
        startActivityForResult(signInIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO figure out why signin result fails!
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        handleSignInResult(result);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            mgsa = result.getSignInAccount();
            authenticateFirebaseUser(mgsa);
        } else {
            Toast.makeText(this, "Sign in failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void authenticateFirebaseUser(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            //TODO get firebase data if user is in system
                            getFirebaseData(mDatabaseReference.child("user"));



                        } else {
                           //TODO if no account set up then tell person to add an account
                            Toast.makeText(LoginActivity.this, "Sorry! Something went wrong! Try again!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private boolean userExists(DataSnapshot snapshot) {

        for (int x = 0; x > snapshot.getChildrenCount() - 1; x++) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String checkedEmail = user.getEmail();
            Iterator<DataSnapshot> it = snapshot.getChildren().iterator();
            while (it.hasNext()) {
                DataSnapshot child = it.next();
                //
            }
            String inputEmail;

        }


        return false;
    }

    private void getFirebaseData(DatabaseReference ref) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO get the location of the specific user and package it as an intent to MapsActivity

                if (userExists(dataSnapshot)) {
                    DataSnapshot dataSnapshot1 = dataSnapshot.child("User1");
                    LatLng location = (LatLng) dataSnapshot1.child("latlng").getValue();
                    goToMaps(location);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void goToMaps(LatLng location) {

        Intent intent = new Intent(this, MapsActivity.class);
        double lat = location.latitude;
        double lon = location.longitude;
        intent.putExtra("latitude", lat);
        intent.putExtra("longitude", lon);
        startActivity(intent);
    }
}
