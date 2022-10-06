package com.JuTemp.Home.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

import com.JuTemp.Home.R;
import com.JuTemp.Home.util.Base64JuTempUtil;
import com.JuTemp.Home.util.ClipJuTemp;
import com.JuTemp.Home.util.UUIDGenerateJuTemp;
import com.alibaba.fastjson2.JSON;

import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

public class PreferenceJuTemp extends PreferenceActivity {
    PreferenceJuTemp This = this;
    SharedPreferences sp = null;
    SharedPreferences.Editor spe = null;
    Resources Re = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        This.addPreferencesFromResource(R.xml.preference);
        sp = PreferenceManager.getDefaultSharedPreferences(This);
        spe = sp.edit();
        Re = This.getResources();

        This.findPreference(Re.getString(R.string.preference_id_startactivity)).setOnPreferenceClickListener(preference -> {
            AppCompatEditText index_edittext = new AppCompatEditText(This);
            index_edittext.setHint(R.string.preference_hint_startactivity);
            new AlertDialog.Builder(This)
                    .setTitle(R.string.preference_startactivity)
                    .setView(index_edittext)
                    .setPositiveButton(R.string.preference_dialog_posi, (dialog, which) -> {
                        spe.putString(ApplJuTemp.START_ACTIVITY_INDEX, Objects.requireNonNull(index_edittext.getText()).toString());
                        spe.apply();
                    })
                    .setNegativeButton(R.string.preference_dialog_nega, null)
                    .create().show();
            return true;
        });

        This.findPreference(Re.getString(R.string.preference_id_dict_mode)).setOnPreferenceChangeListener((preference, newValue) -> {
            spe.putBoolean(ApplJuTemp.DICT_MODE, (Boolean) newValue);
            spe.apply();
            return true;
        });

        This.findPreference(Re.getString(R.string.preference_id_dict_fontsize)).setOnPreferenceChangeListener((preference, newValue) -> {
            spe.putInt(ApplJuTemp.DICT_FONTSIZE, Integer.parseInt(newValue.toString()));
            spe.apply();
            return true;
        });

        This.findPreference(Re.getString(R.string.preference_id_dict_appid)).setOnPreferenceClickListener(preference -> {
            final EditText ed = new EditText(This);
            ed.setHint(R.string.preference_not_modify);
            ApplJuTemp.requestFocus(This, ed);
            new AlertDialog.Builder(This)
                    .setTitle(This.getResources().getString(R.string.preference_dict_appid_dialog_title))
                    .setView(ed)
                    .setPositiveButton(This.getString(R.string.preference_dialog_posi), (dialogInterface, which) -> {
                        if (ed.getText().toString().isEmpty()) return;
                        spe.putString(ApplJuTemp.DICT_APPID_SPF, UUIDGenerateJuTemp.generateShortUUID(7) + Base64JuTempUtil.encode(ed.getText().toString()).replace("=", ""));
                        spe.apply();
                    })
                    .setNegativeButton(This.getString(R.string.preference_dialog_nega), null)
                    .create().show();
            return true;
        });

        This.findPreference(Re.getString(R.string.preference_id_dict_appkey)).setOnPreferenceClickListener(preference -> {
            final EditText ed = new EditText(This);
            ed.setHint(R.string.preference_not_modify);
            ApplJuTemp.requestFocus(This, ed);
            new AlertDialog.Builder(This)
                    .setTitle(This.getResources().getString(R.string.preference_dict_appkey_dialog_title))
                    .setView(ed)
                    .setPositiveButton(This.getString(R.string.preference_dialog_posi), (dialogInterface, which) -> {
                        if (ed.getText().toString().isEmpty()) return;
                        spe.putString(ApplJuTemp.DICT_APPKEY_SPF, UUIDGenerateJuTemp.generateShortUUID(7) + Base64JuTempUtil.encode(ed.getText().toString()).replace("=", ""));
                        spe.apply();
                    })
                    .setNegativeButton(This.getString(R.string.preference_dialog_nega), null)
                    .create().show();
            return true;
        });

