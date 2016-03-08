package com.example.tyler.Path2Success;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputNewGoal extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.tyler.myfirstapp.MESSAGE";
    private Button addButton;
    private EditText taskContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_new_goal);
        addButton = (Button) findViewById(R.id.add_and_back);
        taskContent = (EditText) findViewById(R.id.taskContent);
    }

    public void addNewItem(View view){
        Intent intent = new Intent();
        String task = taskContent.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, task);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
