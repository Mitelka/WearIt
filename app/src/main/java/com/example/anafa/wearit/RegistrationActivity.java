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

public class RegistrationActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mNicknameView;
    private View mProgressView;
    private View mLoginFormView;

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
                String entryFirstName = etFirstName.getText().toString();

                EditText etLastName = (EditText) findViewById(R.id.lastNameEditText);
                String entryLastName = etLastName.getText().toString();

                EditText etNickname = (EditText) findViewById(R.id.nicknameEditText);
                String entryNickname = etNickname.getText().toString();

                EditText etEmailAddress = (EditText) findViewById(R.id.emailAddressEditText);
                String entryEmailAddress = etEmailAddress.getText().toString();

                EditText etPassword = (EditText) findViewById(R.id.passwordEditText);
                String entryPassword = etPassword.getText().toString();

                Snackbar.make(v, "Hello " + entryFirstName + entryLastName +
                        "You entered: \n" + " Nickname: " + entryNickname +
                        "\nEmail: " + entryEmailAddress + "\nPassword: " +
                        entryPassword + "\n", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                registerButtonClickHandler(v);
            }
        });


    }

    private void attemptRegistration() {

        String nickname = mNicknameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
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