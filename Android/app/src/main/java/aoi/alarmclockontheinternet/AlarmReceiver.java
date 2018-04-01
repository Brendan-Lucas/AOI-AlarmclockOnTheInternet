package aoi.alarmclockontheinternet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * Created by Victor on 31/03/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    //TODO configure receiver class

    @Override
    public void onReceive(Context context, Intent intent) {
        AOIModel aoiModel = ActivityManager.getAOIModel();

        Alarm alarm = aoiModel.alarms.get(intent.getIntExtra("alarmIndex", -1));
        alarm.enable(aoiModel.piController);
    }
}
