package com.hpg.tools;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;

import com.unity3d.player.UnityPlayer;

import java.io.File;

/**
 * Created by altunin on 29.08.2016.
 */
public class MailSender {

    public void SendMail(String email, String title, String text, String filename)
    {
        Activity current = UnityPlayer.currentActivity;
        MailSendExecutor executor = new MailSendExecutor(current, email, title, text, filename, true);
        current.runOnUiThread(executor);
    }

    private class MailSendExecutor
            implements Runnable
    {
        private Activity _activity;
        private String _email;
        private String _title;
        private String _text;
        private String _filename;
        private Boolean _isHtml;

        MailSendExecutor(Activity context, String email, String title, String text, String filename, Boolean isHtml)
        {
            this._activity = context;
            this._email = email;
            this._title = title;
            this._text = text;
            this._filename = filename;
            this._isHtml = isHtml;
        }

        public void run()
        {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("message/rfc822");
            if (_filename != null && !_filename.isEmpty())
            {
                // adding an attachment
                Uri uri = Uri.fromFile(new File(_filename));
                emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
            }
            emailIntent.putExtra("android.intent.extra.EMAIL", new String[] { this._email });
            emailIntent.putExtra("android.intent.extra.SUBJECT", this._title);
            if (this._isHtml.booleanValue()) {
                emailIntent.putExtra("android.intent.extra.TEXT", Html.fromHtml(this._text));
            } else {
                emailIntent.putExtra("android.intent.extra.TEXT", this._text);
            }
            this._activity.startActivity(Intent.createChooser(emailIntent, "Select mail client"));
        }
    }
}
