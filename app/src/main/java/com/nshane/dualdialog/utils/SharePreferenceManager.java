package com.nshane.dualdialog.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lzz on 2016/6/15.
 */
public class SharePreferenceManager {
    /**
     * 定义用于SETTING_DOMAIN的xml文件名称，以及xml文件的所有键名与对应的默认键值
     */
    public static class DomainXml {
        // xml文件名称
        public static final String XML_NAME = "SETTING_DOMAIN";


//        public static final KEY<String> KEY_SERVER_DOMAIN = new KEY<String>("server_domain", Constants.SERVER_HOST_DEFAULT);
        public static final KEY<String> KEY_SERVER_DOMAIN_REQUEST = new KEY<String>("server_site", "");

//        public static final KEY<String> KEY_SERVER_LOG = new KEY<String>("server_log_default", Constants.SERVER_LOG_DEFAULT);
        public static final KEY<String> KEY_SERVER_LOG_REQUEST = new KEY<String>("server_log", "");

    }

    public static class SettingXml{
        public static final String XML_SETTING = "SETTINGS_DOMAIN" ;
        public static final KEY<Long> KET_LAST_GET_SITE = new KEY<Long>("last_get_site", 0L);
        public static final KEY<Boolean> KET_FIRST_START = new KEY<Boolean>("first_start_3", true);
        public static final KEY<Boolean> KET_KODI_ZIP = new KEY<Boolean>("kodi", true);
        public static final KEY<Long> KET_KODI_VERSION = new KEY<Long>("kodi_version",0l);
    }

    public static class ThemeXml{
        public static final String XML_NAME = "SETTINGS_THEME" ;
        public static final KEY<Boolean> KET_SET_THEME_FLAG = new KEY<Boolean>("theme_change", false);
    }

    public static class WordsXml{
        // xml文件名称
        public static final String XML_NAME = "db_words";
        public static final KEY<Boolean> KET_WORD = new KEY<Boolean>("insert", false);
    }

    public static class EyeXml{
        // xml文件名称
        public static final String XML_NAME = "eye";
    }

    public static class MD5Xml{
        // xml文件名称
        public static final String XML_NAME = "cacheMD5";
    }



    //获取
    public static String getString(Context ctx, String xmlName, String key, String defaultValue) {
        SharedPreferences sharedPreferences =  ctx.getSharedPreferences(xmlName, Context.MODE_WORLD_READABLE);
        return sharedPreferences.getString(key, defaultValue);
    }
    //设置
    public static void setString(Context ctx, String xmlName, String key, String value) {
        SharedPreferences sharedPreferences =  ctx.getSharedPreferences(xmlName, Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key,value);
        edit.commit();
    }

    public static Long getLong(Context ctx, String xmlName, String key, Long defaultValue) {
        SharedPreferences sharedPreferences =  ctx.getSharedPreferences(xmlName, Context.MODE_WORLD_READABLE);
        return sharedPreferences.getLong(key, defaultValue);
    }
    //设置
    public static void setLong(Context ctx, String xmlName, String key, Long value) {
        SharedPreferences sharedPreferences =  ctx.getSharedPreferences(xmlName, Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong(key,value);
        edit.commit();
    }

    public static Boolean getBoolean(Context ctx, String xmlName, String key, Boolean defaultValue) {
        SharedPreferences sharedPreferences =  ctx.getSharedPreferences(xmlName, Context.MODE_WORLD_READABLE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }
    //设置
    public static void setBoolean(Context ctx, String xmlName, String key, Boolean value) {
        SharedPreferences sharedPreferences =  ctx.getSharedPreferences(xmlName, Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key,value);
        edit.commit();
    }



    /**
     * 用于设置key的名称和默认值,key值运用泛型
     */
    public static class KEY<T> {
        public final String key;// 键的名称
        public final T defaultValue;// 键的默认值

        public KEY(String key, T defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }
    }

}
