package com.example.myreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Context context;
    Button bDate, bTime, bAlarm;
    TextView tvInfo;
    Calendar calendar;
    int nowYear, nowMonth, nowDay, nowHour, nowMinute,
        notiYear, notiMonth, notiDay, notiHour,notiMinute;
    String stWhere="", channelID="", channelName="";
    NotificationChannel channel;
    NotificationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},1);

        initComponents();

        bDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                notiYear = year;
                                notiMonth = month;
                                notiDay = day;
                                stWhere = notiDay + "." + (notiMonth+1) + "." + notiYear;
                                tvInfo.setText(stWhere);
                            }
                        }, nowYear, nowMonth, nowDay);
                //datePickerDialog.getDatePicker().setEnabled(false); // enable date picker for all dates
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());//disable to select past dates
                datePickerDialog.show();
            }
        });

        bTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    TimePickerDialog tpd = new TimePickerDialog(context,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                    notiHour = i;
                                    notiMinute = i1;
                                    stWhere += " in "+notiHour + ":" + notiMinute;
                                    tvInfo.setText(stWhere);
                                }
                            }, nowHour, nowMinute, true);
                    tpd.show();
                }
        });

        bAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar=Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(notiYear,notiMonth,notiDay,notiHour,notiMinute,0);
                Intent intent=new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(context,1,intent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager=context.getSystemService(AlarmManager.class);
                long alarmTime=calendar.getTimeInMillis();
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,alarmTime,pendingIntent);
                Toast.makeText(context, "Alarm in Ok", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initComponents() {
        context=this;
        bDate= (Button) findViewById(R.id.bDate);
        bTime= (Button) findViewById(R.id.bTime);
        bAlarm= (Button) findViewById(R.id.bAlarm);
        tvInfo= (TextView) findViewById(R.id.tvInfo);
        calendar=Calendar.getInstance();
        nowYear=calendar.get(Calendar.YEAR);
        nowMonth=calendar.get(Calendar.MONTH);
        nowDay=calendar.get(Calendar.DAY_OF_MONTH);
        nowHour=calendar.get(Calendar.HOUR_OF_DAY);
        nowMinute=calendar.get(Calendar.MINUTE);
        notiYear=nowYear;
        notiMonth=nowMonth;
        notiDay=nowDay;
        notiHour=nowHour;
        nowMinute=nowMinute;
        channelID="channel_id";
        channelName="Alert_channel";
        channel=new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);
        manager=getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }
}