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

/**
 * Created by pbertel on 4/7/16.
 */
public class LocalStorage extends AppCompatActivity{
    public final static String FILENAME = "goal_file";
    private JSONArray goalList;

    public JSONArray getJSONArray() {
        this.goalList = new JSONArray();

        //goalList must be the goal list that has accumulated all the previous goals
        //so, we need this below chunk of code to make sure goalList is up to date
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
            goalList = new JSONArray(b.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return goalList;
    }

    public void saveGoalLocally(String title, String date, String category) {
        try {
            JSONObject goal;

            goal = new JSONObject();
            goal.put("title", title);
            goal.put("date", date);
            goal.put("category", category);
            goal.put("isChecked", false);
            goalList.put(goal);
            String goals = goalList.toString();

            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(goals.getBytes());
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
        goalList = new JSONArray();
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
            goalList = new JSONArray(b.toString());
            //goalToChange = goalList.getJSONObject(position);
            //instead, here we will get the JSON object that corresponds to the unique ID provided from the Individual Goal class

            goalToChange.put("isChecked", true);

            //now put the goalList back in to local storage
            String goals = goalList.toString();

            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(goals.getBytes());
            fos.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
