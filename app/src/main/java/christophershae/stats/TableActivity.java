package christophershae.stats;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.checkboxStyle;
import static android.R.attr.key;
import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class TableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> roster = extras.getStringArrayList("roster");
        createRoster(roster);

    }

    @Override
    public void onBackPressed(){


    }


    BasketballPlayer Player1 = new BasketballPlayer("Player 1");      //This guy is just a tester

    List<BasketballPlayer> myRoster = new ArrayList<BasketballPlayer>();
    List<BasketballPlayer> activeRoster = new ArrayList<BasketballPlayer>();
    ArrayList<String> roster = new ArrayList<String>();
    ArrayList mSelectedItems = new ArrayList();



    public class Adapter extends ArrayAdapter {
        Context mContext;
        int resourceID;
        ArrayList<String> names;
        public Adapter(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
            this.mContext = context;
            this.resourceID=resource;
            this.names= objects;
        }

        @Override
        public String getItem(int position) {
            return names.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(resourceID, parent, false);

            TextView text = (TextView) row.findViewById(R.id.playerName);

            text.setText(roster.get(position));
            return row;
        }

    }


    public void playerNameClicked(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);
        // Set the dialog title
        builder.setTitle("Roster");
        // Specify the list array, the items to be selected by default (null for none),
        // and the listener through which to receive callbacks when items are selected
        builder.setMultiChoiceItems(roster.toArray(new CharSequence[roster.size()]),null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            mSelectedItems.add(which);
                        } else if (mSelectedItems.contains(which)) {
                            // Else, if the item is already in the array, remove it
                            mSelectedItems.remove(which);
                        }
                    }
                });
        // Set the action buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK, so save the mSelectedItems results somewhere
                // or return them to the component that opened the dialog
                //...
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //...
            }
        });
        builder.show();
    }


    //----------------------------------------------------------------------------------------------------------------
    //Setting roster information
    //----------------------------------------------------------------------------------------------------------------

    //Creates the roster with the information from the intent
    public void createRoster(ArrayList<String> playerNames){
        for (String name : playerNames) {
            //To Implement
            BasketballPlayer nextPlayer = new BasketballPlayer(name);
            myRoster.add(nextPlayer);
            roster.add(name);
        }

        setActiveRoster();
        printPlayerNames();
        printActiveRoster();
    }

    //Sets the starting five on the court
    public void setActiveRoster(){
        for(int i=0; i<5; i++){
            activeRoster.add(i, myRoster.get(i));
        }
        TextView playerOneName = (TextView) findViewById(R.id.activePlayer1);
        playerOneName.setText(activeRoster.get(0).playerName);

        TextView playerTwoName = (TextView) findViewById(R.id.activePlayer2);
        playerTwoName.setText(activeRoster.get(1).playerName);

        TextView playerThreeName = (TextView) findViewById(R.id.activePlayer3);
        playerThreeName.setText(activeRoster.get(2).playerName);

        TextView playerFourName = (TextView) findViewById(R.id.activePlayer4);
        playerFourName.setText(activeRoster.get(3).playerName);

        TextView playerFiveName = (TextView) findViewById(R.id.activePlayer5);
        playerFiveName.setText(activeRoster.get(4).playerName);
    }


    //----------------------------------------------------------------------------------------------------------------
    //Setting stat information
    //----------------------------------------------------------------------------------------------------------------

    public void addStat(View v){
        String statKey = ((Button) v).getText().toString();

        int parentId = ((View) v.getParent().getParent()).getId();

        switch(parentId){
            case R.id.Player1:
                System.out.println("Player1");
                activeRoster.get(0).increaseStat(statKey);
                setStatsToBarPlayer1();
                break;

            case R.id.Player2:
                System.out.println("Player2");
                activeRoster.get(1).increaseStat(statKey);
                setStatsToBarPlayer2();
                break;

            case R.id.Player3:
                System.out.println("Player3");
                activeRoster.get(2).increaseStat(statKey);
                setStatsToBarPlayer3();
                break;

            case R.id.Player4:
                System.out.println("Player4");
                activeRoster.get(3).increaseStat(statKey);
                setStatsToBarPlayer4();
                break;

            case R.id.Player5:
                System.out.println("Player5");
                activeRoster.get(4).increaseStat(statKey);
                setStatsToBarPlayer5();
                break;

            default:
                System.out.println("You failed");
                break;
        }
    }



    //Displays stats to stat bar for each player
    public void setStatsToBarPlayer1(){
        TextView points = (TextView) findViewById(R.id.totPtsPlayer1);
        points.setText(Integer.toString(activeRoster.get(0).points));

        TextView rebounds = (TextView) findViewById(R.id.totReboundsPlayer1);
        rebounds.setText(Integer.toString(activeRoster.get(0).rebounds));

        TextView asssists = (TextView) findViewById(R.id.totAstPlayer1);
        asssists.setText(Integer.toString(activeRoster.get(0).assists));

        TextView steals  = (TextView) findViewById(R.id.totStlPlayer1);
        steals.setText(Integer.toString(activeRoster.get(0).steals));

        TextView blocks = (TextView) findViewById(R.id.totBlkPlayer1);
        blocks.setText(Integer.toString(activeRoster.get(0).blocks));

        TextView fieldGoals = (TextView) findViewById(R.id.totFgsPlayer1);
        fieldGoals.setText(Integer.toString(activeRoster.get(0).madeFg)+"-"+Integer.toString(activeRoster.get(0).attemptFg));

        TextView freeThrows = (TextView) findViewById(R.id.totFtsPlayer1);
        freeThrows.setText(Integer.toString(activeRoster.get(0).madeFt)+"-"+Integer.toString(activeRoster.get(0).attemptFt));
    }

    public void setStatsToBarPlayer2(){
        TextView points = (TextView) findViewById(R.id.totPtsPlayer2);
        points.setText(Integer.toString(activeRoster.get(1).points));

        TextView rebounds = (TextView) findViewById(R.id.totReboundsPlayer2);
        rebounds.setText(Integer.toString(activeRoster.get(1).rebounds));

        TextView asssists = (TextView) findViewById(R.id.totAstPlayer2);
        asssists.setText(Integer.toString(activeRoster.get(1).assists));

        TextView steals  = (TextView) findViewById(R.id.totStlPlayer2);
        steals.setText(Integer.toString(activeRoster.get(1).steals));

        TextView blocks = (TextView) findViewById(R.id.totBlkPlayer2);
        blocks.setText(Integer.toString(activeRoster.get(1).blocks));

        TextView fieldGoals = (TextView) findViewById(R.id.totFgsPlayer2);
        fieldGoals.setText(Integer.toString(activeRoster.get(1).madeFg)+"-"+Integer.toString(activeRoster.get(1).attemptFg));

        TextView freeThrows = (TextView) findViewById(R.id.totFtsPlayer2);
        freeThrows.setText(Integer.toString(activeRoster.get(1).madeFt)+"-"+Integer.toString(activeRoster.get(1).attemptFt));
    }

    public void setStatsToBarPlayer3(){
        TextView points = (TextView) findViewById(R.id.totPtsPlayer3);
        points.setText(Integer.toString(activeRoster.get(2).points));

        TextView rebounds = (TextView) findViewById(R.id.totReboundsPlayer3);
        rebounds.setText(Integer.toString(activeRoster.get(2).rebounds));

        TextView asssists = (TextView) findViewById(R.id.totAstPlayer3);
        asssists.setText(Integer.toString(activeRoster.get(2).assists));

        TextView steals  = (TextView) findViewById(R.id.totStlPlayer3);
        steals.setText(Integer.toString(activeRoster.get(2).steals));

        TextView blocks = (TextView) findViewById(R.id.totBlkPlayer3);
        blocks.setText(Integer.toString(activeRoster.get(2).blocks));

        TextView fieldGoals = (TextView) findViewById(R.id.totFgsPlayer3);
        fieldGoals.setText(Integer.toString(activeRoster.get(2).madeFg)+"-"+Integer.toString(activeRoster.get(2).attemptFg));

        TextView freeThrows = (TextView) findViewById(R.id.totFtsPlayer3);
        freeThrows.setText(Integer.toString(activeRoster.get(2).madeFt)+"-"+Integer.toString(activeRoster.get(2).attemptFt));
    }

    public void setStatsToBarPlayer4(){
        TextView points = (TextView) findViewById(R.id.totPtsPlayer4);
        points.setText(Integer.toString(activeRoster.get(3).points));

        TextView rebounds = (TextView) findViewById(R.id.totReboundsPlayer4);
        rebounds.setText(Integer.toString(activeRoster.get(3).rebounds));

        TextView asssists = (TextView) findViewById(R.id.totAstPlayer4);
        asssists.setText(Integer.toString(activeRoster.get(3).assists));

        TextView steals  = (TextView) findViewById(R.id.totStlPlayer4);
        steals.setText(Integer.toString(activeRoster.get(3).steals));

        TextView blocks = (TextView) findViewById(R.id.totBlkPlayer4);
        blocks.setText(Integer.toString(activeRoster.get(3).blocks));

        TextView fieldGoals = (TextView) findViewById(R.id.totFgsPlayer4);
        fieldGoals.setText(Integer.toString(activeRoster.get(3).madeFg)+"-"+Integer.toString(activeRoster.get(3).attemptFg));

        TextView freeThrows = (TextView) findViewById(R.id.totFtsPlayer4);
        freeThrows.setText(Integer.toString(activeRoster.get(3).madeFt)+"-"+Integer.toString(activeRoster.get(3).attemptFt));
    }

    public void setStatsToBarPlayer5(){
        TextView points = (TextView) findViewById(R.id.totPtsPlayer5);
        points.setText(Integer.toString(activeRoster.get(4).points));

        TextView rebounds = (TextView) findViewById(R.id.totReboundsPlayer5);
        rebounds.setText(Integer.toString(activeRoster.get(4).rebounds));

        TextView asssists = (TextView) findViewById(R.id.totAstPlayer5);
        asssists.setText(Integer.toString(activeRoster.get(4).assists));

        TextView steals  = (TextView) findViewById(R.id.totStlPlayer5);
        steals.setText(Integer.toString(activeRoster.get(4).steals));

        TextView blocks = (TextView) findViewById(R.id.totBlkPlayer5);
        blocks.setText(Integer.toString(activeRoster.get(4).blocks));

        TextView fieldGoals = (TextView) findViewById(R.id.totFgsPlayer5);
        fieldGoals.setText(Integer.toString(activeRoster.get(4).madeFg)+"-"+Integer.toString(activeRoster.get(4).attemptFg));

        TextView freeThrows = (TextView) findViewById(R.id.totFtsPlayer5);
        freeThrows.setText(Integer.toString(activeRoster.get(4).madeFt)+"-"+Integer.toString(activeRoster.get(4).attemptFt));
    }


    //------------------------------------------------------------------------------------------------------------------------------
    //Debugging tools
    //------------------------------------------------------------------------------------------------------------------------------

    public void printPlayerNames(){
        System.out.println("Here is your roster");
        for(BasketballPlayer player: myRoster){
            System.out.println(player.playerName);
        }
    }

    public void printActiveRoster(){
        System.out.println("Here are the five players on the court");
        for(BasketballPlayer player: activeRoster){
            System.out.println(player.playerName);
        }
    }
}




