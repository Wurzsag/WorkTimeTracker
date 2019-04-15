package fi.tuni.worktimetracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView tvTimer;
    private TimePicker timePicker;

    long startTime;
    long currentRunTime;
    long stopTime;
    boolean running;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        tvTimer = findViewById(R.id.timer);
        timePicker = (TimePicker) findViewById(R.id.timePickerStop);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        timePicker.setIs24HourView(true);
    }

    public void updateText() {

        runOnUiThread(() -> {
            long mSeconds = System.currentTimeMillis() - startTime;
            currentRunTime = mSeconds;

            int seconds = (int) (mSeconds / 1000) % 60;
            int minutes = (int) (mSeconds / 1000) / 60 % 60;
            int hours = (int) (mSeconds / 1000) / 60 / 60;

            tvTimer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        });
    }

    public void runTimer() {

        running = true;

        while(running) {

            if(stopTime < System.currentTimeMillis())
                running = false;

            updateText();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startTimer(View v) {
        startTime = System.currentTimeMillis();

        Thread t = new Thread(this::runTimer);
        t.start();
    }

    public void stopTimer(View v) {
        running = false;
    }

    public void addStopTime(View v) {
        timePicker = (TimePicker) findViewById(R.id.timePickerStop);

        int pickerHour = timePicker.getHour();
        int pickerMinute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        long stopTimeMinutes = (pickerHour - currentHour) * 60 + (pickerMinute - currentMinute);

        stopTime = System.currentTimeMillis() + stopTimeMinutes * 60 * 1000;
    }

    public void continueTimer(View v) {
        startTime = System.currentTimeMillis() - currentRunTime;

        Thread t = new Thread(this::runTimer);
        t.start();
    }

    public void addMinutes(View v) {
        startTime -= 600000;
    }

    public void addHour(View v) {
        startTime -= 3600000;
    }

}
