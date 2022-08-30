package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.JuTemp.Home.ApplJuTemp;
import com.JuTemp.Home.R;
import com.JuTemp.Home.util.ClipJuTemp;
import com.JuTemp.Home.util.FragmentLogicJuTemp;
import com.JuTemp.Home.util.ObjectSerializerJuTemp;
import com.JuTemp.Home.util.UUIDGenerateJuTemp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoJuTemp extends FragmentLogicJuTemp {
    SharedPreferences sp = null;
    SharedPreferences.Editor spe = null;
    SimpleAdapter sa = null;
    Map<String, String> spMap = null;
    ArrayList<String> spArrayList = null;
    List<Map<String, Object>> listItems = new ArrayList<>();

    static MemoJuTemp This = null;
    static Activity ThisActivity = null;
    static View view = null;

    @Override
    public void mainLogic(final Activity ThisActivity, final FragmentLogicJuTemp fragmentLogic, final View view) {
        this.This = (MemoJuTemp) fragmentLogic;
        this.ThisActivity = ThisActivity;
        this.view = view;

        sp = ThisActivity.getSharedPreferences(((ApplJuTemp) ThisActivity.getApplication()).MEMO, Context.MODE_PRIVATE);
        spe = sp.edit();
        spMap = (Map<String, String>) sp.getAll();
        spArrayList = (ArrayList<String>) ObjectSerializerJuTemp.deserialize(ThisActivity, sp.getString("General", ObjectSerializerJuTemp.serialize(ThisActivity, new ArrayList<String>())));

        Button bnAdd = (Button) view.findViewById(R.id.memoAdd);
        bnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyTest = null;
                do keyTest = UUIDGenerateJuTemp.generateShortUUID();
                while (spArrayList.contains(keyTest));
                final String key = keyTest;
                final EditText ed = new EditText(ThisActivity);
                requestFocus(ThisActivity, ed);
                new AlertDialog.Builder(ThisActivity)
                        .setTitle(Re.getString(R.string.memo_dialog_textin_title))
                        .setView(ed)
                        .setPositiveButton(Re.getString(R.string.memo_dialog_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                String value = ed.getText().toString();
                                spMap.put(key, value);
                                spArrayList.add(key);
                                spe.putString("General", ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
                                spe.putString(key, value);
                                spe.commit();
                                This.redraw();
                            }
                        })
                        .setNeutralButton(Re.getString(R.string.memo_dialog_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                spMap.put(key, "");
                                spArrayList.add(key);
                                spe.putString("General", ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
                                spe.putString(key, "");
                                spe.commit();
                                This.redraw();
                            }
                        })
                        .setNegativeButton(Re.getString(R.string.memo_dialog_delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                // TODO: Implement this method
                            }
                        })
                        .create().show();
            }
        });
        bnAdd.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View p1) {
                new AlertDialog.Builder(ThisActivity)
                        .setTitle(Re.getString(R.string.memo_dialog_deleteall_title))
                        .setPositiveButton(Re.getString(R.string.memo_dialog_delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                spMap.clear();
                                spArrayList.clear();
                                spe.clear();
                                spe.putString("General", ObjectSerializerJuTemp.serialize(ThisActivity, new ArrayList<String>()));
                                spe.commit();
                                spMap = (Map<String, String>) sp.getAll();
                                This.redraw();
                            }
                        })
                        .setNegativeButton(Re.getString(R.string.memo_dialog_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                // TODO: Implement this method
                            }
                        })
                        .create().show();
                return true;
            }
        });

        listItems.clear();
        for (String str : spArrayList) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("tv", spMap.get(str));
            listItems.add(listItem);
        }
        sa = new SimpleAdapter(ThisActivity, listItems, R.layout.memo_simple_item, new String[]{"tv"}, new int[]{R.id.tv1});
        ListView mainlv = (ListView) view.findViewById(R.id.memoListView);
        mainlv.setAdapter(sa);
        mainlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText ed = new EditText(ThisActivity);
                ed.setText(sp.getString(spArrayList.get(position), ""));
                requestFocus(ThisActivity, ed);
                new AlertDialog.Builder(ThisActivity)
                        .setTitle(Re.getString(R.string.memo_dialog_modify_title))
                        .setView(ed)
                        .setPositiveButton(Re.getString(R.string.memo_dialog_modify), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                String key = spArrayList.get(position);
                                String value = ed.getText().toString();
                                spMap.put(key, value);
                                spe.putString(key, value);
                                spe.commit();
                                This.redraw();
                            }
                        })
                        .setNegativeButton(Re.getString(R.string.memo_dialog_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                // TODO: Implement this method
                            }
                        })
                        .setNeutralButton(Re.getString(R.string.memo_dialog_delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                new AlertDialog.Builder(ThisActivity)
                                        .setTitle(Re.getString(R.string.memo_dialog_delete_title))
                                        .setPositiveButton(Re.getString(R.string.memo_dialog_delete), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int which) {
                                                spMap.remove(spArrayList.get(position));
                                                spe.remove(spArrayList.get(position));
                                                spArrayList.remove(position);
                                                spe.putString("General", ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
                                                spe.commit();
                                                spMap = (Map<String, String>) sp.getAll(); ////
                                                This.redraw();
                                            }
                                        })
                                        .setNegativeButton(Re.getString(R.string.memo_dialog_cancel), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int which) {
                                                // TODO: Implement this method
                                            }
                                        })
                                        .create().show();

                            }
                        })
                        .create().show();

            }
        });
        mainlv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String text = ((TextView) ((LinearLayout) ((LinearLayout) view).getChildAt(0)).getChildAt(0)).getText().toString();
                ClipJuTemp.setClipboard(ThisActivity, text);
                return true;
            }
        });
    }

    private void redraw() {
        listItems.clear();
        spMap = (Map<String, String>) sp.getAll(); ////
        for (String str : spArrayList) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("tv", spMap.get(str));
            listItems.add(listItem);
        }
        sa.notifyDataSetChanged();
    }

    @Override
    public void importJson(String str) {
        try {
            Map<String, String> spMapNew = (Map<String, String>) com.alibaba.fastjson.JSONObject.parse(str);
            ArrayList<String> spArrayListNew = (ArrayList<String>) ObjectSerializerJuTemp.deserialize(ThisActivity, spMapNew.get("General"));
            for (String keyInNew : spArrayListNew) {
                if (keyInNew == "General") continue;
                String key = null;
                do key = UUIDGenerateJuTemp.generateShortUUID(); while (spArrayList.contains(key));
                spArrayList.add(key);
                spe.putString(key, spMapNew.get(keyInNew));
            }
            spe.putString("General", ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
            spe.commit();
            spMap = (Map<String, String>) sp.getAll();
            This.redraw();
        } catch (Exception e) {
            Toast.makeText(ThisActivity, Re.getString(R.string.memo_import_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public String exportJson() {
        return com.alibaba.fastjson.JSONObject.toJSON(spMap).toString();
    }

}
