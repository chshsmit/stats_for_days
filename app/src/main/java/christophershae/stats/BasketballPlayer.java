package christophershae.stats;

import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chrissmith on 7/18/17.
 */

public class BasketballPlayer implements Serializable {

    public String playerName = new String();


    //Variables for stats that are being listed on the stat bar
    int points, rebounds, assists, steals, blocks, madeFt, attemptFt, madeFg, attemptFg, fouls, turnovers, minutes;

    public int secondsPlayed;
    public int totalSecondsPlayed = 0;

    public int startTime, takeoutTime;



    //List<Map<String, Integer>> fullGameInformation = new ArrayList<>();

    Map<String, Map<String, Integer>> fullGameInformation = new HashMap<>();

    Map<String, Map> allGamesPlayed = new HashMap<>();

    //Creates string keys for all stats and declare a hashtable to store them
    List<String> statKeys = new ArrayList<String>(Arrays.asList("2PM", "2PA", "3PM","3PA","AST","PASS","OREB","DREB","BLK","STL","FTM","FTA","TO","FOUL","MINUTES"));
    Map<String, Integer> playerStats = new HashMap<String, Integer>();

    //Stats for each quarter
    Map<String, Integer> firstQuarterStats = new HashMap<String, Integer>();
    Map<String, Integer> secondQuarterStats = new HashMap<String, Integer>();
    Map<String, Integer> thirdQuarterStats = new HashMap<String, Integer>();
    Map<String, Integer> fourthQuarterStats = new HashMap<String, Integer>();


    //Constructor for Basketball player class
    public BasketballPlayer(String name){

        this.playerName = name;
        //Initialize all the player's stats to 0
        for(String stat : this.statKeys){
            this.playerStats.put(stat, 0);
            this.firstQuarterStats.put(stat, 0);
            this.secondQuarterStats.put(stat, 0);
            this.thirdQuarterStats.put(stat, 0);
            this.fourthQuarterStats.put(stat, 0);
            //System.out.println(stat+":" +playerStats.get(stat));
        }
    }

    public BasketballPlayer(){
        for(String stat : this.statKeys){
            this.playerStats.put(stat, 0);
            this.firstQuarterStats.put(stat, 0);
            this.secondQuarterStats.put(stat, 0);
            this.thirdQuarterStats.put(stat, 0);
            this.fourthQuarterStats.put(stat, 0);
            //System.out.println(stat+":" +playerStats.get(stat));
        }
    }

    public void calculateMinutes(){
        this.secondsPlayed = this.startTime - this.takeoutTime;
        this.totalSecondsPlayed += this.secondsPlayed;

        this.playerStats.put("MINUTES", this.totalSecondsPlayed);
    }



    //Padding those stats
    public void increaseStat(String statKey, int quarterNumber){
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

        //Add the stat to the proper quarter
        switch(quarterNumber){
            case 1:
                calculateStatsForCurrentQuarter(firstQuarterStats, statKey);
                break;

            case 2:
                calculateStatsForCurrentQuarter(secondQuarterStats, statKey);
                break;

            case 3:
                calculateStatsForCurrentQuarter(thirdQuarterStats, statKey);
                break;

            case 4:
                calculateStatsForCurrentQuarter(fourthQuarterStats, statKey);
                break;
        }

        calculateTotalStats();

    }



    //Helper function to input stats for the current quarter
    public void calculateStatsForCurrentQuarter(Map<String, Integer> quarterStats, String statKey){
        int oldStat = quarterStats.get(statKey);
        //System.out.println("You are updating this stat: " +statKey);

        if(statKey.equals("2PM")){
            quarterStats.put(statKey, ++oldStat);
            oldStat = quarterStats.get("2PA");
            quarterStats.put("2PA", ++oldStat);
        }else if(statKey.equals("3PM")){
            quarterStats.put(statKey, ++oldStat);
            oldStat = quarterStats.get("3PA");
            quarterStats.put("3PA", ++oldStat);
        }else if(statKey.equals("FTM")){
            quarterStats.put(statKey, ++oldStat);
            oldStat = quarterStats.get("FTA");
            quarterStats.put("FTA", ++oldStat);
        }else {
            quarterStats.put(statKey, ++oldStat);
        }
    }


    public void decreaseStat(String statKey, int quarterNumber){
        int oldStat = playerStats.get(statKey);
        System.out.println("You are updating this stat: " +statKey);

        if(statKey.equals("2PM")){
            playerStats.put(statKey, --oldStat);
            oldStat = playerStats.get("2PA");
            playerStats.put("2PA", --oldStat);
        }else if(statKey.equals("3PM")){
            playerStats.put(statKey, --oldStat);
            oldStat = playerStats.get("3PA");
            playerStats.put("3PA", --oldStat);
        }else if(statKey.equals("FTM")){
            playerStats.put(statKey, --oldStat);
            oldStat = playerStats.get("FTA");
            playerStats.put("FTA", --oldStat);
        }else {
            playerStats.put(statKey, --oldStat);
        }

        //Add the stat to the proper quarter
        switch(quarterNumber){
            case 1:
                decreaseStatsForCurrentQuarter(firstQuarterStats, statKey);
                break;

            case 2:
                decreaseStatsForCurrentQuarter(secondQuarterStats, statKey);
                break;

            case 3:
                decreaseStatsForCurrentQuarter(thirdQuarterStats, statKey);
                break;

            case 4:
                decreaseStatsForCurrentQuarter(fourthQuarterStats, statKey);
                break;
        }

        calculateTotalStats();

    }

    //Helper function to input stats for the current quarter
    public void decreaseStatsForCurrentQuarter(Map<String, Integer> quarterStats, String statKey){
        int oldStat = quarterStats.get(statKey);
        //System.out.println("You are updating this stat: " +statKey);

        if(statKey.equals("2PM")){
            quarterStats.put(statKey, --oldStat);
            oldStat = quarterStats.get("2PA");
            quarterStats.put("2PA", --oldStat);
        }else if(statKey.equals("3PM")){
            quarterStats.put(statKey, --oldStat);
            oldStat = quarterStats.get("3PA");
            quarterStats.put("3PA", --oldStat);
        }else if(statKey.equals("FTM")){
            quarterStats.put(statKey, --oldStat);
            oldStat = quarterStats.get("FTA");
            quarterStats.put("FTA", --oldStat);
        }else {
            quarterStats.put(statKey, --oldStat);
        }
    }


    public int returnStatValue(String statKey){
        return this.playerStats.get(statKey);
    }



    //Prints out all the stats for a certain hashmap
    public void showStats(Map<String, Integer> myStats){
        for(String stat: this.statKeys){
            System.out.println(stat+":" +myStats.get(stat));
        }
    }

    public void setFullGameInformation(){
        this.fullGameInformation.put("Total",playerStats);
        this.fullGameInformation.put("Q1",firstQuarterStats);
        this.fullGameInformation.put("Q2",secondQuarterStats);
        this.fullGameInformation.put("Q3",thirdQuarterStats);
        this.fullGameInformation.put("Q4",fourthQuarterStats);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());

        this.allGamesPlayed.put(formattedDate, fullGameInformation);
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
        this.fouls = playerStats.get("FOUL");
        this.turnovers = playerStats.get("TO");
        this.minutes = this.totalSecondsPlayed;
    }


}
