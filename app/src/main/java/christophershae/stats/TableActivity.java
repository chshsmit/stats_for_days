package christophershae.stats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.key;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class TableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> roster = extras.getStringArrayList("roster");
        createRoster(roster);

    }

    BasketballPlayer Player1 = new BasketballPlayer("Player 1");      //This guy is just a tester

    List<BasketballPlayer> myRoster = new ArrayList<BasketballPlayer>();
    List<BasketballPlayer> activeRoster = new ArrayList<BasketballPlayer>();


    //----------------------------------------------------------------------------------------------------------------
    //Setting roster information
    //----------------------------------------------------------------------------------------------------------------

    //Creates the roster with the information from the intent
    public void createRoster(ArrayList<String> playerNames){
        for (String name : playerNames) {
            //To Implement
            BasketballPlayer nextPlayer = new BasketballPlayer(name);
            myRoster.add(nextPlayer);
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



        activeRoster.get(0).increaseStat(statKey);
        setStatsToBar();
        activeRoster.get(0).showStats();
    }



    //Displays stats to stat bar for Player1
    public void setStatsToBar(){

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




