package com.dstworks.poc.flexibleschedule2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Updates view
 * Created by 4d on 24.12.2017.
 */

class UIManager {
    private final AppCompatActivity activity;
    public static LinearLayout rangeList;
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss dd/MM/yy");

    public UIManager(AppCompatActivity activity) {
        this.activity = activity;
        rangeList = activity.findViewById(R.id.rangeList);
    }

    public void update() {
        clear();

        TimeRange startedRange = null;
        List<TimeRange> ranges = DataManager.getRanges();
        for (TimeRange range : ranges) {
            ConstraintLayout view = createRangeView(range);
            if (range.isStarted() && range.getCountDownExecutor() == null) {
                startedRange = range;
            }
            rangeList.addView(view);
        }

        if (startedRange != null) {
            Calendar cal = Calendar.getInstance();

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startedRange.getLastStartDate());
            calendar.add(Calendar.HOUR, startedRange.getHours());
            calendar.add(Calendar.MINUTE, startedRange.getMinutes());
            calendar.add(Calendar.SECOND, startedRange.getSeconds());
            if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                // run countdown
                final CountDownExecutor countDownExecutor = new CountDownExecutor();
                // save countdown executor to timeRange object
                startedRange.setCountDownExecutor(countDownExecutor);
                countDownExecutor.run(startedRange, calendar.getTimeInMillis());
                AlarmManager.runCurrentRange(activity, cal.getTimeInMillis());
            } else {
                AlarmManager.processCompleteAction(activity);
            }
        }
    }

    private void clear() {
        rangeList.removeAllViews();
    }

    @NonNull
    private ConstraintLayout createRangeView(final TimeRange range) {
        ConstraintLayout view = null;
        try {
            view = (ConstraintLayout) View.inflate(
                    activity,
                    R.layout.timer_range_view_short,
                    null
            );
            range.setView(view);

            TextView nameField = view.findViewById(R.id.name);
            nameField.setText(range.getName());

            TextView valueField = view.findViewById(R.id.value);
            valueField.setText(range.getText());

            TextView lastStartDateField = view.findViewById(R.id.lastStartDate);
            long lastStartDate = range.getLastStartDate();
            lastStartDateField.setText(lastStartDate == 0 ? "" :
                    DATE_FORMAT.format(lastStartDate));

            TextView lastCompleteDateField = view.findViewById(R.id.lastCompleteDate);
            long lastCompleteDate = range.getLastCompleteDate();
            lastCompleteDateField.setText(lastCompleteDate == 0 ? "" :
                    DATE_FORMAT.format(lastCompleteDate));

            final View delBtn = view.findViewById(R.id.delBtn);
            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        List<TimeRange> ranges = DataManager.getRanges();
                        int index = ranges.indexOf(range);
                        int currentRange = DataManager.getCurrentRange();
                        // can't remove started time range
                        if (AlarmManager.isIsAlarmActive() && index == currentRange) {
                            return;
                        }
                        if (index < currentRange ||
                                (index == ranges.size() - 1
                                        && index == currentRange)) {
                            DataManager.setCurrentRange(currentRange - 1);
                        }
                        ranges.remove(range);
                        DataManager.writeConfiguration(activity);
                        update();
                    } catch (Throwable e) {
                        DataManager.err("can't execute processCancelAction(): " + e);
                    }
                }
            });

            final View makeCurrentBtn = view.findViewById(R.id.makeCurrentBtn);
            makeCurrentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        List<TimeRange> ranges = DataManager.getRanges();
                        int index = ranges.indexOf(range);
                        int currentRange = DataManager.getCurrentRange();
                        // can't remove started time range
                        if (!AlarmManager.isIsAlarmActive() && index != currentRange) {
                            DataManager.setCurrentRange(index);
                            DataManager.writeConfiguration(activity);
                            update();
                        }
                    } catch (Throwable e) {
                        DataManager.err("can't execute makeCurrentBtn(): " + e);
                    }
                }
            });
            final View menuWrapper = view.findViewById(R.id.menuWrapper);
            final View menuBtn = view.findViewById(R.id.menuBtn);
            menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (menuWrapper.getVisibility() == View.GONE) {
                        menuWrapper.setVisibility(View.VISIBLE);
                    } else {
                        menuWrapper.setVisibility(View.GONE);
                    }
                }
            });

            // edit btn
            FloatingActionButton editBtn = view.findViewById(R.id.editBtn);
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, EditTimerRangeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // send index to activity
                    List<TimeRange> ranges = DataManager.getRanges();
                    int index = ranges.indexOf(range);

                    Bundle b = new Bundle();
                    b.putInt("rangeIndex", index);
                    intent.putExtras(b);

                    activity.startActivity(intent);
                }
            });

            // move up btn
            final View upBtn = view.findViewById(R.id.upBtn);
            upBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        List<TimeRange> ranges = DataManager.getRanges();
                        int index = ranges.indexOf(range);
                        int currentRange = DataManager.getCurrentRange();
                        if (index > 0) {
                            if (index == currentRange) {
                                DataManager.setCurrentRange(currentRange - 1);
                            } else if (index - 1 == currentRange) {
                                DataManager.setCurrentRange(currentRange + 1);
                            }
                            ranges.remove(range);
                            ranges.add(index - 1, range);
                            DataManager.writeConfiguration(activity);
                            update();
                        }
                    } catch (Throwable e) {
                        DataManager.err("can't execute processCancelAction(): " + e);
                    }
                }
            });
            // move down btn
            final View downBtn = view.findViewById(R.id.downBtn);
            downBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        List<TimeRange> ranges = DataManager.getRanges();
                        int index = ranges.indexOf(range);
                        int currentRange = DataManager.getCurrentRange();
                        if (index < ranges.size() - 1) {
                            if (index == currentRange) {
                                DataManager.setCurrentRange(currentRange + 1);
                            } else if (index + 1 == currentRange) {
                                DataManager.setCurrentRange(currentRange - 1);
                            }
                            ranges.remove(range);
                            ranges.add(index + 1, range);
                            DataManager.writeConfiguration(activity);
                            update();
                        }
                    } catch (Throwable e) {
                        DataManager.err("can't execute processCancelAction(): " + e);
                    }
                }
            });
            if (range.isStarted()) {
                final View completeBtn = view.findViewById(R.id.completeBtn);
                completeBtn.setVisibility(View.VISIBLE);

                final View cancelBtn = view.findViewById(R.id.cancelBtn);
                cancelBtn.setVisibility(View.VISIBLE);

                menuBtn.setVisibility(View.INVISIBLE);

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
                if (DataManager.getRanges().get(DataManager.getCurrentRange()) == range) {
                    view.setBackgroundColor(ContextCompat.getColor(activity, R.color.green));
                } else {
                    int color = DataManager.getRanges().indexOf(range) % 2 == 0 ?
                            R.color.white :
                            R.color.grey;
                    view.setBackgroundColor(ContextCompat.getColor(activity, color));
                }
            }
        } catch (Throwable e) {
            DataManager.err("can't execute createRangeView(): " + e);
        }
        return view;
    }
}
