package aoi.alarmclockontheinternet;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Victor on 14/03/2018.
 */
class HTTPHandler extends AsyncTask<String, Integer, String> {
    static final int BY_GET = 0;
    static final int BY_POST = 1;

    private int byGetOrPost = 0;
    private PostExecuteTask postExecuteTask;
    private String hostname, page;

    //flag 0 means get and 1 means post.(By default it is get.)
    HTTPHandler(int flag, String hostname, String page, PostExecuteTask postExecuteTask) {
        byGetOrPost = flag;
        this.hostname = hostname;
        this.page = page;
        this.postExecuteTask = postExecuteTask;
    }

    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... args) {
        args[0] = hostname + "/" + page;
        String data;
        try {
            if (args.length - 1 % 2 != 0) {
                data = args[1];
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    if (i % 2 != 0) {
                        sb.append(URLEncoder.encode(args[i], "UTF-8"))
                                .append("=")
                                .append(URLEncoder.encode(args[i + 1], "UTF-8"));

                        if (i != args.length - 2) {
                            sb.append("&");
                        }
                    }
                }
                data = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpURLConnection connection = null;
        String line;
        if (byGetOrPost == 0) { //means by Get Method
            try {
                URL url;
                if (data.equals("")) {
                    url = new URL(args[0]);
                } else {
                    url = new URL(args[0] + "?" + data);
                }
                connection = (HttpURLConnection) url.openConnection();
                connection.getResponseMessage();
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(connection.getInputStream()));

                line = in.readLine();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
                line = null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } else {
            try {
                connection = (HttpURLConnection) new URL(args[0]).openConnection();
                connection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

                wr.write(data);
                wr.flush();
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(connection.getInputStream()));

                line = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
                line = null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        return line;
    }

    @Override
    protected void onPostExecute(String result) {
        postExecuteTask.onPostExecute(result);
    }

    static abstract class PostExecuteTask {

        abstract void onPostExecute(String result);
    }
}
