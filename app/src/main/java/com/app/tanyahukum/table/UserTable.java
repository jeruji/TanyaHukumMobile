package com.app.tanyahukum.table;

import android.provider.BaseColumns;

/**
 * Created by echosimanjuntak on 12/2/16.
 */

public class UserTable implements BaseColumns {

    public static final String TABLE_NAME = "users";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "firstname";
    public static final String BORNDATE = "born";
    public static final String PROFILEPIC = "profilpic";
    public static final String PASSWORD = "password";
    public static final String PHONE = "phone";
    public static final String GENDER = "gender";
    public static final String EMAIL = "email";
    public static final String ID = "id";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String ADDRESS = "address";
    public static final String USERTYPE = "usertype";
    public static final String CREATE_TABLE_QUERY = "create table " + TABLE_NAME + " ("+
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            FIRSTNAME +" TEXT, " + LASTNAME +" TEXT, " + BORNDATE +" TEXT, " + PROFILEPIC +" TEXT, " + GENDER + " TEXT, " + EMAIL + " TEXT, " +
             PASSWORD +" TEXT, " + PHONE +" TEXT, " + PROVINCE +" TEXT, " + CITY +" TEXT, " + ADDRESS +" TEXT, " + USERTYPE +" TEXT, " +ID +" TEXT)";

    public static String selectUser(String id)
    {
        return "SELECT * FROM " + TABLE_NAME + " WHERE "+ ID +" ='"+id+"'";
    }

    public static String checkId(String id)
    {
        return "SELECT "+ ID + " FROM "+ TABLE_NAME + " WHERE "+ ID +" ='"+id+"'";
    }

}
