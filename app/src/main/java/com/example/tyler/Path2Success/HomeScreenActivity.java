package com.example.tyler.Path2Success;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import java.io.Serializable;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class HomeScreenActivity extends AppCompatActivity implements Serializable {
    private static final String DEBUGTAG = HomeScreenActivity.class.getSimpleName();
    private ListView listLayout;
    private LocalStorage storage;
  //  private EditText taskContent;
  //  private EditText dueDate;
    private Button addButton;
    private LayoutTransition mTransition;
    public static final int RESULT_CODE_ADD = 9;
    public static final int RESULT_CODE_EDIT = 12;
    private ArrayList<IndividualGoal> goalArrayList =new ArrayList<>();
    private GoalDataAdapter adapter;
    private Toolbar homeToolBar;
    private JSONArray goalList;
    private JSONObject goalToChange;
    private JSONArray goalsToShow;
    private ListView goalDrawer;
    private ArrayAdapter<String> drawerAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private MediaPlayer soundPlayer;
    private String[] dArray = {"All","Fitness","Academics",  "Misc","History"};
    private int currentCategory = -1;
    private int editGoalPosition = -1;
    public final static String FIRST_RUN = "com.example.tyler.myfirstapp.MESSAGE4";
    SharedPreferences prefs = null;


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.storage = new LocalStorage(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        listLayout = (ListView) findViewById(R.id.homescreen_listview);

        soundPlayer = MediaPlayer.create(this,R.raw.cheer);
        int maxVolume = 50;

        float log1=(float)(Math.log(maxVolume-20)/Math.log(maxVolume));
        soundPlayer.setVolume(1-log1,1-log1);

        prefs = getSharedPreferences(FIRST_RUN,MODE_PRIVATE);

        homeToolBar = (Toolbar) findViewById(R.id.homescreen_toolbar);
        setSupportActionBar(homeToolBar);
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
        listLayout.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // or i could have changed to java.lang.object instead. which is better? idk you tell me
                Object g = parent.getAdapter().getItem(position);
                goToEditScreen(view, g, position);
                return true;
            }


        });
        listLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IndividualGoal iG = adapter.getItem(position);
                CheckedTextView a = (CheckedTextView) ((RelativeLayout) view).getChildAt(0);
                if (a.isChecked()) {
                    iG.goalIsUndone();
                    a.setChecked(false);
                    Toast.makeText(HomeScreenActivity.this, "Goal unchecked.", Toast.LENGTH_SHORT).show();
                    storage.setCompleted(iG, false);

                } else {
                    soundPlayer.start();
                    iG.goalIsDone();
                    a.setChecked(true);
//                    Toast.makeText(HomeScreenActivity.this, "checked: " + iG.getTitle(), Toast.LENGTH_SHORT).show();
                    storage.setCompleted(iG, true);
                }
            }
        });

        goalsToShow = storage.getCompletedOrUncompletedGoals(false);
