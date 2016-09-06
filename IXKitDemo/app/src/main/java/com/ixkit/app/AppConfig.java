package com.ixkit.app;


import android.content.Context;

import net.ixkit.octopus.core.DataManager;
import net.ixkit.octopus.core.WebServiceConfig;

public class AppConfig {

	public static class WebServiceDef {
		private static String root =  "http://i2cart.com";
		//private static String root =  "{{BaseUri}}";
 		public static String getBaseUrl() {
			return root;
		}

	}

	public static void setup(Context context){
		String apiRootUrl = WebServiceDef.getBaseUrl();
		WebServiceConfig.getInstance().setApiRootUrl(apiRootUrl);

		DataManager.getInstance().registerContext(context);
	}

}
