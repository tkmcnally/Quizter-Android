package com.tkmcnally.quizter;

import android.content.res.Resources;
import android.widget.TextView;

/**
 * Created by missionary on 1/6/2014.
 */
public class Util {

    public static String getPictureSize(Resources resources) {
        return "" + resources.getDisplayMetrics().density;
    }
}
