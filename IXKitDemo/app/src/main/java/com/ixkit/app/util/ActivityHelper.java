package com.ixkit.app.util;

 
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ActivityHelper {


	public static void startActivity( Context context, String className){
		ActivityHelper.startActivity(context,className,null);
	}

	public static void startActivity( Context context, String className,Bundle bundle){
		Class<?> c = null;
		if(className != null) {
			try {
				c = Class.forName(className );
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ;
			}
		}
		Intent intent = new Intent(context, c);
		if (null != bundle){
			intent.putExtras(bundle);
		}
		context.startActivity(intent);
	}
}
