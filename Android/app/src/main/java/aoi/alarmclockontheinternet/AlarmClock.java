package aoi.alarmclockontheinternet;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.Date;
import java.util.TimerTask;

/**
 * Created by Victor on 01/04/2018.
 */

public class AlarmClock extends TimerTask {
    private AOIModel aoiModel;
    Handler handler = new Handler();
    private boolean running;

    AlarmClock(AOIModel aoiModel) {
        this.aoiModel = aoiModel;
    }

    boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {

        handler.post(new Runnable() {
            public void run() {
                Date now = new Date();
                for (Alarm alarm : aoiModel.alarms) {
                    if (alarm.getTime().before(now) || alarm.getTime().equals(now)) {
                        alarm.enable(aoiModel.piController);
                    }
                }
            }
        });

    }
}
