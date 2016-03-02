package com.example.tyler.Path2Success;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.tyler.myfirstapp.MESSAGE";
    public final static String EXTRA_MESSAGE2 = "com.example.tyler.myfirstapp.MESSAGE2";
    private LinearLayout mLayout;
    private EditText mText1;
    private EditText mText2;
    private Button mButton;

    public void addNewItem(View view)  {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        //Intent intent2 = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        EditText editText2 = (EditText) findViewById(R.id.edit_message2);
        String message = editText.getText().toString();
        String message2 = editText2.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_MESSAGE2, message2);
        startActivity(intent);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mButton = (Button) findViewById(R.id.theButton);
        mText1 = (EditText) findViewById(R.id.edit_message);
        mText2 = (EditText) findViewById(R.id.edit_message2);
        mButton.setOnClickListener(onClick());
        TextView textView = new TextView(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Path 2 Success");
    }

    private CheckBox createNewCheckBox(String text) {
        final AbsListView.LayoutParams lparams = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
        final CheckBox checkBox = new CheckBox(this);
        checkBox.setLayoutParams(lparams);
        checkBox.setText(text);
        return checkBox;
    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox cBox = createNewCheckBox(mText1.getText().toString() + " " + mText2.getText().toString());
                cBox.setOnClickListener(onClickBox(cBox));
                mLayout.addView(cBox);

                //myLayout.addView
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                mText1.setText("");
                mText2.setText("");
            }
        };
    }

    private View.OnClickListener onClickBox(View box){
        return new View.OnClickListener(){
            @Override
            public void onClick(View box){
                mLayout.removeView(box);
            }
        };


    }

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
}
