package aoi.alarmclockontheinternet;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Victor on 31/03/2018.
 */

public class RaspberryPiController {
    private Context context;
    private String hostname, page;

    RaspberryPiController(Context context, String hostname, String page) {
        this.context = context;
        this.hostname = hostname;
        this.page = page;
    }

    //TODO connect raspberry-pi api

    void vibrate(final Alarm alarm) {
        try {
            HTTPHandler httpHandler = new HTTPHandler(HTTPHandler.BY_GET, hostname, page, new HTTPHandler.PostExecuteTask() {
                @Override
                void onPostExecute(String result) {
                }
            });
            httpHandler.execute("");
            Toast.makeText(context, "'Alarm ringing'", Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {
        }
    }

    void stopVibrating(final Alarm alarm) {
        try {
            HTTPHandler httpHandler = new HTTPHandler(HTTPHandler.BY_GET, hostname, page, new HTTPHandler.PostExecuteTask() {
                @Override
                void onPostExecute(String result) {
                }
            });
            httpHandler.execute("enabled", "false");
            Toast.makeText(context, "'Alarm not ringing'", Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {
        }
    }
}
