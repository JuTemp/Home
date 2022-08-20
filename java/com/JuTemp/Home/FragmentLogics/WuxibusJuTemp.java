package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.JuTemp.Home.MyView.BaseAdapterWuxibusBusStop;
import com.JuTemp.Home.MyView.BaseAdapterWuxibusDirection;
import com.JuTemp.Home.R;
import com.JuTemp.Home.util.FragmentLogicJuTemp;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class WuxibusJuTemp extends FragmentLogicJuTemp {

    String idStr = null;
    ListView directionLv = null;
    ListView busStopLv = null;
    Button requestRouteBn=null;
    BaseAdapterWuxibusDirection saDirection = null;
    BaseAdapterWuxibusBusStop saBusStop = null;
    ArrayList<HashMap<String, HashMap<String, Object>>> listitemsDirection = new ArrayList<>();
    ArrayList<HashMap<String, Object>> listitemsBusStop = new ArrayList<>();

    @Override
    public void mainLogic(Activity ThisActivity, FragmentLogicJuTemp fragmentLogic, View view) {
        super.mainLogic(ThisActivity, fragmentLogic, view);

        directionLv = view.findViewById(R.id.wuxibus_direction);
        busStopLv = view.findViewById(R.id.wuxibus_busstop);

        requestFocus(ThisActivity, view.findViewById(R.id.wuxibus_id));

        requestRouteBn=view.findViewById(R.id.wuxibus_search);
        requestRouteBn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                requestRouteBn.setFocusable(false);
                idStr = ((EditText) view.findViewById(R.id.wuxibus_id)).getText().toString();
                refreshListView(ThisActivity);

                if (!idStr.matches("[\\dA-Z\\u4E00-\\u9FA5]+")) {
                    Toast.makeText(ThisActivity, Re.getString(R.string.wuxibus_textin_error), Toast.LENGTH_LONG).show();
                    return;
                }
                postWuxibus("BaseLineInfoList", idStr.matches("\\d+") ? idStr + "路" : idStr, 0, "");
            }

            private void refreshListView(Activity ThisActivity) {
                if (saDirection == null) {
                    saDirection = new BaseAdapterWuxibusDirection(ThisActivity, listitemsDirection) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View itemView = super.getView(position, convertView, parent);
                            Button upButton = itemView.findViewById(R.id.item_direction_up);
                            Button downButton = itemView.findViewById(R.id.item_direction_down);
                            for (Button bn : new Button[]{upButton, downButton}) {
                                bn.setOnClickListener(v -> {
                                    HashMap<String, HashMap<String, Object>> listitemPosition = listitemsDirection.get(position);
                                    listitemsDirection.clear();
                                    listitemsDirection.add(listitemPosition);
                                    saDirection.notifyDataSetChanged();
                                    listitemsBusStop.clear();
                                    saBusStop.notifyDataSetChanged();
                                    JSONObject jsonObject = new JSONObject();
                                    String[] gprsIdAndSegmentId = ((String) bn.getTag()).split("\\|", 2);
                                    boolean companyFlag = gprsIdAndSegmentId[0].contains("-");
                                    jsonObject.put("companyType", companyFlag ? "2" : "1");
                                    jsonObject.put("gprsId", gprsIdAndSegmentId[0]);
                                    if (companyFlag) jsonObject.put("dir", "0");
                                    else jsonObject.put("segmentID", gprsIdAndSegmentId[1]);
                                    postWuxibus("RunDetailByRouteId", null, 0, jsonObject.toString());
                                });
                            }
                            return itemView;
                        }
                    };
                    directionLv.setAdapter(saDirection);
                } else {
                    listitemsDirection.clear();
                    saDirection.notifyDataSetChanged();
                }
                if (saBusStop == null) {
                    saBusStop = new BaseAdapterWuxibusBusStop(ThisActivity, listitemsBusStop);
                    busStopLv.setAdapter(saBusStop);
/*
                    busStopLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View vv, int position, long id) {
                            for (int i = 0; i < listitemsBusStop.size() - 1; i++) {
                                LinearLayout itemLinearLayout = ((LinearLayout) ((ListView) ThisActivity.findViewById(R.id.wuxibus_busstop)).getAdapter().getView(position, null, null));
                                itemLinearLayout.findViewById(R.id.item_focus).setVisibility(View.GONE);
                                if (listitemsBusStop.get(i).containsKey("busName"))
                                    itemLinearLayout.findViewById(R.id.item_arrival).setVisibility(View.VISIBLE);
                                else
                                    itemLinearLayout.findViewById(R.id.item_arrival).setVisibility(View.GONE);
                            }
                            vv.findViewById(R.id.item_arrival).setVisibility(View.GONE);
                            TextView focusTv = vv.findViewById(R.id.item_focus);
                            focusTv.setVisibility(View.VISIBLE);
                            for (int i = position-1; i >= 0; i--) {
                                if (listitemsBusStop.get(i).containsKey("busName")) {
                                    focusTv.setText(ThisActivity.getString(R.string.wuxibus_focus, listitemsBusStop.get(i).get("busName"), listitemsBusStop.get(i).get("arrivalTime"), position-i));
                                    return;
                                }
                            }
                            focusTv.setText(ThisActivity.getString(R.string.wuxibus_focus, "000", listitemsBusStop.get(listitemsBusStop.size()-1).get("initialStationTime"), position+1));
                        }
                    });
*/
                } else {
                    listitemsBusStop.clear();
                    saBusStop.notifyDataSetChanged();
                }
            }

        });
    }

    private ArrayList<HashMap<String, Object>> getBaseLineInfoList(JSONObject requestIdJson) {
        JSONArray lineList = requestIdJson.getJSONArray("list");
        ArrayList<HashMap<String, Object>> lineHashMapArrayList = new ArrayList<>();
        for (Object line : lineList) {
            HashMap<String, Object> lineHashMap = new HashMap<>();
            for (String s : new String[]{"name", "lineid", "gprsid"})
                lineHashMap.put(s, ((JSONObject) line).get(s));
            lineHashMapArrayList.add(lineHashMap);
        }
        return lineHashMapArrayList;
    }

    private ArrayList<HashMap<String, Object>> getLineDetailByGprsId(JSONObject requestIdJson) {
        JSONArray lineList = requestIdJson.getJSONArray("list");
        ArrayList<HashMap<String, Object>> lineHashMapArrayList = new ArrayList<>();
        for (Object line : lineList) {
            HashMap<String, Object> lineHashMap = new HashMap<>();
            for (String s : new String[]{"name", "gprsId", "segmentID", "startStationName", "endStationName"})
                lineHashMap.put(s, ((JSONObject) line).get(s));
            lineHashMapArrayList.add(lineHashMap);
        }
        return lineHashMapArrayList;
    }

    private ArrayList<HashMap<String, Object>> getRunDetailByRouteId(JSONObject requestIdJson) {
        JSONObject jsonObject = requestIdJson.getJSONObject("detail");
        ArrayList<HashMap<String, Object>> lineHashMapArrayList = new ArrayList<>();
        HashMap<String, Object> lineHashMap = new HashMap<>();
        JSONArray lineList = jsonObject.getJSONArray("zxStationRunDetailInfos");
        for (Object line : lineList) {
            lineHashMap = new HashMap<>();
            for (String s : new String[]{"stationId", "stationName", "zxStationBusDetailInfos"})
                lineHashMap.put(s, ((JSONObject) line).get(s));
            lineHashMapArrayList.add(lineHashMap);
        }
        lineHashMap.put("initialStationTime", jsonObject.getString("initialStationTime"));
        lineHashMapArrayList.add(lineHashMap);
        return lineHashMapArrayList;
    }

    final Handler handler = new Handler(new Handler.Callback() {
        String name = null;
        String gprsId = null;
        String[] lineId = null;
        int directionLvIndex = 0;
        JSONObject jsonObject = null;
        String segmentId = null;
        String startStationName = null, endStationName = null;
        String stationName = null;
        String busName = null;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Bundle data = msg.getData();
            directionLvIndex = data.containsKey("directionLvIndex") ? 0 : data.getInt("directionLvIndex");
            JSONObject requestIdJson = JSONObject.parseObject(data.getString("data"));
            switch (data.getString("type")) {
                case "BaseLineInfoList":
                    ArrayList<HashMap<String, Object>> requestIdHashMapArrayList = ((WuxibusJuTemp) This).getBaseLineInfoList(requestIdJson);
                    if (requestIdHashMapArrayList.size() == 0) {
                        Toast.makeText(ThisActivity, Re.getString(R.string.wuxibus_baselineinfolist_null), Toast.LENGTH_LONG).show();
                        return true;
                    } else { // requestIdHashMapArrayList.size() >= 1
                        for (int i = 0; i < requestIdHashMapArrayList.size(); i++) {
                            HashMap<String, Object> lineHashMap = requestIdHashMapArrayList.get(i);
                            name = (String) lineHashMap.get("name");
                            gprsId = ((String) lineHashMap.get("gprsid"));
                            lineId = ((String) Objects.requireNonNull(lineHashMap.get("lineid"))).split(",", 2);
                            jsonObject = new JSONObject();
                            boolean companyFlag=gprsId.contains("-");
                            jsonObject.put("companyType",  companyFlag? "2" : "1");
                            jsonObject.put("gprsId", gprsId);
                            refreshNull(new Object[]{name, gprsId, lineId});
                            postWuxibus("LineDetailByGprsId", null, i, jsonObject.toString());
                        }
                    }
                    break;
                case "LineDetailByGprsId":
                    ArrayList<HashMap<String, Object>> requestLineDetailHashMapArrayList = ((WuxibusJuTemp) This).getLineDetailByGprsId(requestIdJson);
                    HashMap<String, Object> listitemText = new HashMap<>();
                    HashMap<String, Object> listitemTag = new HashMap<>();
                    for (int i = 0; i < requestLineDetailHashMapArrayList.size(); i++) {
                        HashMap<String, Object> item = requestLineDetailHashMapArrayList.get(i);
                        name = (String) item.get("name");
                        gprsId = (String) item.get("gprsId");
                        segmentId = (String) item.get("segmentID");
                        startStationName = (String) item.get("startStationName");
                        endStationName = (String) item.get("endStationName");
                        String upOrDown = name.contains("上") ? "up" : "down";
                        listitemText.put(upOrDown, name + "\n" + startStationName + "->" + endStationName);
                        listitemTag.put(upOrDown, gprsId + "|" + segmentId);
                        refreshNull(new Object[]{name, segmentId, startStationName, endStationName, gprsId});
                    }
                    HashMap<String, HashMap<String, Object>> listitemTextAndTag = new HashMap<>();
                    listitemTextAndTag.put("text", listitemText);
                    listitemTextAndTag.put("tag", listitemTag);
                    listitemsDirection.add(0, listitemTextAndTag); // [sth] do add(0,sth2) -> [sth2,sth]
                    saDirection.notifyDataSetChanged();
                    requestRouteBn.setFocusable(true);
                    break;
                case "RunDetailByRouteId":
                    ArrayList<HashMap<String, Object>> requestRunDetailHashMapArrayList = ((WuxibusJuTemp) This).getRunDetailByRouteId(requestIdJson);
                    HashMap<String, Object> listitem = null;
                    for (int i = 0; i < requestRunDetailHashMapArrayList.size() - 1; i++) {
                        HashMap<String, Object> item = requestRunDetailHashMapArrayList.get(i);
                        listitem = new HashMap<>();
                        listitem.put("stationName", item.get("stationName"));
                        JSONArray arrvialJSONArray = (JSONArray) item.get("zxStationBusDetailInfos");
                        if (arrvialJSONArray != null && !arrvialJSONArray.isEmpty()) {
                            listitem.put("busName", ((JSONObject) arrvialJSONArray.get(0)).getString("busName"));
                            listitem.put("arrivalTime", ((JSONObject) arrvialJSONArray.get(0)).getString("arrivalTime"));
                        }
                        listitemsBusStop.add(listitem);
                    }
                    listitem = new HashMap<>();
                    listitem.put("initialStationTime", requestRunDetailHashMapArrayList.get(requestRunDetailHashMapArrayList.size() - 1).get("initialStationTime"));
                    listitemsBusStop.add(listitem);
                    saBusStop.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
            return true;
        }

        private void refreshNull(Object[] objectArray) {
            for (Object object : objectArray) object = null;
        }
    });

    private void postWuxibus(String type, String idStr, int directionLvIndex, final String param) {
        URL urlTemp = null;
        try {
            switch (type) {
                case "BaseLineInfoList":
                    urlTemp = new URL("http://rtapi.wxbus.com.cn/m/zxXqLineStationInfo/findZxXqLineBaseInfoList?name=" + idStr);
                    break;
                case "LineDetailByGprsId":
                    urlTemp = new URL("http://rtapi.wxbus.com.cn/m/zxXqLineBaseInfo/findLineDetailByGprsId");
                    break;
                case "RunDetailByRouteId":
                    urlTemp = new URL("http://rtapi.wxbus.com.cn/m/zxXqLineStationInfo/findRunDetailByRouteId");
                    break;
            }
        } catch (Exception ex) {
            Log.e("WuxibusJuTemp", ex.toString());
        }
        final URL url = urlTemp;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    conn.setRequestProperty("rtSign", calculateRtSign(param));
                    conn.connect();
                    PrintWriter out = new PrintWriter(conn.getOutputStream());
                    out.print(param);
                    out.flush();
                    InputStream in = conn.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    while ((line = br.readLine()) != null) sb.append(line + "\n");
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    bundle.putInt("directionLvIndex", directionLvIndex);
                    bundle.putString("data", sb.toString());
                    message.setData(bundle);
                    handler.sendMessage(message);
                    conn.disconnect();
                } catch (Exception ex) {
                    Log.e("WuxibusJuTemp", ex.toString());
                }
            }
        }).start();
    }

    private static String calculateRtSign(String str) {
        return parseStrToMd5L32(str + "xbus_ipubtrans");
    }

    private static String parseStrToMd5L32(String str) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                int i = b & 255;
                if (i < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(i));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
