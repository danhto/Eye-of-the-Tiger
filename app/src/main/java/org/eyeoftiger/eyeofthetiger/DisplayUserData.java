package org.eyeoftiger.eyeofthetiger;

//import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/*
import com.cloudant.sync.datastore.BasicDocumentRevision;
import com.cloudant.sync.datastore.DatastoreManager;
import com.cloudant.sync.datastore.Datastore;
import com.cloudant.sync.datastore.DatastoreNotCreatedException;
import com.cloudant.sync.datastore.DocumentBodyFactory;
import com.cloudant.sync.datastore.DocumentException;
import com.cloudant.sync.datastore.DocumentNotFoundException;
import com.cloudant.sync.datastore.MutableDocumentRevision;
import com.cloudant.sync.replication.Replicator;
import com.cloudant.sync.datastore.UnsavedFileAttachment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudant.sync.replication.ReplicatorBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
*/

import java.util.Map;
import java.util.ArrayList;


public class DisplayUserData extends AppCompatActivity
{

    final int NUM_OF_FIELDS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarContent);
        setSupportActionBar(toolbar);
        TableLayout tb = (TableLayout) findViewById(R.id.displayTable);

        //Adds swipe functionality to whole secondary screen
        CoordinatorLayout secondaryview = (CoordinatorLayout) findViewById(R.id.secondaryview);
        secondaryview.setOnTouchListener(new OnSwipeTouchListener(this)
        {
            public void onSwipeTop()
            {

            }

            public void onSwipeRight()
            {

            }

            public void onSwipeLeft()
            {
                //go to home page
                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                nextScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createColumnHeadings();
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
        // home page menu
        else if (id == R.id.home_page)
        {
            Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
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
            Uri uriUrl = Uri.parse("http://www.eyeoftiger.org/");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }

        return super.onOptionsItemSelected(item);
    }

    public void createColumnHeadings()
    {
        int headingTextSize = 16;

        //Get table layout defined in content_main.xml
        TableLayout table = (TableLayout) findViewById(R.id.displayTable);

        //Create a table row object that can be inserted into the table layout
        TableRow tr = new TableRow(this);

        //Define parameters of table layout
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.setMargins(4, 1, 4, 1);

        //TextView is an object that lets you put text on the interface
        TextView USER_ID = new TextView(this);
        USER_ID.setTextSize(headingTextSize);

        //Set parameters of the textview
        USER_ID.setLayoutParams(lp);

        //Set the text that appears in textview
        USER_ID.setText("USER_ID");

        //This calls a drawable class that creates the cells in the table
        USER_ID.setBackgroundResource(R.drawable.cell_shape);

        //This sets the alignment of the text in the cells
        USER_ID.setGravity(Gravity.CENTER);

        TextView LAST_NAME = new TextView(this);
        LAST_NAME.setTextSize(headingTextSize);
        LAST_NAME.setLayoutParams(lp);
        LAST_NAME.setText("LAST_NAME");
        LAST_NAME.setBackgroundResource(R.drawable.cell_shape);
        LAST_NAME.setGravity(Gravity.CENTER);

        TextView FIRST_NAME = new TextView(this);
        FIRST_NAME.setTextSize(headingTextSize);
        FIRST_NAME.setLayoutParams(lp);
        FIRST_NAME.setText("FIRST_NAME");
        FIRST_NAME.setBackgroundResource(R.drawable.cell_shape);
        FIRST_NAME.setGravity(Gravity.CENTER);

        TextView STATUS = new TextView(this);
        STATUS.setTextSize(headingTextSize);
        STATUS.setLayoutParams(lp);
        STATUS.setText("STATUS");
        STATUS.setBackgroundResource(R.drawable.cell_shape);
        STATUS.setGravity(Gravity.CENTER);

        //Each textview represents a column and is added in order to a table row object
        tr.addView(USER_ID);
        tr.addView(LAST_NAME);
        tr.addView(FIRST_NAME);
        tr.addView(STATUS);

        //Table row object is then added to the table layout
        table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        //This method fills in the rest of the table
        fillDisplay(table);

    }

    public void fillDisplay(TableLayout table)
    {
        int rowTextSize = 14;
        //Call the table object
        TableLayout tab = (TableLayout) findViewById(R.id.displayTable);
        final TextView tv = new TextView(this);

        //An array that holds all the field names in the database
        String keys[] = {"user_last_name", "user_first_name", "user_status"};

        TableRow tb = null;
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.setMargins(4, 1, 4, 1);

        ArrayList<Map<String, String>> dbData = MainActivity.dbData.getData();

        for (int i = 0; i < dbData.size(); i++)
        {
            //Create textview that will hold parsed data
            TextView tmpId = new TextView(this);
            tmpId.setTextSize(rowTextSize);
            tmpId.setLayoutParams(lp);

            //Create table row object that will hold row data
            tb = new TableRow(this);
            tb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("CLICKABLE!!");
                }
            });

            //Get a single document from data
            Map<String, String> currentDoc = dbData.get(i);

            //Get current documents id
            String id = currentDoc.get("id");

            //Set current document id into a textview and add it into tale row as first column
            tmpId.setText(id);
            tb.addView(tmpId);

            //Do the same for the rest of the fields and values in the document
            for (String key : keys)
            {

                TextView tmpTv = new TextView(this);
                tmpTv.setTextSize(rowTextSize);
                tmpTv.setLayoutParams(lp);

                String value = currentDoc.get(key);

                if (!value.isEmpty())
                {
                    tmpTv.setText(value);
                    tb.addView(tmpTv);
                }
                else
                {
                    tmpTv.setText("ERROR");
                    tb.addView(tmpTv);
                }
            }

            //Set the parameters of the table row
            tb.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            //Add the whole row with all document data to the table
            table.addView(tb);
        }
    }


}
