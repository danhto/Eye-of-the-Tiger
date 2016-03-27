package org.eyeoftiger.eyeofthetiger;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import android.app.AlertDialog;
import android.widget.Toast;

import com.cloudant.sync.datastore.DocumentNotFoundException;

//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v4.view.MotionEventCompat;
//import android.util.Log;
//import android.view.Gravity;
//import android.widget.AbsListView;
//import android.widget.AbsoluteLayout;
//import android.widget.TableRow;
//import android.content.Intent;
//import android.view.View;

//import org.w3c.dom.Text;

//import java.util.ArrayList;
//import java.util.Map;

//import static android.graphics.Color.BLUE;

public class MainActivity extends AppCompatActivity
{
    // button to load the team website
    private Button websiteButton;
    public static DatabaseInfo dbData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////////////////

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /////////////////////

        //Get database data
        try
        {
            dbData = new DatabaseInfo(getApplicationContext());

            // If NPE is detected keep retreaving data until data is set
            while (dbData == null)
            {
                //dbData = new DatabaseInfo(getApplicationContext());
                DatabaseInfo db = new DatabaseInfo(getApplicationContext());
                db.run();
                dbData = db;
            }
        }
        catch (DocumentNotFoundException e)
        {
            System.err.println(e);
            e.printStackTrace();
        }

        updatePresentCount();

        //Add swipe functionality to whole primary screen
        TableLayout mainview = (TableLayout) findViewById(R.id.mainview);
        mainview.setOnTouchListener(new OnSwipeTouchListener(this)
        {
            public void onSwipeTop()
            {

            }

            public void onSwipeRight()
            {

            }

            public void onSwipeLeft()
            {
                //go to user data page
                Intent nextScreen = new Intent(getApplicationContext(), DisplayUserData.class);
                startActivity(nextScreen);
            }

            public void onSwipeBottom()
            {

            }

            public boolean onTouch(View v, MotionEvent event)
            {
                return gestureDetector.onTouchEvent(event);
            }
        });

    }

    // Resync db everytime main activity is resumed
    void OnResume() {
        super.onResume();

        try {
            //dbData = new DatabaseInfo(getApplicationContext());
            DatabaseInfo db = new DatabaseInfo(getApplicationContext());
            db.run();
            dbData = db;
        }
        catch (DocumentNotFoundException e) {
            System.err.println(e);
        }
    }

    private void updatePresentCount() {
        //Count students
        int present = 0;

        for (int i = 0; i < dbData.getData().size(); i++)
        {

            if (!((String)dbData.getData().get(i).get("user_status")).toUpperCase().equals("ABSENT"))
            {
                present++;
            }
        }

        //Update text views to show present and absent numbers
        TextView presentTv = (TextView) findViewById(R.id.present_number);
        presentTv.setText(String.format("%d", present));
        presentTv.setBackgroundResource(R.drawable.cell_shape_main);

        TextView absentTv = (TextView) findViewById(R.id.away_number);
        absentTv.setText(String.format("%d", Math.abs(present - dbData.getData().size())));
        absentTv.setBackgroundResource(R.drawable.cell_shape_main);
    }
    //back button prompt on home page
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to logout?");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // settings menu list
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        // settings menu
        if (id == R.id.action_settings)
        {
            return true;
        }
        // home page menu
        else if (id == R.id.home_page)
        {
            Intent nextScreen = new Intent(MainActivity.this, MainActivity.class);
            nextScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(nextScreen);
            return true;
        }
        // user data menu
        else if (id == R.id.title_activity_display_user_data)
        {
            Intent nextScreen = new Intent(getApplicationContext(), DisplayUserData.class);
            startActivity(nextScreen);
            return true;
        }
        // website menu
        else if (id == R.id.website_link)
        {
            goToUrl("http://www.eyeoftiger.org/");
        }
        //about page
        else if (id == R.id.about_page)
        {
            Intent nextScreen = new Intent(getApplicationContext(), About.class);
            startActivity(nextScreen);
            return true;
        }
        // logout menu
        else if (id == R.id.quit)
        {
            // logout toast message
            Toast.makeText(getApplicationContext(), "Logging Out ...", Toast.LENGTH_SHORT).show();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    // link for team website
    public void goToWebsite(View view)
    {
        goToUrl("http://www.eyeoftiger.org/");
    }

    // launch web browser to website
    public void goToUrl(String url)
    {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}
