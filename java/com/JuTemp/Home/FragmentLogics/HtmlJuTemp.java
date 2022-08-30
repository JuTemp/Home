package com.JuTemp.Home.FragmentLogics;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import androidx.annotation.NonNull;

import com.JuTemp.Home.*;
import com.JuTemp.Home.util.*;

public class HtmlJuTemp extends FragmentLogicJuTemp {
    String textin = null;
    final int HANDLER_TOAST = 520520;
    final int HANDLER_SETTEXT = 520521;
    String uaString = null;
    String ua_desc_Android = null, ua_desc_Desktop = null;

    @Override
    public void mainLogic(final Activity ThisActivity, final FragmentLogicJuTemp fragmentLogic, final View view) {
        super.mainLogic(ThisActivity, fragmentLogic, view);

        requestFocus(ThisActivity, view.findViewById(R.id.edin));

        ua_desc_Android = Re.getString(R.string.html_ua_desc_android);
        ua_desc_Desktop = Re.getString(R.string.html_ua_desc_desktop);

        final EditText edin = view.findViewById(R.id.edin);
        final Button uaButton = view.findViewById(R.id.ua);
        final Button cap = view.findViewById(R.id.cap);
        final Button dwnload = view.findViewById(R.id.dwn);
//		final TextView edout= view.findViewById(R.id.edout);

        edin.setText("https://");
        edin.requestFocus();
        uaButton.setText(ua_desc_Android);

        uaButton.setOnClickListener(p1 -> {
            if (uaButton.getText().toString().equals(ua_desc_Desktop)) {
                uaButton.setText(ua_desc_Android);
                uaString = ApplJuTemp.UA_ANDROID;
            } else {
                uaButton.setText(ua_desc_Desktop);
                uaString = ApplJuTemp.UA_DESKTOP;
            }
        });
        cap.setOnClickListener(p1 -> {
            textin = edin.getText().toString();
            sendRequestHTML(textin, uaString);
        });
        dwnload.setOnClickListener(p1 -> {
            textin = edin.getText().toString();
            sendRequestFile(textin);
        });
    }

    private final Handler handler = new Handler(message -> {
        String response = (String) message.obj;
        int what = message.what;
        switch (what) {
            case HANDLER_SETTEXT:
                ((TextView) view.findViewById(R.id.edout)).setText(response);
                break;
            case HANDLER_TOAST:
                Toast.makeText(ThisActivity, response, Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return true;
    });

    private void sendRequestHTML(final String textin, final String ua) {
        new Thread(() -> {
            try {
                String response = HTTPJuTemp.getHTML_UA(textin, ua);
                handler.sendMessage(MessageJuTemp.String2Message(HANDLER_SETTEXT, response.length() < 5000 ? response : response.substring(0, 5000)));

                WriteFileJuTemp.write(((TextView) view.findViewById(R.id.edinpath)).getText().toString(), response);
            } catch (Exception e) {
                handler.sendMessage(MessageJuTemp.String2Message(HANDLER_TOAST, e.toString()));
            }
        }).start();
    }

    public void sendRequestFile(final String downloadUrl) {
        new Thread(() -> {
            try {
                String fileName = ((TextView) view.findViewById(R.id.edinpath)).getText().toString();
                if (fileName.equals(""))
                    handler.sendMessage(MessageJuTemp.String2Message(HANDLER_SETTEXT, "FILL IN THE FILENAME BLANK."));
                else {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
                    request.setDestinationInExternalPublicDir("/!temp/temp/", fileName);
                    DownloadManager downloadManager = (DownloadManager) ThisActivity.getSystemService(Context.DOWNLOAD_SERVICE);
                    downloadManager.enqueue(request);

                    handler.sendMessage(MessageJuTemp.String2Message(HANDLER_SETTEXT, "DOWNLOAD WILL FINISH IN SECONDS."));
                }
            } catch (Exception e) {
                handler.sendMessage(MessageJuTemp.String2Message(HANDLER_TOAST, e.toString()));
            }
        }).start();
    }
}
