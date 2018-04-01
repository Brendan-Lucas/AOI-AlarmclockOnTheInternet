package aoi.alarmclockontheinternet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class ManageAlarmsActivity extends AppCompatActivity {
    RecyclerView alarmsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_alarms);

        final Context context = this;

        Button newAlarmButton = findViewById(R.id.manage_newAlarm);
        newAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AddAlarmActivity.class));
            }
        });

        alarmsRecyclerView = findViewById(R.id.alarm_list);
        AlarmRecyclerAdapter alarmRecyclerAdapter = new AlarmRecyclerAdapter(ActivityManager.getAOIModel().alarms);

        ActivityManager.setAlarmRecyclerAdapter(alarmRecyclerAdapter);
        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        alarmsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        alarmsRecyclerView.setAdapter(alarmRecyclerAdapter);
    }
}
