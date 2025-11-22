package gaddounamohamed.grp1.findmyfriend;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.telephony.SmsManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class MyGeoService extends Service {

    private static final String CHANNEL_ID = "GeoServiceChannel";

    public MyGeoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Start as foreground service for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("FindMyFriend")
                    .setContentText("Récupération de la position...")
                    .setSmallIcon(android.R.drawable.ic_dialog_map)
                    .build();
            startForeground(1, notification);
        }

        String sender = intent.getStringExtra("sender");

        // Récupération position GPS
        FusedLocationProviderClient mClient = LocationServices.getFusedLocationProviderClient(this);
        mClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();

                    try {
                        SmsManager manager = SmsManager.getDefault();
                        manager.sendTextMessage(sender, null,
                                "findMYFriend my position est #" + longitude + "#" + latitude,
                                null, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                stopSelf();
            }
        });

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Geo Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}