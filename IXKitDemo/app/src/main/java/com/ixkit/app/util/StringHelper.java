package com.ixkit.app.util;

public class StringHelper {
	public static boolean isEmptyString(String value) {
		return null == value || "" == value;
	}

	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static String fromatAsJsonString(String key, String value){
		String sValue = value;
		if (StringHelper.isEmptyString(value)){
			sValue = "null";
		}
		sValue = sValue.replace("\"", "\'");

		sValue =  StringHelper.mark("\"",sValue,"\"");
		key = StringHelper.mark("\"",key,"\"");

		sValue = key + ":" + sValue;
		return StringHelper.mark("{",sValue,"}");
	}

	public static  String mark(String left, String value, String right ){
		return left + value + right;
	}
}
