package christophershae.stats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class NewStatLayout extends AppCompatActivity {
    public String userId;
    public User user;
    private DatabaseReference mFireBaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    List<String> statKeys = new ArrayList<String>(Arrays.asList("2PM", "2PA", "3PM","3PA","AST","PASS","OREB","DREB","BLK","STL","FTM","FTA","TO","FOUL","MINUTES"));
    List<BasketballPlayer> myRoster = new ArrayList<BasketballPlayer>();
    List<BasketballPlayer> activeRoster = new ArrayList<BasketballPlayer>();
    ArrayList<String> roster = new ArrayList<String>();
    ArrayList mSelectedItems = new ArrayList();

    //Initial quarter number
    int currentQuarter = 1;
    int defaultQuarterLength;
    boolean isCollege;
    boolean isProfessional;


    //----------------------------------------------------------------------------------------------------------------
    //This is the on create method
    //----------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_stat_layout);

        mFirebaseInstance = Utils.getDatabase();

        //Get reference to user nodes
        mFireBaseDatabase = mFirebaseInstance.getReference("users");

        Bundle extras = getIntent().getExtras();
        ArrayList<String> roster = extras.getStringArrayList("roster");
        userId = extras.getString("userId");
        isCollege = extras.getBoolean("isCollege");
        isProfessional = extras.getBoolean("isProfessional");
        createRoster(roster, extras.getInt("quarter_length"));
        displayTime();


        mFireBaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("THIS IS FROM THE DATABASE");
                System.out.println(dataSnapshot.child(userId).getValue(User.class).email);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(NewStatLayout.this);
        builder.setMessage("Are you sure you want to exit? All current game data will be lost.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        changeToMainActivity(userId);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    //----------------------------------------------------------------------------------------------------------------
    //Initial roster creation
    //----------------------------------------------------------------------------------------------------------------

    //Creates the roster with the information from the intent and sets the time for a quarter
    public void createRoster(ArrayList<String> playerNames, int quarterLength){
        defaultQuarterLength = quarterLength;
        seconds = defaultQuarterLength;

        for (String name : playerNames) {
            //Create a new player object for every name in the roster
            BasketballPlayer nextPlayer = new BasketballPlayer(name);
            myRoster.add(nextPlayer);
            roster.add(name);
        }

        makeActiveRoster();
        setActiveRosterNames();
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
    //Functions to make a substitution
    //----------------------------------------------------------------------------------------------------------------

    int playerId, substitutionPlayerIndex;


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
                System.out.println(viewId);
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
        //printPlayerNames();
    }

    Stack<BasketballPlayer> undoPlayerList = new Stack<>();
    Stack<String> undoStatKey = new Stack<>();


    int playerToUpdate;
    public void playerNameClicked(View v){
        playerToUpdate = v.getId();
    }


    public void addStat(View v){
        //if(seconds == 0 || timer == null) return;
        String statKey = ((Button) v).getText().toString();
        addStat(statKey, playerToUpdate);
    }

    private void addStat(String statKey, int playerId){
        switch(playerId){
            case R.id.activePlayer1:
                System.out.println("Player1");
                increasePlayerStat(statKey, 0, currentQuarter, R.id.activePlayer1);
                setStatsToBarPlayer1();
                break;

            case R.id.activePlayer2:
                System.out.println("Player2");
                increasePlayerStat(statKey, 1, currentQuarter, R.id.activePlayer2);
                setStatsToBarPlayer2();
                break;

            case R.id.activePlayer3:
                System.out.println("Player3");
                increasePlayerStat(statKey, 2, currentQuarter, R.id.activePlayer3);
                setStatsToBarPlayer3();
                break;

            case R.id.activePlayer4:
                System.out.println("Player4");
                increasePlayerStat(statKey, 3, currentQuarter, R.id.activePlayer4);
                setStatsToBarPlayer4();
                break;

            case R.id.activePlayer5:
                System.out.println("Player5");
                increasePlayerStat(statKey, 4, currentQuarter, R.id.activePlayer5);
                setStatsToBarPlayer5();
                break;

            default:
                System.out.println("You failed");
                break;
        }

    }

    public void increasePlayerStat(String statKey, int playerIndex, int currentQuarter, int viewId){
        activeRoster.get(playerIndex).increaseStat(statKey, currentQuarter);

        //Increase the stat in the players map
        undoPlayerList.push(activeRoster.get(playerIndex));                              //Push this player to the top of the undo stack
        undoStatKey.push(statKey);                                                       //Push the stat key to the top of the undo stack
        //checkIfPlayerFouledOut(statKey, activeRoster.get(playerIndex), viewId);          //Determine if the player fouled out

        return;
    }

    public void undoLastStat(View v){

        if(undoPlayerList.isEmpty()) return;

        BasketballPlayer player = undoPlayerList.pop();
        String statKey = undoStatKey.pop();
        player.decreaseStat(statKey, currentQuarter);
        setAllStatsToBar();
    }


//    public void checkIfPlayerFouledOut(String statKey, BasketballPlayer player, int playerViewId){
//        System.out.println("Checking if player has fouled out.");
//
//        //If the current stat key is not a foul then end the function
//        if(!statKey.equals("FOUL")) return;
//
//
//        //If the level of play is professional check if the player has 6 fouls
//        if(isProfessional){
//            if(player.returnStatValue(statKey) == 6) indicatePlayerHasFouledOut(player, playerViewId);
//            return;
//        }
//
//        //All other levels fouling out is 5
//        if(player.returnStatValue(statKey) == 5) indicatePlayerHasFouledOut(player, playerViewId);
//
//    }
//
//    public void indicatePlayerHasFouledOut(final BasketballPlayer player, final int playerViewId){
//        AlertDialog.Builder builder = new AlertDialog.Builder(NewStatLayout.this);
//
//        //Set the message of the dialog
//        builder.setMessage(player.playerName+ " has fouled out. You must sub him out.")
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //System.out.println(playerViewId);
//                        //Build the substitution dialog for the player that fouled out
//                        buildSubstitutionDialog(playerViewId);
//                    }
//                });
//
//        // Create the AlertDialog object and return it
//        AlertDialog dialog = builder.create();
//        dialog.show();        //Show the dialog
//    }



    //------------------------------------------------------------------------------------------------------------------------------
    //Functions to set stats to bar
    //------------------------------------------------------------------------------------------------------------------------------
    public void setAllStatsToBar(){
        setStatsToBarPlayer1();
        setStatsToBarPlayer2();
        setStatsToBarPlayer3();
        setStatsToBarPlayer4();
        setStatsToBarPlayer5();
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
    private int seconds;

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

//                    if(currentQuarter == 4){
//                        saveRosterToFile();
//                    }

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

        TextView v = (TextView) findViewById(R.id.time_keeper);
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

        System.out.println(isCollege);

        if(isCollege && currentQuarter == 2 && seconds == 0){
            addPlayerListToDatabase();
        }


        if(currentQuarter == 4 && seconds == 0){
//            for(BasketballPlayer player: myRoster){
//                player.setFullGameInformation();
//            }
            addPlayerListToDatabase();
        }

        //Reset the time to the start of a quarter
        seconds = defaultQuarterLength;

        //Set all the active players start times to the beginning of a quarter
        for(int i = 0; i < 5; i++){
            activeRoster.get(i).startTime = seconds;
        }

        //printQuarterStats();

        currentQuarter++;
        displayTime();

    }

    public void getMinutesForEndOfQuarter(){
        for(int i = 0; i < 5; i++){
            activeRoster.get(i).takeoutTime = 0;
            activeRoster.get(i).calculateMinutes();
        }
    }


    //------------------------------------------------------------------------------------------------------------------------------
    //This is where we add the current roster to the database
    //------------------------------------------------------------------------------------------------------------------------------

    public void addPlayerListToDatabase(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NewStatLayout.this);     //Instantiate the popup that shows the list
        final EditText input = new EditText(this);
        builder.setView(input);

        // Set the dialog title
        builder.setTitle("Input Game Name");

        // Set the action buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String teamName = input.getText().toString();
                teamName = teamName.replace("/","");
                teamName = teamName.replace(".","");
                teamName = teamName.replace("$","");
                teamName = teamName.replace("[","");
                teamName = teamName.replace("]","");
                teamName = teamName.replace("#","");
                user.userRosters.put(teamName, myRoster);
                mFireBaseDatabase.child(userId).setValue(user);
                changeToBoxScore();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //The user pressed "CANCEL" so nothing needs to be done
                changeToBoxScore();
            }
        });
        builder.show();
    }

    //------------------------------------------------------------------------------------------------------------------------------
    //This is the code to change activities
    //------------------------------------------------------------------------------------------------------------------------------

    public void changeToBoxScore(){
        Intent changingToBoxScore = new Intent(getApplicationContext(), EndOfGameStats.class);

        //Adding all the player objects to the intent
        for(BasketballPlayer player: myRoster){
            changingToBoxScore.putExtra(player.playerName, player);
        }

        //

        //Adding the list of player names to the intent
        changingToBoxScore.putStringArrayListExtra("roster", roster);
        changingToBoxScore.putExtra("userId", userId);


        changingToBoxScore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(changingToBoxScore);
    }

    public void changeToMainActivity(String userId){
        Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
        mainScreen.putExtra("userId", userId);
        mainScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainScreen);
    }

}
