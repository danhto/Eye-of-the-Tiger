package org.eyeoftiger.eyeofthetiger;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;

import static android.graphics.Color.BLUE;

public class MainActivity extends AppCompatActivity
{
    // button to load the team website
    private Button websiteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), DisplayUserData.class);
                startActivity(nextScreen);
            }
        });

        toolbar.setOnTouchListener(new OnSwipeTouchListener(this)
        {
            public void onSwipeTop()
            {

            }

            public void onSwipeRight()
            {

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

//    public void createColumnHeadings()
// {
//
//        TableLayout table =(TableLayout) findViewById(R.id.displayTable);
//        TableRow tr = new TableRow(this);
//        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
//        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(4, 1, 4, 1);
//
//        TextView USER_ID = new TextView(this);
//        USER_ID.setLayoutParams(lp);
//        USER_ID.setText("USER_ID");
//        USER_ID.setBackgroundResource(R.drawable.cell_shape);
//        USER_ID.setGravity(Gravity.CENTER);
//
//        TextView LAST_NAME = new TextView(this);
//        LAST_NAME.setLayoutParams(lp);
//        LAST_NAME.setText("LAST_NAME");
//        LAST_NAME.setBackgroundResource(R.drawable.cell_shape);
//        LAST_NAME.setGravity(Gravity.CENTER);
//
//        TextView FIRST_NAME = new TextView(this);
//        FIRST_NAME.setLayoutParams(lp);
//        FIRST_NAME.setText("FIRST_NAME");
//        FIRST_NAME.setBackgroundResource(R.drawable.cell_shape);
//        FIRST_NAME.setGravity(Gravity.CENTER);
//
//        TextView STATUS = new TextView(this);
//        STATUS.setLayoutParams(lp);
//        STATUS.setText("STATUS");
//        STATUS.setBackgroundResource(R.drawable.cell_shape);
//        STATUS.setGravity(Gravity.CENTER);
//
//        tr.addView(USER_ID);
//        tr.addView(LAST_NAME);
//        tr.addView(FIRST_NAME);
//        tr.addView(STATUS);
//
//        table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
//
//        fillDisplay(table);
//    }
//
//    public void fillDisplay(TableLayout table)
// {
//
//        DatabaseInfo db = new DatabaseInfo(this.getApplicationContext(), table);
//        ArrayList<TableRow> rowData = db.getUnsortedData();
//
//        for (int i = 0; i < rowData.size(); i++)
// {
//            table.addView(rowData.get(i));
//        }
//    }
}
