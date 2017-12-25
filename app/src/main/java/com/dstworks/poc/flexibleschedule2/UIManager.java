package com.dstworks.poc.flexibleschedule2;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Updates view
 * Created by 4d on 24.12.2017.
 */

class UIManager {
    private final AppCompatActivity activity;
    private final LinearLayout rangeList;

    public UIManager(AppCompatActivity activity) {
        this.activity = activity;
        rangeList = activity.findViewById(R.id.rangeList);
    }

    public void update() {
        clear();

        List<TimeRange> ranges = SettingsUtils.getRanges();
        for (TimeRange range : ranges) {
            addRangeView(rangeList, range);
        }
    }

    private void clear() {
        rangeList.removeAllViews();
    }

    public void addRangeView(LinearLayout rangeList, TimeRange range) {
        ConstraintLayout view = createRangeView(range);
        rangeList.addView(view);
    }

    @NonNull
    private ConstraintLayout createRangeView(final TimeRange range) {
        ConstraintLayout view = (ConstraintLayout) View.inflate(
                activity,
                R.layout.range_view_short,
                null
        );
        range.setView(view);

        TextView nameField = view.findViewById(R.id.name);
        nameField.setText(range.getName());

        TextView valueField = view.findViewById(R.id.value);
        valueField.setText(range.getText());


        final View delBtn = view.findViewById(R.id.delBtn);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsUtils.getRanges().remove(range);
                SettingsUtils.writeConfiguration(activity);
                update();
            }
        });
        if (range.isStarted()) {
            final View completeBtn = view.findViewById(R.id.completeBtn);
            completeBtn.setVisibility(View.VISIBLE);

            final View cancelBtn = view.findViewById(R.id.cancelBtn);
            cancelBtn.setVisibility(View.VISIBLE);

            delBtn.setVisibility(View.INVISIBLE);

            view.setBackgroundColor(ContextCompat.getColor(activity, R.color.orange));
            completeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlarmManager.processCompleteAction(activity);
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlarmManager.processCancelAction(activity);
                }
            });
        } else {
            if (SettingsUtils.getRanges().get(SettingsUtils.getCurrentRange()) == range) {
                view.setBackgroundColor(ContextCompat.getColor(activity, R.color.green));
            } else {
                view.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
            }
        }
        return view;
    }
}
