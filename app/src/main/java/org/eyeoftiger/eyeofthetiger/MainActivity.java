package org.eyeoftiger.eyeofthetiger;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.net.Uri;
import android.content.Intent;
import android.view.View;

import com.cloudant.sync.datastore.DocumentNotFoundException;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

import static android.graphics.Color.BLUE;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get database data
        try
        {
            dbData = new DatabaseInfo(getApplicationContext());
        }
        catch (DocumentNotFoundException e)
        {
            e.printStackTrace();
        }

        //Count students
        int present = 0;

        for (int i = 0; i < dbData.getData().size(); i++)
        {

            if (dbData.getData().get(i).get("user_status").toLowerCase().equals("present"))
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

        try
        {
            DatabaseInfo db = new DatabaseInfo(getApplicationContext());
        }
        catch (DocumentNotFoundException e)
        {
            e.printStackTrace();
        }

        //Add swipe functionality to whole primary screen
        TableLayout mainview = (TableLayout) findViewById(R.id.mainview);
        mainview.setOnTouchListener(new OnSwipeTouchListener(this)
        {
            public void onSwipeTop()
            {

            }

            public void onSwipeRight()
            {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), DisplayUserData.class);
                startActivity(nextScreen);
            }

            public void onSwipeLeft()
            {

            }

            public void onSwipeBottom()
            {

            }

            public boolean onTouch(View v, MotionEvent event)
            {
                return gestureDetector.onTouchEvent(event);
            }
        });

        // get websiteButton id
        websiteButton = (Button) findViewById(R.id.websitebutton);
        websiteButton.setOnClickListener(new View.OnClickListener()
        {
            // go to website when button clicked
            public void onClick(View view)
            {
                goToWebsite(view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // link for team website
    public void goToWebsite(View view)
    {
        goToUrl("http://www.eyeoftiger.org/");
    }

    // launch web browser to website
    private void goToUrl(String url)
    {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}
