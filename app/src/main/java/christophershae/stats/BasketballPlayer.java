package christophershae.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chrissmith on 7/18/17.
 */

public class BasketballPlayer {

    public String playerName = new String();

    //Creates string keys for all stats and declare a hashtable to store them
    List<String> statKeys = new ArrayList<String>(Arrays.asList("2PM", "2PA", "3PM","3PA","AST","PASS","OREB","DREB","BLK","STL","FTM","FTA","TO","FOUL"));
    Map<String, Integer> playerStats = new HashMap<String, Integer>();

    //Constructor for Basketball player class
    public BasketballPlayer(){
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

    }

    //Sets the players name
    public void setPlayerName(String name){
        this.playerName = name;
    }


    //Prints out all the stats
    public void showStats(){
        for(String stat: this.statKeys){
            System.out.println(stat+":" +playerStats.get(stat));
        }
    }


}
