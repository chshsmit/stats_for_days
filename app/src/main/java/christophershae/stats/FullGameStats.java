package christophershae.stats;

/**
 * Created by chrissmith on 8/17/17.
 */

import android.content.Context;
import android.graphics.Region;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class FullGameStats extends Fragment {

    private ArrayList<ListElement> aList;
    private MyAdapter aa;
    ArrayList<String> roster = new ArrayList<String>();
    Bundle myBundle = new Bundle();
    ArrayList<BasketballPlayer> finalRoster = new ArrayList<BasketballPlayer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.full_game_stats, container, false);

        aList = new ArrayList<ListElement>();
        myBundle = getArguments();
        roster = myBundle.getStringArrayList("roster");


        aa = new MyAdapter(getActivity(), R.layout.stat_line, aList);
        ListView myListView =(ListView) rootView.findViewById(R.id.listViewForStats);
        myListView.setAdapter(aa);
        createStatLines();
        aa.notifyDataSetChanged();
        //readFromFileTest();

        return rootView;
    }



//    ---------------------------------------------------------------------------------------------------
//    This handles creating the list view for the stats
//    ---------------------------------------------------------------------------------------------------


    private class ListElement {
        ListElement(BasketballPlayer myPlayer) {
            playerName = myPlayer.playerName;
            player = myPlayer;
        }


        String playerName;
        BasketballPlayer player;
        //List<String> statKeys = new ArrayList<String>(Arrays.asList("2PM", "2PA", "3PM","3PA","AST","PASS","OREB","DREB","BLK","STL","FTM","FTA","TO","FOUL","MINUTES"));
        //Map<String, Integer> playerStats = new HashMap<String, Integer>();


    }


    private class MyAdapter extends ArrayAdapter<ListElement> {

        int resource;
        Context context;

        public MyAdapter(Context _context, int _resource, List<ListElement> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout newView;

            ListElement w = getItem(position);

            // Inflate a new view if necessary.
            if (convertView == null) {
                newView = new LinearLayout(getContext());
                LayoutInflater vi = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(resource,  newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            // Fills in the view.
            TextView name = (TextView) newView.findViewById(R.id.playerName);
            name.setText(w.playerName);

            TextView minutes = (TextView) newView.findViewById(R.id.playersMinutes);
            minutes.setText(Integer.toString(w.player.minutes));

            TextView fieldGoals = (TextView) newView.findViewById(R.id.playersFieldGoals);
            fieldGoals.setText(Integer.toString(w.player.madeFg)+"-"+Integer.toString(w.player.attemptFg));

            TextView threePointFieldGoals = (TextView) newView.findViewById(R.id.playersThreePointFieldGoals);
            threePointFieldGoals.setText(Integer.toString(w.player.playerStats.get("3PM"))+"-"+Integer.toString(w.player.playerStats.get("3PA")));

            TextView freeThrows = (TextView) newView.findViewById(R.id.playersFreeThrows);
            freeThrows.setText(Integer.toString(w.player.madeFt)+"-"+Integer.toString(w.player.attemptFt));

            TextView offensiveRebounds = (TextView) newView.findViewById(R.id.playersOffensiveRebounds);
            offensiveRebounds.setText(Integer.toString(w.player.playerStats.get("OREB")));

            TextView defensiveRebounds = (TextView) newView.findViewById(R.id.playersDefensiveRebounds);
            defensiveRebounds.setText(Integer.toString(w.player.playerStats.get("DREB")));

            TextView rebounds = (TextView) newView.findViewById(R.id.playersRebounds);
            rebounds.setText(Integer.toString(w.player.rebounds));

            TextView assists = (TextView) newView.findViewById(R.id.playersAssists);
            assists.setText(Integer.toString(w.player.assists));

            TextView steals = (TextView) newView.findViewById(R.id.playersSteals);
            steals.setText(Integer.toString(w.player.steals));

            TextView blocks = (TextView) newView.findViewById(R.id.playersBlocks);
            blocks.setText(Integer.toString(w.player.blocks));

            TextView turnovers = (TextView) newView.findViewById(R.id.playersTurnovers);
            turnovers.setText(Integer.toString(w.player.turnovers));

            TextView fouls = (TextView) newView.findViewById(R.id.playersFouls);
            fouls.setText(Integer.toString(w.player.fouls));

            TextView points = (TextView) newView.findViewById(R.id.playersPoints);
            points.setText(Integer.toString(w.player.points));




            // Set a listener for the whole list item.
            //newView.setTag(w.textLabel);
//            newView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String s = v.getTag().toString();
//                    int duration = Toast.LENGTH_SHORT;
//                    Toast toast = Toast.makeText(context, s, duration);
//                    toast.show();
//                }
//            });

            return newView;
        }
    }


    public void createStatLines(){

        for(String name: roster){
            aList.add(new ListElement((BasketballPlayer) myBundle.getSerializable(name)));
            finalRoster.add((BasketballPlayer) myBundle.getSerializable(name));
        }
    }






}
