package com.example.foregroundservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonStart = findViewById(R.id.buttonStart);
        Button buttonStop = findViewById(R.id.buttonStop);
        Button buttonRestart = findViewById(R.id.buttonRestart);

        buttonStart.setOnClickListener(this::clickStart);
        buttonStop.setOnClickListener(this::clickStop);
        buttonRestart.setOnClickListener(this::clickRestart);
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
        Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();
    }

    public void clickStop(View view) {
        Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
    }

    public void clickRestart(View view) {
        clickStop(view);
        clickStart(view);
    }
}