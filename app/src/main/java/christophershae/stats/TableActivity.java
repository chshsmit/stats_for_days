package christophershae.stats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        myPlayer.showStats();
    }



}
