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
    Runnable timerRunnable;

    {
        try {
            timerRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        long millis = endTime - System.currentTimeMillis();
                        int seconds = (int) (millis / 1000);
                        int minutes = seconds / 60;
                        int hours = minutes / 60;
                        seconds = seconds % 60;
                        minutes = minutes % 60;

                        timerTimeRange.getValueTextView()
                                .setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                    } catch (Throwable e) {
                        System.err.println("can't update view from countdown handler: " + e);
                    }
                    timerHandler.postDelayed(this, 1000);
                }
            };
        } catch (Throwable e) {
            System.err.println("can't create countdown handler: " + e);
        }
    }

    public void run(TimeRange timeRange) {
        try {
            timerTimeRange = timeRange;

            startTime = System.currentTimeMillis();
            endTime = timerTimeRange.getExpectedTimeInMilliseconds();
            timerHandler.postDelayed(timerRunnable, 0);
        } catch (Throwable e) {
            System.err.println("can't run countdown handler: " + e);
        }
    }

    public void stop() {
        try {
            timerHandler.removeCallbacks(timerRunnable);
            timerTimeRange.getValueTextView().setText(timerTimeRange.getText());
        } catch (Throwable e) {
            System.err.println("can't run countdown handler: " + e);
        }
    }

    public void onPause() {
        timerHandler.removeCallbacks(timerRunnable);
    }

}