package com.groupl.project.pier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.google.android.gms.auth.api.Auth;

public class SignOutActivity extends AppCompatActivity {
    String TAG = "SignOutActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);

        AWSConfiguration a = new AWSConfiguration(this);
        CognitoUserPool userPool = new CognitoUserPool(this, a);
        CognitoUser user = userPool.getCurrentUser();
        user.signOut();


        Intent intent = new Intent(SignOutActivity.this, WelcomeSlider.class);
        startActivity(intent);
        finish();
    }
}
