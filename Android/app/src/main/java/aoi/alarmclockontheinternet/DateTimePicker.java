package aoi.alarmclockontheinternet;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Victor on 31/03/2018.
 */

public class DateTimePicker implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private Context context;
    private Date alarmDateTime;
    private boolean useToday;
    private boolean timeSet;
    private boolean launchConsecutive;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private TextView dateView;
    private TextView timeView;

    private OnCompleteRunnable onComplete;

    static void launchConsecutivePickers(Context context, OnCompleteRunnable onComplete) {
        DateTimePicker dateTimePicker = new DateTimePicker(context, true);
        dateTimePicker.onComplete = onComplete;
        dateTimePicker.datePickerDialog.show();
    }

    DateTimePicker(Context context, boolean launchConsecutive) {
        this.context = context;
        this.launchConsecutive = launchConsecutive;

        Calendar calendar = Calendar.getInstance();
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        final int startDay = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(
                context, this, startYear, startMonth, startDay);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(context, this, hour, min, true);

    }

    Date getAlarmDateTime() {
        return alarmDateTime;
    }

    void setUseToday(boolean useToday) {
        this.useToday = useToday;
        if (!useToday) {
            timeSet = false;
            Toast.makeText(context, "Time was reset!", Toast.LENGTH_SHORT).show();
        }
        updateDateTimeViews();
    }

    boolean isTimeSet() {
        return timeSet;
    }

    DatePickerDialog getDatePickerDialog() {
        return datePickerDialog;
    }

    TimePickerDialog getTimePickerDialog() {
        return timePickerDialog;
    }

    void setDateTimeViews(TextView dateView, TextView timeView) {
        this.dateView = dateView;
        this.timeView = timeView;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        alarmDateTime = c.getTime();
        timeSet = false;
        updateDateTimeViews();
        if (!launchConsecutive) {
            Toast.makeText(context, "Time was reset!", Toast.LENGTH_SHORT).show();
        } else {
            timePickerDialog.show();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        if (useToday) {
            alarmDateTime = new Date();
        }
        c.setTime(alarmDateTime);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        alarmDateTime = c.getTime();
        timeSet = true;
        updateDateTimeViews();
        if (launchConsecutive) {
            onComplete.run(alarmDateTime);
        }
    }

    private void updateDateTimeViews() {
        if (dateView != null && timeView != null && alarmDateTime != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM", Locale.US);
            dateView.setText(dateFormat.format(alarmDateTime));

            if (timeSet) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
                timeView.setText(timeFormat.format(alarmDateTime));
            } else {
                timeView.setText("Time");
            }
        }
    }

    static abstract class OnCompleteRunnable {

        public abstract void run(Date alarmDateTime);
    }

}
