package gaddounamohamed.grp1.findmyfriend;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MysmsReceiver extends BroadcastReceiver {

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        String messageBody, phoneNumber;

        if (intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");

                if (pdus != null) {
                    final SmsMessage[] messages = new SmsMessage[pdus.length];

                    for (int i = 0; i < pdus.length; i++) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            String format = bundle.getString("format");
                            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                        } else {
                            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        }
                    }

                    if (messages.length > 0 && messages[0] != null) {
                        messageBody = messages[0].getMessageBody();
                        phoneNumber = messages[0].getDisplayOriginatingAddress();

                        Toast.makeText(context,
                                "Message : " + messageBody + " Reçu de la part de: " + phoneNumber,
                                Toast.LENGTH_LONG).show();

                        // Demande de position
                        if (messageBody.contains("findMYFriend envoyer moi votre position")) {
                            Intent i = new Intent(context, MyGeoService.class);
                            i.putExtra("sender", phoneNumber);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(i);
                            } else {
                                context.startService(i);
                            }
                        }

                        // Réception de position
                        if (messageBody.startsWith("findMYFriend my position est")) {
                            String[] t = messageBody.split("#");

                            if (t.length >= 3) {
                                String longitude = t[1];
                                String latitude = t[2];

                                // Créer le canal de notification
                                createNotificationChannel(context);

                                // Créer l'intent pour MapsActivity
                                Intent i = new Intent(context, MapsActivity.class);
                                i.putExtra("longitude", longitude);
                                i.putExtra("latitude", latitude);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                PendingIntent pi = PendingIntent.getActivity(context, 0, i,
                                        PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                                // Créer la notification
                                NotificationCompat.Builder mynotif = new NotificationCompat.Builder(context, "FindFriends_ChannelID");
                                mynotif.setContentTitle("Position Reçue");
                                mynotif.setContentText("Appuyez pour voir la position sur la carte");
                                mynotif.setSmallIcon(android.R.drawable.ic_dialog_map);
                                mynotif.setAutoCancel(true);
                                mynotif.setContentIntent(pi);
                                mynotif.setPriority(NotificationCompat.PRIORITY_HIGH);

                                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                                notificationManagerCompat.notify(0, mynotif.build());
                            }
                        }
                    }
                }
            }
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    "FindFriends_ChannelID",
                    "Canal pour Find Friends",
                    NotificationManager.IMPORTANCE_HIGH
            );
            canal.setDescription("Notifications de localisation");

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(canal);
            }
        }
    }
}