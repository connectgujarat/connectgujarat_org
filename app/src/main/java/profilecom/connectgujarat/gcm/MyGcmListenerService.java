package profilecom.connectgujarat.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;
import java.util.Random;
import profilecom.connectgujarat.NewsDetailsActivity;
import profilecom.connectgujarat.R;


public class MyGcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @par from SenderID of the sender.
     * @para data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
   /*  @Override
     public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        sendNotification(message);
        // [END_EXCLUDE]
    }*/
    // [END receive_message]


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        String message = remoteMessage.getFrom();
        Map<String, String> data = remoteMessage.getData();
        final Intent intent = new Intent(this, NewsDetailsActivity.class);
        intent.putExtra("title", data.get("title"));
        intent.putExtra("author", data.get("post_author"));
        intent.putExtra("date", data.get("post_date"));
        intent.putExtra("url", data.get("post_image"));
        intent.putExtra("content", data.get("post_content"));
        //intent.putExtra("cat", data.get("post_category"));
        intent.putExtra("cat", -9999);
        intent.putExtra("posturl", data.get("post_URL"));
        //intent.putExtra("posturl", "https://connectgujarat.com/2017/07/07/shahrukh-anushka-pose-with-new-poster-of-jab-harry-met-sejal/");
        intent.putExtra("postId", data.get("id"));

        sendNotification(data.get("title"), intent);

        //final String title = (String) data.get("title");
        //final Object url = data.get("post_URL");
        //if (!TextUtils.isEmpty(title)) {
        //    sendNotification(title, url);
        //}
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(final String message, final Intent intent) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_connect_gujarat_white)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }
}
