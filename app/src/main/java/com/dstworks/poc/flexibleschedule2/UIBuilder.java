package com.dstworks.poc.flexibleschedule2;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.NumberPicker;

import java.util.List;

/**
 * Created by 4d on 24.12.2017.
 */

class UIBuilder {
    private final AppCompatActivity activity;

    public UIBuilder(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void build() {
        LinearLayout rangeList = activity.findViewById(R.id.rangeList);
        List<TimeRange> ranges = SettingsUtils.getRanges();
        for (TimeRange range : ranges) {
            RelativeLayout view = (RelativeLayout) View.inflate(activity, R.layout.time_picker, null);
            //view.setValue(range.getHours());
            rangeList.addView(view);
        }
    }
}
