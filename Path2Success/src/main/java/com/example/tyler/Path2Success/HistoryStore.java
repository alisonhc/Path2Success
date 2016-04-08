package com.example.tyler.Path2Success;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.tyler.Path2Success.R;

public class HistoryStore extends AppCompatActivity {
    public static final int RESULT_CODE = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_button_on_history);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryStore.this, InputNewGoal.class);
                startActivityForResult(intent, RESULT_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
