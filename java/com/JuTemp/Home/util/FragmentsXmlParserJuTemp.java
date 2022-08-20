package com.JuTemp.Home.util;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.JuTemp.Home.R;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentsXmlParserJuTemp {
    public static ArrayList<HashMap<String, Object>> initFragmentsXml(Activity This,boolean whetherDeveloper) {
        ArrayList<HashMap<String, Object>> FragmentsXml = new ArrayList<>();
        try {
            XmlPullParser xpp = This.getResources().getXml(whetherDeveloper?R.xml.fragments_developer:R.xml.fragments);
            o: while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                HashMap<String, Object> item = new HashMap<>();
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("FragmentLogic")) {
                        while (true) {
                            xpp.next();
                            if ((xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equals("FragmentLogic") || xpp.getEventType() == XmlPullParser.END_DOCUMENT))
                                break;
                            if (xpp.getEventType() == XmlPullParser.START_TAG) {
                                if (xpp.getName().equals("entry")) {
                                    String key = "", value = "";
                                    if (xpp.getAttributeName(0).equals("key"))
                                        key = xpp.getAttributeValue(0);
                                    xpp.next();
                                    if (xpp.getEventType() == XmlPullParser.TEXT)
                                        value = xpp.getText();
//                                    if (value.contains("$")) break o;
                                    item.put(key, value);
                                }
                            }
                        }
                    }
                }
                if (!item.isEmpty()) FragmentsXml.add(item);
                xpp.next();
            }
        } catch (Exception e) {
            Toast.makeText(This, e.toString(), Toast.LENGTH_LONG).show();
        }
        return FragmentsXml;
    }

    public static int strId2int(final Resources Re, final String packageName, final String strId) // R.id.xxx -> 0x7f010001
    {
        String[] strIdArray = strId.split("\\.");
        return Re.getIdentifier(strIdArray[2], strIdArray[1], packageName);
    }
}
