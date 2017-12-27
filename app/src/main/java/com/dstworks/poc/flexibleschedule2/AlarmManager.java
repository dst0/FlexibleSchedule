package com.dstworks.poc.flexibleschedule2;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by dst on 25.12.2017.
 */
public class AlarmManager {

    private static boolean isAlarmActive = false;

    public static boolean isIsAlarmActive() {
        return isAlarmActive;
    }

    public static void runCurrentRange(final Activity activity) {
        if (isAlarmActive) {
            DataManager.log("runCurrentRange() wrong - already isAlarmActive==true");
            return;
        }
        isAlarmActive = true;
        //Create a new PendingIntent and add it to the AlarmManager
        int currentRange = DataManager.getCurrentRange();
        final TimeRange timeRange = DataManager.getRanges().get(currentRange);

        // UI update
        final View view = timeRange.getView();

        final View completeBtn = view.findViewById(R.id.completeBtn);
        completeBtn.setVisibility(View.VISIBLE);

        final View cancelBtn = view.findViewById(R.id.cancelBtn);
        cancelBtn.setVisibility(View.VISIBLE);

        final View menuBtn = view.findViewById(R.id.menuBtn);
        menuBtn.setVisibility(View.INVISIBLE);

        view.setBackgroundColor(ContextCompat.getColor(activity, R.color.orange));

        // create alarm
        Intent intent = new Intent(activity, AlarmReceiverActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity,
                currentRange, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        final android.app.AlarmManager am =
                (android.app.AlarmManager) activity.getSystemService(Activity.ALARM_SERVICE);
        am.set(android.app.AlarmManager.RTC_WAKEUP, timeRange.getExpectedTimeInMilliseconds(),
                pendingIntent);

        // save pending intent to timeRange object
        timeRange.setPendingIntent(pendingIntent);

        // run countdown
        final CountDownExecutor countDownExecutor = new CountDownExecutor();
        // save countdown executor to timeRange object
        timeRange.setCountDownExecutor(countDownExecutor);
        countDownExecutor.run(timeRange);

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCompleteAction(activity);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCancelAction(activity);
            }
        });
    }

    public static void runCurrentRange(Activity activity, long timeInMilliseconds) {
        //Create a new PendingIntent and add it to the AlarmManager
        int currentRange = DataManager.getCurrentRange();
        Intent intent = new Intent(activity, AlarmReceiverActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity,
                currentRange, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        android.app.AlarmManager am =
                (android.app.AlarmManager) activity.getSystemService(Activity.ALARM_SERVICE);
        am.set(android.app.AlarmManager.RTC_WAKEUP, timeInMilliseconds,
                pendingIntent);
    }

    /**
     * Process Complete btn click action for given timeRange.
     * Updates UI.
     * Starts next range if any.
     *
     * @param activity any activity
     */
    public static void processCompleteAction(Activity activity) {
        try {
            TimeRange range = DataManager.getRanges().get(DataManager.getCurrentRange());
            Date date = new Date();
            range.setLastCompleteDate(date.getTime());

            DataManager.log("Task " + range.toString() + " was finished at " + date.toString());
            processCancelAction(activity);

            try {
                TextView lastCompleteDateField = range.getView().findViewById(R.id.lastCompleteDate);
                long lastCompleteDate = range.getLastCompleteDate();
                lastCompleteDateField.setText(lastCompleteDate == 0 ? "" :
                        UIManager.DATE_FORMAT.format(Calendar.getInstance().getTime()));
            } catch (Throwable e) {
                DataManager.err("can't update lastCompleteDateField: " + e);
            }

            // check if there are more ranges to do
            int currentRange = DataManager.getCurrentRange();
            if (DataManager.getRanges().size() - 1 > currentRange) {
                // update color
                int color = DataManager.getRanges().indexOf(range) % 2 == 0 ?
                        R.color.white :
                        R.color.grey;
                range.getView().setBackgroundColor(ContextCompat.getColor(activity, color));

                DataManager.setCurrentRange(currentRange + 1);
                DataManager.writeConfiguration(activity);
                AlarmManager.runCurrentRange(activity);
            } else {
                // update color
                range.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.green));
            }
        } catch (Throwable e) {
            DataManager.err("can't execute processCancelAction(): " + e);
        }
    }

    /**
     * Process Cancel btn click action for given timeRange.
     * Updates UI.
     * Starts next range if any.
     *
     * @param activity any activity
     */
    public static void processCancelAction(Activity activity) {
        try {
            TimeRange range = DataManager.getRanges().get(DataManager.getCurrentRange());

            android.app.AlarmManager am =
                    (android.app.AlarmManager) activity.getSystemService(Activity.ALARM_SERVICE);
            am.cancel(range.getPendingIntent());
            range.getCountDownExecutor().stop();
            range.setCountDownExecutor(null);
            range.setPendingIntent(null);

            isAlarmActive = false;

            View view = range.getView();
            View completeBtn = view.findViewById(R.id.completeBtn);
            completeBtn.setVisibility(View.INVISIBLE);
            completeBtn.setOnClickListener(null);

            View cancelBtn = view.findViewById(R.id.cancelBtn);
            cancelBtn.setVisibility(View.INVISIBLE);

            View menuBtn = view.findViewById(R.id.menuBtn);
            menuBtn.setVisibility(View.VISIBLE);

            view.setBackgroundColor(ContextCompat.getColor(activity, R.color.green));
        } catch (Throwable e) {
            DataManager.err("can't execute processCancelAction(): " + e);
        }
    }
}
