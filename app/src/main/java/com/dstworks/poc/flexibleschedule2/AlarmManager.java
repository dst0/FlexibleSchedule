package com.dstworks.poc.flexibleschedule2;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;

/**
 * Created by dst on 25.12.2017.
 */

public class AlarmManager {


    public static void runNextRange(Activity activity) {
        //Create a new PendingIntent and add it to the AlarmManager
        int currentRange = SettingsUtils.getCurrentRange();
        TimeRange timeRange = SettingsUtils.getRanges().get(currentRange);
        Intent intent = new Intent(activity, AlarmReceiverActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity,
                currentRange, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        android.app.AlarmManager am =
                (android.app.AlarmManager) activity.getSystemService(Activity.ALARM_SERVICE);
        am.set(android.app.AlarmManager.RTC_WAKEUP, timeRange.getExpectedTimeInMilliseconds(),
                pendingIntent);
    }

    public static void runNextRange(Activity activity, long timeInMilliseconds) {
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