        This.findPreference(Re.getString(R.string.preference_id_import)).setOnPreferenceClickListener(preference -> {
            final EditText ed = new EditText(This);
            ApplJuTemp.requestFocus(This, ed);
            AlertDialog alertDialog = new AlertDialog.Builder(This)
                    .setTitle(This.getResources().getString(R.string.preference_import))
                    .setView(ed)
                    .setPositiveButton(This.getString(R.string.preference_dialog_posi), (dialogInterface, which) -> {
                        Map<String, Object> spMap = (Map<String, Object>) JSON.parse(ed.getText().toString());
                        spe.clear();
                        for (Map.Entry<String, Object> entry : spMap.entrySet()) {
                            if (entry.getValue() instanceof String) {
                                spe.putString(entry.getKey(), (String) entry.getValue());
                                continue;
                            }
                            if (entry.getValue() instanceof Boolean) {
                                spe.putBoolean(entry.getKey(), (Boolean) entry.getValue());
                                continue;
                            }
                            if (entry.getValue() instanceof Integer) {
                                spe.putInt(entry.getKey(), (Integer) entry.getValue());
                                continue;
                            }
                        }
                        spe.apply();
                    })
                    .setNeutralButton(This.getString(R.string.menu_default_paste), null)
                    .setNegativeButton(This.getString(R.string.preference_dialog_nega), null)
                    .create();
            alertDialog.setOnShowListener(dialogInterface -> {
                // NeutralButton.setOnClickListener(v->{}) without dismiss()
                alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(v -> {
                    ed.setText(ClipJuTemp.getClipboard(This));
                });
            });
            alertDialog.show();
            return true;
        });

        This.findPreference(Re.getString(R.string.preference_id_export)).setOnPreferenceClickListener(preference -> {
            final EditText ed = new EditText(This);
            ed.setText(new JSONObject(sp.getAll()).toString());
            ApplJuTemp.requestFocus(This, ed);
            new AlertDialog.Builder(This)
                    .setTitle(This.getResources().getString(R.string.preference_export))
                    .setView(ed)
                    .setPositiveButton(This.getString(R.string.menu_default_copy), (dialogInterface, which) -> ClipJuTemp.setClipboard(This, ed.getText().toString()))
                    .setNegativeButton(This.getString(R.string.preference_dialog_nega), null)
                    .create().show();
            return true;
        });

        This.findPreference(Re.getString(R.string.preference_id_bootalarmmemo_flag)).setOnPreferenceChangeListener((preference, newValue) -> {
            spe.putBoolean(ApplJuTemp.BOOTALARMMEMO_FLAG, (boolean) newValue);
            spe.apply();
            return true;
        });

        This.findPreference(Re.getString(R.string.preference_id_bootalarmmemo_content)).setOnPreferenceChangeListener((preference, newValue) -> {
            spe.putString(ApplJuTemp.BOOTALARMMEMO_CONTENT, (String) newValue);
            return true;
        });/*.setOnPreferenceClickListener(preference -> {
            final EditText ed = new EditText(This);
            ed.setText(sp.getString(ApplJuTemp.BOOTALARMMEMO_CONTENT, ""));
            ApplJuTemp.requestFocus(This, ed);
            new AlertDialog.Builder(This)
                    .setTitle(This.getResources().getString(R.string.preference_bootalarmmemo_dialog_title))
                    .setView(ed)
                    .setPositiveButton(This.getString(R.string.preference_dialog_posi), (p1, p2) -> {
                        spe.putString(ApplJuTemp.BOOTALARMMEMO_CONTENT, ed.getText().toString());
                        spe.apply();
                    })
                    .setNegativeButton(This.getString(R.string.preference_dialog_nega), null)
                    .create().show();
            return true;
        })*/

        This.findPreference(Re.getString(R.string.preference_id_about_detail)).setOnPreferenceClickListener(preference -> {
            new AlertDialog.Builder(This)
                    .setTitle(This.getResources().getString(R.string.preference_about_title))
                    .setMessage(This.getResources().getString(R.string.preference_about_dialog_message))
                    .setPositiveButton(This.getResources().getString(R.string.preference_about_posi), null)
                    .setNeutralButton(This.getResources().getString(R.string.preference_about_dialog_message_copy), (dialogInterface, which) -> ClipJuTemp.setClipboard(This, This.getResources().getString(R.string.preference_about_dialog_message_qqnumber))).create().show();
            return true;
        });

    }

}
