package christophershae.stats;

/**
 * Created by chrissmith on 8/17/17.
 */

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class FullGameStats extends Fragment {

    private ArrayList<ListElement> aList;
    private MyAdapter aa;
    ArrayList<String> roster = new ArrayList<String>();
    Bundle myBundle = new Bundle();

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

        return rootView;
    }



//    ---------------------------------------------------------------------------------------------------
//    This handles creating the list view for the stats
//    ---------------------------------------------------------------------------------------------------


    private class ListElement {
        ListElement(BasketballPlayer myPlayer) {
            playerName = myPlayer.playerName;
        }

        public String playerName;


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
            TextView tv = (TextView) newView.findViewById(R.id.playerName);
            tv.setText(w.playerName);



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
