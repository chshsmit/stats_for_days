package christophershae.stats;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chrissmith on 7/18/17.
 */

public class BasketballPlayer  {

    public String playerName = new String();


    //Variables for stats that are being listed on the stat bar
    int points, rebounds, assists, steals, blocks, madeFt, attemptFt, madeFg, attemptFg;

    //Creates string keys for all stats and declare a hashtable to store them
    List<String> statKeys = new ArrayList<String>(Arrays.asList("2PM", "2PA", "3PM","3PA","AST","PASS","OREB","DREB","BLK","STL","FTM","FTA","TO","FOUL"));
    Map<String, Integer> playerStats = new HashMap<String, Integer>();

    //Constructor for Basketball player class
    public BasketballPlayer(String name){

        this.playerName = name;
        //Initialize all the player's stats to 0
        for(String stat : this.statKeys){
            this.playerStats.put(stat, 0);
            System.out.println(stat+":" +playerStats.get(stat));
        }
    }



    //Padding those stats
    public void increaseStat(String statKey){
        int oldStat = playerStats.get(statKey);
        System.out.println("You are updating this stat: " +statKey);

        if(statKey.equals("2PM")){
            playerStats.put(statKey, ++oldStat);
            oldStat = playerStats.get("2PA");
            playerStats.put("2PA", ++oldStat);
        }else if(statKey.equals("3PM")){
            playerStats.put(statKey, ++oldStat);
            oldStat = playerStats.get("3PA");
            playerStats.put("3PA", ++oldStat);
        }else if(statKey.equals("FTM")){
            playerStats.put(statKey, ++oldStat);
            oldStat = playerStats.get("FTA");
            playerStats.put("FTA", ++oldStat);
        }else {
            playerStats.put(statKey, ++oldStat);
        }

        calculateTotalStats();

    }



    //Prints out all the stats
    public void showStats(){
        for(String stat: this.statKeys){
            System.out.println(stat+":" +playerStats.get(stat));
        }
    }


    //Calculate stats for the bar
    public void calculateTotalStats(){
        this.points = 2*playerStats.get("2PM") + 3*playerStats.get("3PM") + playerStats.get("FTM");
        this.rebounds = playerStats.get("OREB") + playerStats.get("DREB");
        this.assists = playerStats.get("AST");
        this.steals = playerStats.get("STL");
        this.blocks = playerStats.get("BLK");
        this.madeFg = playerStats.get("2PM") + playerStats.get("3PM");
        this.attemptFg = playerStats.get("2PA") + playerStats.get("3PA");
        this.madeFt = playerStats.get("FTM");
        this.attemptFt = playerStats.get("FTA");
    }


}
