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

    final public String playerName = new String();

    //Creates string keys for all stats and declare a hashtable to store them
    List<String> statKeys = new ArrayList<String>(Arrays.asList("2PM", "2PA", "3PM","3PA","AST","PASS","OREB","DREB","BLK","STL","FTM","FTA","TO","FOUL"));
    Map<String, Integer> playerStats = new HashMap<String, Integer>();

    //Constructor for Basketball player class
    public BasketballPlayer(){
        //Initialize all the player's stats to 0
        for(String stat : statKeys){
            playerStats.put(stat, 0);
        }
    }

    //Padding those stats
    public void increaseStat(String statKey){
        int oldStat = playerStats.get(statKey);
        playerStats.put(statKey, ++oldStat);
    }
}
