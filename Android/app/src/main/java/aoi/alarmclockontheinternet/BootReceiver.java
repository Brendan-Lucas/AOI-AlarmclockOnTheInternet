package aoi.alarmclockontheinternet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

/**
 * Created by Victor on 31/03/2018.
 */

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {

            //TODO set alarms on boot
            if (ActivityManager.getAOIModel() == null) {
                ActivityManager.setAOIModel(new AOIModel(context, null));
            }

            ActivityManager.getAOIModel().init();

        }
    }
}
