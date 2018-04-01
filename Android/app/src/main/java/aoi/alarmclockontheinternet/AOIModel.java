package aoi.alarmclockontheinternet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import xyz.victorolaitan.easyjson.EasyJSON;
import xyz.victorolaitan.easyjson.EasyJSONException;
import xyz.victorolaitan.easyjson.JSONElement;

/**
 * Created by Victor on 31/03/2018.
 */

class AOIModel {
    Context context;
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
            //appDataFile.delete();
            try {
                appData = EasyJSON.open(appDataFile);

                if (!alarmsSet) {
                    ArrayList<JSONElement> arrayList = appData.search("alarms").getChildren();
                    for (JSONElement alarmData : arrayList) {
                        setAlarm(new Date((Long) alarmData.valueOf("time")),
                                (Boolean) alarmData.valueOf("fullSend"),
                                ((Long) alarmData.valueOf("fullSendInterval")).intValue());
                    }
                    alarmsSet = true;
                }

            } catch (IOException | EasyJSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveAlarms() {
        appData = EasyJSON.create(new File(context.getFilesDir(), "appData.txt"));
        JSONElement array = appData.putArray("alarms");
        for (Alarm alarm : alarms) {
            JSONElement struct = array.putStructure("");
            struct.putPrimitive("time", alarm.getTime().getTime());
            struct.putPrimitive("fullSend", alarm.isFullSend());
            struct.putPrimitive("fullSendInterval", alarm.getFullSendInterval());
        }
        try {
            appData.save();
        } catch (EasyJSONException e) {
            e.printStackTrace();
        }
    }

    void setAlarm(Date time, boolean fullSend, int fullSendInterval) {
        boolean enableBootReceiver = alarms.size() == 0;

        Alarm alarm = new Alarm(alarmManager, time, fullSend, fullSendInterval);
        //alarm.setAlarmIntent(generateAlarmIntent());
        alarms.add(alarm);

        ActivityManager.notifyAdapterAlarmInserted(alarms.size() - 1);
        saveAlarms();

        if (enableBootReceiver) {
            ActivityManager.enableBootReceiver(context);
            if (ActivityManager.getAlarmClock() != null && !ActivityManager.getAlarmClock().isRunning()) {
                ActivityManager.initAlarmClock();
            }
        }
    }

    void cancelAlarm(Alarm alarm) {
        cancelAlarm(getIndex(alarm));
    }

    void cancelAlarm(int index) {
        alarms.get(index).disable(piController);
        //alarms.get(index).cancelAlarm(alarmManager);
        alarms.remove(index);

        ActivityManager.notifyAdapterAlarmRemoved(index);
        saveAlarms();

        if (alarms.size() == 0) {
            ActivityManager.disableBootReceiver(context);
            if (ActivityManager.getAlarmClock() != null && ActivityManager.getAlarmClock().isRunning()) {
                ActivityManager.disableAlarmClock();
            }
        }
    }

    int getIndex(Alarm alarm) {
        for (int i = 0; i < alarms.size(); i++) {
            if (alarms.get(i).equals(alarm)) {
                return i;
            }
        }
        return -1;
    }

    PendingIntent generateAlarmIntent() {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("alarmIndex", alarms.size());
        return PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
    }
}
