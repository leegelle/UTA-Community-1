package com.example.utacommunityapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.utacommunityapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button login;
    private Button register;
    private ProgressBar progress;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Moving on to the next page
        HomeActivity = new Intent(this,com.example.utacommunityapp.Activities.Home2Activity.class);
        //Initializing the variables
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.login_email);

        register = findViewById((R.id.button2));
        password = findViewById(R.id.login_pass);
        login = findViewById(R.id.login_btn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });

        //Setting progress bar invisible till user clicks it

        //Once login button has been clicked then we can progress
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login.setVisibility(View.INVISIBLE);

                final String email_c = email.getText().toString();
                final String pass = password.getText().toString();

                if(pass.isEmpty() || email_c.isEmpty())
                {
                    showError("Please Make Sure To Enter All Fields");
                }
                else
                {
                    userSignIn(email_c,pass);

                }

            }
        });
    }

    private void userSignIn(String email_c, String pass) {

        mAuth.signInWithEmailAndPassword(email_c,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    login.setVisibility(View.VISIBLE);

                    updateInterface();

                }
                else
                {
                    showError(task.getException().getMessage());
                    login.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    private void updateInterface() {
        startActivity(HomeActivity);
        finish();
    }

    private void showError(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
        
        
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null)
        {
            //user is connected so we redirect them to home page
            updateInterface();

        }
    }
}