package aoi.alarmclockontheinternet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Victor on 31/03/2018.
 */

public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmRecyclerAdapter.AlarmViewHolder> {

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        Alarm alarm;
        TextView alarmDate;
        TextView alarmTime;

        AlarmViewHolder(final View itemView) {
            super(itemView);
            alarmDate = itemView.findViewById(R.id.alarm_list_row_date);
            alarmTime = itemView.findViewById(R.id.alarm_list_row_time);

            itemView.findViewById(R.id.alarm_list_row_modify).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateTimePicker.launchConsecutivePickers(itemView.getContext(), new DateTimePicker.OnCompleteRunnable() {
                        @Override
                        public void run(Date alarmDateTime) {
                            AOIModel aoiModel = ActivityManager.getAOIModel();
                            aoiModel.cancelAlarm(alarm);
                            aoiModel.setAlarm(alarmDateTime, alarm.isFullSend(), alarm.getFullSendInterval());
                        }
                    });
                }
            });

            itemView.findViewById(R.id.alarm_list_row_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityManager.getAOIModel().cancelAlarm(alarm);
                }
            });

        }

        void updateInfo() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM", Locale.US);
            alarmDate.setText(dateFormat.format(alarm.getTime()));

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
            alarmTime.setText(timeFormat.format(alarm.getTime()));
        }
    }

    private List<Alarm> alarms;

    AlarmRecyclerAdapter(List<Alarm> alarms) {
        this.alarms = alarms;
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alarm_list_row, viewGroup, false);
        AlarmViewHolder pvh = new AlarmViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder alarmViewHolder, int i) {
        alarmViewHolder.alarm = alarms.get(i);
        alarmViewHolder.updateInfo();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
