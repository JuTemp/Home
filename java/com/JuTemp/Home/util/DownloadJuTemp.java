package com.JuTemp.Home.util;

import android.app.*;
import android.net.*;
import android.content.*;
import android.widget.*;

public class DownloadJuTemp
{
	public static void download(final Context This, final String playAddrSrc,final String suff) throws Exception
	{
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(playAddrSrc));
		request.setVisibleInDownloadsUi(true);
		request.setDestinationInExternalPublicDir("/!ADM/Tiktok/", "TiktokMovie-"+suff+".TiktokMp4");
		DownloadManager downloadManager= (DownloadManager)This.getSystemService(This.DOWNLOAD_SERVICE);
		downloadManager.enqueue(request);
	}
}
