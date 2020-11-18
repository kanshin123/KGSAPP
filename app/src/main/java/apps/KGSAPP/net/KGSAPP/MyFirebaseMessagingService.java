package apps.KGSAPP.net.KGSAPP;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by yunseokchoi on 2017. 5. 2..
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //추가한것
        sendNotification(remoteMessage.getData().get("message"),
                remoteMessage.getData().get("title"),
                remoteMessage.getData().get("token"),
                remoteMessage.getData().get("imgUrl"),
                remoteMessage.getData().get("muid"),
                remoteMessage.getData().get("fuid"),
                remoteMessage.getData().get("uuid"),
                remoteMessage.getData().get("imageurl"),
                remoteMessage.getData().get("sexflag"),
                remoteMessage.getData().get("batgicount"),
                remoteMessage.getData().get("sex"));

    }

    private void sendNotification(String messageBody, String title, String token, String imgUrl, String muid, String fuid,
                                  String uuid, String imageurl, String sexflag, String batgicount, String sex) {
        //Intent intent = new Intent(this, MainActivity.class);
        //Intent intent = new Intent(this, ChatPage_Fragment.class);
        String imagepass = imgUrl;
        String Imgtopass = "http://kgs.yunstone.com/uploads/" + imageurl;
        Intent intent = new Intent(this, ChatPage_Fragment.class);  //messengetActiovity로 보내기위함
        intent.putExtra("MUID", muid);
        intent.putExtra("FUID", fuid);
        intent.putExtra("UUID", uuid);
        intent.putExtra("SEX", sex);
        intent.putExtra("SEXFLAG", sexflag);
        intent.putExtra("Imgtopass", Imgtopass);
        intent.putExtra("Imgpass", imagepass);
        //Log.d("mymuid",muid);
        //Log.d("myfuid",fuid);
        //Log.d("myuuid",uuid);
        //Log.d("myimageurl",imageurl);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        /*
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                //.setSubText(token)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        */
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText("プルダウンください▼")
                .setAutoCancel(true)
                .setSound(defaultSoundUri).setLights(000000255, 500, 2000)
                .setContentIntent(pendingIntent);

        if (imgUrl == "" || imgUrl.equals("")) {
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(notificationBuilder);
            style.bigText(title).setBigContentTitle(messageBody);
        } else {
            try {
                URL url = new URL(imgUrl);
                URLConnection conn = url.openConnection();
                conn.connect();

                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap img = BitmapFactory.decodeStream(bis);

                NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle(notificationBuilder);
                style.bigPicture(img).setBigContentTitle(messageBody);
            } catch (Exception e) {

            }
        }
        ////뱃지 작동이 안됨포기 만들어는 놨음..ㅎㅎㅎㅎ///////////////
        //setBadge(getApplicationContext(),0);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        /*
        int count= 2;//count 가 0 일 경우는 사라집니다.

        final Intent badintent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");

        badintent.putExtra("badge_count",count);

        // 뱃지에 나타날 아이콘의 패키지명  ex) day.ex

        badintent.putExtra("badge_count_package_name","apps.minatalk.net.minatalk");

        // 뱃지에 나타날 아이콘의 클래스명 ex) day.ex.mainActivity

        // 클래스는 아무거나 설정해도 눈에 나타나는 차이는 없습니다.

        badintent.putExtra("badge_count_class_name","day.ex.mainActivity");

        sendBroadcast(badintent);
        */
    }


}
