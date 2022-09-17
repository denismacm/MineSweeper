package com.example.gridlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Intent intent = getIntent();

        String stopwatch = intent.getStringExtra("com.example.gridlayout.STOPWATCH");
        TextView stopwatchView = (TextView) findViewById(R.id.stopwatch);
        if (Integer.parseInt(stopwatch) == 1) {
            stopwatchView.setText("Used " + stopwatch + " second");
        } else {
            stopwatchView.setText("Used " + stopwatch + " seconds");
        }

        String message = intent.getStringExtra("com.example.gridlayout.MESSAGE");
        TextView messageView = (TextView) findViewById(R.id.message);
        messageView.setText(message);

        String result = intent.getStringExtra("com.example.gridlayout.RESULT");
        TextView resultView = (TextView) findViewById(R.id.result);
        resultView.setText(result);
    }

    public void backToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
