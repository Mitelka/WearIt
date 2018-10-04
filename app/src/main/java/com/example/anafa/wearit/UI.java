package com.example.anafa.wearit;

import android.app.Activity;
import android.widget.GridView;

import java.util.ArrayList;

public class UI {
    public void showResults(GridView gridView, Activity activity, ArrayList itemList, int showResultsAt) {
        CustomArrayListAdapter myAdapter = new CustomArrayListAdapter(activity, showResultsAt, itemList);
        gridView.setAdapter(myAdapter);
    }
}

