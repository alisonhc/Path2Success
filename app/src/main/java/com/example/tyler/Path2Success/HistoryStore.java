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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryStore extends AppCompatActivity {

    private HistoryDataAdapter adapter;
    private ListView historyList;
    private ArrayList<IndividualGoal> goalArrayList;//This need to be from the internal, of the goal that is checked.
    private LocalStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storage = new LocalStorage(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_store);
        goalArrayList=new ArrayList<>();

        goalArrayList.addAll(storage.getCompletedOrUncompletedGoals(true,-1));

        historyList = (ListView) findViewById(R.id.historyscreen_listview);
        adapter = new HistoryDataAdapter(this,goalArrayList);
        historyList.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.historyscreen_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("History!");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter.notifyDataSetChanged();

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IndividualGoal iG = adapter.getItem(position);
                CheckedTextView a = (CheckedTextView) ((LinearLayout) view).getChildAt(0);
                if (a.isChecked()) {
                    iG.goalIsUndone();
                    a.setChecked(false);
                    Toast.makeText(HistoryStore.this, "This goal has been added to your home screen.", Toast.LENGTH_SHORT).show();
                    storage.setCompleted(iG, false);

                } else {
                    iG.goalIsDone();
                    a.setChecked(true);
                    storage.setCompleted(iG, true);
                }
            }
        });
    }
}