//        Log.d(DEBUGTAG, "Showing goals: " + goalsToShow.toString());

        for (int i = 0; i < goalsToShow.length(); i++) {
            try {
                JSONObject goalToShow = goalsToShow.getJSONObject(i);

                //Code that updates the view
                String title = goalToShow.getString("title");
                String date = goalToShow.getString("dueDate");
                Integer category = goalToShow.getInt("category");

                IndividualGoal newGoal = new IndividualGoal(title, date, category);
                newGoal.setRandomID(goalToShow.getString("id"));
                goalArrayList.add(newGoal);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
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
        drawerAdapter = new ArrayAdapter<>(this,R.layout.drawer_item_info, dArray);
        goalDrawer.setAdapter(drawerAdapter);
    }

    public void goToEditScreen(View view, Object g, int pos) {
        Intent intent = new Intent(this, EditGoal.class);
        intent.putExtra("IndividualGoal", (Serializable) g); // or could be serializable
        startActivityForResult(intent, RESULT_CODE_EDIT);
        editGoalPosition = pos;
    }

    /** Called when the user clicks the plus button */
    public void goToInputScreen(View view) {
        Intent intent = new Intent(this, InputNewGoal.class);
        startActivityForResult(intent, RESULT_CODE_ADD);
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
        if (requestCode == RESULT_CODE_ADD) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                String tContent = data.getStringExtra(InputNewGoal.GOAL_TITLE);
                String tDate = data.getStringExtra((InputNewGoal.DUE_DATE));
                Integer tCategory = data.getIntExtra((InputNewGoal.GOAL_CATEGORY), 0);
                if (!tContent.isEmpty()) {
                    IndividualGoal newGoal = new IndividualGoal(tContent, tDate, tCategory);
                    storage.saveNewGoal(newGoal);
                    if (currentCategory ==-1){
                        goalArrayList.add(newGoal);
                        adapter.notifyDataSetChanged();
                    }
                    else if (newGoal.getCategory() == currentCategory) {
                        goalArrayList.add(newGoal);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }

        else if (requestCode == RESULT_CODE_EDIT) {
            if (resultCode == RESULT_OK) {
                String tContent = data.getStringExtra(EditGoal.GOAL_TITLE);
                String tDate = data.getStringExtra((EditGoal.DUE_DATE));
                Integer tCategory=data.getIntExtra((EditGoal.GOAL_CATEGORY),0);
                if(!tContent.isEmpty()) {
                    IndividualGoal newGoal = new IndividualGoal(tContent, tDate, tCategory);
                   // goalArrayList.add(newGoal);
                    storage.saveNewGoal(newGoal);
                    View viewToEdit = listLayout.getChildAt(editGoalPosition);
                    CheckedTextView a = (CheckedTextView) viewToEdit.findViewById(R.id.taskContent);
                    a.setText(tContent);
                    TextView b = (TextView) viewToEdit.findViewById(R.id.taskDate);
                    b.setText(tDate.substring(0,5));
                    //adapter.notifyDataSetChanged();
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
            //getAll
            if (position==0) {
                getAllUnfinishedGoalsSaved();
                drawerLayout.closeDrawers();
                currentCategory = -1;
            }
            //HistoryPage
            else if(position==dArray.length-1){
                Intent a = new Intent(HomeScreenActivity.this, HistoryStore.class);
                startActivity(a);
            }
            //CategoryFilter
            else{
                Integer categoryIndex=position-1;
                filterGoal(categoryIndex);
                drawerLayout.closeDrawers();
                currentCategory = categoryIndex;
            }
        }

    private void filterGoal(Integer catIndex) {
        goalArrayList.clear();
        JSONArray temp = storage.getCompletedOrUncompletedGoals(false);
        for (int i = 0; i < temp.length(); i++) {
            try {
                JSONObject goalToShow = temp.getJSONObject(i);

                //Code that updates the view
                String title = goalToShow.getString("title");
                String date = goalToShow.getString("dueDate");
                Integer category = goalToShow.getInt("category");
                if(category==catIndex) {
                    IndividualGoal newGoal = new IndividualGoal(title, date, category);
                    goalArrayList.add(newGoal);
                }
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO I might got the code wrong.
    private void getAllUnfinishedGoalsSaved() {
        JSONArray temp = storage.getCompletedOrUncompletedGoals(false);//This seems to get all the goals whether checked or unchecked.
        goalArrayList.clear();
        for (int i = 0; i < temp.length(); i++) {
            try {
                JSONObject goalToShow = temp.getJSONObject(i);
                //Code that updates the view
                String title = goalToShow.getString("title");
                String date = goalToShow.getString("dueDate");
                Integer category = goalToShow.getInt("category");

                IndividualGoal newGoal = new IndividualGoal(title, date, category);
                goalArrayList.add(newGoal);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setArrayList(ArrayList<IndividualGoal> ha) {
        for (int i = 0; i <= 5; i++) {
            ha.add(new IndividualGoal("haha", "haha", 0));
            ha.add(new IndividualGoal("bo", "haha", 0));
            ha.add(new IndividualGoal("no", "haha", 0));
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    //TODO This is the code for first-time tutorial.

//    @Override
//    protected void onResume(){
//        super.onResume();
//
//        if(prefs.getBoolean("firstrun",true)){
//            Toast.makeText(HomeScreenActivity.this, "First run!", Toast.LENGTH_SHORT).show();
//
//            View addTargetView = null;
//            for(int i=0; i<homeToolBar.getChildCount();i++){
//                View child = homeToolBar.getChildAt(i);
//                if (ImageButton.class.isInstance(child)){
//                    addTargetView=child;
//                    break;
//                }
//                if (addTargetView==null){
//                    addTargetView = homeToolBar;
//                }
//            }
//
//            ViewTarget addTarget = new ViewTarget(addTargetView);
//
//            new ShowcaseView.Builder(this)
//                    .setTarget(addTarget)
//                    .setContentTitle("ShowcaseView")
//                    .setContentText("This is the hilighting the home button")
//                    .hideOnTouchOutside()
//                    .build();
//            prefs.edit().putBoolean("firstrun",false).commit();
//        }
//    }
}
