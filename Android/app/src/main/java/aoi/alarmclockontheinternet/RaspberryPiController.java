package aoi.alarmclockontheinternet;

/**
 * Created by Victor on 31/03/2018.
 */

public class RaspberryPiController {
    private String hostname, page;

    RaspberryPiController(String hostname, String page) {
        this.hostname = hostname;
        this.page = page;
    }

    //TODO connect raspberry-pi api

    void vibrate(final Alarm alarm) {
        HTTPHandler httpHandler = new HTTPHandler(HTTPHandler.BY_GET, hostname, page, new HTTPHandler.PostExecuteTask() {
            @Override
            void onPostExecute(String result) {
                if (result != null) {
                    alarm.setEnabled(true);
                }
            }
        });
        httpHandler.execute("enabled", "true");
    }

    void stopVibrating(final Alarm alarm) {
        HTTPHandler httpHandler = new HTTPHandler(HTTPHandler.BY_GET, hostname, page, new HTTPHandler.PostExecuteTask() {
            @Override
            void onPostExecute(String result) {
                if (result != null) {
                    alarm.setEnabled(false);
                }
            }
        });
        httpHandler.execute("enabled", "false");
    }
}
