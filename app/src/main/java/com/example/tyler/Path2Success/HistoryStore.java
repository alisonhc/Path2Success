package com.example.tyler.Path2Success;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
    private IndividualGoal goalToDelete;

    private AlertDialog.Builder warningBuilder;
    private AlertDialog warningDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storage = new LocalStorage(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_store);
        goalArrayList=new ArrayList<>();

        Boolean completed = true;
        int allCategories = -1;
        goalArrayList.addAll(storage.getGoals(completed, allCategories));
//        goalArrayList.addAll(storage.getCompletedGoals());

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
                    iG.setIsCompleted(false);
                    a.setChecked(false);
                    Toast.makeText(HistoryStore.this, "This goal has been added to your home screen.", Toast.LENGTH_SHORT).show();
                    storage.updateGoal(iG.getRandomID(), iG);
                } else {
                    iG.setIsCompleted(true);
                    a.setChecked(true);
                    storage.updateGoal(iG.getRandomID(), iG);
                }
            }
        });
        historyList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // or i could have changed to java.lang.object instead. which is better? idk you tell me
                Object g = parent.getAdapter().getItem(position);
                goalToDelete = (IndividualGoal) g;
                String randomID = goalToDelete.getRandomID();
                deleteWarning();
                return true;
            }


        });
    }

    private void deleteWarning(){
        warningBuilder = new AlertDialog.Builder(this);
        warningBuilder.setTitle("Do you want to delete this goal?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storage.removeGoal(goalToDelete);
                        refreshGoals();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setMessage("You will not be able to recover the goal once it has been deleted.")
                .setCancelable(true);
        warningDialog = warningBuilder.create();
        warningDialog.show();
    }

    private void refreshGoals(){
        goalArrayList.clear();
        Boolean completed = true;
        int allCategories = -1;
        goalArrayList.addAll(storage.getGoals(completed, allCategories));
        adapter.notifyDataSetChanged();
    }
}
