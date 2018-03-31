package aoi.alarmclockontheinternet;

import android.app.AlarmManager;
import android.app.PendingIntent;

import java.util.Date;

/**
 * Created by Victor on 31/03/2018.
 */

class Alarm {
    private Date time;
    private PendingIntent alarmIntent;
    private boolean enabled;

    Alarm(AlarmManager alarmManager, PendingIntent alarmIntent, Date time) {
        this.time = time;
        this.alarmIntent = alarmIntent;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time.getTime(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    boolean alarmTimePassed(Date now) {
        return now.after(time);
    }

    void cancelAlarm(AlarmManager alarmManager) {
        alarmManager.cancel(alarmIntent);
    }

    void enable(ArduinoController controller) {
        if (!enabled) {
            controller.vibrate();
            enabled = true;
        }
    }

    void disable(ArduinoController controller) {
        if (enabled) {
            controller.stopVibrating();
            enabled = false;
        }
    }

}
