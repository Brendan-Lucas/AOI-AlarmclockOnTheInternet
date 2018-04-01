package aoi.alarmclockontheinternet;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddAlarmActivity extends AppCompatActivity {
    DateTimePicker dateTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        final AddAlarmActivity context = this;
        dateTimePicker = new DateTimePicker(this, false);

        dateTimePicker.setDateTimeViews((TextView) findViewById(R.id.textView), (TextView) findViewById(R.id.textView3));

        final Button selectDate = findViewById(R.id.opendatebutt);
        final Button selectTime = findViewById(R.id.opentimebutt);

        CheckBox todayCheck = findViewById(R.id.todaycheck);
        todayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectDate.setEnabled(!isChecked);
                selectTime.setEnabled(true);
                dateTimePicker.setUseToday(isChecked);
            }
        });
        selectDate.setEnabled(!todayCheck.isChecked());

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTimePicker.getDatePickerDialog().show();
                selectTime.setEnabled(true);
            }
        });

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTimePicker.getTimePickerDialog().show();
            }
        });
        selectTime.setEnabled(false);

        final CheckBox fullSendCheck = findViewById(R.id.fullsendcheck);
        final EditText fullSendInterval = findViewById(R.id.fullsendinterval);
        fullSendCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fullSendInterval.setEnabled(isChecked);
            }
        });
        fullSendInterval.setEnabled(fullSendCheck.isChecked());

        Button finalSet = findViewById(R.id.finalset);
        finalSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateTimePicker.isTimeSet()) {
                    ActivityManager.getAOIModel().setAlarm(dateTimePicker.getAlarmDateTime(),
                            fullSendCheck.isChecked(),
                            fullSendCheck.isChecked() ? Integer.parseInt(fullSendInterval.getText().toString()) : -1);
                    finish();
                } else {
                    Toast.makeText(context, "Alarm Time wasn't set or has been reset!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
