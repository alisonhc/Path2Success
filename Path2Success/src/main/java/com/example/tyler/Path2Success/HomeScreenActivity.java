package com.example.tyler.Path2Success;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

//Import statements for writing to local memory
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Path 2 Success");
        adapter = new GoalDataAdapter(this, goalArrayList);
        listLayout.setAdapter(adapter);
        goalList = new JSONArray();


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
                IndividualGoal newGoal = new IndividualGoal(task, date);
                goalArrayList.add(newGoal);
                adapter.notifyDataSetChanged();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//
//            for (int i = 0; i < goalList.length(); i++) {
//                String title = goalList.getJSONObject(i).getString("title");
//                String date = goalList.getJSONObject(i).getString("date");
//
//                //here add each corresponding checkbox to the view
//                IndividualGoal newGoal = new IndividualGoal(title, date);
//                goalArrayList.add(newGoal);
//                adapter.notifyDataSetChanged();
//
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        adapter=new GoalDataAdapter(this, goalArrayList);
//        listLayout.setAdapter(adapter);
//    }

//    private View.OnClickListener onClick() {
//        return new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                String title = taskContent.getText().toString();
//                String date = dueDate.getText().toString();
//
//
//                IndividualGoal newGoal = new IndividualGoal(title, date);
//                goalArrayList.add(newGoal);
//                adapter.notifyDataSetChanged();
//
//                //Making a test toast
//                Context context = getApplicationContext();
//                int duration = Toast.LENGTH_LONG;
//
//                //add new goal to local storage
//                String FILENAME = "goal_file";
//                try {
//                    //here is where you would make a javascript object and add it to local storage
////                    JSONArray goalList = new JSONArray();
//                    JSONObject goal;
//
//                    goal = new JSONObject();
//                    goal.put("title", title);
//                    goal.put("date", date);
//                    goalList.put(goal);
//
//                    String goals = goalList.toString();
//
//                    FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
////                    String combinedString = title + " " + date;
//                    fos.write(goals.getBytes());
//                    fos.close();
//
//                    //Making a test toast
//                    String text = "Goal successfully written to device!\n" + goals;
//                    Toast toast = Toast.makeText(context, text, duration);
//                    toast.show();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
////                CheckBox cBox = createNewCheckBox(mText1.getText().toString() + " " + mText2.getText().toString());
////                cBox.setOnClickListener(onClickBox(cBox));
////                mLayout.addView(cBox);
////                mTransition.addChild(mLayout,cBox);

//    private View.OnClickListener onClickBox(View box){
//        return new View.OnClickListener(){
//            @Override
//            public void onClick(View box){
//           //     mTransition.setStagger(LayoutTransition.DISAPPEARING,100);
//                listLayout.removeView(box);
//            }
//        };
//
//
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_my, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up buttton, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /** Called when the user clicks the Send button */
    public void goToInputScreen(View view) {
        Intent intent = new Intent(this, InputNewGoal.class);
        startActivityForResult(intent, RESULT_CODE);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
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
                if(!tContent.isEmpty()) {
                    IndividualGoal newGoal = new IndividualGoal(tContent, tDate);
                    goalArrayList.add(newGoal);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
