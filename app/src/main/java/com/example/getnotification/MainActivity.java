package com.example.getnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {
    SharedPref sharedpref;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text=(TextView)findViewById(R.id.text);
        sharedpref = new SharedPref(this);
        try {
            text.setText(reciveData());

        } catch (Exception e) {
        }

        notificationRun();


    }


    private void notificationRun() {
        if (!isMyServiceRunning(GetNotificationService.class))
            startService(new Intent(MainActivity.this, GetNotificationService.class));
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private String reciveData () {
        String massage = "";
        int id = 0;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("notificationId");
            massage = bundle.getString("massage");

            sharedpref.setNotifyCount(0);
            sharedpref.setUnMassagee("");


        }
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.cancel(id);
        return massage;
    }
}
