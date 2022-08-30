package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.JuTemp.Home.R;
import com.JuTemp.Home.util.FragmentLogicJuTemp;
import com.JuTemp.Home.util.HTTPJuTemp;
import com.JuTemp.Home.util.MessageJuTemp;
import com.JuTemp.Home.util.ModiJuTemp;

import java.util.Objects;

public class MusicJuTemp extends FragmentLogicJuTemp
{
	String textin="";
	String textout="";
	final int HANDLER_TOAST=520520;
	final int HANDLER_SETTEXT=520521;
	final int HANDLER_SETTITLE=520522;
	final int HANDLER_SETURL_SHORT=520523;
	final int HANDLER_SETURL_LONG=520524;

	@Override
	public void mainLogic(final Activity ThisActivity, final FragmentLogicJuTemp fragmentLogic, final View view)
	{
		super.mainLogic(ThisActivity,fragmentLogic,view);
		
		final Button buType= view.findViewById(R.id.buType);
		final EditText edin= view.findViewById(R.id.edin);
		final Button dwnload= view.findViewById(R.id.dwnload);
		final TextView tle= view.findViewById(R.id.tle);
		final TextView url_short= view.findViewById(R.id.url_short);
		final TextView url_long= view.findViewById(R.id.url_long);
		final TextView edout= view.findViewById(R.id.feedback);

		buType.setText(Re.getString(R.string.music_song));
		requestFocus(ThisActivity, view.findViewById(R.id.edin));
		
		buType.setOnClickListener(p1 -> {
			if (!buType.getText().toString().equals(Re.getString(R.string.music_song)))
			{
				buType.setText(Re.getString(R.string.music_song));
				tle.setText("");
			}
			else
			{
				buType.setText(Re.getString(R.string.music_playlist));
				tle.setText(R.string.music_desc_playlist);
			}
		});

		dwnload.setOnClickListener(p1 -> {
			textin = edin.getText().toString();
			textin = ModiJuTemp.trimUin(textin);
			switch (buType.getText().toString())
			{
				case "Song":
					sendRequestSong(textin, true);
					break;
				case "Playlist":
					sendRequestPlaylist(textin, true);
					break;
				default:break;
			}
		});
		dwnload.setOnLongClickListener(p1 -> {
			textin = edin.getText().toString();
			textin = ModiJuTemp.trimUin(textin);
			switch (buType.getText().toString())
			{
				case "Song":
					sendRequestSong(textin, false);
					break;
				case "Playlist":
					sendRequestPlaylist(textin, false);
					break;
				default:break;
			}
			return true;
		});
		url_short.setOnFocusChangeListener((v, hasFocus) -> {
			if (hasFocus) url_short.setBackgroundColor(0xFFC0C0C0);
				else url_short.setBackgroundColor(0xFFFAFAFA);
		});
		url_long.setOnFocusChangeListener((v, hasFocus) -> {
			if (hasFocus) url_long.setBackgroundColor(0xFFC0C0C0);
				else url_long.setBackgroundColor(0xFFFAFAFA);
		});
	}

	public Handler handler = new Handler(msg -> {
		String context = (String) msg.obj;
		int what = msg.what;
		switch (what) {
			case HANDLER_SETTITLE:
				((TextView) view.findViewById(R.id.tle)).setText(context);
				break;
			case HANDLER_SETURL_SHORT:
				((TextView) view.findViewById(R.id.url_short)).setText(context);
				break;
			case HANDLER_SETURL_LONG:
				((TextView) view.findViewById(R.id.url_long)).setText(context);
				break;
			case HANDLER_SETTEXT:
				((TextView) view.findViewById(R.id.feedback)).setText(context);
				break;
			case HANDLER_TOAST:
				Toast.makeText(ThisActivity, context, Toast.LENGTH_LONG).show();
				break;
			default:
				break;
		}
		return true;
	});

