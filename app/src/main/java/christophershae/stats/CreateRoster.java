package christophershae.stats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.R.attr.name;
import static android.R.id.edit;
import static java.security.AccessController.getContext;

public class CreateRoster extends AppCompatActivity {

    private static final String LOG_TAG = "lv-ex";

    private class ListElement {
        ListElement() {};

        ListElement(String tl, String bl) {
            textLabel = tl;
            buttonLabel = bl;
        }

        public String textLabel;
        public String buttonLabel;
    }

    private ArrayList<ListElement> aList;

    private class MyAdapter extends ArrayAdapter<ListElement> {

        int resource;
        Context context;

        public MyAdapter(Context _context, int _resource, List<ListElement> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout newView;

            ListElement w = getItem(position);

            // Inflate a new view if necessary.
            if (convertView == null) {
                newView = new LinearLayout(getContext());
                LayoutInflater vi = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(resource,  newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            // Fills in the view.
            TextView tv = (TextView) newView.findViewById(R.id.itemText);
            Button b = (Button) newView.findViewById(R.id.itemButton);
            tv.setText(w.textLabel);
            b.setText(w.buttonLabel);

            // Sets a listener for the button, and a tag for the button as well.
            b.setTag(new Integer(position));
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Reacts to a button press.
                    // Gets the integer tag of the button.
                    String s = v.getTag().toString();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, s, duration);
                    toast.show();
                    // Let's remove the list item.
                    int i = Integer.parseInt(s);
                    aList.remove(i);
                    names.remove(i);
                    aa.notifyDataSetChanged();
                }
            });

            // Set a listener for the whole list item.
            newView.setTag(w.textLabel);
            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = v.getTag().toString();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, s, duration);
                    toast.show();
                }
            });

            return newView;
        }
    }



    String playerName;
    ArrayList<String> names = new ArrayList<String>();
    private MyAdapter aa;

    CharSequence[] levelsOfPlay = {"High School", "College", "Professional"};
    public int quarterTime = 480;

    public void setQuarterTime(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateRoster.this);     //Instantiate the popup that shows the list

        // Set the dialog title
        builder.setTitle("Select Level of Play");

        builder.setSingleChoiceItems(levelsOfPlay,0, null);
        // Set the action buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //User clicked "OK"
                //Sub the current active player with the selection made from the list
                switch(((AlertDialog)dialog).getListView().getCheckedItemPosition()){
                    case 0:
                        quarterTime = 480;
                        break;

                    case 1:
                        quarterTime = 600;
                        break;

                    case 2:
                        quarterTime = 720;
                        break;
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //The user pressed "CANCEL" so nothing needs to be done
            }
        });
        builder.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_roster);
        aList = new ArrayList<ListElement>();
        aa = new MyAdapter(this, R.layout.list_element, aList);
        ListView myListView =(ListView) findViewById(R.id.listView);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();
    }





    public void createPlayer(View v){
        EditText playerEntry = (EditText) findViewById(R.id.enterPlayerName);
        playerName = playerEntry.getText().toString();
        names.add(playerName);
        aList.add(new ListElement(playerName, "Delete"));
        aa.notifyDataSetChanged();
        //myRoster.add(newPlayer);
        playerEntry.setText("");
    }



    public void changeToStats(View v){
        Intent changingActivities = new Intent(getApplicationContext(), TableActivity.class);
        changingActivities.putStringArrayListExtra("roster", names);
        changingActivities.putExtra("quarter_length", quarterTime);
        changingActivities.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(changingActivities);
    }


}
