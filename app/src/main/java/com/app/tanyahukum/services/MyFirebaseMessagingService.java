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
        Log.d(TAG,"appBackground : "+String.valueOf(NotificationUtils.isAppIsInBackground(getApplicationContext())));
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data Payload: " + remoteMessage.getData());
            try {
                handleDataMessage(remoteMessage.getData().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        
       // String message = remoteMessage.getData().get("message");
        //String TrueOrFlase = "True";
        //sendNotification(message, TrueOrFlase);
    }
    private void handleNotification(String data) throws JSONException {
        String value= ConvertStringToJson.convertToJson(data);
        JSONObject json = new JSONObject(value);
        Log.d("json handle, ",json.toString());
        String usertype=json.getString("usertype");
        String title=json.getString("title");
        String questions=json.getString("questions");
        String message = json.getString("message");
        String questionsId=json.getString("questionsid");
        String statusQuestions=json.getString("statusQuestions");
        Log.d("json questions, ",questions);
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.d("background","lain");
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        }else{
            Log.d("background","enya");
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            pushNotification.putExtra("statusQuestions",statusQuestions);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        }
    }
    private void handleDataMessage(String data) throws JSONException {
        Log.e(TAG, "push json: " + data);
            String value= ConvertStringToJson.convertToJson(data);
            JSONObject json = new JSONObject(value);
            Log.d("json handle, ",json.toString());
            String usertype=json.getString("usertype");
            String title=json.getString("title");
            String questions=json.getString("questions");
            String message = json.getString("message");
            String questionsId=json.getString("questionsid");
            String statusQuestions=json.getString("statusQuestions");
            Log.d("json questions, ",questions);
            if (usertype.equals("CLIENT")){
                if (statusQuestions.equals("NEW")) {
                    Intent resultIntent = new Intent(getApplicationContext(), AcceptQuestionsActivity.class);
                    resultIntent.putExtra("message", title);
                    resultIntent.putExtra("questionsid", questionsId);
                    resultIntent.putExtra("title", title);
                    resultIntent.putExtra("questions", questions);
                    showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                }
                else if (statusQuestions.equals("RESEND")) {
                    Intent resultIntent = new Intent(getApplicationContext(), AcceptQuestionsActivity.class);
                    resultIntent.putExtra("message", title);
                    resultIntent.putExtra("questionsid", questionsId);
                    resultIntent.putExtra("title", title);
                    resultIntent.putExtra("questions", questions);
                    showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                }else if (statusQuestions.equals("APPOINTMENT")){
                    Intent resultIntent = new Intent(getApplicationContext(), ListAppointmentActivity.class);
                    resultIntent.putExtra("type","APPOINTMENT");
                    showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                }
                else {
                    Intent resultIntent = new Intent(getApplicationContext(), ListConsultationActivity.class);
                    showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                }
            }else{
                if (statusQuestions.equals("APPOINTMENT")){
                    Intent resultIntent = new Intent(getApplicationContext(), ListAppointmentActivity.class);
                    resultIntent.putExtra("type","APPOINTMENT");
                    showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                }else {
                    Intent resultIntent = new Intent(getApplicationContext(), ListConsultationActivity.class);
                    resultIntent.putExtra("message", title);
                    resultIntent.putExtra("questionsid", questionsId);
                    showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                }
            }
    }
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent, imageUrl);
    }
    private void sendNotification(String messageBody, String TrueOrFalse) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("AnotherActivity", TrueOrFalse);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.tanya_hukum_logo)
                .setContentTitle(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

}
