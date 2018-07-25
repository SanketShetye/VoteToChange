package com.codeforfun.himanshu.votetochange;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codeforfun.himanshu.votetochange.NetworkCalls.BackgroundNetworkCall;
import com.codeforfun.himanshu.votetochange.NetworkHelper.UrlData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SignUp extends AppCompatActivity {

    TextView mName,mUsername,mPassword,mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mName = (TextView) findViewById(R.id.name);
        mUsername = (TextView) findViewById(R.id.username);
        mPassword = (TextView) findViewById(R.id.password);
        mEmail = (TextView) findViewById(R.id.email);

        findViewById(R.id.registerUserButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    public void registerNewUser(){
        //If the data entered in the form is not proper.
        if(!checkFormData()) {
            return;
        }

        List<String> queryData = new ArrayList<>();
        queryData.add("name");queryData.add(mName.getText().toString());
        queryData.add("username");queryData.add(mUsername.getText().toString());
        queryData.add("password");queryData.add(mPassword.getText().toString());
        queryData.add("email");queryData.add(mEmail.getText().toString());

        String registrationStatus;
        try {
            registrationStatus = new BackgroundNetworkCall().execute(this,UrlData.REGISTER_URL,queryData);
            if(registrationStatus!=null && registrationStatus.equals("1")){
                //Registration is successful.
                Toast.makeText(this,"Registration Complete",Toast.LENGTH_SHORT).show();

                //Take the user to the login screen to login in the app.
                Intent i = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(i);
                finish();
            }
            else{
                //Registration is failed.
                Toast.makeText(this,"Error In Registration",Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean checkTextViewData(TextView textView){
        return textView.getText().length()>0;
    }

    public boolean checkFormData(){
        if(checkTextViewData(mName)&&checkTextViewData(mUsername)&&checkTextViewData(mPassword)&&checkTextViewData(mEmail))
            return true;
        return false;
    }

}
