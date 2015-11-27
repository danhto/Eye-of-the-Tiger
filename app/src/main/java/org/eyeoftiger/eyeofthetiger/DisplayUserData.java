package org.eyeoftiger.eyeofthetiger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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





//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener()
// {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb.setOnTouchListener(new OnSwipeTouchListener(this)
        {

            public void onSwipeTop()
            {

            }

            public void onSwipeRight()
            {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
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

        return super.onOptionsItemSelected(item);
    }

    public void createColumnHeadings()
    {

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

        //Set parameters of the textview
        USER_ID.setLayoutParams(lp);

        //Set the text that appears in textview
        USER_ID.setText("USER_ID");

        //This calls a drawable class that creates the cells in the table
        USER_ID.setBackgroundResource(R.drawable.cell_shape);

        //This sets the alignment of the text in the cells
        USER_ID.setGravity(Gravity.CENTER);

        TextView LAST_NAME = new TextView(this);
        LAST_NAME.setLayoutParams(lp);
        LAST_NAME.setText("LAST_NAME");
        LAST_NAME.setBackgroundResource(R.drawable.cell_shape);
        LAST_NAME.setGravity(Gravity.CENTER);

        TextView FIRST_NAME = new TextView(this);
        FIRST_NAME.setLayoutParams(lp);
        FIRST_NAME.setText("FIRST_NAME");
        FIRST_NAME.setBackgroundResource(R.drawable.cell_shape);
        FIRST_NAME.setGravity(Gravity.CENTER);

        TextView STATUS = new TextView(this);
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

        //Call the table object
        TableLayout tab = (TableLayout) findViewById(R.id.displayTable);
        final TextView tv = new TextView(this);

        // Create a DatastoreManager using application internal storage path
        File path = getApplicationContext().getDir("datastores", Context.MODE_PRIVATE);
        File path2 = getApplicationContext().getDir("datastores2", Context.MODE_PRIVATE);
        DatastoreManager manager = new DatastoreManager(path.getAbsolutePath());
        DatastoreManager manager2 = new DatastoreManager(path2.getAbsolutePath());

        Datastore ds = null;
        Datastore ds2 = null;

        //Open the local datastore
        try
        {
            ds = manager.openDatastore("datastore");
            ds2 = manager.openDatastore("datastore2");
        }
        catch (DatastoreNotCreatedException e)
        {
            e.printStackTrace();
        }

        String databaseName[] = {"dynamic_user_info", "static_user_info", "administrator_info", "class_info"};
        String databaseKey = "hadjohneftemandstingunty";
        String databasePassword = "9494e46f4adc8778200304f821dc2bf54a9d05d5";
        //Call our online cloudant database changing the name at the end changes which database is called
        URI uri = null;
        URI uri2 = null;

        try
        {
            uri = new URI("https://"+databaseKey+":"+databasePassword+"@eyeofthetiger.cloudant.com/" + databaseName[1]);
            uri2 = new URI("https://"+databaseKey+":"+databasePassword+"@eyeofthetiger.cloudant.com/" + databaseName[0]);
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        //An array that holds all the field names in the database
        String keys[] = {"user_last_name", "user_first_name", "user_status"};

        // Replicate from the remote to local database
        Replicator replicator = ReplicatorBuilder.pull().from(uri).to(ds).build();
        Replicator replicator2 = ReplicatorBuilder.pull().from(uri2).to(ds2).build();

        // Fire-and-forget (there are easy ways to monitor the state too)
        replicator.start();
        replicator2.start();

        TableRow tb = null;

        try
        {
            for (int i = 0; i < ds.getDocumentCount(); i++)
            {

                //Get _id of current document
                String id = ds.getAllDocumentIds().get(i).toString();

                //Gets the json object of the document
                BasicDocumentRevision doc = ds.getDocument(id);
                BasicDocumentRevision doc2 = ds2.getDocument(id);

                //Parse json document into a map of fields -> values
                Map<String, String> docMap = parseJsonDoc(doc.getBody().toString());
                Map<String, String> docMap2 = parseJsonDoc(doc2.getBody().toString());

                //Create textview that will hold parsed data
                TextView tmpId = new TextView(this);

                //Create table row object that will hold row data
                tb = new TableRow(this);

                //Set current docment id into a textview and add it into tale row as first column
                tmpId.setText(id);
                tb.addView(tmpId);

                //Do the same for the rest of the fields and values in the document
                for (String key : keys)
                {

                    TextView tmpTv = new TextView(this);
                    String value = "";

                    if (key.equals("user_status")) {
                        value = docMap2.get(key);
                    }
                    else {
                        value = docMap.get(key);
                    }

                    if (!value.isEmpty()) {
                        tmpTv.setText(value);
                        tb.addView(tmpTv);
                    }
                    else {
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
        catch (DocumentNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    private Map<String, String> parseJsonDoc(String body)
    {
        if (!body.isEmpty()) {
            //Removes the } bracket from the json string
            String removeExtras = body.replaceAll("\\}", "");

            //remove all "'s from the json string
            removeExtras = removeExtras.replaceAll("\"", "");

            //Remove all { from the json string
            removeExtras = removeExtras.replaceAll("\\{", "");

            //Json string you now be field: value, field: value, ...

            //This is map object that will hold fields and values
            Map<String, String> dataMap = new TreeMap<String, String>();

            //Split the json string into field: value pairs
            String docValues[] = removeExtras.split(",");

            //For each field: value pair add it to the datamap with field as key and value as value
            for (String pair : docValues) {
                String keyVal[] = pair.split(":");
                dataMap.put(keyVal[0], keyVal[1]);
            }

            return dataMap;
        }
        else {
            return null;
        }
    }


}
