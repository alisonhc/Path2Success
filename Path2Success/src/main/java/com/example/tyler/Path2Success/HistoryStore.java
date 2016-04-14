package com.example.tyler.Path2Success;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tyler.Path2Success.R;

import java.util.ArrayList;

public class HistoryStore extends AppCompatActivity {

    private HistoryDataAdapter adapter;
    private RecyclerView historyList;
    private LinearLayoutManager llm;
    private CollapsingToolbarLayout collapsingToolbar;
    private ArrayList<IndividualGoal> goalArrayList =new ArrayList<>();//This need to be from the internal, of the goal that is checked.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_store);
        //Initialize goalArrayList here
        goalArrayList=new ArrayList<>();
        setHistoryListView();
        LinearLayoutManager llm = new LinearLayoutManager(this);

        historyList = (RecyclerView) findViewById(R.id.history_view);
        adapter = new HistoryDataAdapter(goalArrayList);
        historyList.setAdapter(adapter);
        historyList.setLayoutManager(llm);

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
        adapter.notifyDataSetChanged();

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle("History");
    }


    private void setHistoryListView(){
        for(int i=0; i<=5;i++) {
            goalArrayList.add(new IndividualGoal("haha", "haha", 0));
            goalArrayList.add(new IndividualGoal("bo", "haha", 0));
            goalArrayList.add(new IndividualGoal("no", "haha", 0));
            goalArrayList.add(new IndividualGoal("oo", "haha", 0));
            goalArrayList.add(new IndividualGoal("h", "haha", 0));
        }
    }
}
