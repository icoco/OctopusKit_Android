/*
*
*  {{ClassName}}
*  {{AppName}}
*
*  Created by {{Author}} on {{CreateDate}}.
*  Copyright (c) {{CopyrightDate}} {{Organize}}. All rights reserved.
*
*  {{ Purpose }}
*
*/
package com.ixkit.app.service;
 
import java.util.HashMap;

import android.util.Log;

import net.ixkit.octopus.core.ServiceEvent;
import net.ixkit.octopus.lang.Argument;

public class ProductService extends OpenCartWebService {
	public static final String TAG = "ProductService";
	/*
	*
	* http://i2cart.com/index.php?route=product/product&path=59_64&product_id=52&json=1
	*
	* */
	public static ProductService getProduct(String path,String product_id){
		Log.d(TAG, "getProduct->" + product_id);
		ProductService service = new ProductService();
		 //@step
		 HashMap params = 
		   Argument.toMap("route","product/product",
				   "path", path,
				   "json", "1",
				   "product_id", product_id
				    );
		 
		 service.setParameters(params);
		 //@step
		 ServiceEvent event = new ServiceEvent(){
			 @Override
				public Object dataFilter(Object response) {
				 Log.d(TAG, "dataFilter->" + response);
					return response;
				}
		 };
		 service.setEvent(event);
		 return service;
	}
	

	
	

 
}
