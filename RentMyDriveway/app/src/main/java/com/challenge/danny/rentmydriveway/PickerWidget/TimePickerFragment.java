package com.challenge.danny.rentmydriveway.PickerWidget;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.challenge.danny.rentmydriveway.R;
import com.challenge.danny.rentmydriveway.RentingOutActivity;

import java.util.Calendar;

/**
 * Created by Gerardo on 10/5/18.
 * Time picker, be able to pick a time and set it
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int hourOfday = 1;
    private int minute = 15;
    private String time;
    String wordsOnStart;
    TextView textView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public int getHourOfday() {
        return this.hourOfday;
    }

    public int getMinute() {
        return this.minute;
    }

    public String getTime() {
        return time;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        setHourOfday(hourOfDay);
        setMinute(minute);
        this.time = new StringBuilder().append(hourOfDay).append(":").append(minute).toString();
        // Downcast to get previous activity rather than the Fragment activity
        RentingOutActivity callActivity = (RentingOutActivity) getActivity();
        callActivity.onTimeChanged(time, wordsOnStart);
        changeUI();
    }

    private void changeUI() {
        textView.setText(wordsOnStart+" time: "+ hourOfday+":"+minute);
    }

    public void setTextView(TextView textView){
        this.textView = textView;
    }

    public void setWordsOnStart(String wordsOnStart){
        this.wordsOnStart = wordsOnStart;
    }

    public void setHourOfday(int hourOfday) {
        this.hourOfday = hourOfday;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}