package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.JuTemp.Home.ApplJuTemp;
import com.JuTemp.Home.MyView.DragAdapter;
import com.JuTemp.Home.MyView.DragListView;
import com.JuTemp.Home.R;
import com.JuTemp.Home.util.FragmentLogicJuTemp;
import com.JuTemp.Home.util.IntentJuTempUtil;
import com.JuTemp.Home.util.ObjectSerializerJuTemp;
import com.JuTemp.Home.util.UUIDGenerateJuTemp;
import com.alibaba.fastjson2.JSON;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class URLListJuTemp extends FragmentLogicJuTemp {

    private SharedPreferences sp = null;
    private SharedPreferences.Editor spe = null;
    private DragAdapter dragAdapter = null;
    private HashMap<String, String> spMap = null;
    private ArrayList<String> spArrayList = null;
    private List<Map<String, Object>> data = null;
    private final String SummaryKey="-";

    private List<Map<String, Object>> initData() {
        List<Map<String, Object>> listItems = new ArrayList<>();
        for (String str : spArrayList) {
            Map<String, Object> listItem = new HashMap<>();
            final String[] titleAndContent = sp.getString(str, "|").split("\\|", 2);
            listItem.put("title", titleAndContent[0]);
            listItem.put("content", titleAndContent[1]);
            listItems.add(listItem);
        }
        return listItems;
    }

    @Override
    public void mainLogic(final Activity ThisActivity, final FragmentLogicJuTemp fragmentLogic, final View view) {
        super.mainLogic(ThisActivity, fragmentLogic, view);

        sp = ThisActivity.getSharedPreferences(ApplJuTemp.URL_ARRAY, Context.MODE_PRIVATE);
        spe = sp.edit();
        spMap = (HashMap<String, String>) sp.getAll();
        spArrayList = (ArrayList<String>) ObjectSerializerJuTemp.deserialize(ThisActivity, sp.getString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, new ArrayList<String>())));
        DragListView mainlv = view.findViewById(R.id.urllistDragListView);
        mainlv.setDragViewId(R.id.urllist_simple_item_handle);
        data = initData();
        dragAdapter = new DragAdapter(data, ThisActivity);
        mainlv.setAdapter(dragAdapter);

        Button bnAdd = view.findViewById(R.id.urllistAdd);
        bnAdd.setOnClickListener(v -> {
            final View adView = ThisActivity.getLayoutInflater().inflate(R.layout.urllist_alertdialog_view, null);
            String keyTest = null;
            do keyTest = UUIDGenerateJuTemp.generateShortUUID();
            while (spArrayList.contains(keyTest));
            final String key = keyTest;
            new AlertDialog.Builder(ThisActivity)
                    .setTitle(Re.getString(R.string.urllist_dialog_textin_title))
                    .setView(adView)
                    .setPositiveButton(Re.getString(R.string.urllist_dialog_confirm), (dialogInterface, which) -> {
                        String title = ((EditText) adView.findViewById(R.id.urllist_title)).getText().toString();
                        if (title.contains("|")) {
                            Toast.makeText(ThisActivity, Re.getString(R.string.urllist_containsplit_error), Toast.LENGTH_LONG).show();
                            return;
                        }
                        String content = ((EditText) adView.findViewById(R.id.urllist_content)).getText().toString();
                        spMap.put(key, title + "|" + content);
                        spArrayList.add(spArrayList.size(), key);
                        spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
                        spe.putString(key, title + "|" + content);
                        spe.commit();
                        ((URLListJuTemp) This).redraw();
                    })
                    .setNegativeButton(Re.getString(R.string.urllist_dialog_cancel), (dialogInterface, which) -> {
                        spMap.put(key, "|");
                        spArrayList.add(key);
                        spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
                        spe.putString(key, "|");
                        spe.commit();
                        ((URLListJuTemp) This).redraw();
                    })
                    .setNeutralButton(Re.getString(R.string.urllist_dialog_delete), null)
                    .create().show();
        });
        bnAdd.setOnLongClickListener(v -> {
            new AlertDialog.Builder(ThisActivity)
                    .setTitle(Re.getString(R.string.urllist_dialog_deleteall_title))
                    .setPositiveButton(Re.getString(R.string.urllist_dialog_delete), (dialogInterface, which) -> {
                        spMap.clear();
                        spArrayList.clear();
                        spe.clear();
                        spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, new ArrayList<String>()));
                        spe.commit();
                        spMap = (HashMap<String, String>) sp.getAll();
                        ((URLListJuTemp) This).redraw();
                    })
                    .setNegativeButton(Re.getString(R.string.urllist_dialog_cancel), null)
                    .create().show();
            return true;
        });

        mainlv.setAdapter(dragAdapter);
        mainlv.setOnItemLongClickListener((parent, v, position, id) -> {
            final View adView = ThisActivity.getLayoutInflater()
                    .inflate(R.layout.urllist_alertdialog_view, null);
            ((EditText) adView.findViewById(R.id.urllist_title)).setText((String) data.get(position).get("title"));
            ((EditText) adView.findViewById(R.id.urllist_content)).setText((String) data.get(position).get("content"));
            new AlertDialog.Builder(ThisActivity)
                    .setTitle(Re.getString(R.string.urllist_dialog_modify_title))
                    .setView(adView)
                    .setPositiveButton(Re.getString(R.string.urllist_dialog_modify), (dialogInterface, which) -> {
                        String title = ((EditText) adView.findViewById(R.id.urllist_title)).getText().toString();
                        if (title.contains("|")) title = title.replace("|", SummaryKey);
                        String content = ((EditText) adView.findViewById(R.id.urllist_content)).getText().toString();
                        String key = spArrayList.get(position);
                        spMap.put(key, title + "|" + content);
                        spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
                        spe.putString(key, title + "|" + content);
                        spe.commit();
                        ((URLListJuTemp) This).redraw();
                    })
                    .setNegativeButton(Re.getString(R.string.urllist_dialog_cancel), null)
                    .setNeutralButton(Re.getString(R.string.urllist_dialog_delete), (dialogInterface, which) -> new AlertDialog.Builder(ThisActivity)
                            .setTitle(Re.getString(R.string.urllist_dialog_delete_title))
                            .setPositiveButton(Re.getString(R.string.urllist_dialog_delete), (dialogInterface1, which1) -> {
                                spMap.remove(spArrayList.get(position));
                                spe.remove(spArrayList.get(position));
                                spArrayList.remove(position);
                                spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
                                spe.commit();
                                spMap = (HashMap<String, String>) sp.getAll();
                                //mainlv.removeView(view);
                                ((URLListJuTemp) This).redraw();
                            })
                            .setNegativeButton(Re.getString(R.string.urllist_dialog_cancel), null)
                            .create().show())
                    .create().show();
            return true;
        });
        mainlv.setOnItemClickListener((parent, v, position, id) -> {
            String text = ((TextView) ((LinearLayoutCompat) ((LinearLayoutCompat) v).getChildAt(0)).getChildAt(1)).getText().toString();
            IntentJuTempUtil.intentUri(ThisActivity, text);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ApplJuTemp.shareFlag) {
            ApplJuTemp.shareFlag = false;
            String keyTest = null;
            do keyTest = UUIDGenerateJuTemp.generateShortUUID(); while (spArrayList.contains(keyTest));
            final String key = keyTest;
            if (ApplJuTemp.viaShareString.contains("http")) {
                int i = ApplJuTemp.viaShareString.indexOf("http");
                String title = ApplJuTemp.viaShareString.substring(0, i).replace("|", SummaryKey).trim();
                String content = ApplJuTemp.viaShareString.substring(i).trim();
                if (content.contains("#")) content = content.substring(0, content.indexOf("#"));
                spMap.put(key, title + "|" + content);
                spe.putString(key, title + "|" + content);
            } else {
                spMap.put(key, key + "|" + ApplJuTemp.viaShareString);
                spe.putString(key, key + "|" + ApplJuTemp.viaShareString);
            }
            spArrayList.add(key);
            spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
            spe.commit();
            ((URLListJuTemp) This).redraw();
        }
    }

    private void redraw() {
        spMap = (HashMap<String, String>) sp.getAll();
        spArrayList = (ArrayList<String>) ObjectSerializerJuTemp.deserialize(ThisActivity, sp.getString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, new ArrayList<String>())));
        data = initData();
        dragAdapter.notifyDataSetChanged(data);
    }

    @Override
    public void importJson(String str) {
        try {
            Map<String, String> spMapNew = (Map<String, String>) JSON.parse(str);
            ArrayList<String> spArrayListNew = (ArrayList<String>) ObjectSerializerJuTemp.deserialize(ThisActivity, spMapNew.get(SummaryKey));
            for (String keyInNew : spArrayListNew) {
                if (Objects.equals(keyInNew, null)) continue;
                String key = null;
                do key = UUIDGenerateJuTemp.generateShortUUID(); while (spArrayList.contains(key));
                spArrayList.add(key);
                spe.putString(key, spMapNew.get(keyInNew));
            }
            spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
            spe.commit();
            spMap = (HashMap<String, String>) sp.getAll();
            ((URLListJuTemp) This).redraw();
        } catch (Exception e) {
            Log.e("import", e.toString());
            Toast.makeText(ThisActivity, Re.getString(R.string.urllist_import_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public String exportJson() {
        return new JSONObject(spMap).toString().replace("\\/", "/");
    }
}

