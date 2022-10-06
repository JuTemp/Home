package com.JuTemp.Home.FragmentLogics;

import android.app.*;
import android.view.*;
import android.view.inputmethod.*;
import android.webkit.*;
import android.widget.*;
import android.widget.SearchView.*;

import com.JuTemp.Home.*;
import com.JuTemp.Home.Activities.ApplJuTemp;
import com.JuTemp.Home.FragmentFramework.FragmentLogicJuTemp;
import com.JuTemp.Home.util.*;

import java.util.Objects;

public class WebViewJuTemp extends FragmentLogicJuTemp {
    WebView webView = null;
    WebSettings webSettings = null;

    @Override
    public void mainLogic(final Activity ThisActivity, final FragmentLogicJuTemp fragmentLogic, final View view) {
        super.mainLogic(ThisActivity, fragmentLogic, view);
    }

    private void refreshWebView(Activity ThisActivity, View view, String strurl, String ua) {
        webView = view.findViewById(R.id.webview);
        webView.requestFocus();
        webView.loadUrl(strurl);
        webSettings = webView.getSettings();
        if (ua != null) webSettings.setUserAgentString(ua);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCacheMaxSize(24 * 1024 * 1024);
        webSettings.setAppCachePath(ThisActivity.getCacheDir().toString());
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView v, String url) {
                v.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ApplJuTemp.viaShareString != null && !ApplJuTemp.viaShareString.isEmpty()) {
            if (ApplJuTemp.fragmentHaveChosen && ApplJuTemp.shareFlag) {
                ApplJuTemp.shareFlag = false;
                String viaShareString = ApplJuTemp.viaShareString.substring(ApplJuTemp.viaShareString.indexOf("http")).trim();
                ApplJuTemp.viaShareString = null;
                refreshWebView(ThisActivity, view, viaShareString, null);
                return;
            }
        }
        refreshWebView(ThisActivity, view, "https://cn.bing.com", null);
    }


    @Override
    public void actionbarMenuItemClickListener(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_copy:
                ClipJuTemp.setClipboard(ThisActivity, webView.getUrl());
                break;
            case R.id.menu_paste:
                String UrlInClipboard = ClipJuTemp.getClipboard(ThisActivity);
                if (Objects.requireNonNull(UrlInClipboard).startsWith("http"))
                    reloadUrl(UrlInClipboard);
                break;
            case R.id.menu_ua_wechat:
                refreshWebView(ThisActivity, view, webView.getUrl(), ApplJuTemp.UA_WECHAT);
                break;
            case R.id.menu_ua_qq:
                refreshWebView(ThisActivity, view, webView.getUrl(), ApplJuTemp.UA_QQ);
                break;
            default:
                break;
        }
    }

    public void setActionbarMenu(Menu menu) {
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_url).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setQueryHint("URL...");

        searchView.setOnSearchClickListener(v -> searchView.setQuery(webView.getUrl(), false));
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String str) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String str) {
                reloadUrl(str);
                return true;
            }
        });
    }

    private void reloadUrl(String str) {
        webView.loadUrl(str);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

}
