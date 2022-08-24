package com.example.clockdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Date;
import java.util.jar.Attributes;

public class ClockView extends View {
    private Paint circlePaint, dialPaint, numberPaint;
    //view的宽和高
    private float mWidth, mHeight;
    private float circleRadius;
    private float circleX, circleY;
    private int minute, second;
    private double hour;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0) {
                invalidate();
            }
        }
    };

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.BLACK);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(10);

        dialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dialPaint.setColor(Color.BLACK);
        dialPaint.setStrokeWidth(5);

        numberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        numberPaint.setColor(Color.BLACK);
        numberPaint.setStrokeWidth(5);
        numberPaint.setTextSize(30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        circleRadius = mWidth / 2 - 10;
        circleX = mWidth / 2;
        circleY = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setTimes();
        canvas.drawCircle(circleX, circleY, 5, circlePaint);
        canvas.drawCircle(circleX, circleY, circleRadius, circlePaint);
        drawDial(canvas);
        drawPointer(canvas);

    }

    private void drawDial(Canvas canvas) {
        Point hourStratPoint = new Point(circleX, circleY - circleRadius);
        Point hourEndPoint = new Point(circleX, circleY - circleRadius + 40);
        Point minuteStartPoint = new Point(circleX, circleY - circleRadius);
        Point minuteEndPoint = new Point(circleX, circleY - circleRadius + 10);

        String clockNumber;
        for(int i = 0; i < 60; ++i) {
            if(i % 5 == 0) {
                if(i == 0) {
                    clockNumber = "12";
                } else {
                    clockNumber = String.valueOf(i / 5);
                }

                canvas.drawLine(hourStratPoint.getX(), hourStratPoint.getY(), hourEndPoint.getX(), hourEndPoint.getY(), circlePaint);
                canvas.drawText(clockNumber, circleX - numberPaint.measureText(clockNumber) / 2, hourEndPoint.getY() + 30, numberPaint);
            } else {
                canvas.drawLine(minuteStartPoint.getX(), minuteStartPoint.getY(), minuteEndPoint.getX(), minuteEndPoint.getY(), circlePaint);
            }

            canvas.rotate(360 / 60, circleX, circleY);
        }
    }

    private void drawPointer(Canvas canvas) {
        canvas.translate(circleX, circleY);
        float hourX = (float) Math.cos(Math.toRadians(hour * 30 + 270)) * circleRadius * 0.5f;
        float hourY = (float) Math.sin(Math.toRadians(hour * 30 + 270)) * circleRadius * 0.5f;
        float minuteX = (float) Math.cos(Math.toRadians(minute * 6 + 270)) * circleRadius * 0.7f;
        float minuteY = (float) Math.sin(Math.toRadians(minute * 6 + 270)) * circleRadius * 0.7f;
        float secondX = (float) Math.cos(Math.toRadians(second * 6 + 270)) * circleRadius * 0.8f;
        float secondY = (float) Math.sin(Math.toRadians(second * 6 + 270)) * circleRadius * 0.8f;

        canvas.drawLine(0, 0, hourX, hourY, circlePaint);
        canvas.drawLine(0, 0, minuteX, minuteY, circlePaint);
        canvas.drawLine(0, 0, secondX, secondY, dialPaint);

        handler.sendEmptyMessageDelayed(0, 1000);
    }

    private void setTimes() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        second = getTimes(date, Calendar.SECOND);
        minute = getTimes(date, Calendar.MINUTE);
        hour = getTimes(date, Calendar.HOUR) + minute / 60;
    }

    private int getTimes(Date date, int calendarField) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(calendarField);
    }

    public void startClock() {
        setTimes();
        invalidate();
    }

    public void stopClock() {
        handler.removeMessages(0);
    }

}
