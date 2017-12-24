package com.dstworks.poc.flexibleschedule2;

import android.webkit.JavascriptInterface;

/**
 * Created by 4d on 24.12.2017.
 */

public class TimeRange {
    private String name;
    private byte hours;
    private byte minutes;
    private byte seconds;

    public TimeRange(String name, byte hours, byte minutes, byte seconds) {
        this.name = name;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
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

    @Override
    public String toString() {
        return name + "`" + hours + "`" + minutes + "`" + seconds;
    }
}
