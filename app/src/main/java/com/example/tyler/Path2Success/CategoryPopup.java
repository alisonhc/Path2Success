package com.example.tyler.Path2Success;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Alison on 4/19/2016.
 */
public class CategoryPopup extends Activity {
    private ArrayAdapter <String> categoryListAdapter;
    //Hard Coded for now
    private String[] categoryList = {"Academics", "Fitness", "Misc"};
    private ListView categoryListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*.8), (int) (height*.6));

        categoryListAdapter = new ArrayAdapter<>(this,R.layout.popup_item_info, categoryList);
        categoryListView = (ListView)findViewById(R.id.category_popup_listview);
        categoryListView.setAdapter(categoryListAdapter);


        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                intent.putExtra(InputNewGoal.POPUP_PICKED, position);
                setResult(RESULT_OK);
                finish();
            }
        });



    }

    /*
    public void TextViewClicked() {
        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
        switcher.showNext(); //or switcher.showPrevious();
        TextView myTV = (TextView) switcher.findViewById(R.id.clickable_text_view);
        myTV.setText("value");
    }
    */
}
