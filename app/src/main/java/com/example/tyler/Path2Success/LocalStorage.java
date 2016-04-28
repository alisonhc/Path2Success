package com.example.tyler.Path2Success;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by pbertel on 4/7/16.
 * Referenced from http://chrisrisner.com/31-Days-of-Android--Day-23-Writing-and-Reading-Files
 */
public class LocalStorage {
    private static final String DEBUGTAG = LocalStorage.class.getSimpleName();
    public final static String GOAL_FILE = "goal_file";
    public final static String CATEGORY_FILENAME = "category_file";
    private Context appContext;

    public LocalStorage(Context c) {
        appContext = c;
//        Log.d(DEBUGTAG, "Working!!");
    }

//    private JSONObject getAllCategories(){
//        JSONObject allCategories = new JSONObject();
//        try{
//            FileInputStream fis = appContext.openFileInput(CATEGORY_FILENAME);
//            BufferedInputStream bis = new BufferedInputStream(fis);
//
//
//        }
//    }
    private JSONObject getAllGoals() {

        JSONObject allGoals = new JSONObject();

        try {
            FileInputStream fis = appContext.openFileInput(GOAL_FILE);
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while (bis.available() != 0) {
                char c = (char) bis.read();
                b.append(c);
            }
            bis.close();
            fis.close();
            allGoals = new JSONObject(b.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return allGoals;
    }

    //If the user passes in 'true' as the value of bool, they wish to retrieve all completed goals
    // return an ArrayList<IndividualGoal>
    public ArrayList getCompletedOrUncompletedGoals(Boolean bool, int filterIndex) {
        //JSONArray completedOrUncompletedGoals = new JSONArray();
        ArrayList<IndividualGoal> individualGoalArrayList = new ArrayList<>();
        JSONObject allGoals = getAllGoals();
//        Log.d(DEBUGTAG, "All the goals are: " + allGoals.toString());
        //Iterator code found from
        // http://stackoverflow.com/questions/13573913/android-jsonobject-how-can-i-loop-through-a-flat-json-object-to-get-each-key-a
        Iterator<String> iterator = allGoals.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                JSONObject iteratedGoal = allGoals.getJSONObject(key);
                Boolean completed = iteratedGoal.getBoolean("completed");
                if(bool.equals(completed)) {
                    Integer category = iteratedGoal.getInt("category");
                    if (category==filterIndex||filterIndex==-1){
                        String title = iteratedGoal.getString("title");
                        String date = iteratedGoal.getString("dueDate");
                        String id = iteratedGoal.getString("id");
                        IndividualGoal goalToAdd = new IndividualGoal(title, date, category);
                        goalToAdd.setRandomID(id);
                        individualGoalArrayList.add(goalToAdd);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        Log.d(DEBUGTAG, "Goals to be returned are: " + completedOrUncompletedGoals.toString());
        return individualGoalArrayList;
    }

    public void saveNewGoal(IndividualGoal goalToAdd) {
        try {
            String title = goalToAdd.getTitle();
            String dueDate = goalToAdd.getDueDate();
            Integer category = goalToAdd.getCategory();

            JSONObject newGoal = new JSONObject();
            newGoal.put("title", title);
            newGoal.put("dueDate", dueDate);
            newGoal.put("category", category);
            newGoal.put("completed", false);

            Random rand = new Random();
            Integer randInt = rand.nextInt(10000);
            String goalID = randInt.toString();

            JSONObject allGoals = getAllGoals();

            while (allGoals.has(goalID)) {
                randInt = rand.nextInt(10000);
                goalID = randInt.toString();
            }

            newGoal.put("id", goalID);

            allGoals.put(goalID, newGoal);
            goalToAdd.setRandomID(goalID);

            //The goals JSON Object must be converted to a string before being
            // written to local storage
//            String convertedGoals = allGoals.toString();
//
//            FileOutputStream fos = appContext.openFileOutput(GOAL_FILE, Context.MODE_PRIVATE);
//            fos.write(convertedGoals.getBytes());
//            fos.close();

            writeAllGoalsLocally(allGoals);
        }

//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //this does not work yet
    //this method is difficult, should probably work on new method of storage (uniqueID for each goal, a single JSON object, etc.)
    public void setCompleted(IndividualGoal goal, Boolean value) {

//        Log.d(DEBUGTAG, "Working!!");

        try {

            JSONObject allGoals = getAllGoals();
            //goalToChange = goalList.getJSONObject(position);
            //instead, here we will get the JSON object that corresponds to the unique ID provided from the Individual Goal class

            String goalID = goal.getRandomID();
            JSONObject goalToChange = allGoals.getJSONObject(goalID);
            goalToChange.remove("completed");
            goalToChange.put("completed", value);

            //now put the goalList back in to local storage
            String convertedGoals = allGoals.toString();

            FileOutputStream fos = appContext.openFileOutput(GOAL_FILE, Context.MODE_PRIVATE);
            fos.write(convertedGoals.getBytes());
            fos.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateGoal(String previousGoalID, IndividualGoal newGoal) {
        JSONObject goals = getAllGoals();
//        goals.remove(previousGoalID);

        String title = newGoal.getTitle();
        String dueDate = newGoal.getDueDate();
        Integer category = newGoal.getCategory();

        try {
            JSONObject goalToUpdate = goals.getJSONObject(previousGoalID);
            goalToUpdate.put("title", title);
            goalToUpdate.put("dueDate", dueDate);
            goalToUpdate.put("category", category);
//            Toast.makeText(appContext, "Goal is: " + goalToUpdate.toString(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        writeAllGoalsLocally(goals);

        //before this, all goals need to be updated
        saveNewGoal(newGoal);
    }

    public void writeAllGoalsLocally(JSONObject allGoals) {
        String convertedGoals = allGoals.toString();

        try {
            FileOutputStream fos = appContext.openFileOutput(GOAL_FILE, Context.MODE_PRIVATE);
            fos.write(convertedGoals.getBytes());
            fos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
