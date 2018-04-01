package aoi.alarmclockontheinternet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        //TODO implement hostname in rasp pi controller
        ActivityManager.setAOIModel(new AOIModel(this, new RaspberryPiController(this, "git-up.com", "RPiApp/manage.py")));
        ActivityManager.getAOIModel().init();
        if (ActivityManager.getAlarmClock() == null) {
            ActivityManager.initAlarmClock();
        }

        Button newAlarmButton = findViewById(R.id.newalarmbutt);
        newAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AddAlarmActivity.class));
            }
        });

        Button manageAlarmsButton = findViewById(R.id.managealarmbutt);
        manageAlarmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ManageAlarmsActivity.class));
            }
        });
    }
}
