package com.dstworks.poc.flexibleschedule2;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

public class NewTimerRangeActivity extends AppCompatActivity {

    private Button addRangeButton;

    private NumberPicker hoursField;
    private EditText nameField;
    private NumberPicker minutesField;
    private NumberPicker secondsField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_timer_range);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addRangeButton = (Button) findViewById(R.id.addRangeButton);
        System.out.println(addRangeButton);

        hoursField = findViewById(R.id.hour);
        //Populate NumberPicker values from minimum and maximum value range
        hoursField.setMinValue(0);
        hoursField.setMaxValue(99);
        //Gets whether the selector wheel wraps when reaching the min/max value.
        hoursField.setWrapSelectorWheel(true);

        minutesField = findViewById(R.id.minute);
        //Populate NumberPicker values from minimum and maximum value range
        minutesField.setMinValue(0);
        minutesField.setMaxValue(59);
        //Gets whether the selector wheel wraps when reaching the min/max value.
        minutesField.setWrapSelectorWheel(true);

        secondsField = findViewById(R.id.seconds);
        //Populate NumberPicker values from minimum and maximum value range
        secondsField.setMinValue(0);
        secondsField.setMaxValue(59);
        //Gets whether the selector wheel wraps when reaching the min/max value.
        secondsField.setWrapSelectorWheel(true);

        nameField = findViewById(R.id.timerName);
        final NewTimerRangeActivity activity = this;

        addRangeButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            public void onClick(View view) {
                String name = nameField.getText().toString();
                int hours = hoursField.getValue();
                int minutes = minutesField.getValue();
                int seconds = secondsField.getValue();
                System.out.println(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":"
                        + String.format("%02d", seconds));
                SettingsUtils.addTimeRange(activity, name, hours, minutes, seconds);
                finish();
            }
        });

    }

}
