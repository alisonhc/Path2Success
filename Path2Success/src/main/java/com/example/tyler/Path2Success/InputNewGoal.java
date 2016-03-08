package com.example.tyler.Path2Success;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;

public class InputNewGoal extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.tyler.myfirstapp.MESSAGE";
    public final static String EXTRA_MESSAGE2 = "com.example.tyler.myfirstapp.MESSAGE2";
    private Button addButton;
    private DatePicker dueDate;
    private EditText taskContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_new_goal);
        addButton = (Button) findViewById(R.id.add_and_back);
        dueDate = (DatePicker) findViewById(R.id.datePicker);
        taskContent = (EditText) findViewById(R.id.taskContent);
    }

    public void addNewItem(View view){
        Intent intent = new Intent();
        String task = taskContent.getText().toString();
        String date = Integer.toString(dueDate.getMonth()+1) + "/" + Integer.toString(dueDate.getDayOfMonth());
        intent.putExtra(EXTRA_MESSAGE, task);
        intent.putExtra(EXTRA_MESSAGE2, date);
        setResult(Activity.RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
