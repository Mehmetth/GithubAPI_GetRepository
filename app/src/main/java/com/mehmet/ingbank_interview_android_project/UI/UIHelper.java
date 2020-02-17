package com.mehmet.ingbank_interview_android_project.UI;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.mehmet.ingbank_interview_android_project.MainActivity;

public class UIHelper
{
    public static void HideKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null)
        {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void ProgressBar(final ProgressBar progressBar, final boolean val)
    {
        MainActivity.Current.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if(val)
                {
                    progressBar.setVisibility(View.VISIBLE);
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
