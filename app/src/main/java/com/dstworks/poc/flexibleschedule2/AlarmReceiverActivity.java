package com.dstworks.poc.flexibleschedule2;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.IOException;
import java.util.Calendar;

import static android.view.View.INVISIBLE;

public class AlarmReceiverActivity extends Activity {
    private MediaPlayer mMediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        final Activity thisActivity = this;
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alarm_receiver);

        Button stopAlarm = findViewById(R.id.stopAlarm);
        int currentRange = SettingsUtils.getCurrentRange();
        if (SettingsUtils.getRanges().size() - 1 > currentRange) {
            stopAlarm.setText("Go to next task");
        } else {
            stopAlarm.setText("Finish");
        }

        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();

                AlarmManager.processCompleteAction(thisActivity);

                finish();
            }
        });

        Button delayAlarm1 = findViewById(R.id.delayAlarm1);
        delayAlarm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, 1);
                AlarmManager.runCurrentRange(thisActivity, cal.getTimeInMillis());

                finish();
            }
        });

        Button delayAlarm5 = findViewById(R.id.delayAlarm5);
        delayAlarm5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, 5);
                AlarmManager.runCurrentRange(thisActivity, cal.getTimeInMillis());

                finish();
            }
        });

        Button delayAlarm10 = findViewById(R.id.delayAlarm10);
        delayAlarm10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, 10);
                AlarmManager.runCurrentRange(thisActivity, cal.getTimeInMillis());

                finish();
            }
        });

        Button delayAlarm15 = findViewById(R.id.delayAlarm15);
        delayAlarm15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, 15);
                AlarmManager.runCurrentRange(thisActivity, cal.getTimeInMillis());

                finish();
            }
        });

        Button delayAlarm30 = findViewById(R.id.delayAlarm30);
        delayAlarm30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, 30);
                AlarmManager.runCurrentRange(thisActivity, cal.getTimeInMillis());

                finish();
            }
        });

        Button delayAlarm60 = findViewById(R.id.delayAlarm60);
        delayAlarm60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, 60);
                AlarmManager.runCurrentRange(thisActivity, cal.getTimeInMillis());

                finish();
            }
        });

        playSound(this, getAlarmUri());
    }

    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }

    //Get an alarm sound. Try for an alarm. If none set, try notification,
    //Otherwise, ringtone.
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
}
