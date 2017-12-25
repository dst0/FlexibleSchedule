package com.dstworks.poc.flexibleschedule2;

import android.os.Handler;


/**
 * Countdown impl for range view
 * Created by dst on 25.12.2017.
 */
public class CountDownExecutor {

    private TimeRange timerTimeRange;
    private long startTime = 0;
    private long endTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = endTime - System.currentTimeMillis();
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;

            timerTimeRange.getValueTextView()
                    .setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            timerHandler.postDelayed(this, 1000);
        }
    };

    public void run(TimeRange timeRange) {
        timerTimeRange = timeRange;

        startTime = System.currentTimeMillis();
        endTime = timerTimeRange.getExpectedTimeInMilliseconds();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public void stop() {
        timerHandler.removeCallbacks(timerRunnable);
        timerTimeRange.getValueTextView().setText(timerTimeRange.getText());
    }

    public void onPause() {
        timerHandler.removeCallbacks(timerRunnable);
    }

}