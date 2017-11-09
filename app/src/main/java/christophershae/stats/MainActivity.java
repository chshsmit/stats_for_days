package christophershae.stats;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static christophershae.stats.R.id.playerName;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {


    public String userId;
    public User user;
    private DatabaseReference mFireBaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private MyAdapter aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aList = new ArrayList<ListElement>();                          //This chunk of code sets up the adpater for the list view
        aa = new MyAdapter(this, R.layout.my_teams_list_element, aList);
        ListView myListView =(ListView) findViewById(R.id.myCurrentTeams);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();

        Bundle extras = getIntent().getExtras();                         //Retrieve all the extras from the intent
        userId = extras.getString("userId");
        System.out.println(userId);

        //Current user that is logged in id

        mFirebaseInstance = FirebaseDatabase.getInstance();

        //Get reference to user nodes
        mFireBaseDatabase = mFirebaseInstance.getReference("users");
        System.out.println("Its skipping this");

        mFireBaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("THIS IS FROM THE DATABASE");
                // System.out.println(dataSnapshot.child(userId).getValue(User.class).email);

                //System.out.println(dataSnapshot.child(userId).child("userRosters").getKey());


                user = dataSnapshot.child(userId).getValue(User.class);
                addTeamsToListView();
                System.out.println("This is from the user class from the database");
                System.out.println(user.email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //addTeamsToListView();
    }




    private class ListElement {
        ListElement() {}

        ListElement(String tl) {
            textLabel = tl;
        }

        public String textLabel;
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

            ListElement teamListElement = getItem(position);

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
            TextView tv = (TextView) newView.findViewById(R.id.teamName);
            tv.setText(teamListElement.textLabel);


            // Set a listener for the whole list item.
            newView.setTag(teamListElement.textLabel);
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


    public void addTeamsToListView(){
        for(String teamname: user.userRosters.keySet()){
            aList.add(new ListElement(teamname));
            aa.notifyDataSetChanged();
        }
    }


    public void createNewRosterActivity(View v){
        Intent changeToCreateNewRoster = new Intent(getApplicationContext(), CreateRoster.class);
        changeToCreateNewRoster.putExtra("userId", userId);
        changeToCreateNewRoster.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(changeToCreateNewRoster);
    }
}
