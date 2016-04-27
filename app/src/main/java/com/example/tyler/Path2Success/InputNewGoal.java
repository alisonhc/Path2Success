package com.example.tyler.Path2Success;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class InputNewGoal extends AppCompatActivity {
    //final string to bring information to the main activity
    public final static String GOAL_TITLE = "com.example.tyler.myfirstapp.MESSAGE";
    public final static String DUE_DATE = "com.example.tyler.myfirstapp.MESSAGE2";
    public final static String GOAL_CATEGORY = "com.example.tyler.myfirstapp.MESSAGE3";
    public final static String NEW_CAT = "com.example.tyler.myfirstapp.MESSAGE6";
    //an integer to indicate the category of the goal
    private Integer category;
    //private DatePicker dueDate;
    private EditText taskContent;
    private Button addButton;
    private JSONArray goalList;
    private EditText dateInput;
    private EditText categoryInput;
    private Calendar myCalendar;

    private CategoryAdapter adapter;
    private ListView categoryList;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private boolean putTitelIn = false;
    private boolean putDateIn = false;
    private boolean putCategoryIn = false;

    private String newCat = "";
    SharedPreferences cat_record = null;
    private ArrayList<String> catsArray;
    private int cats_count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myCalendar = Calendar.getInstance();
        setContentView(R.layout.activity_input_new_goal);
        addButton = (Button) findViewById(R.id.add_and_back);
        cat_record = getSharedPreferences(HomeScreenActivity.CAT_STORE, MODE_PRIVATE);
        initializeCats();

        makeToolbar("Start a new goal");
        makeGoalInput();
        makeDateInput();
        makeCategoryInput();
    }

    private void makeToolbar(String title) {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.input_toolbar);
        setSupportActionBar(myToolbar);
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeCats() {
        cats_count = cat_record.getInt("cats_size", 0);
        catsArray = new ArrayList<>(cats_count + 1);
        for (int i = 0; i < cats_count; i++) {
            catsArray.add(cat_record.getString("cat_" + i, "Loading error"));
        }
//        Toast.makeText(InputNewGoal.this, cats_size, Toast.LENGTH_SHORT).show();
        catsArray.add("Input your own");
    }


    private void makeCategoryInput(){
        categoryInput = (EditText) findViewById(R.id.categorySelector);
        categoryInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (categoryInput.hasFocus()) {
                    pickCategory();
                    categoryInput.clearFocus();

                }
            }
        });
    }


    private void pickCategory() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a category");
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.category_selector, null);
//        builder.setItems(categoryArray, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                category = which;
//                categoryInput.setText(categoryArray[category]);
//                putCategoryIn = true;
//            }
//        });
        builder.setView(convertView);
        adapter = new CategoryAdapter(this, catsArray);
        categoryList = (ListView) convertView.findViewById(R.id.category_selection);
        categoryList.setAdapter(adapter);
        categoryList.setOnItemClickListener(new CategoryItemClickListener());
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(1200,800);
    }

    private void makeGoalInput() {
        taskContent = (EditText) findViewById(R.id.taskContent);
        taskContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    /**
     * Add a new item, and bring the information from this activity to the main activity
     *
     * @param view
     */
    public void addNewItem(View view) {

        putTitelIn = (taskContent.getText().toString().trim().length() > 0);

        if (putTitelIn && putDateIn && putCategoryIn) {
            Intent intent = this.getIntent();
            String task = taskContent.getText().toString();
            String date = dateInput.getText().toString();
            intent.putExtra(GOAL_TITLE, task);
            intent.putExtra(DUE_DATE, date);
            intent.putExtra(GOAL_CATEGORY, category);
            intent.putExtra(NEW_CAT, newCat);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(InputNewGoal.this, "Cannot save an empty goal", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Hide keyboard
     *
     * @param view
     */
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
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

    private void makeDateInput() {
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
        dateInput.setKeyListener(null);
    }


    private void pickDate() {
        new DatePickerDialog(InputNewGoal.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * This method will update the text in the date field to the date that is selected
     */
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateInput.setText(sdf.format(myCalendar.getTime()).substring(0, 5));
        putDateIn = true;
    }

    private class CategoryItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int pos) {
            if (pos < cats_count) {
                categoryInput.setText(catsArray.get(pos));
                putCategoryIn = true;
                alertDialog.cancel();
            } else {
                alertDialog.cancel();
                inputNewCat();
            }
        }

        private void inputNewCat() {
            AlertDialog.Builder builder = new AlertDialog.Builder(InputNewGoal.this);
            builder.setTitle("Start a new category");

            final EditText input = new EditText(InputNewGoal.this);

            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setSingleLine(true);
            builder.setView(input);

            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newCat = input.getText().toString();
                    categoryInput.setText(newCat);
                    if (newCat.length() != 0) {
                        putCategoryIn = true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

