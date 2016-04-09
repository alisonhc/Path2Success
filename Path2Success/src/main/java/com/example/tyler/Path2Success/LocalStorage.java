package com.example.tyler.Path2Success;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

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
 * Referenced from http://chrisrisner.com/31-Days-of-Android--Day-23-Writing-and-Reading-Files/
 */
public class LocalStorage extends AppCompatActivity{
    public final static String FILENAME = "goal_file";
    private JSONObject goals;

    public LocalStorage() {
        this.goals = getGoals();
    }

    public JSONObject getGoals() {
        goals = new JSONObject();

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
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return goals;
    }

    public void saveGoalLocally(String title, String date, Integer category) {
        try {
            JSONObject newGoal;
            Integer goalID;

            newGoal = new JSONObject();
            newGoal.put("title", title);
            newGoal.put("date", date);
            newGoal.put("category", category);
            newGoal.put("isChecked", false);

            Random rand = new Random();
            goalID = rand.nextInt(10000);

            while (goals.has(goalID.toString())) {
                goalID = rand.nextInt(10000);
            }

            goals.put(goalID.toString(), newGoal);

            //The goals JSON Object must be converted to a string before being
            // written to local storage
            String convertedGoals = goals.toString();

            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(convertedGoals.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //this method is difficult, should probably work on new method of storage (uniqueID for each goal, a single JSON object, etc.)
    public void setIsChecked(IndividualGoal goal, Boolean value) {
        JSONObject goalToChange;
        JSONObject goals;

        //need to use the parameter of "goal" in order to determine which to change

        goalToChange = new JSONObject();
        goals = this.goals;

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
