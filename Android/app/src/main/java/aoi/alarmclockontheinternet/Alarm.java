package aoi.alarmclockontheinternet;

import android.app.AlarmManager;
import android.app.PendingIntent;

import java.util.Date;

/**
 * Created by Victor on 31/03/2018.
 */

class Alarm {
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private Date time;
    private boolean enabled;

    Alarm(AlarmManager alarmManager, Date time) {
        this.alarmManager = alarmManager;
        this.time = time;
    }

    Date getTime() {
        return time;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    void setAlarmIntent(PendingIntent alarmIntent) {
        this.alarmIntent = alarmIntent;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time.getTime(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    void cancelAlarm(AlarmManager alarmManager) {
        alarmManager.cancel(alarmIntent);
    }

    void enable(RaspberryPiController controller) {
        if (!enabled) {
            controller.vibrate(this);
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
