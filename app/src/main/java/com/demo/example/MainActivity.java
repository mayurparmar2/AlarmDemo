package com.demo.example;


import static java.lang.System.currentTimeMillis;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.demo.example.databinding.ActivityMainBinding;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
public class MainActivity extends AppCompatActivity  implements TimePickerDialog.OnTimeSetListener {
    ActivityMainBinding binding;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.alarmToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                 DialogFragment timePickerDialog =new timePickerDialogFragment();
                timePickerDialog.show(getSupportFragmentManager(),"timePicker");
            } else {
                cancelAlarm();
                Log.d("MyActivity", "Alarm Off");
            }
        });
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);
        UpdateTimeText(c);
        startAlarm(c);
    }

    private void UpdateTimeText(Calendar c) {
        String timeText =" Alarm Set for ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        binding.timeText.setText(timeText);
    }


    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        binding.timeText.setText("Alarm canceled");
    }
}
