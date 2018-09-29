package com.example.anafa.wearit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        serverConnector = new ServerConnector();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        //android:onClick="registerButtonClickHandler"
        Button button = (Button) findViewById(R.id.registerButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Boolean isRegistration = attemptRegistration();
                registerButtonClickHandler(v,isRegistration);
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
                return true;
            }
            else
            {
                showAlert("Email is already exist!");
                return false;
            }
        } catch (JSONException e)

        {
            e.printStackTrace();
        }

        return true;
    }

    private void showAlert(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void registerButtonClickHandler(View view, Boolean isRegistration)
    {
        Intent intent;

        if (isRegistration)
        {
            intent = new Intent(this, UserMenuActivity.class);
        }
        else
        {
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
    }
}