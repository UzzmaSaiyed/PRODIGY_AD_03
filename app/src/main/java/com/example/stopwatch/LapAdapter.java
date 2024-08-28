package com.example.stopwatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.stopwatch.LapItem;

import java.util.List;

public class LapAdapter extends ArrayAdapter<LapItem> {

    private Context context;
    private List<LapItem> lapList;

    public LapAdapter(Context context, List<LapItem> lapList) {
        super(context, 0, lapList);
        this.context = context;
        this.lapList = lapList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lap_list_item, parent, false);
        }

        LapItem currentLap = lapList.get(position);

        TextView tvLapNumber = convertView.findViewById(R.id.tvLapNumber);
        TextView tvLapTime = convertView.findViewById(R.id.tvLapTime);
        TextView tvOverallTime = convertView.findViewById(R.id.tvOverallTime);

        tvLapNumber.setText(""+currentLap.getLapNumber());
        tvLapTime.setText(currentLap.getLapTime());
        tvOverallTime.setText(currentLap.getOverallTime());

        return convertView;
    }
}