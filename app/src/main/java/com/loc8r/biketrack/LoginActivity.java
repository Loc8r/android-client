package com.loc8r.biketrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
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
    String mPhoneNumber;
    SignInButton mSignInButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		FirebaseApp.initializeApp(this);
		mAuth = FirebaseAuth.getInstance();

		mSignInButton = (SignInButton) findViewById(R.id.login);
		mSignInButton.setSize(SignInButton.SIZE_WIDE);

		//gets sharedprefs.
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
		mPhoneNumber = prefs.getString("phone_number", "111111111");

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

		//Database reference points to a specific user in list of users
		mDatabaseReference = FirebaseDatabase
				.getInstance()
				.getReference()
				.child(mPhoneNumber);

		//if user is already logged in, then immediately call getFirebaseData
		FirebaseUser user = mAuth.getCurrentUser();
		if (user != null) {
			getFirebaseData(mDatabaseReference);
		}

	}

	public void loginWithGoogle(View v) {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mgac);
		startActivityForResult(signInIntent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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

							//if you sign in and no number has been linked to the DEVICE (not the acct bc the acct is useless atm)
							//then mPhoneNumber is 1111111111 and we make an intent to go signup the user.
							//otherwise, we call getFirebaseData()
							if (mPhoneNumber.equals("111111111")) {
							    Intent intent = new Intent(LoginActivity.this, SignUpUser.class);

                            } else {
							    getFirebaseData(mDatabaseReference);
                            }

						} else {
							Toast.makeText(LoginActivity.this, "Sorry! Something went wrong! Try again!",
									Toast.LENGTH_SHORT).show();
						}

					}
				});
	}


	private void getFirebaseData(DatabaseReference ref) {
		ref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {


				//TODO work with the snapshot (hashmap) and get last location
				//TODO call goToMaps



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
