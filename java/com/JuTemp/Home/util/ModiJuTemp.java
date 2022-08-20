package com.JuTemp.Home.util;

import java.util.Objects;

public final class ModiJuTemp {

    public static String modiTextInDict(String textin) {
        return textin.replace(" ", "+");
    }

    public static String[] modiTextOutDict(String str) {
        // <ul>...</ul> <div>...</div>
        String expl = "", pron = "";
        int startt = 0, endd = 0;

        int ssssstart = str.indexOf("<ul>");
        if (ssssstart == -1) return new String[]{"", "Wrong spelling."};
        int eeeeend = str.indexOf("</ul>", ssssstart);
        String substr = str.substring(ssssstart, eeeeend + 5);
        // The substring "</div>" get the length of six.
        // The string substr is a string like "<div>...</div>".

        pron = "/" + str.substring(str.indexOf("[", str.indexOf("phonetic\">")) + 1, str.indexOf("]", str.indexOf("phonetic\">"))) + "/";

        while (true) {
            startt = substr.indexOf("<li>", endd);
            if (startt == -1) break;
            endd = substr.indexOf("</li>", startt);
            if (substr.indexOf("<a", startt) != -1)
                expl += substr.substring(substr.indexOf(">", substr.indexOf("<a", startt)) + 1, substr.indexOf("</a>", substr.indexOf("<a", startt))).trim() + " ";
            expl += substr.substring(substr.indexOf("</a>") == -1 ? startt + 4 : substr.indexOf("</a>", startt) + 4, endd).trim() + "\n";
            // The string expl is a string without the tab "<li/>".
        }
        expl += "\n";
        while (true) {
            startt = str.indexOf("<p class=\"grey\"> ", endd);
            if (startt == -1) break;
            endd = str.indexOf(" </p>", startt);
            expl += str.substring(startt + 17, endd) + "\n";
            // The string expl is a string without the tab "<p/>".
        }

        return new String[]{pron, expl};
    }

    public static String trimUin(String textin) {
        String uin = "";
        if (textin.contains("song"))
			/*uin = textin.substring(
				textin.indexOf("?id=") + 4,
				textin.indexOf("&", textin.indexOf("?id=") + 4)
			);*/
            uin = textin;
        if (textin.contains("playlist"))
			/*uin = textin.substring(
				textin.indexOf("?id=") + 4,
				textin.indexOf("&", textin.indexOf("?id=") + 4)
			);*/
            uin = textin;
        return !Objects.equals(uin, "") ? uin : "-1";
    }

    public static String modiTitleSong(String html) throws Exception {
        String title = "";
        title = html.substring(html.indexOf("<title>") + 7, html.lastIndexOf("</title>"));
        title = title.substring(0, title.lastIndexOf(" - "));
        title = title.substring(0, title.lastIndexOf(" - "));
        return title;
    }

    public static String modiTitlePlaylist(String html) throws Exception {
        String title = "";
        title = html.substring(html.indexOf("<title>") + 7, html.lastIndexOf("</title>"));
        title = title.substring(0, title.lastIndexOf(" - "));
        return title;
    }

    public static String[] trimUinPlaylist(String html) {
        String rt[] = new String[]{"", "", "", "", "", "", "", "", "", ""};
        for (int i = 0; i < 9; i++) {
            html = html.substring(html.indexOf("m-sgitem"));
            rt[i] = html.substring(
                    html.indexOf("?id=") + 4,
                    html.indexOf("\"", html.indexOf("?id=") + 4)
            );
            //if (html.indexOf("u-hmsprt sgchply") == -1) return rt;
            html = html.substring(html.indexOf("u-hmsprt sgchply"));
        }
        return rt;
    }

    public static String[] trimUinNamePlaylist(String html) {
        String rt[] = new String[]{"", "", "", "", "", "", "", "", "", ""};
        for (int i = 0; i < 9; i++) {
            html = html.substring(html.indexOf("<div class=\"f-thide sgtl\">") + 26);
            rt[i] = html.substring(0, html.indexOf("<"));
            html = html.substring(html.indexOf("<div class=\"f-thide sginfo\">") + 28);
            rt[i] = rt[i] + " - " + html.substring(0, html.indexOf("</div>"));
            rt[i] = rt[i].substring(0, rt[i].lastIndexOf(" - "));
            //if (html.indexOf("<span class=\"u-hmsprt sgchply\">") == -1) return rt;
            html = html.substring(html.indexOf("<span class=\"u-hmsprt sgchply\">"));
            rt[i] = rt[i].replace("<!-- -->", "").replace(" / ", " ");
        }
        return rt;
    }
}
