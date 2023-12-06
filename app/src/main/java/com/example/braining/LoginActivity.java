package com.example.braining;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;

    FirebaseUser user;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.loginButton);
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        Log.d("Login: ", "Checking If User Is Logged In");
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d("Login: ", "User Is Already Logged In");
            Log.d("Login: ", "Switching Intent To MainActivity");
            Intent foo = new Intent(this, MainActivity.class);
            startActivity(foo);

        }
        Log.d("Login: ", "User Was Not Logged In");
        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();

        loginButton.setOnClickListener(v -> {
            signInLauncher.launch(signInIntent);
        });

    }
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> onSignInResult(result)
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            // ...
            Log.wtf("Login: ", "Login Successful");
            user = FirebaseAuth.getInstance().getCurrentUser();
            Log.wtf("Login: ", "Got User");
            Intent foo = new Intent(this, MainActivity.class);
            Log.wtf("Login: ", "Main Intent Created");
            startActivity(foo);
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            Log.wtf("Login: ", "Login Failed");
            Log.wtf("Login: ", "Checking For User Error");
            if (response == null) {
                Log.wtf("Login: ", "User Error Confirmed");
                Toast.makeText(LoginActivity.this, "Please try Again", Toast.LENGTH_LONG).show();
            } else {
                Log.wtf("Login: ", "Not User Error");
                Log.wtf("Login: ", "Getting Error Code");
                @SuppressLint("RestrictedApi") String code = ErrorCodes.toFriendlyMessage(response.getError().getErrorCode());
                Log.wtf("Login: ", code);
            }
        }
    }

}