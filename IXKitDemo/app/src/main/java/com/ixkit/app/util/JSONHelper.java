package com.ixkit.app.util;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelper {


	public static JSONArray safeJSONArry(String value) throws Exception{

		JSONArray result = null;
		try {
			result = new JSONArray(value);
		} catch (JSONException e) {

			// Tracer.e(e,"Failed while generate JSONArray");
			//@step
			JSONObject item = new JSONObject(value);
			if (null != item){
				result = new JSONArray();
				result.put(item);
			}
		}
		return result;
	}

	public static JSONObject getObject(JSONObject json, String name) {
		if (null == json || null == name) return null;

		JSONObject result = null;
		try {
			result = json.getJSONObject(name);
		} catch (JSONException e) {
			Tracer.e(e,"getObject");
		}
		return result;
	}

	private static String getStringValue(JSONObject json, String name) {
		if (null == json) return null;
		
		String value = null;
		try {
			value = json.getString(name);
		} catch (JSONException e) {

			 Tracer.e(e,"Failed getStringValue [" + name + "]" );
		}
		//Log.d("JSONHelper", "getStr->["+ json + "]"+ "name=[" + name + "],value=[" + value + "]");
		return value;
	}


	private static ExtJSON getLastNode (JSONObject json, String path){
		if (null == path || null == json) return null;

		String[] list = path.split("\\.");

		int len = list.length;
		if (1 == len){
			return  new ExtJSON (json,path);
		}

		//@step
		JSONObject node = json;
		for (int i = 0 ; i <len - 1 ; i++ ) {
			String name = list[i];
			try {
				JSONObject item = node.getJSONObject(name);
				if (null == item){
					break;
				}
				//@step
				node = item;

			}catch (Exception e){
				Tracer.e(e,"getLastNode, path=" + path);
			}
		}
		String name = list[len -1];
		return  new ExtJSON (node,name);
	}

	public static String getString (JSONObject json, String path){
		ExtJSON node = JSONHelper.getLastNode(json,path);

		if (null == node) return null;
		String result = JSONHelper.getStringValue(node.json,node.name);
		//@step
		return result;
	}

	public static String toPairString(JSONObject json)   {
		if (null == json) return null;
		JSONArray list =  json.names();
		String buf = "";
		int len = list.length();
		for (int i = 0; i < len; i++) {
			try {
				String key = (String) list.get(i);
				String value = JSONHelper.getStringValue(json,key);
				String mark = (i == len - 1) ?   "" : ",";
				buf = buf + key + ":" + value + mark;
			}catch (Exception e){
				Tracer.e(e,"toPairString " );
			}

		}
		return buf;
	}
}
