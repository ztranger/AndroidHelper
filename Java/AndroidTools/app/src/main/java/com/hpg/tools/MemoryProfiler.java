package com.hpg.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

import java.util.List;

/**
 * Created by altunin on 30.08.2016.
 */
public class MemoryProfiler {
    private static int _processId = -1;

    public float GetUsedMemory(Constant.MemoryMeasure measure)
    {
        try
        {
            return measure.ConvertFromKb((float)GetPssMemoryKb());
        }
        catch(Exception e)
        {
            return 0;
        }
    }

    public float GetAvailableMemory(Constant.MemoryMeasure measure)
    {
        return measure.ConvertFromBytes(GetMemoryInfo().availMem);
    }

    // total RAM on device
    public float GetTotalMemory(Constant.MemoryMeasure measure)
    {
        try
        {
            if (Build.VERSION.SDK_INT < 16) {
                Log.w(Constant.TAG,"android version 16 or more required. returned zero by default");
                return 0;
            }
            else {
                return measure.ConvertFromBytes(GetMemoryInfo().totalMem);
            }
        }
        catch(Exception e)
        {
            return 0;
        }
    }

    private ActivityManager.MemoryInfo GetMemoryInfo()
    {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)UnityPlayer.currentActivity.getSystemService(Activity.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi;
    }

    public float GetTotalSpace(Constant.MemoryMeasure measure)
    {
        if (Build.VERSION.SDK_INT < 18) {
            Log.w(Constant.TAG,"android version 18 or more required. returned zero by default");
            return 0;
        }
        else {
            StatFs fs = GetStatFs();
            long blocks = fs.getBlockCountLong();
            long blockSize = fs.getBlockSizeLong(); // in bytes
            return measure.ConvertFromBytes((float) blockSize * blocks);
        }
    }

    public float GetFreeSpaceMb(Constant.MemoryMeasure measure)
    {
        if (Build.VERSION.SDK_INT < 18) {
            Log.w(Constant.TAG,"android version 18 or more required. returned zero by default");
            return 0;
        }
        else {
            StatFs fs = GetStatFs();
            long blocks = fs.getAvailableBlocksLong();
            long blockSize = fs.getBlockSizeLong(); // in bytes
            return measure.ConvertFromBytes((float) blockSize * blocks);
        }
    }

    public boolean IsStorageAvailable()
    {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED;
    }

    private long GetPssMemoryKb()
    {
        ActivityManager activityManager = (ActivityManager)UnityPlayer.currentActivity.getSystemService(Activity.ACTIVITY_SERVICE);
        if (_processId < 0)
        {
            List<ActivityManager.RunningAppProcessInfo> pids = activityManager.getRunningAppProcesses();
            Log.i(Constant.TAG, "pkg name: " + UnityPlayer.currentActivity.getPackageName());
            for (int i = 0; i < pids.size(); i++)
            {
                Log.i(Constant.TAG, "pids.get(i).processName: " + ((ActivityManager.RunningAppProcessInfo)pids.get(i)).processName);
                if (((ActivityManager.RunningAppProcessInfo)pids.get(i)).processName.equalsIgnoreCase(UnityPlayer.currentActivity.getPackageName()))
                {
                    Log.i(Constant.TAG, "pid: " + ((ActivityManager.RunningAppProcessInfo)pids.get(i)).pid);
                    _processId = ((ActivityManager.RunningAppProcessInfo)pids.get(i)).pid;
                    break;
                }
                Log.i(Constant.TAG, "pid: " + ((ActivityManager.RunningAppProcessInfo)pids.get(i)).pid);
            }
        }
        if (_processId > 0)
        {
            int[] pids = new int[] { _processId };
            Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(pids);
            if (memoryInfoArray.length > 0)
            {
                long memory = memoryInfoArray[0].getTotalPss();
                Log.i(Constant.TAG, "our pid: " + _processId + " memory: " + memory);
                return memory;
            }
        }
        return 0L;
    }

    private StatFs GetStatFs()
    {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        return new StatFs(path);
    }
}
