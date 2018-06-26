package christophershae.stats;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;



public class EndOfGameStats extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    ArrayList<String> roster = new ArrayList<String>();
    Bundle myBundle = new Bundle();

    public String userId;
    //public User user;
    ArrayList<BasketballPlayer> myPlayers = new ArrayList<BasketballPlayer>();
    BasketballPlayer newPlayer = new BasketballPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_game_stats);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        myBundle = getIntent().getExtras();
        userId = myBundle.getString("userId");
        roster = myBundle.getStringArrayList("roster");


//        for(String name: roster){
//            newPlayer = (BasketballPlayer) myBundle.getSerializable(name);
//            myPlayers.add(newPlayer);
//        }
//
//        printPlayerNames();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    @Override
    public void onBackPressed(){
        Intent changingActivities = new Intent(getApplicationContext(),MainActivity.class);
        changingActivities.putExtra("userId", userId);
        changingActivities.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(changingActivities);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_end_of_game_stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //deleted fragment page adapter class

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    FullGameStats fullGame = new FullGameStats();
                    fullGame.setArguments(myBundle);
                    return fullGame;
                case 1:
                    FirstQuarterStats firstQuarter = new FirstQuarterStats();
                    firstQuarter.setArguments(myBundle);
                    return firstQuarter;
                case 2:
                    SecondQuarterStats secondQuarter = new SecondQuarterStats();
                    secondQuarter.setArguments(myBundle);
                    return secondQuarter;

                case 3:
                    ThirdQuarterStats thirdQuarter = new ThirdQuarterStats();
                    thirdQuarter.setArguments(myBundle);
                    return thirdQuarter;

                case 4:
                    FourthQuarterStats fourthQuarter = new FourthQuarterStats();
                    fourthQuarter.setArguments(myBundle);
                    return fourthQuarter;

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Full Game";
                case 1:
                    return "First Quarter";
                case 2:
                    return "Second Quarter";

                case 3:
                    return "Third Quarter";

                case 4:
                    return "Fourth Quarter";
            }
            return null;
        }
    }


//    public void printPlayerNames(){
//        System.out.println("HERE IS YOUR ROSTER FOR THE END OF THE GAME STATS");
//        for(BasketballPlayer player: myPlayers){
//            System.out.println("Name:" +player.playerName);
//            //System.out.println("Seconds Played:" +player.totalSecondsPlayed);
//
//        }
//    }
}
