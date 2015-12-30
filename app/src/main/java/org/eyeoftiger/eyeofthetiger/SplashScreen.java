package org.eyeoftiger.eyeofthetiger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by simon on 20-Nov-2015.
 */

// set up the splash screen before app starts
public class SplashScreen extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread timerThread = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(1500);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }

}