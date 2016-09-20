package com.hpg.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.unity3d.player.UnityPlayer;

/**
 * Created by altunin on 30.08.2016.
 */
public class YesNoDialogExecutor implements Runnable
{
    private Activity _context;

    private String _title;
    private String _yesTitle;
    private String _noTitle;
    private String _message;

    YesNoDialogExecutor(Activity context, String title, String message, String yesTitle, String noTitle)
    {
        _context = context;
        _title = title;
        _message = message;
        _yesTitle = yesTitle;
        _noTitle = noTitle;
    }

    public void run()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this._context);
        builder.setCancelable(false);
        builder.setTitle(_title);
        builder.setMessage(_message);
        builder.setPositiveButton(_yesTitle,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        UnityPlayer.UnitySendMessage(DialogManager.ReceiverName, "OnYesNoClicked", "true");
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(_noTitle,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        UnityPlayer.UnitySendMessage(DialogManager.ReceiverName, "OnYesNoClicked", "false");
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
