package com.ixkit.octopus.util;

import android.os.Build;

/**
 * Created by icoco on 07/06/2018.
 */

public class DeviceUtils {
    public static String getDefaultUserAgent() {
        StringBuilder result = new StringBuilder(64);

        String userAgent = System.getProperty( "http.agent" );
        result.append("agent:"+ userAgent + ";");

        result.append("jvm:"+ System.getProperty("java.vm.version") + ";"); // such as 1.1.0


        String version = Build.VERSION.RELEASE; // "1.0" or "3.4b5"
        result.append(version.length() > 0 ? version : "1.0");

        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            String model = Build.MODEL;
            if (model.length() > 0) {
                result.append("; ");
                result.append(model);
            }
        }
        String id = Build.ID; // "MASTER" or "M4-rc20"
        if (id.length() > 0) {
            result.append(" Build/");
            result.append(id);
        }
        result.append(")");
        return result.toString();
    }
}
