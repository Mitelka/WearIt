package com.example.anafa.wearit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
    private String RepeatPassword;
    private ProgressDialog pd;
    ProgressBar progressBar;


    public static final String MESSAGE_KEY = "com.example.anafa.wearit.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        pd = null;

        //android:onClick="registerButtonClickHandler"
        Button button = findViewById(R.id.registerButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText etFirstName = findViewById(R.id.firstNameEditText);
                FirsName = etFirstName.getText().toString();

                EditText etLastName = findViewById(R.id.lastNameEditText);
                lastName = etLastName.getText().toString();

                EditText etNickname = findViewById(R.id.nicknameEditText);
                nickname = etNickname.getText().toString();

                EditText etEmailAddress = findViewById(R.id.emailAddressEditText);
                EmailAddress = etEmailAddress.getText().toString();

                EditText etPassword = findViewById(R.id.passwordEditText);
                Password = etPassword.getText().toString();

                EditText eRepeatPassword = findViewById(R.id.repeatPasswordEditText);
                RepeatPassword = eRepeatPassword.getText().toString();


//                Snackbar.make(v, "Hello " + FirsName + lastName +
//                        "You entered: \n" + " Nickname: " + nickname +
//                        "\nEmail: " + EmailAddress + "\nPassword: " +
//                        Password + "\n", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

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
                else if (!(Password.equals(RepeatPassword)))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    {
                        builder = new AlertDialog.Builder(RegistrationActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(RegistrationActivity.this);
                    }

                    builder.setTitle("Password error: Passwords fields are not equals")
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
        HashMap<String,String> mapToSend = new HashMap<>();
        mapToSend.put("FirsName", FirsName);
        mapToSend.put("lastName", lastName);
        mapToSend.put("nickname", nickname);
        mapToSend.put("email", EmailAddress);
        mapToSend.put("password", Password);
        JSONObject RegistrationJson  = ServerConnector.getInstance().createJSonToServer(mapToSend);
        String registrationResponse = ServerConnector.getInstance().sendRequestToServer(RegistrationJson, ServerConnector.RequestType.SIGNUP);

        try
        {
            JSONObject response = new JSONObject(registrationResponse);
            if (response.has("_id"))
            {
                showAlert("Registration completed successfully!", true);

            }
            else
            {
                showAlert("Email is already exist!", false);
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(String message, final Boolean MoveToNextAct)
    {
        final Context context = this;
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (MoveToNextAct)
                        {
                            progressBar = new ProgressBar();
                            progressBar.getProgressBar(context);
                            registerButtonClickHandler(true);
                        }
                    }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public void registerButtonClickHandler(Boolean isRegistration)
    {
        Intent intent;

        if (isRegistration)
        {
            //pass data to next activity
            EditText editText = findViewById(R.id.nicknameEditText);
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