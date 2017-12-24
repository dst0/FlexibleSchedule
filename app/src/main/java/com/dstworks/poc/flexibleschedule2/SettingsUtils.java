package com.dstworks.poc.flexibleschedule2;

import android.content.Context;
import android.util.Range;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 4d on 24.12.2017.
 */

public class SettingsUtils {
    private static final String CONFIG_FILE_NAME = "config.txt";

    public static List<TimeRange> getRanges() {
        return ranges;
    }

    private static List<TimeRange> ranges = new ArrayList<>();

    public static int getCurrentRange() {
        return currentRange;
    }

    public static void setCurrentRange(int currentRange) {
        SettingsUtils.currentRange = currentRange;
    }

    private static int currentRange = 0;

    public static void addTimeRange(Context ctx, String name, int hours, int minutes, int seconds) {
        ranges.add(new TimeRange(name, (byte) hours, (byte) minutes, (byte) seconds));
        writeConfiguration(ctx);
    }

    /**
     * Saves ranges to file
     *
     * @param ctx - any used activity instance
     */
    public static void writeConfiguration(Context ctx) {
        String config = "";

        // write current range
        config += currentRange + "\n";
        for (TimeRange range : ranges) {
            config += range.toString() + "\n";
        }
        try (FileOutputStream openFileOutput =
                     ctx.openFileOutput(CONFIG_FILE_NAME, Context.MODE_PRIVATE);) {

            openFileOutput.write(config.getBytes());
            System.out.println("writeConfiguration() success: \n" + config);
        } catch (Exception e) {
            System.out.println("writeConfiguration() error: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Clears existing ranges and fills with ones from saved file
     *
     * @param ctx - any used activity instance
     */
    public static void readConfiguration(Context ctx) {
        try (FileInputStream fileInputStream =
                     ctx.openFileInput(CONFIG_FILE_NAME);) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            ranges.clear();
            String debugValue = "";
            if ((line = reader.readLine()) != null) {
                // read current range
                currentRange = Integer.parseInt(line);

                while ((line = reader.readLine()) != null) {
                    debugValue += line + "\n";
                    String[] values = line.split("`");
                    TimeRange range = new TimeRange(values[0], (byte) Integer.parseInt(values[1]),
                            (byte) Integer.parseInt(values[2]), (byte) Integer.parseInt(values[3]));
                    ranges.add(range);
                }
            }
            reader.close();
            System.out.println("readConfiguration() success: \n" + debugValue);
        } catch (Exception e) {
            System.out.println("Config file absent yet, nothing to read.");
        }
    }
}
