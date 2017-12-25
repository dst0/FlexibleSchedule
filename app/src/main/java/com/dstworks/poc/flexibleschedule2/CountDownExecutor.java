package com.dstworks.poc.flexibleschedule2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Countdown impl for range view
 * Created by dst on 25.12.2017.
 */
public class CountDownExecutor {

    private TextView timerTextView;
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
            seconds = seconds % 60;
            int minutes = seconds / 60;
            int hours = minutes / 60;

            timerTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            timerHandler.postDelayed(this, 1000);
        }
    };

    public void run(TextView textView, TimeRange timeRange) {
        timerTextView = textView;
        timerTimeRange = timeRange;

        startTime = System.currentTimeMillis();
        endTime = timerTimeRange.getExpectedTimeInMilliseconds();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public void stop() {
        timerHandler.removeCallbacks(timerRunnable);
        timerTextView.setText(timerTimeRange.getText());
    }

    public void onPause() {
        timerHandler.removeCallbacks(timerRunnable);
    }

}