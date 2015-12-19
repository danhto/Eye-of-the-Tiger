package org.eyeoftiger.eyeofthetiger;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cloudant.sync.datastore.BasicDocumentRevision;
import com.cloudant.sync.datastore.Datastore;
import com.cloudant.sync.datastore.DatastoreManager;
import com.cloudant.sync.datastore.DatastoreNotCreatedException;
import com.cloudant.sync.datastore.DocumentNotFoundException;
import com.cloudant.sync.replication.Replicator;
import com.cloudant.sync.replication.ReplicatorBuilder;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by DanLaptop on 11/10/2015.
 */
public class DatabaseInfo
{

    private  ArrayList<Map<String, String>> unsortedData = new ArrayList<Map<String, String>>();
    private ArrayList<String[]> dataByLastName = new ArrayList<String[]>();
    private ArrayList<String[]> dataByFirstName = new ArrayList<String[]>();
    private ArrayList<String[]> dataByNumOfLates = new ArrayList<String[]>();
    private ArrayList<String[]> dataByNumOfAbsents = new ArrayList<String[]>();
    private ArrayList<String[]> dataByStatus = new ArrayList<String[]>();
    private Context applicationContext;
    private TableLayout activeTable;

    public DatabaseInfo(Context context) throws DocumentNotFoundException {
        applicationContext = context;
        unsortedData = retrieveRawData();

    }

    public ArrayList<Map<String, String>> getData() {
        return unsortedData;
    }

    private ArrayList<Map<String, String>> retrieveRawData() throws DocumentNotFoundException {

        ArrayList<Map<String, String>> tmpList = new ArrayList<Map<String, String>>();

        // Create a DatastoreManager using application internal storage path
        File path = applicationContext.getDir("datastores", Context.MODE_PRIVATE);
        File path2 = applicationContext.getDir("datastores2", Context.MODE_PRIVATE);
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

        for (int i = 0; i < ds.getDocumentCount(); i++) {

            //Get _id of current document
            String id = ds.getAllDocumentIds().get(i).toString();

            //Gets the json object of the document
            BasicDocumentRevision doc = ds.getDocument(id);
            BasicDocumentRevision doc2 = ds2.getDocument(id);

            //Parse json document into a map of fields -> values
            Map<String, String> docMap = parseJsonDoc(doc.getBody().toString());
            Map<String, String> docMap2 = parseJsonDoc(doc2.getBody().toString());

            //Place current document id into map
            docMap.put("id", id);

            for (String key: docMap2.keySet()) {
                docMap.put(key, docMap2.get(key));

            }

            tmpList.add(docMap);
        }

        return tmpList;
    }

    private Map<String, String> parseJsonDoc(String body) {
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

                if (keyVal.length > 1) {
                    dataMap.put(keyVal[0], keyVal[1]);
                }
            }

            return dataMap;
        } else {
            return null;
        }
    }

    public ArrayList<TableRow> getUnsortedData()
    {
        return null;
    }

}
