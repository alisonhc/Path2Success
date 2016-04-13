package com.example.tyler.Path2Success;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.tyler.Path2Success.R;

import java.util.ArrayList;

public class HistoryStore extends AppCompatActivity {

    private HistoryDataAdapter adapter;
    private ListView historyList;
    private ArrayList<IndividualGoal> goalArrayList =new ArrayList<>();//This need to be from the internal, of the goal that is checked.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyList = (ListView)findViewById(R.id.history_view);
        //Initialize goalArrayList here
        adapter = new HistoryDataAdapter(this, goalArrayList);
        setContentView(R.layout.activity_history_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_button_on_history);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryStore.this, InputNewGoal.class);
                startActivityForResult(intent, HomeScreenActivity.RESULT_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHistoryListView();
    }
    private void setHistoryListView(){
        historyList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
