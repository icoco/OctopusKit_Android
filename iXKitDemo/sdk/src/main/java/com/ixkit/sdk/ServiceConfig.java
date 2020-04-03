package com.ixkit.sdk;


import android.content.Context;

import com.ixkit.octopus.WebServiceConfig;
import com.orhanobut.logger.Logger;

public class ServiceConfig {


    public static class WebServiceDef {
         private static String root =  "http://www.ixkit.com/api/";

        public static String getBaseUrl() {
            return root;
        }

    }

    public static void setup(String url, Context context) {
        if (null != url){
            WebServiceDef.root = url;
        }
        String apiRootUrl = WebServiceDef.getBaseUrl();
        WebServiceConfig.getInstance().setApiRootUrl(apiRootUrl,context);

        Logger.d("ServiceConfig", "ServiceConfig:" + url);

    }
}