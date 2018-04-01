package aoi.alarmclockontheinternet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.Date;

/**
 * Created by Victor on 31/03/2018.
 */

class Alarm {
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private Date time;
    private boolean enabled, fullSend;
    private int fullSendInterval;

    Alarm(AlarmManager alarmManager, Date time, boolean fullSend, int fullSendIntervalSeconds) {
        this.alarmManager = alarmManager;
        this.time = time;
        this.fullSend = fullSend;
        this.fullSendInterval = fullSendIntervalSeconds;
    }

    Date getTime() {
        return time;
    }

    boolean isFullSend() {
        return fullSend;
    }

    int getFullSendInterval() {
        return fullSendInterval;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    void setAlarmIntent(PendingIntent alarmIntent) {
        this.alarmIntent = alarmIntent;
        alarmManager.setInexactRepeating(AlarmManager.RTC, time.getTime(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    void cancelAlarm(AlarmManager alarmManager) {
        alarmManager.cancel(alarmIntent);
    }

    void enable(RaspberryPiController controller) {
        if (!enabled) {
            controller.vibrate(this);
            Intent ringerIntent = new Intent(ActivityManager.getAOIModel().context, ActivityAlarmRinging.class);
            ringerIntent.putExtra("alarmIndex", ActivityManager.getAOIModel().getIndex(this));
            ActivityManager.getAOIModel().context.startActivity(ringerIntent);
            enabled = true;
        }
    }

    void disable(RaspberryPiController controller) {
        if (enabled) {
            controller.stopVibrating(this);
            enabled = false;
        }
    }

}
