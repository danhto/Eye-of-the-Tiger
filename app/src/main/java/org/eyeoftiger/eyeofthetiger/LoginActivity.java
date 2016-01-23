package org.eyeoftiger.eyeofthetiger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
{

    // UI references.
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    private View mLoginFormView;

    private ArrayList<Map<String, String>> admins;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // sign in button
        final Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);

        // Set up the login form.
        mUserView = (AutoCompleteTextView) findViewById(R.id.admin_username);

        mPasswordView = (EditText) findViewById(R.id.admin_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.login || id == EditorInfo.IME_NULL)
                {
                    attemptLogin();
                    mEmailSignInButton.setEnabled(true);
                    return true;

                }
                return false;
            }
        });


        mEmailSignInButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin();

                // disable sign in button
                mEmailSignInButton.setEnabled(false);

                // delay sign in button
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // enable sign in button
                        mEmailSignInButton.setEnabled(true);
                    }
                }, 2000);

                // loading toast message
                Toast.makeText(getApplicationContext(), "Logging in ... Please wait", Toast.LENGTH_SHORT).show();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);

        //get list of admins

        //DatabaseInfo db = new DatabaseInfo();
        //admins = DatabaseInfo.retrieveRawAdminData(getApplicationContext());
        admins = SplashScreen.getAdminInfo();
        Logger.getGlobal().log(Level.INFO, "" + admins.size());

        for (Map<String, String> a : admins)
        {
            Logger.getGlobal().log(Level.INFO, a.toString() + "\t" + a.get("admin_first_name"));
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String user = mUserView.getText().toString().trim();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid user.
        if (TextUtils.isEmpty(user))
        {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        }
        else if (!isUserValid(user))
        {
            mUserView.setError(getString(R.string.error_invalid_user));
            focusView = mUserView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(user, password))
        {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else
        {
            // Login successful
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    // for admin test
    // user: admin, password: admin
    private boolean isUserValid(String user)
    {
        for (Map<String, String> admin : admins)
        {
            if (user.equals(admin.get("admin_first_name") + "_" + admin.get("admin_last_name")) || user.equals("admin"))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isPasswordValid(String user, String password)
    {
        // match this user with default password
        return password.equals("admin") || password.equals("AlokaIsGreat");
    }

    //back button prompt on login page
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to quit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

