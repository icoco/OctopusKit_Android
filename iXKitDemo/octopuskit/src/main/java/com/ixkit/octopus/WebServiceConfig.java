/**
 * WebServiceConfig
 * <p/>
 *
 * For more information, visit the project page:
 * https://github.com/icoco/ixkit
 *
 * @author Robin Cheung <iRobinCheung@hotmail.com>
 * @version 1.0.1
 */

package com.ixkit.octopus;


import android.content.Context;

import com.ixkit.network.retrofit.RetrofitMaster;

public class WebServiceConfig {
	private static WebServiceConfig sInstance;

	private WebServiceConfig() {
		// initialize the singleton
		sInstance = this;
	}

	/**
	 * @return singleton instance
	 */
	public static WebServiceConfig getInstance() {
		if (null == sInstance) {
			synchronized (WebServiceConfig.class) {
				if (null == sInstance) {
					sInstance = new WebServiceConfig();
				}
			}

		}
		return sInstance;
	}
	
	private String mApiRootUrl  ; //@default ?

	public String getApiRootUrl() {
		return mApiRootUrl;
	}

	public void setApiRootUrl(String apiRootUrl, Context context) {

		this.mApiRootUrl = apiRootUrl;
		RetrofitMaster.getInstance().setup(this.mApiRootUrl,context);

	}
}
