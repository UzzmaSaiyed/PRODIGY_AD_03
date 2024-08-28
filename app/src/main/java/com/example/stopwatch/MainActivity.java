package com.example.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvTimer;
    private Button btnStart, btnPause, btnLap, btnReset;
    private ListView lvLaps;
    private Handler handler;
    private long startTime, timeInMilliseconds, timeSwapBuff, updateTime = 0L;
    private int seconds, minutes, milliseconds;
    private boolean isRunning = false;
    private Runnable updateTimer;
    private ArrayList<LapItem> lapList;
    private LapAdapter lapAdapter;
    private long lastLapTime = 0L;  // Track the time of the last lap
    private boolean isFirstLap = true; // Track if it's the first lap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTimer = findViewById(R.id.tvTimer);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnLap = findViewById(R.id.btnLap);
        btnReset = findViewById(R.id.btnReset);
        lvLaps = findViewById(R.id.lvLaps);
        btnPause.setEnabled(false);
        btnLap.setEnabled(false);
        btnReset.setEnabled(false);

        handler = new Handler();
        lapList = new ArrayList<>();
        lapAdapter = new LapAdapter(this, lapList);
        lvLaps.setAdapter(lapAdapter);

        updateTimer = new Runnable() {
            @Override
            public void run() {
                timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
                updateTime = timeSwapBuff + timeInMilliseconds;
                seconds = (int) (updateTime / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                milliseconds = (int) (updateTime % 1000);
                milliseconds = milliseconds / 10;

                tvTimer.setText(String.format("%02d:%02d:%02d", minutes, seconds, milliseconds));
                handler.postDelayed(this, 60);
            }
        };

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimer, 0);
                    isRunning = true;
                    btnStart.setEnabled(false);
                    btnPause.setEnabled(true);
                    btnLap.setEnabled(true);
                    btnReset.setEnabled(true);
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    timeSwapBuff += timeInMilliseconds;
                    handler.removeCallbacks(updateTimer);
                    isRunning = false;
                    btnStart.setEnabled(true);
                    btnPause.setEnabled(false);
                    btnLap.setEnabled(false);
                }
            }
        });


        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = SystemClock.uptimeMillis() - startTime; // Get current elapsed time
                String lapTime;

                String overallTime = tvTimer.getText().toString(); // Get the overall time
                String[] timeComponents = overallTime.split(":"); // Split the time into components (MM:SS:MS)
                long minutes = Long.parseLong(timeComponents[0]);
                long seconds = Long.parseLong(timeComponents[1]);
                long milliseconds = Long.parseLong(timeComponents[2]);

                long overallTimeInMillis = minutes * 60 * 1000 + seconds * 1000 + milliseconds * 10; // Calculate the overall time in milliseconds
                int lapNumber = lapList.size() + 1;
                if (isFirstLap) {
                    lapTime = overallTime; // Use the overall time as lap time for the first lap
                    isFirstLap = false; // No longer the first lap
                } else {
                    LapItem lastLapItem = lapList.get(0);
                    String lastLapTimeStr = lastLapItem.getOverallTime();
                    String[] lastLapTimeComponents = lastLapTimeStr.split(":");
                    long lastLapMinutes = Long.parseLong(lastLapTimeComponents[0]);
                    long lastLapSeconds = Long.parseLong(lastLapTimeComponents[1]);
                    long lastLapMilliseconds = Long.parseLong(lastLapTimeComponents[2]);

                    long lastLapTimeInMillis = lastLapMinutes * 60 * 1000 + lastLapSeconds * 1000 + lastLapMilliseconds * 10;
                    lapTime = formatTime(overallTimeInMillis - lastLapTimeInMillis); // Calculate lap time for subsequent laps
                }

                // Add new lap item
                lapList.add(0, new LapItem(lapNumber, lapTime, overallTime));
                lapAdapter.notifyDataSetChanged();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSwapBuff = 0L;
                startTime = 0L;
                updateTime = 0L;
                seconds = 0;
                minutes = 0;
                milliseconds = 0;
                tvTimer.setText("00:00:00");
                handler.removeCallbacks(updateTimer);
                lapList.clear();
                lapAdapter.notifyDataSetChanged();
                isRunning = false;
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
                btnLap.setEnabled(false);
                btnReset.setEnabled(false);
                lastLapTime = 0L; // Reset the last lap time
                isFirstLap = true; // Reset the first lap flag
            }
        });
    }

    // Helper method to format time in hh:mm:ss
    private String formatTime(long timeInMillis) {
        int seconds = (int) (timeInMillis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int milliseconds = (int) (timeInMillis % 1000);
        milliseconds = milliseconds / 10;
        return String.format("%02d:%02d:%02d", minutes, seconds, milliseconds);
    }
}