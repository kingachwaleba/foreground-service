package com.example.foregroundservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button buttonStart;
    private Button buttonStop;
    private Button buttonRestart;

    private TextView textInfoService;
    private TextView textInfoSettings;

    private String message;
    private Boolean showTime;
    private Boolean work;
    private Boolean workDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);
        buttonRestart = findViewById(R.id.buttonRestart);

        textInfoService = findViewById(R.id.textInfoServiceState);
        textInfoSettings = findViewById(R.id.textInfoSettings);

        buttonStart.setOnClickListener(this::clickStart);
        buttonStop.setOnClickListener(this::clickStop);
        buttonRestart.setOnClickListener(this::clickRestart);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemSettings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.itemExit) {
            finishAndRemoveTask();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    public void clickStart(View view) {
        getPreferences();

        Intent startIntent = new Intent(this, MyForegroundService.class);
        startIntent.putExtra(MyForegroundService.MESSAGE, message);
        startIntent.putExtra(MyForegroundService.TIME, showTime);
        startIntent.putExtra(MyForegroundService.WORK, work);
        startIntent.putExtra(MyForegroundService.WORK_DOUBLE, workDouble);


        ContextCompat.startForegroundService(this, startIntent);
        updateUI();
    }
    public void clickStop(View view) {
        Intent stopIntent = new Intent(this, MyForegroundService.class);
        stopService(stopIntent);
        updateUI();

    }

    public void clickRestart(View view) {
        clickStop(view);
        clickStart(view);
    }

    private String getPreferences() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        message = sharedPreferences.getString("message","ForegroundService");
        showTime = sharedPreferences.getBoolean("show_time", true);
        work = sharedPreferences.getBoolean("sync",true);
        workDouble = sharedPreferences.getBoolean("double", false);

        return "Message: " + message + "\n"
                +"show_time: " + showTime.toString() +"\n"
                +"work: " + work.toString() + "\n"
                +"double: " + workDouble.toString();
    }

    private void updateUI(){
        if(isMyForegroundServiceRunning()){
            buttonStart.setEnabled(false);
            buttonStop.setEnabled(true);
            buttonRestart.setEnabled(true);
            textInfoService.setText(getString(R.string.info_service_running));
        }
        else {
            buttonStart.setEnabled(true);
            buttonStop.setEnabled(false);
            buttonRestart.setEnabled(false);
            textInfoService.setText(getString(R.string.info_service_not_running));
        }

        textInfoSettings.setText(getPreferences());
    }

    @SuppressWarnings("deprecation")
    private boolean isMyForegroundServiceRunning(){

        String myServiceName = MyForegroundService.class.getName();
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for(ActivityManager.RunningServiceInfo runningService
                : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            String runningServiceName = runningService.service.getClassName();
            if(runningServiceName.equals(myServiceName))
                return true;
        }
        return false;
    }
}