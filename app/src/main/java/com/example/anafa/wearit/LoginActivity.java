package com.example.anafa.wearit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private LinearLayout mdata_login_form;
    private ProgressDialog pd;
    private PropertyReader propertyReader;
    private String nickName = "UNKNOWN";
    private Intent intent;

    public static final String MESSAGE_KEY = "com.example.anafa.wearit.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupActionBar();
        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        //populateAutoComplete();
        pd = null;

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mdata_login_form = findViewById(R.id.data_login_form);
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //populateAutoComplete();
            }
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password1));
            focusView = mPasswordView;
            cancel = true;
        }
        else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password2));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            
            //pass data to next activity

            Intent intent = new Intent(this, UserMenuActivity.class);

            HashMap<String,String> mapToSend = new HashMap<String, String>();
            mapToSend.put("email", email);
            mapToSend.put("password", password);

            JSONObject RegistrationJson  = ServerConnector.getInstance().createJSonToServer(mapToSend);
            String loginResponse = ServerConnector.getInstance().sendRequestToServer(RegistrationJson, ServerConnector.RequestType.LOGIN);
            try
            {
                JSONObject response = new JSONObject(loginResponse);
                if (response.has("email"))
                {
                    String nickName = response.getString("nickname");
                    String successMessage = "Login successfully!";

                    intent = new Intent(this, UserMenuActivity.class);
                    intent.putExtra(MESSAGE_KEY, nickName);

                    showAlert(successMessage, true, intent);

                    //TODO: GET user nickname from SERVER to pass to next window

                }
                else
                {
                    String serverMessage = response.getString("message");

                    showAlert(serverMessage, false, intent);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void showAlert(String message, final Boolean MoveToNextAct, final Intent intent)
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
                    ProgressBar.getProgressBar(context);
                    goToNextActivity(true, intent);
                }
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void goToNextActivity(Boolean canGoNext, Intent intent)
    {
        //Intent intent = new Intent(this, UserMenuActivity.class);
        if (canGoNext)
        {

            startActivity(intent);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void connectButtonClickHandler(View view) {
        //Snackbar.make(coordinatorLayout, "You selected Sign In", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        Toast.makeText(LoginActivity.this, "You selected sign in", Toast.LENGTH_LONG).show();

    }

    public void forgetPasswordButtonClickHandler(View view)
    {
        addEditText();
    }

    private void addEditText()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        final EditText editText = new EditText(this);
        alertDialog.setMessage("Enter Your Email for reset password");
        alertDialog.setView(editText);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String email = editText.getText().toString();
                sendEmailToServer(email);
            }
        });
        alertDialog.show();


    }

    private void sendEmailToServer(String email)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        HashMap<String,String> mapToSend = new HashMap<>();
        mapToSend.put("email", email);

        JSONObject forgetPassword  = ServerConnector.getInstance().createJSonToServer(mapToSend);
        String passwordRecovery = ServerConnector.getInstance().sendRequestToServer(forgetPassword, ServerConnector.RequestType.ForgotPassword);

        try
        {
            JSONObject response = new JSONObject(passwordRecovery);
            if (response.has("passwordRecovery"))
            {
                String password = response.getString("passwordRecovery");
                sendEmail(email, password);
                builder.setTitle("Email Sent Successfully!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
            else
            {
                builder.setTitle("Email is Not exist!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
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
    }

    protected void sendEmail(String email, String password) {
        try
        {
            propertyReader = new PropertyReader(getBaseContext());
            final String wearitappMail = propertyReader.getProperties().getProperty("stringWearitAppMail");
            final String wearitApppassword = propertyReader.getProperties().getProperty("stringWearitAppPassword");
            GmailSender sender = new GmailSender(wearitappMail, wearitApppassword);
            sender.sendMail("Your Recovery Password",
                    "Your Password is: " + " "+ password,
                    "wearitapp2018@gmail.com",
                    email);
        } catch (Exception e)
        {
            Toast toast = Toast.makeText(this, "Error Sending Mail", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

