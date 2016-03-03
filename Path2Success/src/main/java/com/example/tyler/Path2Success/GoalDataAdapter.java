package com.example.tyler.Path2Success;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by angelica on 3/2/16.
 */
public class GoalDataAdapter extends ArrayAdapter<IndividualGoal> {
    private ArrayList<IndividualGoal> goalList;
    public GoalDataAdapter(Context context, int resource, ArrayList<IndividualGoal> goalList) {
        super(context, resource);
        this.goalList=new ArrayList<>();
        this.goalList.addAll(goalList);
    }

    private class ViewHolder{
        TextView code;
        CheckBox name;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));


        IndividualGoal goal = goalList.get(position);
        holder.code.setText("");
        holder.name.setText(goal.getTitle());

        return convertView;

    }

}

