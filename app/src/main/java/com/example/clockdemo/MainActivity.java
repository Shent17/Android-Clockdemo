package com.example.clockdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ClockView clockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clockView = (ClockView) findViewById(R.id.clock);
        clockView.startClock();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        clockView.startClock();
//    }

    @Override
    protected void onStop() {
        super.onStop();
        clockView.stopClock();
    }
}