package com.example.tyler.Path2Success;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
        String message2 = intent.getStringExtra(MyActivity.EXTRA_MESSAGE2);
        TextView textView = new TextView(this);
        TextView textView2 = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message + "        "+message2); //only shows whichever comes second right now.

//Hi.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.content);
        layout.addView(textView);
    }
}