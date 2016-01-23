package org.eyeoftiger.eyeofthetiger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cloudant.sync.datastore.DocumentNotFoundException;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by simon on 20-Nov-2015.
 */

// set up the splash screen before app starts
public class SplashScreen extends Activity
{
    private static ArrayList<Map<String, String>> adminInfo;

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
                    // delay start up in milliseconds
                    //sleep(1);

                    adminInfo = DatabaseInfo.retrieveRawAdminData(getApplicationContext());
                }
                //catch (InterruptedException e)
                //{
                //    e.printStackTrace();
                //}
                catch (DocumentNotFoundException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    public static ArrayList<Map<String, String>> getAdminInfo()
    {
        return adminInfo;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }

}