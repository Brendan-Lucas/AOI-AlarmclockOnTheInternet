package aoi.alarmclockontheinternet;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityAlarmRinging extends AppCompatActivity {
    Ringtone r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);

        final Alarm alarm = ActivityManager.getAOIModel().alarms
                .get(getIntent().getIntExtra("alarmIndex", -1));

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE d MMMM YYYY", Locale.US);
        ((TextView) findViewById(R.id.ringer_txtDate)).setText(dateFormat.format(alarm.getTime()));

        dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        ((TextView) findViewById(R.id.ringer_txtTime)).setText(dateFormat.format(alarm.getTime()));

        Button btnSnooze = findViewById(R.id.ringer_btnSnooze);
        if (alarm.isFullSend()) {
            btnSnooze.setEnabled(false);
            findViewById(R.id.ringer_fullSend).setVisibility(View.VISIBLE);
            Handler startupDelayHandler = new Handler();
            startupDelayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        AOIModel aoiModel = ActivityManager.getAOIModel();
                        aoiModel.cancelAlarm(alarm);
                        aoiModel.setAlarm(new Date(), true, alarm.getFullSendInterval());
                    } catch (Exception ignored) {
                    }
                    finish();

                }
            }, alarm.getFullSendInterval() * 1000);
        }
        btnSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AOIModel aoiModel = ActivityManager.getAOIModel();
                aoiModel.cancelAlarm(alarm);
                aoiModel.setAlarm(new Date(alarm.getTime().getTime() + (5*1000)), false, -1);
                finish();
            }
        });

        Button btnDismiss = findViewById(R.id.ringer_btnDismiss);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getAOIModel().cancelAlarm(alarm);
                finish();
            }
        });

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
        r.play();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        r.stop();
    }
}
