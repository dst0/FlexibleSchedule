package com.dstworks.poc.flexibleschedule2;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;

/**
 * Created by dst on 25.12.2017.
 */
public class AlarmManager {


    public static void runCurrentRange(final Activity activity) {
        //Create a new PendingIntent and add it to the AlarmManager
        int currentRange = SettingsUtils.getCurrentRange();
        final TimeRange timeRange = SettingsUtils.getRanges().get(currentRange);

        // UI update
        final View view = timeRange.getView();

        final View completeBtn = view.findViewById(R.id.completeBtn);
        completeBtn.setVisibility(View.VISIBLE);

        final View cancelBtn = view.findViewById(R.id.cancelBtn);
        cancelBtn.setVisibility(View.VISIBLE);

        final View delBtn = view.findViewById(R.id.delBtn);
        delBtn.setVisibility(View.INVISIBLE);

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
        int currentRange = SettingsUtils.getCurrentRange();
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
        TimeRange range = SettingsUtils.getRanges().get(SettingsUtils.getCurrentRange());
        processCancelAction(activity);

        // check if there are more ranges to do
        int currentRange = SettingsUtils.getCurrentRange();
        if (SettingsUtils.getRanges().size() - 1 > currentRange) {
            // update color
            range.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.white));

            SettingsUtils.setCurrentRange(currentRange + 1);
            SettingsUtils.writeConfiguration(activity);
            AlarmManager.runCurrentRange(activity);
        } else {
            // update color
            range.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.green));
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
        TimeRange range = SettingsUtils.getRanges().get(SettingsUtils.getCurrentRange());
        View view = range.getView();

        android.app.AlarmManager am =
                (android.app.AlarmManager) activity.getSystemService(Activity.ALARM_SERVICE);
        am.cancel(range.getPendingIntent());
        range.getCountDownExecutor().stop();
        range.setCountDownExecutor(null);
        range.setPendingIntent(null);

        View completeBtn = view.findViewById(R.id.completeBtn);
        completeBtn.setVisibility(View.INVISIBLE);
        completeBtn.setOnClickListener(null);

        View cancelBtn = view.findViewById(R.id.cancelBtn);
        cancelBtn.setVisibility(View.INVISIBLE);

        View delBtn = view.findViewById(R.id.delBtn);
        delBtn.setVisibility(View.VISIBLE);

        view.setBackgroundColor(ContextCompat.getColor(activity, R.color.green));
    }
}
