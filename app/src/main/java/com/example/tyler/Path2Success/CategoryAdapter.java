package com.example.tyler.Path2Success;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by angelica on 4/25/16.
 */
public class CategoryAdapter extends BaseAdapter {
    private ArrayList<String> categoryList;
    private Context context;
    private LayoutInflater inflater;
    public CategoryAdapter (Context context,  ArrayList<String> categoryList) {
        this.context = context;
        this.categoryList =categoryList;
        inflater = LayoutInflater.from(this.context);
    }

    private class ViewHolder{
        TextView categoryText;
        public ViewHolder(View item){
            categoryText = (TextView) item.findViewById(R.id.category_item);

        }
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public String getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView==null){
            convertView =inflater.inflate(R.layout.category_item_info,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String cat=getItem(position);
        viewHolder.categoryText.setText(cat);
       // viewHolder.categoryText.setKeyListener(null);

        return convertView;

    }



}
