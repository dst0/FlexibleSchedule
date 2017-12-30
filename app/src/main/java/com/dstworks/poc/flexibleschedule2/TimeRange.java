package com.dstworks.poc.flexibleschedule2;

import android.app.PendingIntent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by 4d on 24.12.2017.
 */

public class TimeRange {

    private ConstraintLayout view;

    private String name;
    private byte hours;
    private byte minutes;
    private byte seconds;
    private PendingIntent pendingIntent;

    private CountDownExecutor countDownExecutor;
    private long lastCompleteDate;
    private long lastStartDate;
    private boolean started = false;
    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy MMM dd, HH:mm:ss");

    public TimeRange(String name, byte hours, byte minutes, byte seconds) {
        this.name = name;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public TextView getValueTextView() {
        return view.findViewById(R.id.value);
    }

    public ConstraintLayout getView() {
        return view;
    }

    public void setView(ConstraintLayout view) {
        this.view = view;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getHours() {
        return hours;
    }

    public void setHours(byte hours) {
        this.hours = hours;
    }

    public byte getMinutes() {
        return minutes;
    }

    public void setMinutes(byte minutes) {
        this.minutes = minutes;
    }

    public byte getSeconds() {
        return seconds;
    }

    public void setSeconds(byte seconds) {
        this.seconds = seconds;
    }

    /**
     * @return time in millis from now
     */
    public long getExpectedTimeInMilliseconds() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, hours);
        calendar.add(Calendar.MINUTE, minutes);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTimeInMillis();
    }

    @Override
    public String toString() {
        return name + "`" + hours + "`" + minutes + "`" + seconds + "`"+
                DATE_FORMAT.format(lastStartDate) + "'" +
                DATE_FORMAT.format(lastCompleteDate) + "'" +
                started;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }

    public CountDownExecutor getCountDownExecutor() {
        return countDownExecutor;
    }

    public void setCountDownExecutor(CountDownExecutor countDownExecutor) {
        this.countDownExecutor = countDownExecutor;
    }

    @NonNull
    public String getText() {
        return format(getHours()) + ":" +
                format(getMinutes()) + ":" +
                format(getSeconds());
    }

    private String format(byte value) {
        return String.format("%02d", value);
    }

    private String format(long value) {
        return DATE_FORMAT.format(value);
    }

    public boolean isStarted() {
        return started;
    }
    public boolean setStarted(boolean started) {
        return this.started = started;
    }

    public long getLastStartDate() {
        return lastStartDate;
    }

    public void setLastStartDate(long lastStartDate) {
        this.lastStartDate = lastStartDate;
    }

    public long getLastCompleteDate() {
        return lastCompleteDate;
    }

    public void setLastCompleteDate(long lastCompleteDate) {
        this.lastCompleteDate = lastCompleteDate;
    }
}
