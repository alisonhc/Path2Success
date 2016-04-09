package com.example.tyler.Path2Success;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.FileOutputStream;
import java.util.ArrayList;

//Import statements for writing to local memory
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class HomeScreenActivity extends AppCompatActivity {

    public static final String FILENAME = "goal_file";
    public final static String EXTRA_MESSAGE = "com.example.tyler.myfirstapp.MESSAGE";
    public final static String EXTRA_MESSAGE2 = "com.example.tyler.myfirstapp.MESSAGE2";
    private ListView listLayout;
    private LocalStorage storage;
  //  private EditText taskContent;
  //  private EditText dueDate;
    private Button addButton;
    private LayoutTransition mTransition;
    public static final int RESULT_CODE = 9;
    private ArrayList<IndividualGoal> goalArrayList =new ArrayList<>();
    private GoalDataAdapter adapter;
    private JSONArray goalList;
    private JSONObject goalToChange;
    private ListView goalDrawer;
    private ArrayAdapter<String> drawerAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private MediaPlayer soundPlayer;


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.storage = new LocalStorage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        listLayout = (ListView) findViewById(R.id.homescreen_listview);

        soundPlayer = MediaPlayer.create(this,R.raw.cheer);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.homescreen_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Path 2 Success");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        goalDrawer = (ListView)findViewById(R.id.drawer_list_layout);
        goalDrawer.setOnItemClickListener(new DrawerItemClickListener());
        addDrawerItems();

        drawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        setupDrawer();

        adapter = new GoalDataAdapter(this, goalArrayList);
        listLayout.setAdapter(adapter);
        goalList = new JSONArray();
        listLayout.setItemsCanFocus(false);
        listLayout.setChoiceMode(listLayout.CHOICE_MODE_MULTIPLE);
        listLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IndividualGoal iG = adapter.getItem(position);
                CheckedTextView a = (CheckedTextView)((LinearLayout) view).getChildAt(0);
                if (a.isChecked()) {
                    iG.goalIsUndone();
                    a.setChecked(false);
                    Toast.makeText(HomeScreenActivity.this, "unchecked: "+iG.getTitle()+" "+iG.getCategory(), Toast.LENGTH_SHORT).show();
                    storage.setIsChecked(iG, false);

                } else {
                    soundPlayer.start();
                    iG.goalIsDone();
                    a.setChecked(true);
                    Toast.makeText(HomeScreenActivity.this, "checked: "+iG.getTitle(), Toast.LENGTH_SHORT).show();
                    storage.setIsChecked(iG, true);
                }
            }
        });

        JSONObject goalsToShow = storage.getGoals();
        for (int i = 0; i < goalsToShow.length(); i++) {
            //Iterator code found from
            // http://stackoverflow.com/questions/13573913/android-jsonobject-how-can-i-loop-through-a-flat-json-object-to-get-each-key-a
            Iterator<String> iter = goalsToShow.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    JSONObject iteratedGoal = goalsToShow.getJSONObject(key);
                    Boolean checked = iteratedGoal.getBoolean("isChecked");
                    if (!checked) {
                        String title = iteratedGoal.getString("title");
                        String date = iteratedGoal.getString("date");
                        Integer category = iteratedGoal.getInt("category");

                        IndividualGoal newGoal = new IndividualGoal(title, date, category);
                        //add newGoal to local storage
                        goalArrayList.add(newGoal);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sets up navigation drawer and navigation drawer toggle
     */
    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.drawer_open,R.string.drawer_close)
        {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();// creates call to onPrepareOptionsMenu()
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    /**
     * Adds elements of an array to the navigation drawer
     * Sets up and initializes an adapter for the drawer
     */
    private void addDrawerItems(){
        String[] dArray = {"History","Academics", "Fitness", "Misc"};
        drawerAdapter = new ArrayAdapter<>(this,R.layout.drawer_item_info, dArray);
        goalDrawer.setAdapter(drawerAdapter);
    }

    /** Called when the user clicks the plus button */
    public void goToInputScreen(View view) {
        Intent intent = new Intent(this, InputNewGoal.class);
        startActivityForResult(intent, RESULT_CODE);
    }

    /**
     * Gets data from InputNewGoal screen
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == RESULT_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                //what does tContent mean?? what is it's purpose in the code?
                String tContent = data.getStringExtra(InputNewGoal.GOAL_TITLE);
                String tDate = data.getStringExtra((InputNewGoal.DUE_DATE));
                Integer tCategory=data.getIntExtra((InputNewGoal.GOAL_CATEGORY),0);
                if(!tContent.isEmpty()) {
                    IndividualGoal newGoal = new IndividualGoal(tContent, tDate,tCategory);
                    goalArrayList.add(newGoal);
                    storage.saveGoalLocally(newGoal);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id==R.id.action_settings){
            return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
        private void selectItem(int position){
            switch (position){
                case 0:
                    Intent a = new Intent(HomeScreenActivity.this, HistoryStore.class);
                    startActivity(a);
                    break;
            }
        }


    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
}
