package christophershae.stats;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;


public class TableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> roster = extras.getStringArrayList("roster");
        createRoster(roster);
        displayTime();

    }

    @Override
    public void onBackPressed(){


    }

    List<BasketballPlayer> myRoster = new ArrayList<BasketballPlayer>();
    List<BasketballPlayer> activeRoster = new ArrayList<BasketballPlayer>();
    ArrayList<String> roster = new ArrayList<String>();
    ArrayList mSelectedItems = new ArrayList();
    int playerId, substitutionPlayerIndex;





    //----------------------------------------------------------------------------------------------------------------
    //Functions to make a substitution
    //----------------------------------------------------------------------------------------------------------------

    //Handles the event of a player's name being clicked indicating a substitution
    public void playerNameClicked(View v){

        if(timer != null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);     //Instantiate the popup that shows the list
        final int viewId = v.getId();
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
                            //If an item is checked, set the playerId to the viewId
                            //Also the index of the person coming into the game is the index that was selected in the list
                            playerId = viewId;
                            substitutionPlayerIndex = which;
                        } else if (mSelectedItems.contains(roster.get(which))) {
                            // Else, if the item is already in the array, remove it
                            mSelectedItems.remove(roster.get(which));
                        }
                    }
                });
        // Set the action buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //User clicked "OK"
                //Sub the current active player with the selection made from the list
                subUtilityFunction(playerId, substitutionPlayerIndex);
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

    public void subUtilityFunction(int viewId, int which){
        switch(viewId){
            case R.id.activePlayer1:
                makeSubstitution(0,which);
                setActiveRosterNames();
                setStatsToBarPlayer1();
                break;

            case R.id.activePlayer2:
                makeSubstitution(1,which);
                setActiveRosterNames();
                setStatsToBarPlayer2();
                break;

            case R.id.activePlayer3:
                makeSubstitution(2,which);
                setActiveRosterNames();
                setStatsToBarPlayer3();
                break;

            case R.id.activePlayer4:
                makeSubstitution(3,which);
                setActiveRosterNames();
                setStatsToBarPlayer4();
                break;

            case R.id.activePlayer5:
                makeSubstitution(4,which);
                setActiveRosterNames();
                setStatsToBarPlayer5();
                break;

            default:
                System.out.println("You failed");
                break;
        }
    }

    public void makeSubstitution(int currentActivePlayerIndex, int newPlayerIndex){

        //If the player selected for a substitution is already in the game, do nothing
        if(activeRoster.contains(myRoster.get(newPlayerIndex))) return;

        //The current player is getting taken out of the game at the current time
        activeRoster.get(currentActivePlayerIndex).takeoutTime = seconds;

        //Calculate the total minutes the player has currently played
        activeRoster.get(currentActivePlayerIndex).calculateMinutes();

        //Make the actual substitution
        activeRoster.set(currentActivePlayerIndex, myRoster.get(newPlayerIndex));

        //Set the new active player start time to the current time
        activeRoster.get(currentActivePlayerIndex).startTime = seconds;

        //For debugging purposes
        printPlayerNames();
    }


    //----------------------------------------------------------------------------------------------------------------
    //Initial roster creation
    //----------------------------------------------------------------------------------------------------------------


    //Creates the roster with the information from the intent
    public void createRoster(ArrayList<String> playerNames){
        for (String name : playerNames) {
            //Create a new player object for every name in the roster
            BasketballPlayer nextPlayer = new BasketballPlayer(name);
            myRoster.add(nextPlayer);
            roster.add(name);
        }

        makeActiveRoster();
        setActiveRosterNames();
        printPlayerNames();
        printActiveRoster();
    }

    //Creates the initial starting five
    public void makeActiveRoster(){
        for(int i=0; i<5; i++){
            activeRoster.add(i, myRoster.get(i));
            activeRoster.get(i).startTime = seconds;
        }
    }

    //Puts the starting five names in all the proper text views
    public void setActiveRosterNames(){
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

        if(seconds == 0) return;

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
    //This is where we keep track of the time
    //------------------------------------------------------------------------------------------------------------------------------

    // Counter for the number of seconds.
    private int seconds = 480;

    // Countdown timer.
    private CountDownTimer timer = null;

    // One second.  We use Mickey Mouse time.
    private static final int ONE_SECOND_IN_MILLIS = 1000;

    public void onClickStart(View v) {

        if (seconds == 0) {
            cancelTimer();
        }
        if (timer == null) {
            // We create a new timer.
            timer = new CountDownTimer(seconds * ONE_SECOND_IN_MILLIS, ONE_SECOND_IN_MILLIS) {
                @Override
                public void onTick(long millisUntilFinished) {
                    seconds = Math.max(0, seconds - 1);
                    displayTime();
                }

                @Override
                public void onFinish() {
                    seconds = 0;
                    timer = null;
                    getMinutesForEndOfQuarter();
                    printPlayerNames();

                    displayTime();
                }
            };
            timer.start();
        }
    }

    public void onClickStop(View v) {
        cancelTimer();
        displayTime();
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    // Updates the time display.
    private void displayTime() {

        TextView v = (TextView) findViewById(R.id.display);
        int m = seconds / 60;
        int s = seconds % 60;
        v.setText(String.format("%d:%02d", m, s));
        // Manages the buttons.
        Button stopButton = (Button) findViewById(R.id.button_stop);
        Button startButton = (Button) findViewById(R.id.button_start);
        Button newQuarterButton = (Button) findViewById(R.id.newQuarterButton);
        startButton.setEnabled(timer == null && seconds > 0);
        stopButton.setEnabled(timer != null && seconds > 0);
        newQuarterButton.setEnabled(timer == null && seconds == 0);
    }

    public void startNewQuarter(View v){

        //Reset the time to the start of a quarter
        seconds = 480;

        //Set all the active players start times to the beginning of a quarter
        for(int i = 0; i < 5; i++){
            activeRoster.get(i).startTime = seconds;
        }

        displayTime();

    }


    public void getMinutesForEndOfQuarter(){
        for(int i = 0; i < 5; i++){
            activeRoster.get(i).takeoutTime = 0;
            activeRoster.get(i).calculateMinutes();
        }
    }




    //------------------------------------------------------------------------------------------------------------------------------
    //Debugging tools
    //------------------------------------------------------------------------------------------------------------------------------

    public void printPlayerNames(){
        System.out.println("Here is your roster");
        for(BasketballPlayer player: myRoster){
            System.out.println("Name:" +player.playerName);
            System.out.println("Seconds Played:" +player.totalSecondsPlayed);

        }
    }

    public void printList(ArrayList<String> list){
        for(String item: list){
            System.out.println(item);
        }
    }



    public void printActiveRoster(){
        System.out.println("Here are the five players on the court");
        for(BasketballPlayer player: activeRoster){
            System.out.println(player.playerName);
        }
    }
}




