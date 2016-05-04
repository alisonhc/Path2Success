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
 * Code for reading and writing to local storage was referenced from
 *      http://chrisrisner.com/31-Days-of-Android--Day-23-Writing-and-Reading-Files
 */
public class LocalStorage {
    public final static String GOAL_FILE = "goal_file";
    public final static String CATEGORY_FILENAME = "category_file";
    private Context appContext;

    public LocalStorage(Context c) {
        appContext = c; //having the context is necessary to access the phone's local storage
    }

    private JSONObject getAllCategories(){
        JSONObject allCategories = new JSONObject();
        try{
            FileInputStream fis = appContext.openFileInput(CATEGORY_FILENAME);
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while (bis.available() != 0){
                char c = (char) bis.read();
                b.append(c);
            }
            bis.close();
            fis.close();
            allCategories = new JSONObject(b.toString());

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return allCategories;
    }
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

    public ArrayList getAllCategoriesToShow(){
        ArrayList<String> categoryArrayList = new ArrayList<>();
        JSONObject allCategories = getAllCategories();
        Iterator<String> iterator = allCategories.keys();
        while(iterator.hasNext()){
            String key = iterator.next();
            try{
                JSONObject iteratedCategory = allCategories.getJSONObject(key);
                categoryArrayList.add(iteratedCategory.getString("categoryTitle"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return categoryArrayList;
    }

    public ArrayList getCompletedGoals() {
        ArrayList<IndividualGoal> completedGoals = new ArrayList<>();
        JSONObject allGoals = getAllGoals();
        //Iterator code found from
        // http://stackoverflow.com/questions/13573913/android-jsonobject-how-can-i-loop-through-a-flat-json-object-to-get-each-key-a
        Iterator<String> iterator = allGoals.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                JSONObject iteratedGoal = allGoals.getJSONObject(key);
                Boolean goalIsCompleted = iteratedGoal.getBoolean("completed");
                if (goalIsCompleted) {
                    Integer category = iteratedGoal.getInt("category");
                    String title = iteratedGoal.getString("title");
                    String date = iteratedGoal.getString("dueDate");
                    String id = iteratedGoal.getString("id");
                    IndividualGoal goalToAdd = new IndividualGoal(title, date, category);
                    goalToAdd.setRandomID(id);
                    completedGoals.add(goalToAdd);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return completedGoals;
    }

    public ArrayList getUncompletedGoals(int filterIndex) {
        ArrayList<IndividualGoal> uncompletedGoals = new ArrayList<>();
        JSONObject allGoals = getAllGoals();
        Iterator<String> iterator = allGoals.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                JSONObject iteratedGoal = allGoals.getJSONObject(key);
                Boolean goalIsCompleted = iteratedGoal.getBoolean("completed");
                if (!goalIsCompleted) {
                    Integer category = iteratedGoal.getInt("category");
                    if (category==filterIndex||filterIndex==-1){
                        String title = iteratedGoal.getString("title");
                        String date = iteratedGoal.getString("dueDate");
                        String id = iteratedGoal.getString("id");
                        IndividualGoal goalToAdd = new IndividualGoal(title, date, category);
                        goalToAdd.setRandomID(id);
                        uncompletedGoals.add(goalToAdd);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return uncompletedGoals;
    }

    public void saveNewCategory(String categoryToAdd){
        try{
            JSONObject newCategory = new JSONObject();
            newCategory.put("categoryTitle",categoryToAdd);
            JSONObject allCategories = getAllCategories();
            String categoryID = String.valueOf(allCategories.length());
            allCategories.put(categoryID,newCategory);
            writeAllCategoriesLocally(allCategories);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            writeAllGoalsLocally(allGoals);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setCompleted(IndividualGoal goal, Boolean value) {
        try {

            JSONObject allGoals = getAllGoals();
            String goalID = goal.getRandomID();
            JSONObject goalToChange = allGoals.getJSONObject(goalID);
            goalToChange.remove("completed");
            goalToChange.put("completed", value);
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
        String title = newGoal.getTitle();
        String dueDate = newGoal.getDueDate();
        Integer category = newGoal.getCategory();

        try {
            JSONObject goalToUpdate = goals.getJSONObject(previousGoalID);
            goalToUpdate.put("title", title);
            goalToUpdate.put("dueDate", dueDate);
            goalToUpdate.put("category", category);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        writeAllGoalsLocally(goals);
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

    public void writeAllCategoriesLocally(JSONObject allCategories){
        String convertedCategories = allCategories.toString();

        try{
            FileOutputStream fos = appContext.openFileOutput(CATEGORY_FILENAME, Context.MODE_PRIVATE);
            fos.write(convertedCategories.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
