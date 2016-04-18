package com.example.tyler.Path2Success;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryStore extends AppCompatActivity {

    private HistoryDataAdapter adapter;
    private ListView historyList;
    private LinearLayoutManager llm;
    private CollapsingToolbarLayout collapsingToolbar;
    private ArrayList<IndividualGoal> goalArrayList =new ArrayList<>();//This need to be from the internal, of the goal that is checked.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_store);
        //Initialize goalArrayList here
        goalArrayList=new ArrayList<>();

        setArrayList();

        historyList = (ListView) findViewById(R.id.historyscreen_listview);
        adapter = new HistoryDataAdapter(this,goalArrayList);
        historyList.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.historyscreen_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("History!");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.history_add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryStore.this, InputNewGoal.class);
                startActivityForResult(intent, HomeScreenActivity.RESULT_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter.notifyDataSetChanged();

        //TODO OnItemClickListener storage implement
        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IndividualGoal iG = adapter.getItem(position);
                CheckedTextView a = (CheckedTextView) ((LinearLayout) view).getChildAt(0);
                if (a.isChecked()) {
                    iG.goalIsUndone();
                    a.setChecked(false);
//                    Toast.makeText(HomeScreenActivity.this, "unchecked: " + iG.getTitle() + " " + iG.getCategory(), Toast.LENGTH_SHORT).show();
                 //   storage.setCompleted(iG, false);

                } else {
                    iG.goalIsDone();
                    a.setChecked(true);
//                    Toast.makeText(HomeScreenActivity.this, "checked: " + iG.getTitle(), Toast.LENGTH_SHORT).show();
                 //   storage.setCompleted(iG, true);
                }
            }
        });

    }


    private void setArrayList(){
        for(int i=0; i<=5;i++) {
            goalArrayList.add(new IndividualGoal("haha", "haha", 0));
            goalArrayList.add(new IndividualGoal("bo", "haha", 0));
            goalArrayList.add(new IndividualGoal("no", "haha", 0));
            goalArrayList.add(new IndividualGoal("oo", "haha", 0));
            goalArrayList.add(new IndividualGoal("h", "haha", 0));
        }
    }
}
