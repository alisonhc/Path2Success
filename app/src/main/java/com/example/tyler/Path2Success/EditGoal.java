package com.example.tyler.Path2Success;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class EditGoal extends AppCompatActivity {


    //TODO can we have a common activities for EditGoal and InputNewGoal, that they will all extend this class

    //final string to bring information to the main activity
//    public final static String GOAL_TITLE = "com.example.tyler.myfirstapp.MESSAGE7";
//    public final static String DUE_DATE = "com.example.tyler.myfirstapp.MESSAGE8";
//    public final static String GOAL_CATEGORY = "com.example.tyler.myfirstapp.MESSAGE9";
//    public final static String GOAL_ID = "com.example.tyler.myfirstapp.MESSAGE10";
    //an integer to indicate the category of the goal
    private Integer category;
    //private DatePicker dueDate;
    private EditText taskContent;
    private Button addButton;
    private JSONArray goalList;
    private EditText dateInput;
    private EditText categoryInput;
    private Calendar myCalendar;
    private IndividualGoal goal;

    private CategoryAdapter adapter;
    private ListView categoryList;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private boolean putTitelIn = false;
    private boolean putDateIn = false;

    private String newCat = "";
    SharedPreferences cat_record = null;
    private ArrayList<String> catsArray;
    private int cats_count = 0;

    //Make this an array that is retrieved from internal storage every time.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent homeIntent = getIntent();
        goal = (IndividualGoal) homeIntent.getSerializableExtra("IndividualGoal");
        myCalendar = Calendar.getInstance();
        setContentView(R.layout.activity_edit_goal);
        addButton = (Button) findViewById(R.id.add_and_back);
        //dueDate = (DatePicker) findViewById(R.id.datePicker);
        taskContent = (EditText) findViewById(R.id.taskContent);
        taskContent.setText(goal.getTitle());
        cat_record = getSharedPreferences(HomeScreenActivity.CAT_STORE, MODE_PRIVATE);


        initializeCats();


        Toolbar myToolbar = (Toolbar) findViewById(R.id.input_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Edit goal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Handle date input
        dateInput = (EditText) findViewById(R.id.datePicker);
        dateInput.setText(goal.getDueDate());
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
        category = goal.getCategory();
        categoryInput.setText(catsArray.get(goal.getCategory()));
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
// get info from putExtra and then setText to

    private void pickCategory() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a category");
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.category_selector, null);

        builder.setView(convertView);
        adapter = new CategoryAdapter(this, catsArray);
        categoryList = (ListView) convertView.findViewById(R.id.category_selection);
        categoryList.setAdapter(adapter);
        categoryList.setOnItemClickListener(new CategoryItemClickListener());
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(1200,800);
    }
    private class CategoryItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int pos) {
            if (pos < cats_count) {
                categoryInput.setText(catsArray.get(pos));
                category=pos;
                alertDialog.cancel();
            } else {
                alertDialog.cancel();
                inputNewCat();
            }
        }

        private void inputNewCat() {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditGoal.this);
            builder.setTitle("Start a new category");

            final EditText input = new EditText(EditGoal.this);

            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setSingleLine(true);
            builder.setView(input);

            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newCat = input.getText().toString();
                    categoryInput.setText(newCat);
                    if (newCat.length() != 0) {
                        category = cats_count;
                        writeInternally();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }

        private void writeInternally() {
            SharedPreferences.Editor editor = cat_record.edit();
            editor.remove("cats_size");
            editor.putInt("cats_size", cats_count + 1);
            editor.putString("cat_" + cats_count, newCat);
            editor.commit();
        }
    }


    /**
     * Add a new item, and bring the information from this activity to the main activity
     * @param view
     */
    public void addNewItem(View view){
        LocalStorage storage = new LocalStorage(this.getApplicationContext());
        Intent intent = getIntent();
        String task = taskContent.getText().toString();
        if (task.length()!=0){
            putTitelIn = true;
        }
        String date = dateInput.getText().toString();
        if (date.length()!=0){
            putDateIn = true;
        }
        if(putDateIn&&putTitelIn) {
            String id = goal.getRandomID();
            goal.setTitle(task);
            goal.setDueDate(date);
            goal.setCategory(category);
            storage.updateGoal(id, goal);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        else {
            Toast.makeText(EditGoal.this, "Cannot save goal without all options filled", Toast.LENGTH_SHORT).show();
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
        new DatePickerDialog(EditGoal.this, date,myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * This method will update the text in the date field to the date that is selected
     */
    private void updateLabel() {
        String myFormat = "MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateInput.setText(sdf.format(myCalendar.getTime()));

    }

    private void initializeCats() {
        cats_count = cat_record.getInt("cats_size", 0);
        catsArray = new ArrayList<>(cats_count + 1);
        for (int i = 0; i < cats_count; i++) {
            catsArray.add(cat_record.getString("cat_" + i, "Loading error"));
        }

        catsArray.add("Input your own");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }
}
