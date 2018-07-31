package com.example.anafa.wearit;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {

    private String FirsName;
    private String lastName;
    private String nickname;
    private String EmailAddress;
    private String Password;
    private ServerRegistration RegistrationAttempt;
    int i = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                attemptRegistration();
                registerButtonClickHandler(v);
            }
        });
    }

    private void attemptRegistration() {
        boolean RegistrationSuccess;

        JSONObject RegistrationJson  = createJSonToServer(FirsName,lastName,nickname,EmailAddress,Password);
        RegistrationAttempt = new ServerRegistration(RegistrationJson);
        RegistrationSuccess = RegistrationAttempt.sendRequestToserver(RegistrationJson);
    }

    public JSONObject createJSonToServer(String FirsName,String lastName,String nickname, String EmailAddress, String Password)
    {
        JSONObject RegistrationJson  = new JSONObject();
        try {
            RegistrationJson.put("FirsName", FirsName);
            RegistrationJson.put("lastName", lastName);
            RegistrationJson.put("nickname", nickname);
            RegistrationJson.put("email", EmailAddress);
            RegistrationJson.put("Password", Password);
        }
        catch (JSONException e)
        {
            //TODO - Auto-generated catch block
            e.printStackTrace();
        }
        return RegistrationJson;
    }

    public void registerButtonClickHandler(View view) {
//        Toast.makeText(RegistrationActivity.this,
//                "You selected sign in with email: " + email + " nickname " + nickname +
//                        " and your fassword is: " + password,
//                Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}