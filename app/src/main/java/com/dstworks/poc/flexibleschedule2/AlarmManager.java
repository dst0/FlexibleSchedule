package com.dstworks.poc.flexibleschedule2;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

/**
 * Created by dst on 25.12.2017.
 */

public class AlarmManager {


    public static void runCurrentRange(final Activity activity) {
        //Create a new PendingIntent and add it to the AlarmManager
        int currentRange = SettingsUtils.getCurrentRange();
        TimeRange timeRange = SettingsUtils.getRanges().get(currentRange);

        // UI update
        final View rangeView = timeRange.getView();
        final View completeBtn = rangeView.findViewById(R.id.completeBtn);
        completeBtn.setVisibility(View.VISIBLE);
        rangeView.setBackgroundColor(ContextCompat.getColor(activity, R.color.orange));

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
        TextView valueField = rangeView.findViewById(R.id.value);
        countDownExecutor.run(valueField, timeRange);

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.cancel(pendingIntent);
                countDownExecutor.stop();
                completeBtn.setVisibility(View.INVISIBLE);
                completeBtn.setOnClickListener(null);
                rangeView.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
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
}
