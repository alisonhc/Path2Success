package com.example.tyler.Path2Success;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import java.util.ArrayList;

/**
 * Created by angelica on 4/17/16.
 */
public class HistoryDataAdapter extends BaseAdapter {
        private ArrayList<IndividualGoal> goalList;
        private Context context;
        private LayoutInflater inflater;
        public HistoryDataAdapter (Context context,  ArrayList<IndividualGoal> goalList) {
            this.context = context;
            this.goalList=goalList;
            inflater = LayoutInflater.from(this.context);
        }

        private class ViewHolder{
            CheckedTextView task;
            public ViewHolder(View item){
                task = (CheckedTextView)item.findViewById(R.id.history_taskContent);
            }
        }

        @Override
        public int getCount() {
            return goalList.size();
        }

        @Override
        public IndividualGoal getItem(int position) {
            return goalList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if(convertView==null){
                convertView =inflater.inflate(R.layout.history_item_info,parent,false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder)convertView.getTag();
            }

            IndividualGoal currentGoalInList=getItem(position);
            viewHolder.task.setText(currentGoalInList.getTitle());

            return convertView;

        }

    }
