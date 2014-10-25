package com.codepath.columbus.columbus.activities;


import android.app.ActionBar;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.columbus.columbus.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class LoginActivity extends FragmentActivity
        implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

     /*
        * Track whether the sign-in button has been clicked so that we know to resolve
        * all issues preventing sign-in without waiting.
        */
    private boolean mSignInClicked;

    /* Store the connection result from onConnectionFailed callbacks so that we can
     * resolve them when the user clicks sign-in.
     */
    private ConnectionResult mConnectionResult;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
    * us from starting further intents.
    */
    private boolean mIntentInProgress;

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            // Note: revoking the access/signing out - not needed for real scenarios
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        SignInButton btSignIn = (SignInButton)findViewById(R.id.btSignIn);
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mGoogleApiClient.isConnecting()) {
                    mSignInClicked = true;
                    resolveSignInError();
                }
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        setActionBar();
    }

    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        if (mConnectionResult != null &&
                mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                this.startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);

            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress) {
            // Store the ConnectionResult so that we can use it later when the user clicks
            // 'sign-in'.
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    public void onConnected(Bundle connectionHint) {
        // We've resolved any connection errors.  mGoogleApiClient can be used to
        // access Google APIs on behalf of the user.
        mSignInClicked=false;
        setProfileInformation();
    }

    private void setProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( this);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("username",personName);
                edit.putString("imageURL",personPhotoUrl);
                edit.putString("email",email);
                edit.commit();

                Toast.makeText(this, "Hello "+personName, Toast.LENGTH_SHORT).show();
                Log.d("LoginActivity","username:"+personName);
                Log.d("LoginActivity","imageURL:"+personPhotoUrl);
                Log.d("LoginActivity","email:"+email);

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }else{
                Toast.makeText(this,"Sign in failed. Please try again",Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e){
                e.printStackTrace();
            }
        }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    public void setActionBar() {
        ActionBar actionBar = getActionBar();
      /*
        String title = "<font color=\""+getResources().getColor(R.color.actionbar_title_color)+"\">Log In</font>";
        actionBar.setTitle(Html.fromHtml(title));
       */
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
