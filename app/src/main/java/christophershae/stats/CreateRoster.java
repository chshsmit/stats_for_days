package christophershae.stats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.name;
import static android.R.id.edit;

public class CreateRoster extends AppCompatActivity {

    //List<BasketballPlayer> myRoster = new ArrayList<BasketballPlayer>();
    String playerName;
    ArrayList<String> names = new ArrayList<String>();
    //Intent changingActivities = new Intent(getApplicationContext(), TableActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_roster);
    }

    public void createPlayer(View v){
        EditText playerEntry = (EditText) findViewById(R.id.enterPlayerName);
        playerName = playerEntry.getText().toString();
        names.add(playerName);
        //myRoster.add(newPlayer);
        playerEntry.setText("");
    }

    public void changeToStats(View v){
        Intent changingActivities = new Intent(getApplicationContext(), TableActivity.class);
        changingActivities.putStringArrayListExtra("roster", names);
        startActivity(changingActivities);
    }


}
