package christophershae.stats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.R.attr.name;
import static android.R.id.edit;
import static java.security.AccessController.getContext;

public class CreateRoster extends AppCompatActivity {


    //------------------------------------------------------------------------------------------------
    //This is just the on create method
    //------------------------------------------------------------------------------------------------

    private DatabaseReference mFireBaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    public String userId;
    public User user;

    public Map<String, List<BasketballPlayer>> userRosters = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_roster);


        Bundle extras = getIntent().getExtras();                         //Retrieve all the extras from the intent
        userId = extras.getString("userId");                             //Current user that is logged in id


        //System.out.println(user.email);
        System.out.println(userId);


        aList = new ArrayList<ListElement>();                          //This chunk of code sets up the adpater for the list view
        aa = new MyAdapter(this, R.layout.list_element, aList);
        ListView myListView =(ListView) findViewById(R.id.listView);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();


        mFirebaseInstance = FirebaseDatabase.getInstance();

        //Get reference to user nodes
        mFireBaseDatabase = mFirebaseInstance.getReference("users");

        mFireBaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("THIS IS FROM THE DATABASE");
               // System.out.println(dataSnapshot.child(userId).getValue(User.class).email);

                System.out.println(dataSnapshot.child(userId).child("userRosters").getKey());


                user = dataSnapshot.child(userId).getValue(User.class);
                System.out.println("This is from the user class from the database");
                System.out.println(user.email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onBackPressed(){
        Intent changingActivities = new Intent(getApplicationContext(),MainActivity.class);
        changingActivities.putExtra("userId", userId);
        changingActivities.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(changingActivities);

    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        setContentView(R.layout.activity_create_roster);
//        ListView myListView = (ListView) findViewById(R.id.listView);
//        myListView.setAdapter(aa);
//        aa.notifyDataSetChanged();
//
//    }



    //------------------------------------------------------------------------------------------------
    //This code handles the generation of the list view
    //------------------------------------------------------------------------------------------------

    private class ListElement {
        ListElement() {}

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


    //------------------------------------------------------------------------------------------------
    //This is where we are able to set the quarter time
    //------------------------------------------------------------------------------------------------

    String playerName;
    ArrayList<String> names = new ArrayList<String>();
    private MyAdapter aa;

    CharSequence[] levelsOfPlay = {"High School", "College", "Professional"};
    public int quarterTime = 2;
    public boolean isCollege = false;

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
                //Set the time to the selected level
                switch(((AlertDialog)dialog).getListView().getCheckedItemPosition()){
                    case 0:
                        quarterTime = 480;
                        break;

                    case 1:
                        quarterTime = 2;
                        isCollege = true;
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




    //------------------------------------------------------------------------------------------------
    //This code handles adding players to the list view
    //------------------------------------------------------------------------------------------------

    List<CharSequence> charSequences = new ArrayList<>();
    List<BasketballPlayer> oldPlayerNames = new ArrayList<>();
    String chosenTeamName;
    //CharSequence[] previousRosterTitles;



    public void createPlayer(View v){
        EditText playerEntry = (EditText) findViewById(R.id.enterPlayerName);
        playerName = playerEntry.getText().toString();
        names.add(playerName);
        aList.add(new ListElement(playerName, "Delete"));
        aa.notifyDataSetChanged();
        //myRoster.add(newPlayer);
        playerEntry.setText("");
    }



    public void generateNamesFromOldRoster(View v){
        System.out.println("This function has executed");

        System.out.println(user.email);

//        for(String key: user.userRosters.keySet()){
//            System.out.println(key);
//        }

        //Retrieve Names of Previous rosters
        for(String key: user.userRosters.keySet()){
            charSequences.add(key);
        }

        final CharSequence[] previousRosterTitles = charSequences.toArray(new CharSequence[charSequences.size()]);


        AlertDialog.Builder builder = new AlertDialog.Builder(CreateRoster.this);     //Instantiate the popup that shows the list

        // Set the dialog title
        builder.setTitle("Choose which roster to generate names from");

        builder.setSingleChoiceItems(previousRosterTitles,0, null);
        // Set the action buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //User clicked "OK"
                chosenTeamName = (String) previousRosterTitles[((AlertDialog)dialog).getListView().getCheckedItemPosition()];
                oldPlayerNames = user.userRosters.get(chosenTeamName);

                for(BasketballPlayer player: oldPlayerNames){
                    System.out.println(player.playerName);
                    names.add(player.playerName);
                    aList.add(new ListElement(player.playerName, "Delete"));
                    aa.notifyDataSetChanged();
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

        System.out.println("This is from the char sequence");
        for(CharSequence cs : previousRosterTitles){
            System.out.println(cs);
        }

        charSequences.clear();


    }



    //------------------------------------------------------------------------------------------------
    //This code handles changing activities
    //------------------------------------------------------------------------------------------------


    public void changeToStats(View v){
        Intent changingActivities = new Intent(getApplicationContext(), TableActivity.class);
        changingActivities.putStringArrayListExtra("roster", names);
        changingActivities.putExtra("userId", userId);
        changingActivities.putExtra("quarter_length", quarterTime);
        changingActivities.putExtra("isCollege", isCollege);
        changingActivities.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(changingActivities);
    }

}
