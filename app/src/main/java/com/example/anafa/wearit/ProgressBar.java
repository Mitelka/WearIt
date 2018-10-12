package com.example.anafa.wearit;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressBar
{

    public static void getProgressBar(Context context)
    {
        ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading ...");
        progressBar.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressBar.show();//displays the progress bar
    }
}
