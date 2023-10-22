package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText mGetPhoneNumber;
    android.widget.Button mSendOTP;
    CountryCodePicker mCountyCodePicker;
    String countryCode, phoneNumber, codeSent;
    FirebaseAuth firebaseAuth;
    ProgressBar mProgressBarOfMain;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_main);

        mCountyCodePicker = findViewById(R.id.countrycodepicker);
        mSendOTP = findViewById(R.id.sendotpbutton);
        mSendOTP = findViewById(R.id.sendotpbutton);
        mGetPhoneNumber = findViewById(R.id.getphonenumber);
        mProgressBarOfMain = findViewById(R.id.progressbarofmain);

        // Show current user whether logged in ot not
        firebaseAuth = FirebaseAuth.getInstance();
        // Store selected country code
        countryCode = mCountyCodePicker.getSelectedCountryCodeWithPlus();
        //When someone wants to change their country code
        mCountyCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = mCountyCodePicker.getSelectedCountryCodeWithPlus();
            }
        });

        // combine ccp string and phone number to send OTP
        mSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number;

                number = mGetPhoneNumber.getText().toString();
                if (number.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter Your Number",Toast.LENGTH_SHORT).show();
                } else if (number.length()<10) {
                    Toast.makeText(getApplicationContext(),"Please Enter Correct Number",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mProgressBarOfMain.setVisibility(View.VISIBLE);
                    phoneNumber = countryCode+number;

                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(mCallbacks)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });

        // check if phone number is correct and send OTP
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // how to automatically fetch code here
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(),"OTP is sent",Toast.LENGTH_SHORT).show();
                mProgressBarOfMain.setVisibility(View.INVISIBLE);
                codeSent = s;
                Intent intent = new Intent(MainActivity.this, otpAuthentication.class);
                intent.putExtra("otp",codeSent);
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        // check if user already exists, else send user to chat activity

        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(MainActivity.this,chatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
