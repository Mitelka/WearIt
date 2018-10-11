package com.example.anafa.wearit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private String FirsName;
    private String lastName;
    private String nickname;
    private String EmailAddress;
    private String Password;
    private ServerConnector serverConnector;
    private ProgressDialog pd;

    public static final String MESSAGE_KEY = "com.example.anafa.wearit.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        serverConnector = new ServerConnector();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        pd = null;


        //android:onClick="registerButtonClickHandler"
        Button button = (Button) findViewById(R.id.registerButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText etFirstName = (EditText) findViewById(R.id.firstNameEditText);
                FirsName = etFirstName.getText().toString();

                EditText etLastName = (EditText) findViewById(R.id.lastNameEditText);
                lastName = etLastName.getText().toString();

                EditText etNickname = (EditText) findViewById(R.id.nicknameEditText);
                nickname = etNickname.getText().toString();

                EditText etEmailAddress = (EditText) findViewById(R.id.emailAddressEditText);
                EmailAddress = etEmailAddress.getText().toString();

                EditText etPassword = (EditText) findViewById(R.id.passwordEditText);
                Password = etPassword.getText().toString();


                Snackbar.make(v, "Hello " + FirsName + lastName +
                        "You entered: \n" + " Nickname: " + nickname +
                        "\nEmail: " + EmailAddress + "\nPassword: " +
                        Password + "\n", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                AlertDialog.Builder builder;

                if (FirsName.isEmpty() || lastName.isEmpty() || nickname.isEmpty() || EmailAddress.isEmpty() || Password.isEmpty())
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    {
                        builder = new AlertDialog.Builder(RegistrationActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(RegistrationActivity.this);
                    }

                    builder.setTitle("You Must fill All fields")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
                else
                {
                    attemptRegistration();
                }

            }
        });
    }

    private boolean attemptRegistration()
    {
        HashMap<String,String> mapToSend = new HashMap<String, String>();
        mapToSend.put("FirsName", FirsName);
        mapToSend.put("lastName", lastName);
        mapToSend.put("nickname", nickname);
        mapToSend.put("email", EmailAddress);
        mapToSend.put("password", Password);
        JSONObject RegistrationJson  = serverConnector.createJSonToServer(mapToSend);
        String registrationResponse = serverConnector.sendRequestToServer(RegistrationJson, ServerConnector.RequestType.SIGNUP);

        try
        {
            JSONObject response = new JSONObject(registrationResponse);
            if (response.has("_id"))
            {
                showAlert("Registration completed successfully!");
                registerButtonClickHandler(true);
            }
            else
            {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Email is already exist!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                registerButtonClickHandler(false);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void registerButtonClickHandler(Boolean isRegistration)
    {
        Intent intent;

        if (isRegistration)
        {
            //pass data to next activity
            EditText editText = (EditText)findViewById(R.id.nicknameEditText);
            String message = editText.getText().toString();

            intent = new Intent(this, UserMenuActivity.class);

            intent.putExtra(MESSAGE_KEY, message);
        }
        else
        {
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
    }

    @Override
    public void onPause(){

        super.onPause();
        if(pd != null)
            pd.dismiss();
    }
}