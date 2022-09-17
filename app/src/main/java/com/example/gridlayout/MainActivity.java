package com.example.gridlayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 8;
    private int [][] grid = new int[10][8];

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

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                grid[i][j] = 0;
            }
        }
        generateMines();

        cell_tvs = new ArrayList<TextView>();

//        // Method (1): add statically created cells
//        TextView tv00 = (TextView) findViewById(R.id.textView00);
//        TextView tv01 = (TextView) findViewById(R.id.textView01);
//        TextView tv10 = (TextView) findViewById(R.id.textView10);
//        TextView tv11 = (TextView) findViewById(R.id.textView11);
//
//        tv00.setTextColor(Color.GRAY);
//        tv00.setBackgroundColor(Color.GRAY);
//        tv00.setOnClickListener(this::onClickTV);
//
//        tv01.setTextColor(Color.GRAY);
//        tv01.setBackgroundColor(Color.GRAY);
//        tv01.setOnClickListener(this::onClickTV);
//
//        tv10.setTextColor(Color.GRAY);
//        tv10.setBackgroundColor(Color.GRAY);
//        tv10.setOnClickListener(this::onClickTV);
//
//        tv11.setTextColor(Color.GRAY);
//        tv11.setBackgroundColor(Color.GRAY);
//        tv11.setOnClickListener(this::onClickTV);
//
//        cell_tvs.add(tv00);
//        cell_tvs.add(tv01);
//        cell_tvs.add(tv10);
//        cell_tvs.add(tv11);

        // Method (2): add four dynamically created cells
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

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.setMargins(dpToPixel(2), dpToPixel(2), dpToPixel(2), dpToPixel(2));
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);

                cell_tvs.add(tv);
            }
        }

//        // Method (3): add four dynamically created cells with LayoutInflater
//        LayoutInflater li = LayoutInflater.from(this);
//        for (int i = 4; i<=5; i++) {
//            for (int j=0; j<=1; j++) {
//                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);
//                //tv.setText(String.valueOf(i)+String.valueOf(j));
//                tv.setTextColor(Color.GRAY);
//                tv.setBackgroundColor(Color.GRAY);
//                tv.setOnClickListener(this::onClickTV);
//
//                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tv.getLayoutParams();
//                lp.rowSpec = GridLayout.spec(i);
//                lp.columnSpec = GridLayout.spec(j);
//
//                grid.addView(tv, lp);
//
//                cell_tvs.add(tv);
//            }
//        }

    }

    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    public void onClickTV(View view){
        Button button = findViewById(R.id.button);
        TextView tv = (TextView) view;
        int n = findIndexOfCellTextView(tv);
        int i = n/COLUMN_COUNT;
        int j = n%COLUMN_COUNT;

        // Flag and Unflag
        if (tv.getBackground() instanceof ColorDrawable) {
            if (button.getText().equals("\uD83D\uDEA9")) {
                if (tv.getText().equals("\uD83D\uDEA9")) {
                    tv.setText("");
                } else if (tv.getText().equals("")) {
                    tv.setText(R.string.flag);
                }
            }
            // Pick
            else if (tv.getText().equals("")) {
                tv.setBackgroundColor(Color.LTGRAY);
                if (grid[i][j] == 1) {
                    tv.setText(R.string.mine);
                    // REVEAL & LOSE
                } else {
                    // REVEAL & CONTINUE
                    // UNRAVEL FUNCTION
                    int number = getNumber(i, j);
                    if (number > 0) {
                        tv.setText(String.valueOf(getNumber(i, j)));
                    } else {
                        tv.setText(" ");
                    }
                }
            }
        }
//
//        tv.setText(String.valueOf(i)+String.valueOf(j));
//        if (tv.getCurrentTextColor() == Color.GRAY) {
//            tv.setTextColor(Color.BLACK);
////            tv.setText(R.string.flag);
//            tv.setBackgroundColor(Color.parseColor("lime"));
//        }else {
//            tv.setTextColor(Color.GRAY);
//            tv.setBackgroundColor(Color.LTGRAY);
//        }
//        // Pick
//        if (button.getText().equals("\u26CF")) {
//            tv.setText(String.valueOf(i)+String.valueOf(j));
//        }
//        // Flag
//        else {
//            tv.setText(R.string.flag);
//        }
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

    public void revealAdjacent(View view, int row_index, int col_index) {
//        if (grid[row_index][col_index] == 1) {
//
//        }
    }

    public void buttonPress(View view) {
        Button button = (Button) view;
        if (button.getText().equals("\u26CF")) {
            button.setText(R.string.flag);
        } else {
            button.setText(R.string.pick);
        }
    }

}