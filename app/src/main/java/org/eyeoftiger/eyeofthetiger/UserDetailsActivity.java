package org.eyeoftiger.eyeofthetiger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class UserDetailsActivity extends AppCompatActivity
{

    private ArrayList<Map<String, String>> userData;
    private String selectedPersonID;
    static private boolean statusChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        statusChanged = false;
        userData = MainActivity.dbData.getData();
        TextView selectedUser = (TextView) findViewById(R.id.selected_name);
        selectedUser.setText(DisplayUserData.selectedUser);
        selectedPersonID = DisplayUserData.selectedUserID;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillDetailDisplay();
    }

    private void fillDetailDisplay()
    {

        int rowTextSize = 14;
        //Call the table object
        TableLayout tab = (TableLayout) findViewById(R.id.userDetailsTable);
        //LinearLayout lin = (LinearLayout) findViewById(R.id.userDetailsTimeTableInfo);
        final TextView tv = new TextView(this);

        //An array that holds all the field names in the database
        String keys[] = {"user_number_of_absences", "user_number_of_lates", "user_status", "user_timetable"};

        TableRow tb = null;
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.setMargins(4, 1, 4, 1);

        //String userId = DisplayUserData.selectedUser.replaceAll(" ", "").trim();
        String userId = DisplayUserData.selectedUserID;
        Map<String, String> userDataMap = null;

        //Search array of maps for map containing specific user data based on selected user
        for (Map<String, String> userMap : userData)
        {

            if (userMap.get("id").equals(userId))
            {
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

        TextView currentStatus = new TextView(this);
        currentStatus.setTextSize(rowTextSize);
        currentStatus.setLayoutParams(lp);
        currentStatus.setText("Current status");
        currentStatus.setBackgroundResource(R.drawable.cell_shape);
        currentStatus.setGravity(Gravity.CENTER);

        //Add headers to a table row and add row to table layout
        tb.addView(totalAbsentColumn);
        tb.addView(totalLateColumn);
        tb.addView(currentStatus);
        tab.addView(tb);

        //Create table row object that will hold row data
        tb = new TableRow(this);

        for (String field : keys)
        {
            //Create textview that will hold parsed data
            TextView tmpId = new TextView(this);
            tmpId.setTextSize(rowTextSize);
            tmpId.setLayoutParams(lp);
            tmpId.setGravity(Gravity.CENTER);


            switch (field)
            {
                // Adds appropriate columns and their data
                case "user_number_of_absences":
                    tmpId.setText(userDataMap.get(field));
                    tb.addView(tmpId);
                    break;
                case "user_number_of_lates":
                    tmpId.setText(userDataMap.get(field));
                    tb.addView(tmpId);
                    //tab.addView(tb);
                    break;
                case "user_status":
                    // Creates a combo box for statuses
                    Spinner spin = new Spinner(this);
                    String array[] = {"PRESENT", "LATE", "ABSENT"};
                    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
                    spin.setAdapter(adapter);

                    // Identifies and displays user's current status
                    final String userCurrentStatus = userDataMap.get("user_status").toUpperCase();

                    // Determine and set initial position of spinner
                    int spinnerPosition = 0;

                    if (!userCurrentStatus.equals(0))
                    {
                        spinnerPosition = adapter.getPosition(userCurrentStatus);
                    }
                    else
                    {
                        spinnerPosition = adapter.getPosition("ABSENT");
                    }
                    spin.setSelection(spinnerPosition);

                    // Method to be performed when a new status is selected
                    spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            //String status = ((Spinner) view).getAdapter().getItem(position).toString();
                            //String status = ((TextView)view).getText().toString();
                            String status = parent.getSelectedItem().toString();

                            // If status is changed write this change to the DB
                            if (!status.equals(userCurrentStatus))
                            {
                                // loading toast message
                                Toast.makeText(getApplicationContext(), "Status Updated", Toast.LENGTH_SHORT).show();

                                writeNewStatusToDB(status);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent)
                        {

                        }
                    });

                    // Add spinner to activity screen
                    tb.addView(spin);
                    tab.addView(tb);
                    break;
                // Display user time table data
                case "user_timetable":
                    // Set time table format properties
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
                    tb.addView(timeHeader);
                    tab.addView(tb);

                    // Read user timetable
                    String timeTable[] = userDataMap.get(field).split("/");
                    int currentPeriod = 1;

                    // Format timetable display by Period: Class_name
                    for (String course : timeTable)
                    {
                        tb = new TableRow(this);
                        tb.setLayoutParams(lp);

                        // Pair time table with periods
                        TextView period = new TextView(this);
                        TextView crs = new TextView(this);
                        period.setTextSize(rowTextSize);
                        period.setGravity(Gravity.CENTER);
                        crs.setTextSize(rowTextSize);
                        crs.setGravity(Gravity.CENTER);

                        period.setText("Period " + currentPeriod + ":");
                        crs.setText(course);

                        tb.addView(period);
                        tb.addView(crs);
                        tab.addView(tb);

                        currentPeriod++;
                    }

                    break;
            }

        }
    }

    // Writes newly selected status to DB
    private void writeNewStatusToDB(String status)
    {
        statusChanged = true;
        DatabaseInfo.setNewStatusData(this, selectedPersonID, status);
    }

    static public boolean getChangeStatus()
    {
        return statusChanged;
    }

}
