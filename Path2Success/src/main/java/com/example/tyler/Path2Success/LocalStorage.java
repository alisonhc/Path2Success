package com.example.tyler.Path2Success;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.HashSet;

/**
 * Created by pbertel on 4/7/16.
 * Referenced from http://chrisrisner.com/31-Days-of-Android--Day-23-Writing-and-Reading-Files
 */
public class LocalStorage extends AppCompatActivity{
    public final static String FILENAME = "goal_file";
    private JSONObject goals;

    public JSONObject getGoals() {
        goals = new JSONObject();

        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedInputStream bis = new BufferedInputStream(fis);
//            StringBuffer b = new StringBuffer();
//            while (bis.available() != 0) {
//                char c = (char) bis.read();
//                b.append(c);
//            }
//            bis.close();
//            fis.close();
//            goals = new JSONObject(b.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }

        return goals;
    }

    public void saveGoalLocally(IndividualGoal goalToAdd) {
        try {
            JSONObject newGoal;
            String title;
            String dueDate;
            Integer category;
            Integer randInt;
            String goalID;

            title = goalToAdd.getTitle();
            dueDate = goalToAdd.getDueDate();
            category = goalToAdd.getCategory();

            newGoal = new JSONObject();
            newGoal.put("title", title);
            newGoal.put("dueDate", dueDate);
            newGoal.put("category", category);
            newGoal.put("isChecked", false);

            Random rand = new Random();
            randInt = rand.nextInt(10000);
            goalID = randInt.toString();

            goals = new JSONObject();
//
//            goals = getGoals();
//
//            while (goals.has(goalID)) {
//                randInt = rand.nextInt(10000);
//                goalID = randInt.toString();
//            }
//
            goals.put(goalID, newGoal);
            goalToAdd.setRandomID(goalID);

            //The goals JSON Object must be converted to a string before being
            // written to local storage

            JSONArray tempArray;
            tempArray = new JSONArray();

            tempArray.put(goals);

            String convertedGoals = tempArray.toString();

//            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
//            fos.write(convertedGoals.getBytes());
//            fos.close();

        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //this method is difficult, should probably work on new method of storage (uniqueID for each goal, a single JSON object, etc.)
    public void setIsChecked(IndividualGoal goal, Boolean value) {
        JSONObject goalToChange;

        //need to use the parameter of "goal" in order to determine which to change

        goalToChange = new JSONObject();

        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while (bis.available() != 0) {
                char c = (char) bis.read();
                b.append(c);
            }
            bis.close();
            fis.close();
            goals = new JSONObject(b.toString());
            //goalToChange = goalList.getJSONObject(position);
            //instead, here we will get the JSON object that corresponds to the unique ID provided from the Individual Goal class

            goalToChange.put("isChecked", value);

            //now put the goalList back in to local storage
            String convertedGoals = goals.toString();

            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(convertedGoals.getBytes());
            fos.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
