package com.app.tanyahukum.fcm;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by emerio on 4/13/17.
 */

public class FCMHelper {
    private static FCMHelper instance = null;
    private static final String URL_SEND = "https://fcm.googleapis.com/fcm/send";
    public static final String TYPE_TO = "to";  // Use for single devices, device groups and topics
    public static final String TYPE_CONDITION = "condition"; // Use for Conditions
   private static final String FCM_SERVER_KEY ="AAAAsnaKXX0:APA91bGFiKMyri8Er28LuFaVqE90Dn7WGbEJjrvuWihCz8xrCCuqK6OdXS39skstfYOwOh024cq7sJYGdlqFaK9QQhNmNXwxzhmUO7HdC5aF7NMIBM3DsU8u7kFn4I3FDL2au9NhrpKM";
     public static FCMHelper getInstance() {
        if (instance == null) instance = new FCMHelper();
        return instance;
    }
    private FCMHelper() {}
    public String sendNotification(String type, String typeParameter, JsonObject notificationObject) throws IOException {
        return sendNotifictaionAndData(type, typeParameter, notificationObject, null);
    }
    public String sendData(String type, String typeParameter, JsonObject dataObject) throws IOException {
        return sendNotifictaionAndData(type, typeParameter, null, dataObject);
    }
    public String sendNotifictaionAndData(String type, String typeParameter, JsonObject notificationObject, JsonObject dataObject) throws IOException {
        String result = null;
        if (type.equals(TYPE_TO) || type.equals(TYPE_CONDITION)) {
            JsonObject sendObject = new JsonObject();
            sendObject.addProperty(type, typeParameter);
            result = sendFcmMessage(sendObject, notificationObject, dataObject);
        }
        return result;
    }
    public String sendTopicData(String topic, JsonObject dataObject) throws IOException{
        return sendData(TYPE_TO, "/topics/" + topic, dataObject);
    }
    public String sendTopicNotification(String topic, JsonObject notificationObject) throws IOException{
        return sendNotification(TYPE_TO, "/topics/" + topic, notificationObject);
    }
    public String sendTopicNotificationAndData(String topic, JsonObject notificationObject, JsonObject dataObject) throws IOException{
        return sendNotifictaionAndData(TYPE_TO, "/topics/" + topic, notificationObject, dataObject);
    }

    private String sendFcmMessage(JsonObject sendObject, JsonObject notificationObject, JsonObject dataObject) throws IOException {
       /* HttpPost httpPost = new HttpPost(URL_SEND);

        // Header setzen
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "key=" + FCM_SERVER_KEY);

        if (notificationObject != null) sendObject.add("notification", notificationObject);
        if (dataObject != null) sendObject.add("data", dataObject);

        String data = sendObject.toString();

        StringEntity entity = new StringEntity(data);

        // JSON-Object übergeben
        httpPost.setEntity(entity);

        HttpClient httpClient = HttpClientBuilder.create().build();

        BasicResponseHandler responseHandler = new BasicResponseHandler();
        String response = (String) httpClient.execute(httpPost, responseHandler);

        */

        return null;
    }

    public void sendNotificationDeviceToDevice(String deviceToken,String message,String title,String usertype, String questionsId,String clickAction,String fromId,String toId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject obj = new JSONObject();
        try {
            JSONObject msgObject = new JSONObject();
            JSONObject dataObject=new JSONObject();
            msgObject.put("body", message);
            msgObject.put("title", title);
            msgObject.put("click_action",clickAction);
            dataObject.put("usertype",usertype);
            dataObject.put("questionsid",questionsId);
            dataObject.put("fromId",fromId);
            dataObject.put("toId",toId);
            obj.put("to", deviceToken);
            obj.put("data",dataObject);
            obj.put("notification",msgObject);
        }catch (JSONException i){
            Log.d("exception : ",i.toString());
        }
        RequestBody body = RequestBody.create(mediaType, obj.toString());
        Request request = new Request.Builder().url(URL_SEND).post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "key="+FCM_SERVER_KEY).build();
        Response response = client.newCall(request).execute();
        Log.d("response","Notification response >>>" +response.body().string());
    }
    public void sendNotificationMultipleDevice(String statusQuestions,List deviceToken,String consultationId, String message, String title,String questions,String usertype,String clickAction,String fromId, String toId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject obj = new JSONObject();
        try {
            JSONObject msgObject = new JSONObject();
            JSONObject dataObject=new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i=0; i < deviceToken.size(); i++) {
                jsonArray.put(deviceToken.get(i));
            }
            Log.d("device array : ",jsonArray.toString());
            msgObject.put("body",message);
            msgObject.put("title",questions);
            msgObject.put("click_action",clickAction);
            msgObject.put("sound","default");
            dataObject.put("title",title);
            dataObject.put("message",message);
            dataObject.put("statusQuestions",statusQuestions);
            dataObject.put("timestamp","");
            dataObject.put("questions",questions);
            dataObject.put("usertype",usertype);
            dataObject.put("consultationId",consultationId);
            dataObject.put("fromId",fromId);
            dataObject.put("toId",toId);
            obj.put("registration_ids",jsonArray);
            obj.put("notification",msgObject);
            obj.put("data",dataObject);
            Log.d("param :",obj.toString());
        }catch (JSONException i){
            Log.d("exception : ",i.toString());
        }
        RequestBody body = RequestBody.create(mediaType, obj.toString());
        Request request = new Request.Builder().url(URL_SEND).post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "key="+FCM_SERVER_KEY).build();
        Response response = client.newCall(request).execute();
        Log.d("response","Notification response >>>" +response.body().string());

        if(response.body().string().contains("error")){
            sendNotificationMultipleDevice(statusQuestions, deviceToken, consultationId, message,  title, questions, usertype, clickAction, fromId, toId);
        }
    }

}
