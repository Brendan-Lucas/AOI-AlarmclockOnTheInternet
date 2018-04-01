package aoi.alarmclockontheinternet;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Victor on 31/03/2018.
 */

class ActivityManager {
    private static AOIModelClass AOIModelClass;
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
