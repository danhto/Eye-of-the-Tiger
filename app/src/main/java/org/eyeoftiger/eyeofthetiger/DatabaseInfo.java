package org.eyeoftiger.eyeofthetiger;

import android.content.Context;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.cloudant.sync.datastore.BasicDocumentRevision;
import com.cloudant.sync.datastore.Datastore;
import com.cloudant.sync.datastore.DatastoreManager;
import com.cloudant.sync.datastore.DatastoreNotCreatedException;
import com.cloudant.sync.datastore.DocumentNotFoundException;
import com.cloudant.sync.notifications.ReplicationCompleted;
import com.cloudant.sync.notifications.ReplicationErrored;
import com.cloudant.sync.replication.ErrorInfo;
import com.cloudant.sync.replication.Replicator;
import com.cloudant.sync.replication.ReplicatorBuilder;
import com.google.common.eventbus.Subscribe;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

//import android.view.Gravity;
//import android.widget.TextView;


/**
 * Created by DanLaptop on 11/10/2015.
 */
public class DatabaseInfo
{

    private ArrayList<Map<String, String>> unsortedData = new ArrayList<Map<String, String>>();
    private ArrayList<String[]> dataByLastName = new ArrayList<String[]>();
    private ArrayList<String[]> dataByFirstName = new ArrayList<String[]>();
    private ArrayList<String[]> dataByNumOfLates = new ArrayList<String[]>();
    private ArrayList<String[]> dataByNumOfAbsents = new ArrayList<String[]>();
    private ArrayList<String[]> dataByStatus = new ArrayList<String[]>();
    private Context applicationContext;
    private TableLayout activeTable;

    public DatabaseInfo(Context context) throws DocumentNotFoundException
    {
        applicationContext = context;
        unsortedData = retrieveRawData();

    }

    public ArrayList<Map<String, String>> getData()
    {
        return unsortedData;
    }

    private ArrayList<Map<String, String>> retrieveRawData() throws DocumentNotFoundException
    {

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
            uri = new URI("https://" + databaseKey + ":" + databasePassword + "@eyeofthetiger.cloudant.com/" + databaseName[1]);
            uri2 = new URI("https://" + databaseKey + ":" + databasePassword + "@eyeofthetiger.cloudant.com/" + databaseName[0]);
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

        // Begin replication for both datastores and wait until they are both complete
        try
        {

            CountDownLatch latch = new CountDownLatch(1);
            Listener listener = new Listener(latch);
            replicator.getEventBus().register(listener);
            replicator.start();
            latch.await();
            replicator.getEventBus().unregister(listener);

            latch = new CountDownLatch(1);
            listener = new Listener(latch);
            replicator2.getEventBus().register(listener);
            replicator2.start();
            latch.await();
            replicator2.getEventBus().unregister(listener);

        }
        catch (InterruptedException e)
        {
            System.err.print(e);
        }


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

            //Place current document id into map
            docMap.put("id", id);

            //Combine two documents maps into one
            for (String key : docMap2.keySet())
            {
                docMap.put(key, docMap2.get(key));

            }

            tmpList.add(docMap);
        }

        return tmpList;
    }

    static ArrayList<Map<String, String>> retrieveRawAdminData(Context appContext) throws DocumentNotFoundException
    {

        ArrayList<Map<String, String>> tmpList = new ArrayList<>();

        // Create a DatastoreManager using application internal storage path
        File path = appContext.getDir("datastores", Context.MODE_PRIVATE);
        DatastoreManager manager = new DatastoreManager(path.getAbsolutePath());

        Datastore ds = null;

        //Open the local datastore
        try
        {
            ds = manager.openDatastore("admins");
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

        try
        {
            uri = new URI("https://" + databaseKey + ":" + databasePassword + "@eyeofthetiger.cloudant.com/" + databaseName[2]);
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        //An array that holds all the field names in the database
        String keys[] = {"admin_last_name", "admin_first_name"};

        // Replicate from the remote to local database
        Replicator replicator = ReplicatorBuilder.pull().from(uri).to(ds).build();

        // Begin replication of data and waits for it to complete
        try
        {
            CountDownLatch latch = new CountDownLatch(1);
            Listener listener = new Listener(latch);
            replicator.getEventBus().register(listener);
            replicator.start();
            latch.await();
            replicator.getEventBus().unregister(listener);
        }
        catch (InterruptedException e)
        {
        }

        for (int i = 0; i < ds.getDocumentCount(); i++)
        {

            //Get _id of current document
            String id = ds.getAllDocumentIds().get(i);

            //Gets the json object of the document
            BasicDocumentRevision doc = ds.getDocument(id);

            Map<String, String> adminUser = new HashMap<>();

            //Parse json document into a map of fields -> values
            Map<String, String> docMap = parseJsonDoc(doc.getBody().toString());
            if (docMap.isEmpty())
            {
                continue;
            }

            for (String key : keys)
            {
                if (docMap.containsKey(key))
                {
                    adminUser.put(key, docMap.get(key));
                }
            }

            if (!adminUser.isEmpty())
            {
                tmpList.add(adminUser);
            }
        }

        /*try {
            ds.close();
            manager.deleteDatastore("admins");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return tmpList;
    }

    private static Map<String, String> parseJsonDoc(String body)
    {
        //i think theres a built-in JSON parser for android but whatever, whatever yourself bro! Come get some Charlie Murphy!
        if (!body.isEmpty())
        {
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
            for (String pair : docValues)
            {
                String keyVal[] = pair.split(":");

                if (keyVal.length > 1)
                {
                    dataMap.put(keyVal[0], keyVal[1]);
                }
            }

            return dataMap;
        }
        else
        {
            return null;
        }
    }

    public ArrayList<TableRow> getUnsortedData()
    {
        return null;
    }

    /**
     * A {@code ReplicationListener} that sets a latch when it's told the
     * replication has finished.
     */
    private static class Listener
    {

        private final CountDownLatch latch;
        public ErrorInfo error = null;
        public int documentsReplicated;
        public int batchesReplicated;

        Listener(CountDownLatch latch)
        {
            this.latch = latch;
        }

        @Subscribe
        public void complete(ReplicationCompleted event)
        {
            this.documentsReplicated = event.documentsReplicated;
            this.batchesReplicated = event.batchesReplicated;
            latch.countDown();
        }

        @Subscribe
        public void error(ReplicationErrored event)
        {
            this.error = event.errorInfo;
            latch.countDown();
        }
    }
}
