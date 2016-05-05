package com.example.tyler.Path2Success;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;


public class HomeScreenActivity extends AppCompatActivity implements Serializable {
    private static final String DEBUGTAG = HomeScreenActivity.class.getSimpleName();
    private ListView listLayout;
    private LocalStorage storage;
    private Button addButton;
    public static final int RESULT_CODE_ADD = 9;
    public static final int RESULT_CODE_EDIT = 12;
    private ArrayList<IndividualGoal> goalArrayList;
    private GoalDataAdapter adapter;
    private Toolbar homeToolBar;
    private ListView goalDrawer;
    private ArrayAdapter<String> drawerAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private MediaPlayer soundPlayer;
    private ArrayList<String> catsArray;
    private int currentCategory = -1;

    public final static String FIRST_RUN = "com.example.tyler.myfirstapp.MESSAGE4";
    public final static String CAT_STORE = "com.example.tyler.myfirstapp.MESSAGE5";
    SharedPreferences runner_record = null;
//    SharedPreferences cat_record = null;


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.storage = new LocalStorage(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        soundPlayer = MediaPlayer.create(this,R.raw.cheer);
        int maxVolume = 50;
        float log1=(float)(Math.log(maxVolume-10)/Math.log(maxVolume));
        soundPlayer.setVolume(1-log1,1-log1);

        runner_record = getSharedPreferences(FIRST_RUN,MODE_PRIVATE);

        homeToolBar = (Toolbar) findViewById(R.id.homescreen_toolbar);
        setSupportActionBar(homeToolBar);
        setTitle("Path 2 Success");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        goalDrawer = (ListView)findViewById(R.id.drawer_list_layout);
        goalDrawer.setOnItemClickListener(new DrawerItemClickListener());

        drawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        setupDrawer();
        catsArray = new ArrayList<>();
        addDrawerItems();
        refreshCategory();

        goalArrayList=new ArrayList<>();
        listLayout = (ListView) findViewById(R.id.homescreen_listview);

        adapter = new GoalDataAdapter(this, goalArrayList);
        listLayout.setAdapter(adapter);
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
                    iG.setIsCompleted(false);
                    a.setChecked(false);
                    Toast.makeText(HomeScreenActivity.this, "Goal unchecked.", Toast.LENGTH_SHORT).show();
                    storage.updateGoal(iG.getRandomID(), iG);

                } else {
                    soundPlayer.start();
                    iG.setIsCompleted(true);
                    a.setChecked(true);
                    storage.updateGoal(iG.getRandomID(), iG);
                }
            }
        });
        refreshGoal();
    }

    private void refreshCategory() {
        catsArray.clear();
        catsArray.addAll(storage.getAllCategoriesToShow());
        if (catsArray.size() == 0) {
            storage.saveNewCategory("Fitness");
            storage.saveNewCategory("Academics");
            storage.saveNewCategory("Miscellaneous");
            catsArray.clear();
            catsArray.addAll(storage.getAllCategoriesToShow());
        }
        catsArray.add(0, "All");
        catsArray.add("History");
        drawerAdapter.notifyDataSetChanged();
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
        drawerAdapter = new ArrayAdapter<>(this,R.layout.drawer_item_info, catsArray);
        goalDrawer.setAdapter(drawerAdapter);
    }

    public void goToEditScreen(View view, Object g, int pos) {
        Intent intent = new Intent(this, EditGoal.class);
        intent.putExtra("IndividualGoal", (Serializable) g); // or could be serializable
        startActivityForResult(intent, RESULT_CODE_EDIT);
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
                currentCategory=-1;
                goBackToMainScreen();
                refreshCategory();
                if(listLayout.getChildCount()!=0
                        && runner_record.getBoolean("firstinput",true)) {
                    editTut();
                    runner_record.edit().putBoolean("firstinput", false).commit();
                }
            }
        }

        else if (requestCode == RESULT_CODE_EDIT) {
            if (resultCode == RESULT_OK) {
                currentCategory = -1;
                goBackToMainScreen();
                refreshCategory();
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

            //HistoryPage
            if(position== catsArray.size()-1){
                Intent a = new Intent(HomeScreenActivity.this, HistoryStore.class);
                startActivity(a);
            }
            //CategoryFilter or get all goals
            else{
                currentCategory=position-1;
                refreshGoal();
                homeToolBar.setTitle(catsArray.get(position));
                drawerLayout.closeDrawers();
            }
        }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    // This is the code for first-time tutorial.

    @Override
    protected void onResume(){
        super.onResume();

        if(runner_record.getBoolean("firstrun",true)){
            navTut();
            runner_record.edit().putBoolean("firstrun",false).commit();
        }
    }

    private void editTut(){
        View editTargetView = listLayout.getChildAt(0).findViewById(R.id.taskContent);
        ViewTarget editTarget = new ViewTarget(editTargetView);
        new ShowcaseView.Builder(this)
                .setTarget(editTarget)
                .setContentTitle("Edit")
                .setContentText("You can edit your goal by clicking and holding one!")
                .hideOnTouchOutside()
                .setStyle(R.style.CustomShowcaseTheme2)
                .build();
    }

    private void navTut(){
        View addTargetView = null;
        for(int i=0; i<homeToolBar.getChildCount();i++){
            View child = homeToolBar.getChildAt(i);
            if (ImageButton.class.isInstance(child)){
                addTargetView=child;
                break;
            }
            if (addTargetView==null){
                addTargetView = homeToolBar;
            }
        }

        ViewTarget addTarget = new ViewTarget(addTargetView);

        new ShowcaseView.Builder(this)
                .setTarget(addTarget)
                .setContentTitle("Menu")
                .setContentText("You can filter your goal and access your history.")
                .hideOnTouchOutside()
                .setStyle(R.style.CustomShowcaseTheme2)
                .build();
    }

    private void refreshGoal(){
        goalArrayList.clear();
        Boolean uncompleted = false;
        goalArrayList.addAll(storage.getGoals(uncompleted, currentCategory));
//        goalArrayList.addAll(storage.getUncompletedGoals(currentCategory));
        adapter.notifyDataSetChanged();
    }

    private void goBackToMainScreen(){
        refreshGoal();
        homeToolBar.setTitle(catsArray.get(0));
    }
}
