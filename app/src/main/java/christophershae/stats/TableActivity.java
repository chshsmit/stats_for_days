package christophershae.stats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class TableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
    }

    BasketballPlayer myPlayer = new BasketballPlayer();

    public void addStat(View v){
        String statKey = ((Button) v).getText().toString();
        myPlayer.increaseStat(statKey);
        setStatsToBar();
        myPlayer.showStats();
    }


    public void setStatsToBar(){

        TextView points = (TextView) findViewById(R.id.totPtsPlayer1);
        points.setText(Integer.toString(myPlayer.points));

        TextView rebounds = (TextView) findViewById(R.id.totReboundsPlayer1);
        rebounds.setText(Integer.toString(myPlayer.rebounds));

        TextView asssists = (TextView) findViewById(R.id.totAstPlayer1);
        asssists.setText(Integer.toString(myPlayer.assists));

        TextView steals  = (TextView) findViewById(R.id.totStlPlayer1);
        steals.setText(Integer.toString(myPlayer.steals));

        TextView blocks = (TextView) findViewById(R.id.totBlkPlayer1);
        blocks.setText(Integer.toString(myPlayer.blocks));

        TextView fieldGoals = (TextView) findViewById(R.id.totFgsPlayer1);
        fieldGoals.setText(Integer.toString(myPlayer.madeFg)+"-"+Integer.toString(myPlayer.attemptFg));

        TextView freeThrows = (TextView) findViewById(R.id.totFtsPlayer1);
        freeThrows.setText(Integer.toString(myPlayer.madeFt)+"-"+Integer.toString(myPlayer.attemptFt));


    }
}
