package org.eyeoftiger.eyeofthetiger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class UserDetailsActivity extends AppCompatActivity
{

    private ArrayList<Map<String, String>> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userData = MainActivity.dbData.getData();
        TextView selectedUser = (TextView) findViewById(R.id.selected_name);
        selectedUser.setText(DisplayUserData.selectedUser);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillDetailDisplay();
    }

    private void fillDetailDisplay() {

        int rowTextSize = 14;
        //Call the table object
        TableLayout tab = (TableLayout) findViewById(R.id.userDetailsTable);
        //LinearLayout lin = (LinearLayout) findViewById(R.id.userDetailsTimeTableInfo);
        final TextView tv = new TextView(this);

        //An array that holds all the field names in the database
        String keys[] = {"user_number_of_absences", "user_number_of_lates", "user_timetable"};

        TableRow tb = null;
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.setMargins(4, 1, 4, 1);

        String userId = DisplayUserData.selectedUser.replaceAll(" ", "").trim();
        Map<String, String> userDataMap = null;

        //Search array of maps for map containing specific user data based on selected user
        for (Map<String, String> userMap : userData) {

            if(userMap.get("id").equals(userId)) {
                userDataMap = userMap;
                break;
            }
        }

        //Initialize table row object
        tb = new TableRow(this);

        //Add column headers for total absences and lates
        TextView totalAbsentColumn = new TextView(this);
        totalAbsentColumn.setTextSize(rowTextSize);
        totalAbsentColumn.setLayoutParams(lp);
        totalAbsentColumn.setText("Total times absent");
        totalAbsentColumn.setBackgroundResource(R.drawable.cell_shape);
        totalAbsentColumn.setGravity(Gravity.CENTER);

        TextView totalLateColumn = new TextView(this);
        totalLateColumn.setTextSize(rowTextSize);
        totalLateColumn.setLayoutParams(lp);
        totalLateColumn.setText("Total times late");
        totalLateColumn.setBackgroundResource(R.drawable.cell_shape);
        totalLateColumn.setGravity(Gravity.CENTER);

        //Add headers to a table row and add row to table layout
        tb.addView(totalAbsentColumn);
        tb.addView(totalLateColumn);
        tab.addView(tb);

        //Create table row object that will hold row data
        tb = new TableRow(this);

        for (String field: keys)
        {
            //Create textview that will hold parsed data
            TextView tmpId = new TextView(this);
            tmpId.setTextSize(rowTextSize);
            tmpId.setLayoutParams(lp);
            tmpId.setGravity(Gravity.CENTER);


            switch (field) {
                case "user_number_of_absences":
                    tmpId.setText(userDataMap.get(field));
                    tb.addView(tmpId);
                    break;
                case "user_number_of_lates":
                    tmpId.setText(userDataMap.get(field));
                    tb.addView(tmpId);
                    tab.addView(tb);
                    break;
                case "user_timetable":
                    tb = new TableRow(this);
                    TableRow.LayoutParams timelp = new TableRow.LayoutParams();
                    timelp.span = 2;
                    timelp.weight = 1;
                    TextView timeHeader = new TextView(this);
                    timeHeader.setTextSize(rowTextSize);
                    timeHeader.setLayoutParams(timelp);
                    timeHeader.setText("Timetable");
                    timeHeader.setBackgroundResource(R.drawable.cell_shape);
                    timeHeader.setGravity(Gravity.CENTER);
                    tb.setLayoutParams(timelp);
                    //lin.addView(timeHeader);
                    tb.addView(timeHeader);
                    tab.addView(tb);

                    String timeTable[] = userDataMap.get(field).split("/");
                    int currentPeriod = 1;

                    for (String course: timeTable) {
                        tb = new TableRow(this);
                        tb.setLayoutParams(lp);

                        TextView period = new TextView(this);
                        TextView crs = new TextView(this);
                        //LinearLayout row = new LinearLayout(getApplicationContext());
                        //row.setOrientation(LinearLayout.HORIZONTAL);
                        period.setTextSize(rowTextSize);
                        period.setGravity(Gravity.CENTER);
                        crs.setTextSize(rowTextSize);
                        crs.setGravity(Gravity.CENTER);

                        period.setText("Period " + currentPeriod + ":");
                        crs.setText(course);

                        tb.addView(period);
                        tb.addView(crs);
                        tab.addView(tb);
                        //row.addView(period);
                        //row.addView(crs);
                        //lin.addView(row);

                        currentPeriod++;
                    }

                    break;
            }




        }
    }


}
