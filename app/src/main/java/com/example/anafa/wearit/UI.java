package com.example.anafa.wearit;

import android.app.Activity;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

public class UI {

    public static final int Grid_View_Type = 1;
    public static final int List_View_Type = 2;

    //type=1-->GridView
    //type=2-->ListView
    public static void showResults(View view, Activity activity, ArrayList itemList, int showResultsAt, int type) {
        CustomArrayListAdapter myAdapter = new CustomArrayListAdapter(activity, showResultsAt, itemList);

        if(type == Grid_View_Type) {
            ((GridView) view).setAdapter(myAdapter);
        } else if (type == List_View_Type){
            ((ListView) view).setAdapter(myAdapter);
        }
    }
}