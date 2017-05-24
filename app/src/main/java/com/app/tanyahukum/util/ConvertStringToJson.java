package com.app.tanyahukum.util;

/**
 * Created by emerio on 4/17/17.
 */

public class ConvertStringToJson {
    public static String convertToJson(String data){
        String reg= data.replaceAll("[^\\{\\},]+", "\"$0\"");
        String value=reg.replace("\"[\"{", "[{").replace("=","\"=\"").replace("\" ","\"").replace("}\"]\"","}]").replace("\"true\"", "true").replace("\"false\"", "false");
        System.out.println("value :: "+value);
        return value;
    }
}
