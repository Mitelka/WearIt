package com.example.anafa.wearit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class ToolsActivity extends AppCompatActivity {

    private AutoCompleteTextView emailView;
    private EditText oldPasswordView;
    private EditText newPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        oldPasswordView = (EditText) findViewById(R.id.oldPassword);
        newPasswordView = (EditText) findViewById(R.id.newPassword);

        Button changePasswordButton = (Button) findViewById(R.id.changePasswordButton);

        // Store values at the time of the change password attempt.
        String email = emailView.getText().toString();
        String oldPassword = oldPasswordView.getText().toString();
        String newPassword = newPasswordView.getText().toString();

    }

    public void changePasswordButtonClickHandler(View view) {
    }
}
