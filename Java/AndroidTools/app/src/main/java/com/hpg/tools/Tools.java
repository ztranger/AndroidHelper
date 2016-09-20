package com.hpg.tools;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;

import com.hpg.tools.Constant;
import com.unity3d.player.UnityPlayer;

/**
 * Created by altunin on 26.08.2016.
 */
public class Tools {

    public static void ShareIt(String title, String message, String imgSrc, String shareCaptureText) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("application/image");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        Log.i(Constant.TAG,"imgsrc: "+ imgSrc);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imgSrc));
        Activity current = UnityPlayer.currentActivity;
        current.startActivity(Intent.createChooser(sharingIntent, shareCaptureText));
    }

    public static void Copy(final String str)
    {
        Runnable r = new Runnable()
        {
            public void run()
            {
                ClipboardManager cb = (ClipboardManager)UnityPlayer.currentActivity
                        .getSystemService(Activity.CLIPBOARD_SERVICE);
                ClipData currentClip = ClipData.newPlainText("Copy", str);
                if (currentClip != null)
                    cb.setPrimaryClip(currentClip);
            }
        };
        UnityPlayer.currentActivity.runOnUiThread(r);
    }

    public static String GetDeviceId()
    {
        try
        {
            return Settings.Secure.getString(
                    UnityPlayer.currentActivity.getContentResolver(),
                    "android_id");
        }
        catch (Exception e)
        {
            Log.e(Constant.TAG, "Error in GetDeviceId: " + e.getMessage());
        }
        return "";
    }

    public static String GetMacAddress()
    {
        String macAddress = "";
        try
        {
            WifiManager wifi = (WifiManager)UnityPlayer.currentActivity.getSystemService(Activity.WIFI_SERVICE);
            macAddress = wifi.getConnectionInfo().getMacAddress();
        }
        catch (Exception e)
        {
            Log.e(Constant.TAG, "Error in GetMacAddress: " + e.getMessage());
        }
        return macAddress;
    }

    public static String GetProxy()
    {
        String host = System.getProperty("http.proxyHost");
        String port = System.getProperty("http.proxyPort");
        Log.i(Constant.TAG, "Proxy = " + host + ":" + port);
        if ((host == null) || (host == ""))
            return "";
        return host + ":" + port;
    }

    public static boolean IsNetworkConnected()
    {
        NetworkInfo info = GetNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static String GetNetworkType()
    {
        NetworkInfo info = GetNetworkInfo();
        if (info != null && info.isConnected())
        {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE)
                return "Mobile";
            if (info.getType() == ConnectivityManager.TYPE_WIFI)
                return "Wifi";
            return "Unknown";
        }
        return "None";
    }

    public static String GetCountryCode()
    {
        String countryCode = null;
        countryCode = UnityPlayer.currentActivity.getResources().getConfiguration().locale.getCountry();
        Log.i(Constant.TAG, "country code = " + countryCode);
        return countryCode;
    }

    private static NetworkInfo GetNetworkInfo()
    {
        ConnectivityManager cm = (ConnectivityManager) UnityPlayer.currentActivity.getSystemService(Activity.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
}
