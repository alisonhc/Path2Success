package com.example.tyler.Path2Success;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InputNewGoal extends AppCompatActivity {
    //final string to bring information to the main activity
    public final static String GOAL_TITLE = "com.example.tyler.myfirstapp.MESSAGE";
    public final static String DUE_DATE = "com.example.tyler.myfirstapp.MESSAGE2";
    public final static String GOAL_CATEGORY = "com.example.tyler.myfirstapp.MESSAGE3";
    public final static String FILENAME = "goal_file";
    //an integer to indicate the category of the goal
    private Integer category;
    //private DatePicker dueDate;
    private EditText taskContent;
    private Button addButton;
    private JSONArray goalList;
    private EditText dateInput;
    private EditText categoryInput;
    private Calendar myCalendar;
    private TextView repeatOptionView;

    //Make this an array that is retrieved from internal storage every time.
    private CharSequence categories[] =new CharSequence[]{"Fitness","Academics", "Miscellaneous"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myCalendar = Calendar.getInstance();
        setContentView(R.layout.activity_input_new_goal);
        addButton = (Button) findViewById(R.id.add_and_back);
        //dueDate = (DatePicker) findViewById(R.id.datePicker);
        taskContent = (EditText) findViewById(R.id.taskContent);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.input_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Start a new goal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        //Handle date input
        dateInput = (EditText) findViewById(R.id.datePicker);
        dateInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (dateInput.hasFocus()) {
                    pickDate();
                    dateInput.clearFocus();
                }
            }
        });
        taskContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        dateInput.setKeyListener(null);


        //Handle category input
        categoryInput = (EditText)findViewById(R.id.categorySelector);
        categoryInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(categoryInput.hasFocus()){
                    pickCategory();
                    categoryInput.clearFocus();

                }
            }
        });
    }


    private void pickCategory(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a category");
        builder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                category = which;
                categoryInput.setText(categories[category]);
            }
        });
        builder.show();
    }


    /**
     * Add a new itme, and bring the information from this activity to the main activity
     * @param view
     */
    public void addNewItem(View view){
        ////            Making a test toast
//        Context context = getApplicationContext();
//        int duration = Toast.LENGTH_LONG;
//        String text = "Goal successfully written to device!\n";
//        Toast toast = Toast.makeText(context, text, duration);
//        toast.show();
        Intent intent = new Intent();
        String task = taskContent.getText().toString();
        String date = dateInput.getText().toString();
        intent.putExtra(GOAL_TITLE, task);
        intent.putExtra(DUE_DATE, date);
        intent.putExtra(GOAL_CATEGORY, category);
        setResult(Activity.RESULT_OK, intent);
        finish();
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        //adds a javascript object to local storage
        try {
            JSONObject goal;

            goal = new JSONObject();
            goal.put("title", task);
            goal.put("date", date);
            goal.put("isChecked", false);
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

    /**
     * Hide keyboard
     * @param view
     */
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();
        }

    };


    private void pickDate(){
        new DatePickerDialog(InputNewGoal.this, date,myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * This method will update the text in the date field to the date that is selected
     */
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateInput.setText(sdf.format(myCalendar.getTime()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }
}
