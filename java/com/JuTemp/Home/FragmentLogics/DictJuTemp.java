package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.JuTemp.Home.Activities.ApplJuTemp;
import com.JuTemp.Home.R;
import com.JuTemp.Home.util.Base64JuTempUtil;
import com.JuTemp.Home.util.CurrentTimeJuTemp;
import com.JuTemp.Home.FragmentFramework.FragmentLogicJuTemp;
import com.JuTemp.Home.util.HTTPJuTemp;
import com.JuTemp.Home.util.IntentJuTempUtil;
import com.JuTemp.Home.util.MessageJuTemp;
import com.JuTemp.Home.util.ModiJuTemp;
import com.JuTemp.Home.util.SHA256JuTemp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class DictJuTemp extends FragmentLogicJuTemp {
    static String textin = "";
    static String textout = "";
    static final int HANDLER_TOAST = 520520;
    static final int HANDLER_SETTEXT = 520521;
    static final int TYPE_BrE = 1;
    static final int TYPE_NAmE = 2;
//    static final String APPID = "5acc96a879d92623";
//    static final String APPKEY = "zrt5RLBLAlMziCVhI1AHwOgUr7bexdlw";
    DictJuTemp This = null;
    Activity ThisActivity = null;
    View view = null;
    static boolean MODE = true;
    static int FONTSIZE = 6; //init

    @Override
    public void mainLogic(final Activity ThisActivity, final FragmentLogicJuTemp fragmentLogic, final View view) {
        this.This = (DictJuTemp) fragmentLogic;
        this.ThisActivity = ThisActivity;
        this.view = view;

        final EditText edinEditText = view.findViewById(R.id.edin_edittext);
        final TextView edinTextView = view.findViewById(R.id.edin_textview);
        final TextView edout = view.findViewById(R.id.edout);
        requestFocus(ThisActivity, edinEditText);

        SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(ThisActivity);
        ApplJuTemp.DICT_APPID = userInfo.getString(ApplJuTemp.DICT_APPID_SPF, null);
        if (ApplJuTemp.DICT_APPID != null)
            ApplJuTemp.DICT_APPID = Base64JuTempUtil.decode(ApplJuTemp.DICT_APPID.substring(7));
        ApplJuTemp.DICT_APPKEY = userInfo.getString(ApplJuTemp.DICT_APPKEY_SPF, null);
        if (ApplJuTemp.DICT_APPKEY != null)
            ApplJuTemp.DICT_APPKEY = Base64JuTempUtil.decode(ApplJuTemp.DICT_APPKEY.substring(7));
        MODE = userInfo.getBoolean(ApplJuTemp.DICT_MODE, true);
        FONTSIZE = userInfo.getInt(ApplJuTemp.DICT_FONTSIZE, 6);

        if (!MODE) view.findViewById(R.id.dict_voice).setVisibility(View.GONE);

        view.findViewById(R.id.trans).setOnClickListener(v -> {
            if (ApplJuTemp.DICT_APPID==null || ApplJuTemp.DICT_APPKEY==null) {
                Toast.makeText(ThisActivity, Re.getString(R.string.dict_no_appid_or_appkey), Toast.LENGTH_LONG).show();
                return;
            }
            textin = edinEditText.getText().toString().trim();
            if (textin.isEmpty()) return;
            edinTextView.setText(textin);
            edinEditText.setText("");
            edout.setText("");
            This.sendRequest(textin);
        });
        view.findViewById(R.id.trans).setOnLongClickListener(v -> {
            IntentJuTempUtil.intentUri(ThisActivity, "https://mobile.youdao.com/dict?le=eng&q=" + ModiJuTemp.modiTextInDict(textin));
            return true;
        });
        edinTextView.setTextSize(TypedValue.COMPLEX_UNIT_PT, FONTSIZE);
        edout.setTextSize(TypedValue.COMPLEX_UNIT_PT, FONTSIZE);
    }

    private final Handler handler = new Handler(new Handler.Callback() {
        String speechUrlBre = null;
        String speechUrlName = null;

        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.obj instanceof String) {
                Toast.makeText(ThisActivity, ((String) message.obj), Toast.LENGTH_LONG).show();
                return true;
            }
            final HashMap<String, String> response = (HashMap<String, String>) message.obj;
            ((TextView) view.findViewById(R.id.edin_textview)).setText(response.get("returnPhrase"));
            view.findViewById(R.id.edout).setBackgroundColor(0x80FFFFFF);
            ((TextView) view.findViewById(R.id.edout)).setText(response.get("explainsAdd") != null ? response.get("explains") + response.get("explainsAdd") : response.get("explains"));
            view.findViewById(R.id.trans).setOnLongClickListener(p1 -> {
                IntentJuTempUtil.intentUri(ThisActivity, response.get("url"));
                return true;
            });
            if (MODE) {
                speechUrlBre = response.get("uk-speech");
                speechUrlName = response.get("us-speech");
                view.findViewById(R.id.bre).setOnClickListener(ocl);
                view.findViewById(R.id.name).setOnClickListener(ocl);
            }
            return true;
        }

        private final OnClickListener ocl = v -> {
            try {
                switch (v.getId()) {
                    case R.id.bre:
                        HTTPJuTemp.MediaPlayerDictVoiceOnPath(speechUrlBre);
                        break;
                    case R.id.name:
                        HTTPJuTemp.MediaPlayerDictVoiceOnPath(speechUrlName);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                Toast.makeText(ThisActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
        };
    });

    private void sendRequest(final String textin) {
        new Thread(() -> {
            try {
                String response = null;
                if (textin.matches("[\u4E00-\u9FA5]+"))
                    response = HTTPJuTemp.getHTML(makeRequestedUrl(textin, false));
                else response = HTTPJuTemp.getHTML(makeRequestedUrl(textin, true));
                handler.sendMessage(MessageJuTemp.String2Message(modiFromYouDaoJson(response)));
            } catch (Exception e) {
                handler.sendMessage(MessageJuTemp.String2Message(e.toString()));
            }
        }).start();
    }

    private String makeRequestedUrl(String q, boolean fromTo) throws Exception // fromTo true en2zh false zh2en
    {
        String curtime = CurrentTimeJuTemp.getCurrentTimeSecond();
        String input = q.length() > 20 ? q.substring(0, 10) + q.length() + q.substring(q.length() - 10) : q;
        String salt = String.valueOf(System.currentTimeMillis());
        String signStr = ApplJuTemp.DICT_APPID + input + salt + curtime + ApplJuTemp.DICT_APPKEY;
        String sign = SHA256JuTemp.getDigestSHA256(signStr);
        return "https://openapi.youdao.com/api?"
                + "q=" + HTTPJuTemp.encode(q)
                + (fromTo ? "&from=en&to=zh-CHS" : "&from=zh-CHS&to=en")
                + "&appKey=" + ApplJuTemp.DICT_APPID
                + "&salt=" + salt
                + "&sign=" + sign
                + "&signType=v3"
                + "&curtime=" + curtime;
    }

    private HashMap<String, String> modiFromYouDaoJson(final String JsonFile) throws Exception {
        JSONObject jsonObject = new JSONObject(JsonFile);
        boolean fromTo = jsonObject.has("returnPhrase"); // fromTo true en2zh false zh2en
        JSONObject jsonObjectBasic = jsonObject.getJSONObject("basic");
        HashMap<String, String> result = new HashMap<>();

        result.put("returnPhrase", fromTo ? (String) jsonObject.getJSONArray("returnPhrase").get(0)
                + ((jsonObjectBasic.has("phonetic")) && (!(jsonObjectBasic.getString("phonetic").isEmpty())) ?
                " [" + jsonObjectBasic.getString("phonetic") + "]" : "") : textin);
        result.put("url", jsonObject.getJSONObject("webdict").getString("url"));
        result.put("explains", fromTo ? jsonObjectBasic.getJSONArray("explains").toString()
                .replace("[\"", "").replace("\"]", "").replace("\",\"", "\n") + "\n\n" : (String) jsonObject.getJSONArray("translation").get(0));
        if (jsonObjectBasic.has("uk-speech")) {
            result.put("uk-speech", jsonObjectBasic.getString("uk-speech"));
            result.put("us-speech", jsonObjectBasic.getString("us-speech"));
        }
        if (fromTo && jsonObjectBasic.has("wfs")) {
            JSONArray wfsJSONArray = jsonObjectBasic.getJSONArray("wfs");
            StringBuilder wfsSb = new StringBuilder();
            for (int index = 0; index < wfsJSONArray.length(); index++) {
                JSONObject wfJSONArray = ((JSONObject) wfsJSONArray.get(index)).getJSONObject("wf");
                wfsSb.append(wfJSONArray.getString("name")).append(" ").append(wfJSONArray.getString("value")).append("\n");
            }
            result.put("explainsAdd", wfsSb.toString());
        }
        return result;
    }
}
