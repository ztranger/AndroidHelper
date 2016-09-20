package com.hpg.dialogs;

import android.view.Gravity;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;

/**
 * Created by altunin on 30.08.2016.
 */
public class DialogManager{
    public static String ReceiverName;

    private OkDialogExecutor okDialog;
    private YesNoDialogExecutor yesNoDialog;
    private RateUsDialogExecutor rateDialog;

    public DialogManager(String receiverName)
    {
        ReceiverName = receiverName;
    }

    public void ShowOk(String _title, String _message, String _buttonName)
    {
        okDialog = new OkDialogExecutor(UnityPlayer.currentActivity, _title, _message, _buttonName);
        UnityPlayer.currentActivity.runOnUiThread(okDialog);
    }

    public void ShowYesNo(String _title, String _message, String _yesTitle, String _noTitle)
    {
        yesNoDialog = new YesNoDialogExecutor(UnityPlayer.currentActivity, _title, _message, _yesTitle, _noTitle);
        UnityPlayer.currentActivity.runOnUiThread(yesNoDialog);
    }

    public void ShowRateUs(String _title, String _message, String _yesTitle, String _noTitle, String _laterButton)
    {
        rateDialog = new RateUsDialogExecutor(UnityPlayer.currentActivity, _title, _message, _yesTitle, _noTitle, _laterButton);
        UnityPlayer.currentActivity.runOnUiThread(rateDialog);
    }

    public void ShowToast(String text)
    {
        ShowToast(text, Gravity.CENTER, Toast.LENGTH_SHORT, 0,0);
    }

    public void ShowToast(String text,int gravity,int duration, int xOffset, int yOffset)
    {
        Toast toast = Toast.makeText(UnityPlayer.currentActivity, text, duration);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.show();
    }

}
