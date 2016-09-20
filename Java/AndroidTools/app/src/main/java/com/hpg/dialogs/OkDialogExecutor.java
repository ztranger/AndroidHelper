package com.hpg.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.unity3d.player.UnityPlayer;

/**
 * Created by altunin on 30.08.2016.
 */
public class OkDialogExecutor  implements Runnable
{
    private Activity _context;

    private String _title;
    private String _message;
    private String _buttonTitle;

    OkDialogExecutor(Activity context, String title, String message, String buttonTitle)
    {
        _context = context;
        _title = title;
        _message = message;
        _buttonTitle = buttonTitle;
    }

    public void run()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setCancelable(false);
        builder.setTitle(_title);
        builder.setMessage(_message);
        builder.setPositiveButton(_buttonTitle,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        UnityPlayer.UnitySendMessage(DialogManager.ReceiverName, "OnOkClicked", "");
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
