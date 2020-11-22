package com.rndtechnosoft.foodcart.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.SharedPref;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    //   LoginButton loginButton;
    String mVerificationId;

    private EditText editText_phoneNumber;
    private EditText editText_otp,editText_name,edittext_dob;
    private Button button_submit,button_send;
    private LinearLayout otp_layout,form_layout;
    private ProgressBar
            progressBar;
    private String TAG = "TAG";
    private String fullname,phone,userid,dob;
    private TextView editText_skip;
    private CountDownTimer timer;
    private int mYear, mMonth, mDay;
    LinearLayout dob_linear;
    TextView resendOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        innitView();
        mAuth = FirebaseAuth.getInstance();
        phoneNumberAuthCallbackListener();
    }
    private void startTimer() {

        timer =  new CountDownTimer(60000, 1000){
            public void onTick(long millisUntilFinished){
                editText_skip.setVisibility(View.VISIBLE);
                editText_skip.setText("seconds remaining: " + millisUntilFinished / 1000);


            }
            public  void onFinish(){
                editText_skip.setVisibility(View.GONE);
                resendOtp.setVisibility(View.VISIBLE);
                                //  cancel.setEnabled(true);
            }
        }.start();
    }

    private void innitView() {

        editText_name = (EditText) findViewById(R.id.textView_name_contactUS_fragment);
        editText_otp = (EditText) findViewById(R.id.otp);
        edittext_dob = (EditText) findViewById(R.id.textView_dob);
        dob_linear = findViewById(R.id.dob);
        editText_phoneNumber = (EditText) findViewById(R.id.textView_phoneNo_contactUS_fragment);
        button_send = (Button) findViewById(R.id.send_otp);
        button_submit = (Button) findViewById(R.id.submit);
        progressBar = (ProgressBar) findViewById(R.id.progresbar_home_fragment);
        editText_skip = (TextView) findViewById(R.id.skip);
        form_layout = (LinearLayout) findViewById(R.id.form_layout);
        otp_layout = (LinearLayout) findViewById(R.id.otp_layout);
        resendOtp = findViewById(R.id.resendOtp);
        resendOtp.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        otp_layout.setVisibility(View.GONE);

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noWithCode = "+91"+phone;
                startPhoneNumberVerification(noWithCode);

                //editText_skip.setVisibility(View.VISIBLE);
                //cancel.setEnabled(false);

                startTimer();
            }
        });

       /* editText_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText_skip.getText().equals("Skip"))
                {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });*/
       /* cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpCodeText.setText("");
                formLayout.setVisibility(View.VISIBLE);
                otpLayout.setVisibility(View.GONE);
            }
        });*/

       dob_linear.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final Calendar c = Calendar.getInstance();
               mYear = 2000;
               mMonth = 0;
               mDay = 1;


               DatePickerDialog datePickerDialog = new DatePickerDialog(LoginActivity.this,
                       new DatePickerDialog.OnDateSetListener() {

                           @Override
                           public void onDateSet(DatePicker view, int year,
                                                 int monthOfYear, int dayOfMonth) {

                               edittext_dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                           }
                       }, mYear, mMonth, mDay);
               datePickerDialog.show();
           }
       });

       edittext_dob.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final Calendar c = Calendar.getInstance();
               mYear = 2000;
               mMonth = 0;
               mDay = 1;


               DatePickerDialog datePickerDialog = new DatePickerDialog(LoginActivity.this,
                       new DatePickerDialog.OnDateSetListener() {

                           @Override
                           public void onDateSet(DatePicker view, int year,
                                                 int monthOfYear, int dayOfMonth) {

                               edittext_dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                           }
                       }, mYear, mMonth, mDay);
               datePickerDialog.show();
           }
       });


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editText_otp.setError(null);
                if (editText_otp.length() == 0) {

                    editText_otp.setError(getString(R.string.please_enter_otp));

                    return;
                }
                if(progressBar.getVisibility()==View.GONE)
                    progressBar.setVisibility(View.VISIBLE);
                verifyPhoneNumberWithCode(mVerificationId,editText_otp.getText().toString().trim());

            }
        });

        ;
        editText_name.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                checkFullname();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });



        editText_phoneNumber.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                boolean flag = checkPhone();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        edittext_dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkDate();
            }
        });

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean flag = checkPhone();
                boolean dobcheck = checkDate();
                if(flag && dobcheck)
                {


                    phone = editText_phoneNumber.getText().toString();
                    fullname = editText_name.getText().toString();
                    dob = edittext_dob.getText().toString();

                    if (verifyRegForm()) {
                        if(progressBar.getVisibility()==View.GONE)
                            progressBar.setVisibility(View.VISIBLE);
                        String noWithCode = "+91"+phone;
                        startPhoneNumberVerification(noWithCode);

                        //editText_skip.setVisibility(View.VISIBLE);
                        //cancel.setEnabled(false);

                        startTimer();
                    }

                }

            }
        });

    }

    private boolean checkDate() {
        dob = edittext_dob.getText().toString();

        if (dob.length() == 0) {

            edittext_dob.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (dob.length() < 8) {

            edittext_dob.setError(getString(R.string.enter_your_dob));

            return false;
        }

        edittext_dob.setError(null);

        return  true;
    }

    public void phoneNumberAuthCallbackListener()
    {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d("TAG", "onVerificationCompleted:" + credential.getSmsCode());
                if(progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
                editText_otp.setText(credential.getSmsCode());
                registration();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("TAG", "onVerificationFailed", e);
                if(progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    return;
                }
                else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    return;

                }
                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                if(progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
                Log.d("TAG", "onCodeSent:" + verificationId);
                Toast.makeText(LoginActivity.this,"Otp has been sent to your mobile number",Toast.LENGTH_LONG).show();
                // Save verification ID and resending token so we can use them later

                mVerificationId = verificationId;
                form_layout.setVisibility(View.GONE);
                editText_otp.setVisibility(View.VISIBLE);
                otp_layout.setVisibility(View.VISIBLE);



                // mResendToken = token;
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(progressBar.getVisibility() == View.VISIBLE)
                            progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            editText_otp.setText("");
                            Toast.makeText(LoginActivity.this,"Verified successfully",Toast.LENGTH_LONG).show();
                            registration();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LoginActivity.this,"Verification failed!Please enter correct otp",Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                LoginActivity.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }
    public void registration()
    {

        phone = editText_phoneNumber.getText().toString();
        fullname = editText_name.getText().toString();
        dob = edittext_dob.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient(true,80,443);
        client.addHeader("App-Id",Constant_Api.HOTEL_ID);
        String url=Constant_Api.login+"?mobile="+phone+"&name="+fullname+"&dob="+dob;//+"&name="+fullname+"&mobileid="+ Method.getAndroidID(LoginActivity.this)+"&token="
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {

                    if(timer!=null)
                        timer.cancel();

                    JSONObject jsonObject=new JSONObject(res);

                    JSONObject json=jsonObject.getJSONObject("LOGGED_IN_USER");

                    String error=json.getString("error");

                    if(progressBar.getVisibility() == View.VISIBLE)
                        progressBar.setVisibility(View.GONE);
                    if((res.toLowerCase()).contains("false")) {
                        userid=json.getString("id");
                        Log.d("logged user ",res);
                        SharedPref.setPreference(SharedPref.USER_NAME,fullname,LoginActivity.this);
                        SharedPref.setPreference(SharedPref.USER_MOBILE,phone,LoginActivity.this);
                        SharedPref.setPreference(SharedPref.USER_ID,userid,LoginActivity.this);
                        editText_name.setText("");
                        editText_phoneNumber.setText("");
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    else
                    {

                        //editText_skip.setText("Skip");
                        Toast.makeText(LoginActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
/*

                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);*/
                        //finish();
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if(progressBar.getVisibility() == View.VISIBLE)
                        progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    public Boolean checkFullname() {

        fullname = editText_name.getText().toString();

        if (fullname.length() == 0) {

            editText_name.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (fullname.length() < 2) {

            editText_name.setError(getString(R.string.enter_your_name));

            return false;
        }

        editText_name.setError(null);

        return  true;
    }




    public Boolean checkPhone() {

        phone = editText_phoneNumber.getText().toString();


        if (phone.length() == 0) {

            editText_phoneNumber.setError(getString(R.string.error_field_empty));

            return false;
        }


        if (phone.length()!=10) {

            editText_phoneNumber.setError(getString(R.string.wrong_format));

            return false;
        }
        editText_phoneNumber.setError(null);

        return true;
    }

    public Boolean verifyRegForm() {


        editText_name.setError(null);

        editText_phoneNumber.setError(null);




        if (fullname.length() == 0) {

            editText_name.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (fullname.length() < 2) {

            editText_name.setError(getString(R.string.error_small_fullname));

            return false;
        }


        if (phone.length() == 0) {

            editText_phoneNumber.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (phone.length() != 10) {

            editText_phoneNumber.setError(getString(R.string.wrong_format));

            return false;
        }

        return true;
    }

}
