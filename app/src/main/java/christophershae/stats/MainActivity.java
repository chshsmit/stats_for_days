package christophershae.stats;


import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "lv-ex";

    private class ListElement {
        ListElement() {
        }

        ;

        ListElement(String tl, String bl) {
            textLabel = tl;
            buttonLabel = bl;
        }

        public String textLabel;
        public String buttonLabel;
    }

    private ArrayList<ListElement> aList;

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
                vi.inflate(resource, newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            // Fills in the view.
            TextView tv = (TextView) newView.findViewById(R.id.itemText);
            Button b = (Button) newView.findViewById(R.id.itemButton);
            tv.setText(w.textLabel);
            b.setText(w.buttonLabel);

            // Sets a listener for the button, and a tag for the button as well.
            b.setTag(new Integer(position));
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Reacts to a button press.
                    // Gets the integer tag of the button.
                    String s = v.getTag().toString();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, s, duration);
                    toast.show();
                    // Let's remove the list item.
                    int i = Integer.parseInt(s);
                    aList.remove(i);
                    aa.notifyDataSetChanged();
                }
            });

            // Set a listener for the whole list item.
            newView.setTag(w.textLabel);
            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = v.getTag().toString();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, s, duration);
                    toast.show();
                }
            });

            return newView;
        }
    }

    private MyAdapter aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aList = new ArrayList<ListElement>();
        aa = new MyAdapter(this, R.layout.list_element, aList);
        ListView myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();
    }


    public void clickRefresh(View v) {
        Log.i(LOG_TAG, "Requested a refresh of the list");
        Random rn = new Random();
        SecureRandomString srs = new SecureRandomString();
        // How long a list do we make?
        int n = 4 + rn.nextInt(10);
        // Let's fill the array with n random strings.
        // NOTE: aList is associated to the array adapter aa, so
        // we cannot do here aList = new ArrayList<ListElement>() ,
        // otherwise we create another ArrayList which would not be
        // associated with aa.
        // aList = new ArrayList<ListElement>(); --- NO
        aList.clear();
        for (int i = 0; i < n; i++) {
            aList.add(new ListElement(
                    srs.nextString(), "Delete"
            ));
        }
        // We notify the ArrayList adapter that the underlying list has changed,
        // triggering a re-rendering of the list.
        aa.notifyDataSetChanged();
    }
}