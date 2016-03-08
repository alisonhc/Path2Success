package com.example.tyler.Path2Success;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {



    private ListView listLayout;
  //  private EditText taskContent;
  //  private EditText dueDate;
    private Button addButton;
    private LayoutTransition mTransition;
    public static final int RESULT_CODE = 9;
    private ArrayList<IndividualGoal> goalArrayList =new ArrayList<>();
    private GoalDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        listLayout = (ListView) findViewById(R.id.checkboxes);

    //    addButton = (Button) findViewById(R.id.add_a_new_task);
     //   taskContent = (EditText) findViewById(R.id.edit_message);
    //    mTransition = new LayoutTransition();
     //   addButton.setOnClickListener(onClick());
      //  listLayout.setLayoutTransition(mTransition);
       // mTransition.setAnimateParentHierarchy(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Path 2 Success");
        adapter=new GoalDataAdapter(this, goalArrayList);
        listLayout.setAdapter(adapter);
    }


//    private View.OnClickListener onClick() {
//        return new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                IndividualGoal newGoal = new IndividualGoal(taskContent.getText().toString(), dueDate.getText().toString());
//                goalArrayList.add(newGoal);
//                adapter.notifyDataSetChanged();
////                CheckBox cBox = createNewCheckBox(taskContent.getText().toString() + " " + dueDate.getText().toString());
////                cBox.setOnClickListener(onClickBox(cBox));
////                listLayout.addFooterView(cBox);
////                mTransition.addChild(listLayout,cBox);
//
//                InputMethodManager inputManager = (InputMethodManager)
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);
//                taskContent.setText("");
//                dueDate.setText("");
//            }
//        };
//    }

//    private View.OnClickListener onClickBox(View box){
//        return new View.OnClickListener(){
//            @Override
//            public void onClick(View box){
//           //     mTransition.setStagger(LayoutTransition.DISAPPEARING,100);
//                listLayout.removeView(box);
//            }
//        };
//
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up buttton, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Send button */
    public void goToInputScreen(View view) {
        Intent intent = new Intent(this, InputNewGoal.class);
        startActivityForResult(intent, RESULT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == RESULT_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String tContent = data.getStringExtra(InputNewGoal.EXTRA_MESSAGE);
                String tDate = data.getStringExtra((InputNewGoal.EXTRA_MESSAGE2));
                if(!tContent.isEmpty()) {
                    IndividualGoal newGoal = new IndividualGoal(tContent, tDate);
                    goalArrayList.add(newGoal);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
