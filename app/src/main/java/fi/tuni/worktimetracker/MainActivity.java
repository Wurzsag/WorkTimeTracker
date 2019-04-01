package fi.tuni.worktimetracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView tvTimer;

    long startTime;
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
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void updateText() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long mSeconds = System.currentTimeMillis() - startTime;
                int seconds = (int) (mSeconds / 1000) % 60;
                int minutes = (int) (mSeconds / 1000) / 60;
                int hours = minutes / 60;

                //tvTimer.setText(Long.toString(System.currentTimeMillis() - startTime));
                tvTimer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }
        });
    }

    public void runTimer() {

        running = true;

        while(running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            updateText();
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

}
