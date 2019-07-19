package neo.vn.imbeautiful.firebase_service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;
import neo.vn.imbeautiful.MainActivity;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.untils.SharedPrefs;


/**
 * Created by QQ on 8/29/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";
    private static int ONGOING_NOTIFICATION_ID = 76;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e(TAG, "onNewToken: " + s);
        if (s != null && s.length() > 0) {
            SharedPrefs.getInstance().put(Constants.KEY_TOKEN, s);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            hienThiThongBao(remoteMessage.getFrom(), remoteMessage.getMessageType());
        } else {
            Map<String, String> mMap = new ArrayMap<>();
            mMap = remoteMessage.getData();
            hienThiThongBao(mMap);
            String sUserMe = mMap.get("usermother");
            String sUserKid = mMap.get("userchild");
            String sUserName = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USERNAME, String.class);
            // String sUserCon = SharedPrefs.getInstance().get(Constants.KEY_USER_CON, String.class);
        /*    if (sUserMe != null && sUserKid != null && sUserName != null && sUserCon != null) {
                if (sUserMe.equals(sUserName) && sUserCon.equals(sUserKid)) {
                    displayCustomNotificationForOrders(mMap);
                }
            }*/
            displayCustomNotificationForOrders(mMap);
        }

    }

    private void hienThiThongBao(Map<String, String> mMap) {
        Intent intent = null;
        intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Home365Kid")
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(""))
                .setContentText("test")
                /* .setSound(Uri.parse("android.resource://"
                         + getApplicationContext().getPackageName() + "/"
                         + R.raw.sound_notification))*/
                .setContentIntent(pendingIntent);
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(100, builder.build());
    }

    private void hienThiThongBao(String title, String body) {
        int number_notifycation = 0;
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))


                /* .setSound(Uri.parse("android.resource://"
                         + getApplicationContext().getPackageName() + "/"
                         + R.raw.notifi))*/
                .setNumber(++number_notifycation)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        //    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(100, builder.build());
    }

    private NotificationChannel mChannel;
    private NotificationManager notifManager;

    private void displayCustomNotificationForOrders(Map<String, String> mMap) {
        String sUserMe = mMap.get("usermother");
        String sUserKid = mMap.get("userchild");
        String title = "Home365";
        String description = mMap.get("content");
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService
                    (Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder;
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel == null) {
                mChannel = new NotificationChannel
                        ("0", title, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, "0");
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher_round) // required
                    .setContentText(description)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    /*.setLargeIcon(BitmapFactory.decodeResource
                            (getResources(), R.mipmap.logo))
                    .setBadgeIconType(R.mipmap.logo)*/
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_NOTIFICATION));
            Notification notification = builder.build();
            notifManager.notify(0, notification);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = null;
            pendingIntent = PendingIntent.getActivity(this, 1251,
                    intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary))
                    .setSound(defaultSoundUri)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(description));

            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1251, notificationBuilder.build());
        }
    }
}
