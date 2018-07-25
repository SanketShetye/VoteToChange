package com.codeforfun.himanshu.votetochange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codeforfun.himanshu.votetochange.NetworkCalls.BackgroundNetworkCall;
import com.codeforfun.himanshu.votetochange.NetworkHelper.UrlData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginPage extends AppCompatActivity {

    EditText mUsernameInput,mPasswordInput;
    TextView mLoginFailedNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        //Initialize all the variables
        mUsernameInput = (EditText) findViewById(R.id.usernameInput);
        mPasswordInput = (EditText) findViewById(R.id.passwordInput);
        mLoginFailedNotice = (TextView) findViewById(R.id.loginFailedNotice);

        findViewById(R.id.signInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUp.class);
                startActivity(i);
                finish();
            }
        });

    }

    private boolean isUsernameValid(String username){
        return username.length() > 0;
    }

    private boolean isPasswordValid(String password){
        return password.length() > 0;
    }

    private void signIn(){

        //If it is login after failure we need erase the login failure message.
        mLoginFailedNotice.setVisibility(View.GONE);

        String username,password;

        username = mUsernameInput.getText().toString();
        password = mPasswordInput.getText().toString();


        //Make sure that the username and password are valid.
        if(!isUsernameValid(username) || !isPasswordValid(password)){
            Toast.makeText(this, "Input data is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> queryData = new ArrayList<>();
        queryData.add("username");
        queryData.add(username);
        queryData.add("password");
        queryData.add(password);

        try {
            String loginResult = new BackgroundNetworkCall().execute(this,UrlData.LOGIN_URL,queryData);
            Log.i(AppConstants.TAG,"Login Result = "+loginResult);
            if(loginResult!=null && loginResult.equals("1") ){
                //Login is Success;
                Log.i(AppConstants.TAG,"Login success");

                saveUserCredentials(username,password);

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
            else{
                //Login Failed
                //Toast.makeText(this, "Username/Password is incorrect", Toast.LENGTH_SHORT).show();
                notifyLoginFailed();
                return;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make the UI changes for login failed.
     */
    private void notifyLoginFailed(){
        mPasswordInput.setText("");
        mLoginFailedNotice.setVisibility(View.VISIBLE);
    }

    /**
     * Save the username and password of the user in SharedPreferences
     * @param username of the logged in user
     * @param password of the logged in user
     */
    private void saveUserCredentials(String username, String password){

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("UserPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AppConstants.SHARED_PREFS_USERNAME,username);
        editor.putString(AppConstants.SHARED_PREFS_PASSWORD,password);
        editor.apply();
        SharedPreferences pref =getSharedPreferences("UserPrefs",MODE_PRIVATE);
        String userna = pref.getString(AppConstants.SHARED_PREFS_USERNAME, "DEFAULT");

        Log.i("SaveUserCredentials",userna);
    }

}
