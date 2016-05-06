package com.example.tyler.Path2Success;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Code for reading and writing to local storage was referenced from
 *      http://chrisrisner.com/31-Days-of-Android-Day-23-Writing-and-Reading-Files
 */
public class LocalStorage {
    public final static String GOAL_FILE = "goal_file";
    public final static String CATEGORY_FILE = "category_file";
    private Context appContext;

    public LocalStorage(Context c) {
        appContext = c; //having the context is necessary to access the phone's local storage
    }

    private JSONObject getAllData(String filename) {
        JSONObject allData = new JSONObject();
        try {
            FileInputStream fis = appContext.openFileInput(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while (bis.available() != 0) {
                char c = (char) bis.read();
                b.append(c);
            }
            bis.close();
            fis.close();
            allData = new JSONObject(b.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return allData;
    }

    public ArrayList getAllCategoriesToShow(){
        ArrayList<String> categoryArrayList = new ArrayList<>();
        JSONObject allCategories = getAllData(CATEGORY_FILE);
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

    //If the user passes in 'true' as the value of bool, they wish to retrieve all completed goals
    public ArrayList getGoals(Boolean bool, int filterIndex) {
        ArrayList<IndividualGoal> individualGoalArrayList = new ArrayList<>();
        JSONObject allGoals = getAllData(GOAL_FILE);
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
        return individualGoalArrayList;
    }

    public void saveNewCategory(String categoryToAdd){
        try{
            JSONObject newCategory = new JSONObject();
            newCategory.put("categoryTitle",categoryToAdd);
            JSONObject allCategories = getAllData(CATEGORY_FILE);
            String categoryID = String.valueOf(allCategories.length());
            allCategories.put(categoryID,newCategory);
            writeDataLocally(allCategories, CATEGORY_FILE);
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
            JSONObject allGoals = getAllData(GOAL_FILE);

            while (allGoals.has(goalID)) {
                randInt = rand.nextInt(10000);
                goalID = randInt.toString();
            }
            newGoal.put("id", goalID);
            allGoals.put(goalID, newGoal);
            goalToAdd.setRandomID(goalID);
            writeDataLocally(allGoals, GOAL_FILE);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateGoal(String previousGoalID, IndividualGoal newGoal) {
        JSONObject goals = getAllData(GOAL_FILE);
        String title = newGoal.getTitle();
        String dueDate = newGoal.getDueDate();
        Integer category = newGoal.getCategory();
        Boolean isCompleted = newGoal.getIsCompleted();

        try {
            JSONObject goalToUpdate = goals.getJSONObject(previousGoalID);
            goalToUpdate.put("title", title);
            goalToUpdate.put("dueDate", dueDate);
            goalToUpdate.put("category", category);
            goalToUpdate.put("completed", isCompleted);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        writeDataLocally(goals, GOAL_FILE);
    }

    public void writeDataLocally(JSONObject data, String filename) {
        //data needs to be a String before being written to local storage
        String convertedData = data.toString();
        try{
            FileOutputStream fos = appContext.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(convertedData.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeGoal(IndividualGoal goal) {
        JSONObject allGoals = getAllData(GOAL_FILE);
        allGoals.remove(goal.getRandomID());
        writeDataLocally(allGoals, GOAL_FILE);
    }
}
