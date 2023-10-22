package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otpAuthentication extends AppCompatActivity {

    String enteredOTP;
    TextView mChangeNumber;
    EditText mGetOTP;
    android.widget.Button mVerifyOTP;
    ProgressBar mProgressBarOfOTPAuth;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_authentication);

        mChangeNumber = findViewById(R.id.changenumber);
        mVerifyOTP = findViewById(R.id.verifyotp);
        mGetOTP = findViewById(R.id.getotp);
        mProgressBarOfOTPAuth = findViewById(R.id.progressbarofotpauth);

        firebaseAuth = FirebaseAuth.getInstance();

        mChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(otpAuthentication.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredOTP = mGetOTP.getText().toString();
                if (enteredOTP.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter your OTP First",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mProgressBarOfOTPAuth.setVisibility(View.VISIBLE);
                    String code_received = getIntent().getStringExtra("otp");
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code_received,enteredOTP);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    mProgressBarOfOTPAuth.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(otpAuthentication.this, setProfile.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        mProgressBarOfOTPAuth.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }
}