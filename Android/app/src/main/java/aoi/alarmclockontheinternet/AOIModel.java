package aoi.alarmclockontheinternet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Victor on 31/03/2018.
 */

class AOIModel {
    private Context context;
    private AlarmManager alarmManager;
    private ArrayList<Alarm> alarms = new ArrayList<Alarm>();

    AOIModel(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    void setAlarm(Date time) {
        alarms.add(new Alarm(alarmManager,
                PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), 0), time));
        //TODO notify Recycler View Inserted
    }

    void cancelAlarm(int index) {
        alarms.get(index).cancelAlarm(alarmManager);
        alarms.remove(index);
        //TODO notify Recycler View Removed
    }
}
