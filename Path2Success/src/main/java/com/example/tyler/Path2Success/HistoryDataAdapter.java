package com.example.tyler.Path2Success;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by angelica on 3/2/16.
 */
public class HistoryDataAdapter extends RecyclerView.Adapter<HistoryDataAdapter.ViewHolder>{
    private ArrayList<IndividualGoal> goalList;
    private Context context;
    private LayoutInflater inflater;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView task;

        public ViewHolder (View itemView) {
            super(itemView);
            task = (TextView)itemView.findViewById(R.id.history_item);
        }
        public void onClick(View v) {
        }
    }

    public HistoryDataAdapter(ArrayList<IndividualGoal> goalList) {
        this.goalList = goalList;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_info,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.task.setText(goalList.get(position).getTitle());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

}

