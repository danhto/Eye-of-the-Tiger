package org.eyeoftiger.eyeofthetiger;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by DanLaptop on 11/10/2015.
 */
public class DatabaseInfo
{

    private ArrayList<TableRow> unsortedData = new ArrayList<TableRow>();
    private ArrayList<String[]> dataByLastName = new ArrayList<String[]>();
    private ArrayList<String[]> dataByFirstName = new ArrayList<String[]>();
    private ArrayList<String[]> dataByNumOfLates = new ArrayList<String[]>();
    private ArrayList<String[]> dataByNumOfAbsents = new ArrayList<String[]>();
    private ArrayList<String[]> dataByStatus = new ArrayList<String[]>();
    private Context applicationContext;
    private TableLayout activeTable;

    String fakeData[] = {"01, Lico, April, Late",
            "02, Something, Teepan, Late",
            "03, Pompalmousse, Nicholas, Absent",
            "04, Everyone, Else, Present"};

    public DatabaseInfo(Context context, TableLayout table)
    {

        applicationContext = context;
        activeTable = table;
        // Get info from cloudant here
        unsortedData = retrieveRawData();

        //dataByLastName = sortDataByLastName();
    }

    private ArrayList<TableRow> retrieveRawData()
    {

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.setMargins(4, 1, 4, 1);
        ArrayList<TableRow> tmpArray = new ArrayList<TableRow>();


        //Read from cloud database
        for (int i = 0; i < fakeData.length; i++)
        {
            String userData[] = fakeData[i].split(",");
            TableRow tmpRow = new TableRow(applicationContext);

            for (int k = 0; k < userData.length; k++)
            {
                TextView tv = new TextView(applicationContext);
                tv.setLayoutParams(lp);
                tv.setText(userData[k]);
                tv.setBackgroundResource(R.drawable.cell_shape);

                tmpRow.addView(tv);
            }

            tmpArray.add(tmpRow);
        }

        return tmpArray;
    }

    public ArrayList<TableRow> getUnsortedData()
    {
        return unsortedData;
    }

}
