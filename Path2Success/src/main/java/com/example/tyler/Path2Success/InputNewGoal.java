package com.example.tyler.Path2Success;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class InputNewGoal extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.tyler.myfirstapp.MESSAGE";
    public final static String EXTRA_MESSAGE2 = "com.example.tyler.myfirstapp.MESSAGE2";
    public final static String FILENAME = "goal_file";
    private Button addButton;
    private DatePicker dueDate;
    private EditText taskContent;
    private JSONArray goalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_new_goal);
        addButton = (Button) findViewById(R.id.add_and_back);
        dueDate = (DatePicker) findViewById(R.id.datePicker);
        taskContent = (EditText) findViewById(R.id.taskContent);
        goalList = new JSONArray();

        //goalList must be the goal list that has accumulated all the previous goals
        //so, we need this below chunk of code to make sure goalList is up to date
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
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addNewItem(View view){
        ////            Making a test toast
//        Context context = getApplicationContext();
//        int duration = Toast.LENGTH_LONG;
//        String text = "Goal successfully written to device!\n";
//        Toast toast = Toast.makeText(context, text, duration);
//        toast.show();
        Intent intent = new Intent();
        String task = taskContent.getText().toString();
        String date = Integer.toString(dueDate.getMonth()+1) + "/" + Integer.toString(dueDate.getDayOfMonth());
        intent.putExtra(EXTRA_MESSAGE, task);
        intent.putExtra(EXTRA_MESSAGE2, date);
        setResult(Activity.RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        //adds a javascript object to local storage
        try {
            JSONObject goal;

            goal = new JSONObject();
            goal.put("title", task);
            goal.put("date", date);
            goalList.put(goal);
            String goals = goalList.toString();

            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(goals.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
