package com.JuTemp.Home.util;

import android.media.MediaPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Objects;

public final class HTTPJuTemp {

    public static final String ENC_UTF8="UTF-8";

    public static String encode(String str) throws Exception {
        return HTTPJuTemp.encode(str,ENC_UTF8);
    }

    public static String decode(String str) throws Exception
    {
        return HTTPJuTemp.decode(str,ENC_UTF8);
    }

    public static String encode(String str,String enc) throws Exception {
        return URLEncoder.encode(str,enc);
    }

    public static String decode(String str,String enc) throws Exception
    {
        return URLDecoder.decode(str,enc);
    }

    public static String Post(String hostPath, Map<String, String> postContentList) throws IOException {
        URL url = new URL(hostPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        OutputStream out = conn.getOutputStream();
        StringBuilder postContentString = new StringBuilder();
        for (Map.Entry<String, String> postContentListPart : postContentList.entrySet()) {
            if (Objects.equals(postContentListPart.getKey(), "")) continue;
            postContentString.append(postContentListPart.getKey()).append("=").append(postContentListPart.getValue()).append("&");
        }
        out.write(postContentString.substring(0, (postContentString.length() == 0) ? postContentString.length() : postContentString.length() - 1).getBytes());
        conn.connect();

        InputStream inputStream = conn.getInputStream();
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = br.readLine()) != null) sb.append(line).append("\n");
        String rt = sb.toString();

        conn.disconnect();

        return rt;
    }

    public static String getHTML(String urlstr) throws Exception {
        return getHTML_UA(urlstr, null);
    }

    public static String getHTML_UA(String urlstr, String ua) throws Exception {
        HttpURLConnection conn = null;
        URL url = new URL(urlstr);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        if (ua != null) conn.setRequestProperty("User-agent", ua);
        conn.setFollowRedirects(true);
        conn.setInstanceFollowRedirects(true);
        conn.connect();

        InputStream inputStream = conn.getInputStream();
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = br.readLine()) != null) sb.append(line).append("\n");
        String rt = sb.toString();

        conn.disconnect();

        return rt;
    }

    public static boolean confirmHTML302(String urlstr, String title) throws Exception {
        HttpURLConnection conn = null;
        URL url = new URL(urlstr);
        conn = (HttpURLConnection) url.openConnection();
        conn.setFollowRedirects(true);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestMethod("GET");
        conn.connect();
        conn.getInputStream();
        if (conn.getURL().toString().indexOf("404") > 0)
            throw new Exception("此歌曲 " + title + " 无法下载，因为缺少版权");

        return true;
    }

    public static String getURL302(String urlstr) throws Exception {
        HttpURLConnection conn = null;
        //HttpURLConnection conn2=null;
        URL url = new URL(urlstr);
        conn = (HttpURLConnection) url.openConnection();
        conn.setFollowRedirects(true);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestMethod("GET");
        conn.connect();
        conn.getInputStream();
        return conn.getURL().toString();
    }

    public static void MediaPlayerDictVoiceOnPath(String path) throws Exception {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(path);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(MediaPlayer::start);
    }

}
