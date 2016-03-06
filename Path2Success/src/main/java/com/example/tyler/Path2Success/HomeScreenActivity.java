package com.example.tyler.Path2Success;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class HomeScreenActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.tyler.myfirstapp.MESSAGE";
    public final static String EXTRA_MESSAGE2 = "com.example.tyler.myfirstapp.MESSAGE2";
    private ListView mLayout;
    public static final String FILENAME = "goal_file";
    private EditText mText1;
    private EditText mText2;
    private Button mButton;
    private LayoutTransition mTransition;
    private ArrayList<IndividualGoal> itemListHolder=new ArrayList<>();
    //private ArrayList<String> itemListHolder =new ArrayList<>();
    private GoalDataAdapter adapter;
    //private ArrayAdapter<String> adapter;
    private JSONArray goalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mLayout = (ListView) findViewById(R.id.checkboxes);

        mButton = (Button) findViewById(R.id.theButton);
        mText1 = (EditText) findViewById(R.id.edit_message);
        mText2 = (EditText) findViewById(R.id.edit_message2);
        mTransition = new LayoutTransition();
        mButton.setOnClickListener(onClick());
      //  mLayout.setLayoutTransition(mTransition);
       // mTransition.setAnimateParentHierarchy(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Path 2 Success");
        adapter=new GoalDataAdapter(this,itemListHolder);
        mLayout.setAdapter(adapter);

        //add prior goals stored locally
        goalList = new JSONArray();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

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

//            JSONArray goals = new JSONArray(b.toString());
            goalList = new JSONArray(b.toString());

//            String text = goals.getJSONObject(0).getString("title");

            Toast toast = Toast.makeText(context, String.valueOf(goalList.length()), duration);
//            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            for (int i = 0; i < goalList.length(); i++) {
                String title = goalList.getJSONObject(i).getString("title");
                String date = goalList.getJSONObject(i).getString("date");

                //here add each corresponding checkbox to the view

//                CheckBox cBox = createNewCheckBox(title + " " + date);
//                cBox.setOnClickListener(onClickBox(cBox));
//                mLayout.addView(cBox);
//                mTransition.addChild(mLayout,cBox);
                IndividualGoal newGoal = new IndividualGoal(title, date);
                itemListHolder.add(newGoal);
                adapter.notifyDataSetChanged();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //on load, we want to read all of the task objects into the view
        //load them in the same way they are printed in the AddNewItem way
        //maybe make a new method (that comes from this onCreate method) that adds
        //    the previously stored goals to the view
    }

//
//
//    private CheckBox createNewCheckBox(String text) {
//        final AbsListView.LayoutParams lparams = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
//        final CheckBox checkBox = new CheckBox(this);
//
//        checkBox.setLayoutParams(lparams);
//        checkBox.setText(text);
//        return checkBox;
//    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                CheckBox cBox = createNewCheckBox(mText1.getText().toString() + " " + mText2.getText().toString());
//                cBox.setOnClickListener(onClickBox(cBox));
//                mLayout.addFooterView(cBox);
//                mTransition.addChild(mLayout,cBox);

                //Making a test toast
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;

                String title = mText1.getText().toString();
                String date = mText2.getText().toString();

                //add new goal to local storage
//                String FILENAME = "goal_file";
                try {
                    //here is where you would make a javascript object and add it to local storage
//                    JSONArray goalList = new JSONArray();
                    JSONObject goal;

                    goal = new JSONObject();
                    goal.put("title", title);
                    goal.put("date", date);
                    goalList.put(goal);

                    String goals = goalList.toString();

                    FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
//                    String combinedString = title + " " + date;
                    fos.write(goals.getBytes());
                    fos.close();

                    //Making a test toast
                    String text = "Goal successfully written to device!\n" + goals;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                IndividualGoal newGoal = new IndividualGoal(title, date);
                itemListHolder.add(newGoal);
                adapter.notifyDataSetChanged();


//                CheckBox cBox = createNewCheckBox(mText1.getText().toString() + " " + mText2.getText().toString());
//                cBox.setOnClickListener(onClickBox(cBox));
//                mLayout.addView(cBox);
//                mTransition.addChild(mLayout,cBox);

                //myLayout.addView
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                mText1.setText("");
                mText2.setText("");
            }
        };
    }

//    private View.OnClickListener onClickBox(View box){
//        return new View.OnClickListener(){
//            @Override
//            public void onClick(View box){
//           //     mTransition.setStagger(LayoutTransition.DISAPPEARING,100);
//                mLayout.removeView(box);
//            }
//        };
//
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up buttton, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
