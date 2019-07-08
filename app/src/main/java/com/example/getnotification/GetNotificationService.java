package com.example.getnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class GetNotificationService extends Service {
    SharedPref sharedpref;
    // static korte hoy Uri but ame kore nai cz ae app ta background e use korbo na
    Uri alarmSound;

    private DatabaseReference notification;
    private DatabaseReference respons;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();

        notification = FirebaseDatabase.getInstance().getReference("notification");
        notification.keepSynced(true);

        respons = FirebaseDatabase.getInstance().getReference("Respons");
        respons.keepSynced(true);
        sharedpref = new SharedPref(this);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {


        /*super.onDestroy();*/
    }


    private void Notification() {
        notification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String s = Objects.requireNonNull(dataSnapshot.getValue()).toString();


                if (!"".equals(s) && s != null) {
                    respons.setValue(s);

                    if (!sharedpref.getMassagee().equals(s)) {


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel =
                                    new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);

                            NotificationManager manager = getSystemService(NotificationManager.class);
                            manager.createNotificationChannel(channel);

                        }
                        long[] patterns = {100, 300, 100, 300};
                        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        /*   NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);*/
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(GetNotificationService.this, "MyNotifications")
                                .setContentTitle("SIMEC Notification")
                                .setSmallIcon(R.drawable.logo)

                                .setTicker("New Massage Alert!")

                                .setSound(alarmSound)
                                .setVibrate(patterns);


                        sharedpref.setNotifyCount(sharedpref.getNotifycount() + 1);
                        int co=sharedpref.getNotifycount();
                        builder.setNumber(co);
                        if (co==1)
                        {
                           sharedpref.setUnMassagee(s);
                        }
                        else {
                            sharedpref.setUnMassagee(sharedpref.getUnMassagee()+"\n"+s);
                        }
                        /*sharedpref.setUnMassagee(sharedpref.getUnMassagee()+"  "+s);*/
                        builder.setContentText(sharedpref.getUnMassagee());
                        builder.setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(sharedpref.getUnMassagee())
                        );
                        Intent i = new Intent(GetNotificationService.this, MainActivity.class);
                        i.putExtra("notificationId", 111);
                        i.putExtra("massage",  sharedpref.getUnMassagee());

                        // Task Builder to maintain task for pending intent
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(GetNotificationService.this);
                        stackBuilder.addParentStack(MainActivity.class);

                        // Adds the Intent that starts the Activity to the top of the stack
                        stackBuilder.addNextIntent(i);

                        //pass request code and flag
                        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(pendingIntent);

                        // notificationID allows to update the notification tater on
                        NotificationManagerCompat manager = NotificationManagerCompat.from(GetNotificationService.this);
                        manager.notify(111, builder.build());

                        sharedpref.setMassagee(s);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
