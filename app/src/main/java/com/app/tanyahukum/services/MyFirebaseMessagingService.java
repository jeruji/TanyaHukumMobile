package com.app.tanyahukum.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.tanyahukum.R;
import com.app.tanyahukum.util.Config;
import com.app.tanyahukum.util.ConvertStringToJson;
import com.app.tanyahukum.util.NotificationUtils;
import com.app.tanyahukum.view.AcceptQuestionsActivity;
import com.app.tanyahukum.view.DashboardActivity;
import com.app.tanyahukum.view.ListAppointmentActivity;
import com.app.tanyahukum.view.ListConsultationActivity;
import com.app.tanyahukum.view.MyAccountActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.toString());
        Log.d(TAG,"data : "+remoteMessage.getData().toString());
        Log.d(TAG," notification : "+remoteMessage.getNotification().getClickAction().toString());
        Log.d(TAG,"appBackground : "+String.valueOf(NotificationUtils.isAppIsInBackground(getApplicationContext())));
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data Payload: " + remoteMessage.getData());
            try {
                String click_action=remoteMessage.getNotification().getClickAction().toString();
                sendNotification(remoteMessage.getData().toString(),click_action);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void sendNotification(String data,String clickAction) throws JSONException{
        Log.e(TAG, "push json: " + data);
        String value= ConvertStringToJson.convertToJson(data);
        JSONObject json = new JSONObject(value);
        /*payload*/
        Log.d("json handle, ",json.toString());
        String usertype=json.getString("usertype");
        String title=json.getString("title");
        String questions=json.getString("questions");
        String message = json.getString("message");
        String consultationId=json.getString("consultationid");
        String fromId=json.getString("fromId");
        String toId=json.getString("toId");


         /*payload*/
        Intent intent = new Intent(clickAction);
        intent.putExtra("consultationId",consultationId);
        intent.putExtra("fromId",fromId);
        intent.putExtra("toId",toId);
        intent.putExtra("userType",usertype);
        intent.putExtra("questions",questions);
        intent.putExtra("title",title);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.tanya_hukum_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

}
