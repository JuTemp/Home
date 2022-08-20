package com.JuTemp.Home.util;

public class ModiTiktokJuTemp
{
	public static String modiUrl(String edin)
	{
		return edin.substring(edin.indexOf("https://"), edin.lastIndexOf("/")+1);
	}

	public static String modiRenderData(String html) throws Exception
	{
		return html.substring(html.indexOf(">", html.indexOf("RENDER_DATA")) + 1, html.indexOf("</script></head><body >"))
			.replace("%7B", "{").replace("%7D", "}")
			.replace("%5B", "[").replace("%5D", "]")
			.replace("%22", "\"")
			.replace("%3D", "=")
			.replace("%2C", ",")
			.replace("%3A", ":")
			.replace("%2F", "/")
			.replace("%3F", "?")
			.replace("%25", "%")
			.replace("%26", "&")
			.replace("%5C", "\\");
	}
	
	public static String modiPlayAddr(String jsonRenderData) throws Exception
	{
		return "http:"+jsonRenderData.substring(
			jsonRenderData.indexOf("//",jsonRenderData.indexOf("playAddr")),
			jsonRenderData.indexOf("\"",jsonRenderData.indexOf("//",jsonRenderData.indexOf("playAddr")))
		);
	}
}
