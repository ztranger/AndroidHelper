package com.hpg.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.unity3d.player.UnityPlayer;

/**
 * Created by altunin on 30.08.2016.
 */
public class RateUsDialogExecutor implements Runnable
{
    private Activity _context;

    private String _title;
    private String _nowTitle;
    private String _neverTitle;
    private String _message;
    private String _laterTitle;

    RateUsDialogExecutor(Activity context, String title, String message, String nowTitle, String laterTitle, String neverTitle)
    {
        _context = context;
        _title = title;
        _message = message;
        _nowTitle = nowTitle;
        _neverTitle = neverTitle;
        _laterTitle = laterTitle;
    }

    public void run()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this._context);
        builder.setCancelable(false);
        builder.setTitle(_title);
        builder.setMessage(_message);
        builder.setPositiveButton(_nowTitle,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        UnityPlayer.UnitySendMessage(DialogManager.ReceiverName, "OnRateUsClicked", "0");
                        dialog.dismiss();
                    }
                });
        builder.setNeutralButton(_laterTitle,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        UnityPlayer.UnitySendMessage(DialogManager.ReceiverName, "OnRateUsClicked", "1");
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(_neverTitle,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        UnityPlayer.UnitySendMessage(DialogManager.ReceiverName, "OnRateUsClicked", "2");
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
