/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.nshane.dualdialog.utils;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * A collection of utility methods, all static.
 */
public class Utils {

    /*
     * Making sure public utility methods remain static
     */


    private Utils() {
    }

    public static void startAPP(Context context, String appPackageName) {
        try {
            Intent
                    intent = context.getPackageManager().getLeanbackLaunchIntentForPackage(appPackageName);
            if (intent == null) {
                intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            LogUtil.d("Exception = " + e.toString());
        }
    }

    //TODO 日期国际化
    private static SimpleDateFormat sSdf = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat sSdfYear = new SimpleDateFormat("d MMM yyyy");
    private static SimpleDateFormat sSdfMonth = new SimpleDateFormat("EEE, MMM d, ''yy");
    private static SimpleDateFormat sSdfWeek = new SimpleDateFormat("EEE");

    /**
     * 获取当前日期包括星期几
     *
     * @return
     */
    public static String getStringData(int i) {

        Date date = new Date();
        if (i == 0) {
            return sSdfYear.format(date);
        }
        return sSdfYear.format(date);
    }

    public static String getStringWeek(int i) {

        Date date = new Date();
        if (i == 0) {
            return sSdfWeek.format(date);
        }
        return sSdfWeek.format(date);
    }

    public static String getStringTime(String type) {
        Date date = new Date();
        return sSdf.format(date);
    }

    public static boolean hasNetwork(Context context) {
        try {
            ConnectivityManager con = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo workinfo = con.getActiveNetworkInfo();
            if (workinfo == null || !workinfo.isAvailable()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean needGetSite(Context context) {
        long lastUpdateTime = SharePreferenceManager.getLong(context, SharePreferenceManager.SettingXml.XML_SETTING,
                SharePreferenceManager.SettingXml.KET_LAST_GET_SITE.key, SharePreferenceManager.SettingXml.KET_LAST_GET_SITE.defaultValue);
        if (0 != lastUpdateTime && System.currentTimeMillis() - lastUpdateTime > 1000 * 60 * 60 * 24) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the screen/display size
     */
    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }


    /**
     * Shows a (long) toast
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Shows a (long) toast.
     */
    public static void showToast(Context context, int resourceId) {
        Toast.makeText(context, context.getString(resourceId), Toast.LENGTH_LONG).show();
    }

    public static int convertDpToPixel(Context ctx, int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static String getFromAssets(Context context, String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            sb.append(new String(buffer));
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.d("lzz-app", "read top app = " + sb.toString());
        return sb.toString();
    }


    /**
     * Formats time in milliseconds to hh:mm:ss string format.
     */
    public static String formatMillis(int millis) {
        String result = "";
        int hr = millis / 3600000;
        millis %= 3600000;
        int min = millis / 60000;
        millis %= 60000;
        int sec = millis / 1000;
        if (hr > 0) {
            result += hr + ":";
        }
        if (min >= 0) {
            if (min > 9) {
                result += min + ":";
            } else {
                result += "0" + min + ":";
            }
        }
        if (sec > 9) {
            result += sec;
        } else {
            result += "0" + sec;
        }
        return result;
    }

    public static void unzipFiles(File file, String destDir)
            throws FileNotFoundException, IOException {
        //压缩文件
        File srcZipFile = file;
        //基本目录
        if (!destDir.endsWith("/")) {
            destDir += "/";
        }
        String prefixion = destDir;

        //压缩输入流
        ZipInputStream zipInput = new ZipInputStream(new FileInputStream(srcZipFile));
        //压缩文件入口
        ZipEntry currentZipEntry = null;
        //循环获取压缩文件及目录
        while ((currentZipEntry = zipInput.getNextEntry()) != null) {
            //获取文件名或文件夹名
            String fileName = currentZipEntry.getName();
            //Log.v("filename",fileName);
            //构成File对象
            File tempFile = new File(prefixion + fileName);
            //父目录是否存在
            if (!tempFile.getParentFile().exists()) {
                //不存在就建立此目录
                tempFile.getParentFile().mkdir();
            }
            //如果是目录，文件名的末尾应该有“/"
            if (currentZipEntry.isDirectory()) {
                //如果此目录不在，就建立目录。
                if (!tempFile.exists()) {
                    tempFile.mkdir();
                }
                //是目录，就不需要进行后续操作，返回到下一次循环即可。
                continue;
            }
            //如果是文件
            if (!tempFile.exists()) {
                //不存在就重新建立此文件。当文件不存在的时候，不建立文件就无法解压缩。
                tempFile.createNewFile();
            }
            //输出解压的文件
            FileOutputStream tempOutputStream = new FileOutputStream(tempFile);

            //获取压缩文件的数据
            byte[] buffer = new byte[1024];
            int hasRead = 0;
            //循环读取文件数据
            while ((hasRead = zipInput.read(buffer)) > 0) {
                tempOutputStream.write(buffer, 0, hasRead);
            }
            tempOutputStream.flush();
            tempOutputStream.close();
        }
        zipInput.close();
    }

    public static boolean canLaunch(Context context, String appPackageName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            if (intent == null) {
                intent = context.getPackageManager().getLeanbackLaunchIntentForPackage(appPackageName);
            }
            if (intent != null) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    private static List<String> sTopAppList = null;

    public static List<String> getTopApp(Context context) {
        if (sTopAppList != null) {
            return sTopAppList;
        }
        String topAppStr = Utils.getFromAssets(context, "top_app");
        String[] packageNameArray = topAppStr.split("\n");
        sTopAppList = Arrays.asList(packageNameArray);
        return sTopAppList;
    }

    private static List<String> sHidenAppList = null;

    public static List<String> getHidenApp(Context context) {
        if (sHidenAppList != null) {
            return sHidenAppList;
        }
        String topAppStr = Utils.getFromAssets(context, "hiden_app");
        String[] packageNameArray = topAppStr.split("\n");
        sHidenAppList = Arrays.asList(packageNameArray);
        return sHidenAppList;
    }

    public static String getSelfSignMd5(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            String signMd5 = md5(sign.toByteArray());
            return signMd5;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }
        return version;
    }


    public static boolean isTv(Context context) {
        UiModeManager m = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        if (m.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
            return true;
        }
        return false;
    }

    public static String md5(byte[] buffer) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(buffer);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 获取本地站点
     *
     * @param context
     * @return
     */
    public static String getServerSite(Context context) {
        if (null == context) {
            return Constants.SERVER_HOST_DEFAULT;
        }
        String host = SharePreferenceManager.getString(context, SharePreferenceManager.DomainXml.XML_NAME,
                SharePreferenceManager.DomainXml.KEY_SERVER_DOMAIN_REQUEST.key,
                SharePreferenceManager.DomainXml.KEY_SERVER_DOMAIN_REQUEST.defaultValue);
        if (TextUtils.isEmpty(host)) {
            host = SharePreferenceManager.getString(context, SharePreferenceManager.DomainXml.XML_NAME,
                    SharePreferenceManager.DomainXml.KEY_SERVER_DOMAIN.key,
                    SharePreferenceManager.DomainXml.KEY_SERVER_DOMAIN.defaultValue);
        }
        return host;
    }

    /**
     * 设置获取的站点sp
     *
     * @param context
     * @param url
     */
    public static void setServerSite(Context context, String url) {
        SharePreferenceManager.setString(context, SharePreferenceManager.DomainXml.XML_NAME,
                SharePreferenceManager.DomainXml.KEY_SERVER_DOMAIN_REQUEST.key,
                url);
    }

    /**
     * 获取日志站点
     *
     * @param context
     * @return
     */
    public static String getLogSite(Context context) {
        if (null == context) {
            return Constants.SERVER_HOST_DEFAULT;
        }
        String host = SharePreferenceManager.getString(context, SharePreferenceManager.DomainXml.XML_NAME,
                SharePreferenceManager.DomainXml.KEY_SERVER_LOG_REQUEST.key,
                SharePreferenceManager.DomainXml.KEY_SERVER_LOG_REQUEST.defaultValue);
        if (TextUtils.isEmpty(host)) {
            host = SharePreferenceManager.getString(context, SharePreferenceManager.DomainXml.XML_NAME,
                    SharePreferenceManager.DomainXml.KEY_SERVER_LOG.key,
                    SharePreferenceManager.DomainXml.KEY_SERVER_LOG.defaultValue);
        }
        return host;
    }

    /**
     * 设置日志的站点sp
     *
     * @param context
     * @param url
     */
    public static void setLogSite(Context context, String url) {
        SharePreferenceManager.setString(context, SharePreferenceManager.DomainXml.XML_NAME,
                SharePreferenceManager.DomainXml.KEY_SERVER_LOG_REQUEST.key,
                url);
    }


    public static String installSilently(String path) {
        LogUtil.d("lzz-ota", "ota install path = " + path);

        // 通过命令行来安装APK
        String[] args = {"pm", "install", "-r", path};
        String result = "";
        // 创建一个操作系统进程并执行命令行操作
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        LogUtil.d("lzz-ota", "ota install result = " + result);
        return result;
    }

    private static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }
}