    public void sendRequestSong(final String textin, final boolean downloadOrNot)
	{
		new Thread(() -> {
			try
			{
				if (textin.isEmpty()) throw new Exception("Invalid textin.");
				String html=HTTPJuTemp.getHTML("https://y.music.163.com/m/song/" + textin + "/");
				String title=ModiJuTemp.modiTitleSong(html);
				handler.sendMessage(MessageJuTemp.String2Message(HANDLER_SETTITLE, title));

				String name=title.replace("/", "");
				String songUrl="http://music.163.com/song/media/outer/url?id=" + textin + ".mp3";
				if (HTTPJuTemp.confirmHTML302(songUrl, name))
				{
					if (downloadOrNot) downloadFileSong(songUrl, name + " - " + textin + ".mp3");
					handler.sendMessage(MessageJuTemp.String2Message(HANDLER_SETURL_SHORT, songUrl));
					handler.sendMessage(MessageJuTemp.String2Message(HANDLER_SETURL_LONG, HTTPJuTemp.getURL302(songUrl)));
				}
				handler.sendMessage(MessageJuTemp.String2Message(HANDLER_SETTEXT, downloadOrNot ?"DOWNLOAD WILL FINISH AFTER SECONDS.": "YOU CANCALED THE DOWNLOAD."));
			}
			catch (Exception e)
			{
				handler.sendMessage(MessageJuTemp.String2Message(HANDLER_TOAST, e.toString()));
			}
		}).start();
	}

	public void sendRequestPlaylist(final String textin, final boolean downloadOrNot)
	{
		new Thread(() -> {
			try
			{
				if (Objects.equals(textin, "-1")) throw new Exception("Invalid textin.");
				String html=HTTPJuTemp.getHTML("https://y.music.163.com/m/playlist/" + textin + "/");
				String title=ModiJuTemp.modiTitlePlaylist(html);
				handler.sendMessage(MessageJuTemp.String2Message(HANDLER_SETTITLE, title));

				String[] uinList =ModiJuTemp.trimUinPlaylist(html);
				String[] uinNameList =ModiJuTemp.trimUinNamePlaylist(html);
				for (int i=0;i < 9;i++)
				{
					try
					{
						if (Objects.equals(uinList[i], "")) break;
						String songUrl="http://music.163.com/song/media/outer/url?id=" + uinList[i] + ".mp3";
						if (HTTPJuTemp.confirmHTML302(songUrl, uinNameList[i]))
							if (downloadOrNot) downloadFilePlaylist(songUrl, title, uinNameList[i] + " - " + uinList[i] + ".mp3");
					}
					catch (Exception e)
					{
						handler.sendMessage(MessageJuTemp.String2Message(HANDLER_TOAST, e.toString()));
					}
				}
				handler.sendMessage(MessageJuTemp.String2Message(HANDLER_SETTEXT, downloadOrNot ?"DOWNLOADS WILL FINISH AFTER SECONDS.": "YOU CANCALED THE DOWNLOADS."));
			}
			catch (Exception e)
			{
				handler.sendMessage(MessageJuTemp.String2Message(HANDLER_TOAST, e.toString()));
			}
		}).start();
	}

	public void downloadFileSong(String downloadUrl, String fileName)
	{
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
		request.setDestinationInExternalPublicDir("/Download/MUSIC163JuTemp/", fileName);
		DownloadManager downloadManager= (DownloadManager)ThisActivity.getSystemService(Context.DOWNLOAD_SERVICE);
		downloadManager.enqueue(request);
	}
	public void downloadFilePlaylist(String downloadUrl, String path, String fileName)
	{
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
		request.setDestinationInExternalPublicDir("/Download/MUSIC163JuTemp/" + path + "/", fileName);
		DownloadManager downloadManager= (DownloadManager)ThisActivity.getSystemService(Context.DOWNLOAD_SERVICE);
		downloadManager.enqueue(request);
	}
}
