package aoi.alarmclockontheinternet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import xyz.victorolaitan.easyjson.EasyJSON;
import xyz.victorolaitan.easyjson.EasyJSONException;
import xyz.victorolaitan.easyjson.JSONElement;

/**
 * Created by Victor on 31/03/2018.
 */

class AOIModel {
    private Context context;
    private AlarmManager alarmManager;
    RaspberryPiController piController;

    private EasyJSON appData;
    private boolean alarmsSet;
    ArrayList<Alarm> alarms = new ArrayList<>();

    AOIModel(Context context, RaspberryPiController piController) {
        this.context = context;
        this.piController = piController;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    void init() {
        File appDataFile = new File(context.getFilesDir(), "appData.txt");
        if (appDataFile.exists()) {
            try {
                appData = EasyJSON.open(appDataFile);

                if (!alarmsSet) {
                    ArrayList<JSONElement> arrayList = appData.search("alarms").getChildren();
                    for (JSONElement alarmData : arrayList) {
                        setAlarm(new Date((Long) alarmData.getValue()));
                    }
                    alarmsSet = true;
                }

            } catch (IOException | EasyJSONException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    boolean saveAlarms() {
        appData = EasyJSON.create(new File(context.getFilesDir(), "appData.txt"));
        JSONElement array = appData.putArray("alarms");
        for (Alarm alarm : alarms) {
            array.putPrimitive(alarm.getTime().getTime());
        }
        try {
            appData.save();
        } catch (EasyJSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    void setAlarm(Date time) {
        boolean enableBootReceiver = alarms.size() == 0;

        Alarm alarm = new Alarm(alarmManager, time);
        alarm.setAlarmIntent(generateAlarmIntent());
        alarms.add(alarm);

        ActivityManager.notifyAdapterAlarmInserted(alarms.size() - 1);
        saveAlarms();

        if (enableBootReceiver) {
            ActivityManager.enableBootReceiver(context);
        }
    }

    void cancelAlarm(Alarm alarm) {
        for (int i = 0; i < alarms.size(); i++) {
            if (alarms.get(i).equals(alarm)) {
                cancelAlarm(i);
                return;
            }
        }
    }

    void cancelAlarm(int index) {
        alarms.get(index).cancelAlarm(alarmManager);
        alarms.remove(index);

        ActivityManager.notifyAdapterAlarmRemoved(index);
        saveAlarms();

        if (alarms.size() == 0) {
            ActivityManager.disableBootReceiver(context);
        }
    }

    PendingIntent generateAlarmIntent() {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("alarmIndex", alarms.size());
        return PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
    }
}
