package com.example.gridlayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
//import android.view.LayoutInflater;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Random;
//import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 8;
    private final int [][]  grid = new int[10][8];
    private int digCount = 10 * 8 - 4;
    int end = 0;

    private int clock = 0;
    private boolean running = false;

    // save the TextViews of all cells in an array, so later on,
    // when a TextView is clicked, we know which cell it is
    private ArrayList<TextView> cell_tvs;

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start timer
        if (savedInstanceState != null) {
            clock = savedInstanceState.getInt("clock");
            running = savedInstanceState.getBoolean("running");
        }
        runTimer();
        running = true; // This determines when stopwatch is running

        // Initialize grid with 0's representing unclicked
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                grid[i][j] = 0;
            }
        }
        generateMines();

        cell_tvs = new ArrayList<TextView>();

        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
        for (int i = 0; i<=9; i++) {
            for (int j=0; j<=7; j++) {
                TextView tv = new TextView(this);
                tv.setId(Integer.parseInt(String.valueOf(i) + String.valueOf(j)));
                tv.setHeight( dpToPixel(32) );
                tv.setWidth( dpToPixel(32) );
                tv.setTextSize( 16 );//dpToPixel(32) );
                tv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(Color.GRAY);
                tv.setBackgroundColor(Color.GREEN);
                tv.setOnClickListener(this::onClickTV);
                tv.setText("");

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.setMargins(dpToPixel(2), dpToPixel(2), dpToPixel(2), dpToPixel(2));
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);

                cell_tvs.add(tv);
            }
        }
    }

    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    public void onClickTV(View view){
        if (end == 0) {
            Button button = findViewById(R.id.button);
            TextView tv = (TextView) view;
            int n = findIndexOfCellTextView(tv);
            int i = n / COLUMN_COUNT;
            int j = n % COLUMN_COUNT;

            // Flag and Unflag
            if (tv.getBackground() instanceof ColorDrawable) {
                if (button.getText().equals("\uD83D\uDEA9")) {
                    TextView flagCountView = (TextView) findViewById(R.id.textView2);
                    int flagCount = Integer.parseInt(flagCountView.getText().toString());
                    if (tv.getText().equals("\uD83D\uDEA9")) {
                        tv.setText("");
                        flagCountView.setText(String.valueOf(flagCount + 1));
                    } else if (tv.getText().equals("")) {
                        if (flagCount > 0) {
                            tv.setText(R.string.flag);
                            flagCountView.setText(String.valueOf(flagCount - 1));
                        }
                    }
                }
                // Pick
                else if (tv.getText().equals("")) {
                    tv.setBackgroundColor(Color.LTGRAY);
                    if (grid[i][j] == 1) {
                        tv.setText(R.string.mine);
                        // REVEAL & LOSE
                        updateLose();
                    } else {
                        // REVEAL & CONTINUE
                        if (getNumber(i, j) > 0) {
                            updateWin();
                            tv.setText(String.valueOf(getNumber(i, j)));
                        } else {
                            revealAdjacent(i, j);
                        }
                    }
                }
            }
        }
        else {
            // Move onto results page
            Intent intent = new Intent(this, EndActivity.class);
            String result, message;
            if (end == 1) {
                result = "You won.";
                message = "Good job!";
            } else {
                result = "You lost.";
                message = "Try again!";
            }
            intent.putExtra("com.example.gridlayout.STOPWATCH", ((TextView) findViewById(R.id.timeView)).getText());
            intent.putExtra("com.example.gridlayout.RESULT", result);
            intent.putExtra("com.example.gridlayout.MESSAGE", message);

            startActivity(intent);
        }
    }

    public void generateMines() {
        int mines = 4;
        while (mines > 0) {
            Random rand = new Random();
            int row_random = rand.nextInt(10); // 0-9
            int col_random = rand.nextInt(8); // 0-7
            if (grid[row_random][col_random] == 0) {
                grid[row_random][col_random] = 1;
                mines -= 1;
            }
        }
    }

    public int getNumber(int row_index, int col_index) {
        int count = 0;
        for (int i = Math.max(0, row_index-1); i <= Math.min(row_index+1, 9); i++) {
            for (int j = Math.max(0, col_index-1); j <= Math.min(col_index+1, 7); j++) {
                if (grid[i][j] == 1) {
                    count += 1;
                }
            }
        }
        return count;
    }

    public void revealAdjacent(int row_index, int col_index) {
        if ( (row_index >= 0 && row_index <= 9) && (col_index >= 0 && col_index <= 7) ) {
            int id = Integer.parseInt(String.valueOf(row_index) + String.valueOf(col_index));
            TextView tv = findViewById(id);
            if (tv.getText().equals("")) {
                if (getNumber(row_index, col_index) > 0) {
                    updateWin();
                    tv.setBackgroundColor(Color.LTGRAY);
                    tv.setText(String.valueOf(getNumber(row_index, col_index)));
                } else {
                    tv.setBackgroundColor(Color.LTGRAY);
                    tv.setText(" ");
                    updateWin();

                    for (int i = Math.max(0, row_index-1); i <= Math.min(row_index+1, 9); i++) {
                        for (int j = Math.max(0, col_index-1); j <= Math.min(col_index+1, 7); j++) {
                            if (i != row_index || j != col_index) {
                                revealAdjacent(i, j);
                            }
                        }
                    }
                }
            }
        }
    }

    public void buttonPress(View view) {
        Button button = (Button) view;
        if (button.getText().equals("\u26CF")) {
            button.setText(R.string.flag);
        } else {
            button.setText(R.string.pick);
        }
    }

    public void updateWin() {
        if (--digCount == 0) {
            end = 1;
            running = false;
        }
    }

    public void updateLose() {
        end = -1;
        running = false;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("clock", clock);
        savedInstanceState.putBoolean("running", running);
    }

    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.timeView);
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int seconds = clock%60;
                String time = String.valueOf(seconds);
                timeView.setText(time);

                if (running) {
                    clock++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
}