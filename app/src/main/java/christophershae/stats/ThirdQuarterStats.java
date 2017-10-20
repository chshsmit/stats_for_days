package christophershae.stats;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ThirdQuarterStats extends Fragment {
    private ArrayList<ListElement> aList;
    private MyAdapter aa;
    ArrayList<String> roster = new ArrayList<String>();
    Bundle myBundle = new Bundle();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.third_quarter_stats, container, false);

        aList = new ArrayList<ListElement>();
        myBundle = getArguments();
        roster = myBundle.getStringArrayList("roster");


        aa = new MyAdapter(getActivity(), R.layout.stat_line, aList);
        ListView myListView =(ListView) rootView.findViewById(R.id.listViewForStats);
        myListView.setAdapter(aa);
        createStatLines();
        aa.notifyDataSetChanged();

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
            minutes.setText(String.format("%d",w.player.thirdQuarterStats.get("MINUTES")));

            TextView fieldGoals = (TextView) newView.findViewById(R.id.playersFieldGoals);
            fieldGoals.setText(String.format("%d",w.player.thirdQuarterStats.get("2PM") + w.player.thirdQuarterStats.get("3PM"))+"-"+String.format("%d",w.player.thirdQuarterStats.get("2PA") + w.player.thirdQuarterStats.get("3PA")));

            TextView threePointFieldGoals = (TextView) newView.findViewById(R.id.playersThreePointFieldGoals);
            threePointFieldGoals.setText(String.format("%d",w.player.thirdQuarterStats.get("3PM"))+"-"+String.format("%d",w.player.thirdQuarterStats.get("3PA")));

            TextView freeThrows = (TextView) newView.findViewById(R.id.playersFreeThrows);
            freeThrows.setText(String.format("%d",w.player.thirdQuarterStats.get("FTM"))+"-"+String.format("%d",w.player.thirdQuarterStats.get("FTA")));

            TextView offensiveRebounds = (TextView) newView.findViewById(R.id.playersOffensiveRebounds);
            offensiveRebounds.setText(String.format("%d",w.player.thirdQuarterStats.get("OREB")));

            TextView defensiveRebounds = (TextView) newView.findViewById(R.id.playersDefensiveRebounds);
            defensiveRebounds.setText(String.format("%d",w.player.thirdQuarterStats.get("DREB")));

            TextView rebounds = (TextView) newView.findViewById(R.id.playersRebounds);
            rebounds.setText(String.format("%d",w.player.thirdQuarterStats.get("DREB") + w.player.thirdQuarterStats.get("OREB")));

            TextView assists = (TextView) newView.findViewById(R.id.playersAssists);
            assists.setText(String.format("%d",w.player.thirdQuarterStats.get("AST")));

            TextView steals = (TextView) newView.findViewById(R.id.playersSteals);
            steals.setText(String.format("%d",w.player.thirdQuarterStats.get("STL")));

            TextView blocks = (TextView) newView.findViewById(R.id.playersBlocks);
            blocks.setText(String.format("%d",w.player.thirdQuarterStats.get("BLK")));

            TextView turnovers = (TextView) newView.findViewById(R.id.playersTurnovers);
            turnovers.setText(String.format("%d",w.player.thirdQuarterStats.get("TO")));

            TextView fouls = (TextView) newView.findViewById(R.id.playersFouls);
            fouls.setText(String.format("%d",w.player.thirdQuarterStats.get("FOUL")));

            TextView points = (TextView) newView.findViewById(R.id.playersPoints);
            points.setText(String.format("%d",(w.player.thirdQuarterStats.get("2PM") * 2) + (w.player.thirdQuarterStats.get("3PM") * 3) + w.player.thirdQuarterStats.get("FTM")));




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
        }
    }
}
