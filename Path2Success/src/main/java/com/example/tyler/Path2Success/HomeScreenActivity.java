package com.example.tyler.Path2Success;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.effect.Effect;
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
import java.util.ArrayList;

//Import statements for writing to local memory
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class HomeScreenActivity extends AppCompatActivity {

    public static final String FILENAME = "goal_file";
    public final static String EXTRA_MESSAGE = "com.example.tyler.myfirstapp.MESSAGE";
    public final static String EXTRA_MESSAGE2 = "com.example.tyler.myfirstapp.MESSAGE2";
    private ListView listLayout;
  //  private EditText taskContent;
  //  private EditText dueDate;
    private Button addButton;
    private LayoutTransition mTransition;
    public static final int RESULT_CODE = 9;
    private ArrayList<IndividualGoal> goalArrayList =new ArrayList<>();
    private GoalDataAdapter adapter;
    private JSONArray goalList;
    private ListView goalDrawer;
    private ArrayAdapter<String> drawerAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private MediaPlayer soundPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        listLayout = (ListView) findViewById(R.id.checkboxes);

        //    addButton = (Button) findViewById(R.id.add_a_new_task);
        //   taskContent = (EditText) findViewById(R.id.edit_message);
        //    mTransition = new LayoutTransition();
        //   addButton.setOnClickListener(onClick());
        //  listLayout.setLayoutTransition(mTransition);
        // mTransition.setAnimateParentHierarchy(false);

        soundPlayer = MediaPlayer.create(this,R.raw.harp);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.homescreen_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Path 2 Success");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        goalDrawer = (ListView)findViewById(R.id.drawer_list_layout);
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

                } else {
                    soundPlayer.start();
                    iG.goalIsDone();
                    a.setChecked(true);
                    Toast.makeText(HomeScreenActivity.this, "checked: "+iG.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }
        });


            //Code used from http://chrisrisner.com/31-Days-of-Android--Day-23-Writing-and-Reading-Files/
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while (bis.available() != 0) {
                char c = (char) bis.read();
                b.append(c);
            }
            bis.close();
            fis.close();

            goalList = new JSONArray(b.toString());
//            Toast toast = Toast.makeText(context, String.valueOf(goalList.length()), duration);
//            Toast toast = Toast.makeText(context, String.valueOf(goalList), duration);
//            toast.show();

            for (int i = 0; i < goalList.length(); i++) {
                String task = goalList.getJSONObject(i).getString("title");
                String date = goalList.getJSONObject(i).getString("date");

                //Please change the code here since we have added a new input for the constructor of IndividualGoal)
                IndividualGoal newGoal = new IndividualGoal(task, date,0);
                goalArrayList.add(newGoal);
                adapter.notifyDataSetChanged();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

    private void addDrawerItems(){
        String[] dArray = {"aaa", "bbb", "ccc"};
        drawerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dArray);
        goalDrawer.setAdapter(drawerAdapter);
    }

    /** Called when the user clicks the Send button */
    public void goToInputScreen(View view) {
        Intent intent = new Intent(this, InputNewGoal.class);
        startActivityForResult(intent, RESULT_CODE);
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == RESULT_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                //what does tContent mean?? what is it's purpose in the code?
                String tContent = data.getStringExtra(InputNewGoal.EXTRA_MESSAGE);
                String tDate = data.getStringExtra((InputNewGoal.EXTRA_MESSAGE2));
                Integer tCategory=data.getIntExtra((InputNewGoal.EXTRA_MESSAGE3),0);
                if(!tContent.isEmpty()) {
                    IndividualGoal newGoal = new IndividualGoal(tContent, tDate,tCategory);
                    goalArrayList.add(newGoal);
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
}
