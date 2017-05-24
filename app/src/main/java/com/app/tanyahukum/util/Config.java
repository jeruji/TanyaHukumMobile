package com.app.tanyahukum.util;

import com.app.tanyahukum.App;

/**
 * Created by emerio on 4/6/17.
 */

public class Config {
    public static final String FIREBASE_URL = "https://tanyahukum-9d16f.firebaseio.com/";
    public static final String TOPIC_GLOBAL = "global";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String SHARED_PREF = "tanya_hukum";
    public static String USER_TYPE= App.getInstance().getPrefManager().getUser().getUsertype();
    public static String USER_NAME= App.getInstance().getPrefManager().getUser().getName();
    public static String USER_ID= App.getInstance().getPrefManager().getUser().getId();

}
