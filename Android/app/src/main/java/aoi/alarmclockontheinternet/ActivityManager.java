package aoi.alarmclockontheinternet;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.Timer;

/**
 * Created by Victor on 31/03/2018.
 */

class ActivityManager {
    private static AOIModelClass AOIModelClass;
    private static AlarmClock alarmClock;
    private static AlarmRecyclerAdapter alarmRecyclerAdapter;

    static void setAOIModel(AOIModel aoiModel) {
        if (AOIModelClass == null) {
            AOIModelClass = new AOIModelClass(aoiModel);
        } else {
            getAOIModel().piController = aoiModel.piController;
        }
    }

    static AOIModel getAOIModel() {
        return AOIModelClass.aoiModel;
    }

    static AlarmClock getAlarmClock() {
        return alarmClock;
    }

    static void initAlarmClock() {
        alarmClock = new AlarmClock(getAOIModel()) ;
        Timer timer = new Timer(true);
        timer.schedule(alarmClock, 5 * 1000, 10 * 1000);
        alarmClock.setRunning(true);
    }

    static void disableAlarmClock() {
        alarmClock.cancel();
        alarmClock.setRunning(false);
    }

    static void setAlarmRecyclerAdapter(AlarmRecyclerAdapter alarmRecyclerAdapter) {
        ActivityManager.alarmRecyclerAdapter = alarmRecyclerAdapter;
    }

    static void notifyAdapterAlarmInserted(int position) {
        if (alarmRecyclerAdapter != null) {
            alarmRecyclerAdapter.notifyItemInserted(position);
        }
    }

    static void notifyAdapterAlarmRemoved(int position) {
        if (alarmRecyclerAdapter != null) {
            alarmRecyclerAdapter.notifyItemRemoved(position);
        }
    }

    static void enableBootReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    static void disableBootReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    private static class AOIModelClass {

        private AOIModel aoiModel;

        private AOIModelClass(AOIModel aoiModel) {
            this.aoiModel = aoiModel;
        }
    }
}
