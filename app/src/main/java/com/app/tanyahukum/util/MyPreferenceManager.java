package com.app.tanyahukum.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.tanyahukum.model.User;


public class MyPreferenceManager {
    private String TAG = MyPreferenceManager.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "tanya_hukum";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PROVINCE= "province";
    private static final String KEY_CITY= "city";
    private static final String KEY_ADDRESS= "address";
    private static final String KEY_GENDER= "gender";
    private static final String KEY_USERTYPE= "usertype";
    private static final String KEY_BORNDATE= "borndate";
    private static final String KEY_LAWFIRM= "lawfirm";
    private static final String KEY_SPECIALIZATION= "specialization";
    private static final String KEY_LAWFIRMCITY= "lawfirmcity";
    private static final String KEY_LAWFIRMADDRESS= "lawfirmaddress";
    private static final String KEY_LAWFIRMPHONE= "lawfirmphone";
    private static final String KEY_USER_FIREBASE_TOKEN = "firebase_token";
    private static final String KEY_NOTIFICATIONS = "notifications";

    private static final String KEY_PHOTO = "photo";

    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_BORNDATE, user.getBornDate());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_GENDER, user.getGender());
        editor.putString(KEY_PROVINCE, user.getProvince());
        editor.putString(KEY_CITY, user.getCity());
        editor.putString(KEY_ADDRESS, user.getAddress());
        editor.putString(KEY_USERTYPE, user.getUsertype());
        editor.putString(KEY_LAWFIRM,user.getLawFirm());
        editor.putString(KEY_LAWFIRMCITY,user.getLawFirmCity());
        editor.putString(KEY_LAWFIRMADDRESS,user.getLawFirmAddress());
        editor.putString(KEY_LAWFIRMPHONE,user.getLawFirmPhone());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. user id : "+user.getId()+" , " + user.getName() + ", " + user.getEmail()+" , "+getFirebaseToken());
    }


    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id,firstname,gender, name, email,password,borndate,phone,province,city,address,usertype,firebaseToken,lawfirm,lawfirmcity,lawfirmaddress,lawfirmphone,specialization;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_NAME, null);
            email = pref.getString(KEY_EMAIL, null);
            password = pref.getString(KEY_PASSWORD, null);
            borndate=pref.getString(KEY_BORNDATE,null);
            phone=pref.getString(KEY_PHONE,null);
            province=pref.getString(KEY_PROVINCE,null);
            city=pref.getString(KEY_CITY,null);
            address=pref.getString(KEY_ADDRESS,null);
            usertype=pref.getString(KEY_USERTYPE,null);
            gender=pref.getString(KEY_GENDER,null);
            lawfirm=pref.getString(KEY_LAWFIRM,null);
            lawfirmcity=pref.getString(KEY_LAWFIRMCITY,null);
            lawfirmaddress=pref.getString(KEY_LAWFIRMADDRESS,null);
            lawfirmphone=pref.getString(KEY_LAWFIRMPHONE,null);
           // specialization=pref.getString(KEY_SPECIALIZATION,null);
            firebaseToken = pref.getString(KEY_USER_FIREBASE_TOKEN, null);
            User user=new User();
            user.setId(id);
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setBornDate(borndate);
            user.setProvince(province);
            user.setCity(city);
            user.setPhone(phone);
            user.setAddress(address);
            user.setUsertype(usertype);
            user.setFirebaseToken(firebaseToken);
            user.setGender(gender);
            user.setLawFirm(lawfirm);
            user.setLawFirmCity(lawfirmcity);
            user.setLawFirmAddress(lawfirmaddress);
            user.setLawFirmPhone(lawfirmphone);
            return user;
        }
        return null;
    }

    public String getUserId(){
        String userId=pref.getString(KEY_USER_ID, null);
        Log.d("User Id" ,userId);
        return userId;
    }
    public String getUserType(){
        String userType=pref.getString(KEY_USERTYPE, null);
        Log.d("User type" ,userType);
        return userType;
    }
    public String getUserName(){
        String name=pref.getString(KEY_NAME, null);
        return name;
    }

    public void addNotification(String notification) {
    String oldNotifications = getNotifications();
        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }
        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void addTokenFirebase(String token){
        editor.putString(KEY_USER_FIREBASE_TOKEN,token);
        editor.commit();
    }
    public String getFirebaseToken(){
        return pref.getString(KEY_USER_FIREBASE_TOKEN,null);
    }
    public void deleteToken(){
        pref.edit().remove(KEY_USER_FIREBASE_TOKEN).commit();
    }
    public void clear() {
        editor.clear();
        editor.commit();
    }
}
