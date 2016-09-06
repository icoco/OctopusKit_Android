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

public class CategoryService extends OpenCartWebService {
	public static final String TAG = "CategoryService";

	public static CategoryService getCategories(){
		return CategoryService.getCategories("");
	}

	public static CategoryService getCategories(String categoryId){
		Log.d(TAG, "getCategories->categoryId:" + categoryId);
		 CategoryService service = new CategoryService();
		 //@step
		 HashMap params = 
		   Argument.toMap("route","common/home",
				   "path", categoryId,
				   "json", "1",
				   "int", 100,
				   "double",100.999);
		 
		 service.setParameters(params);
		 //@step
		 ServiceEvent event = new ServiceEvent(){
			 @Override
				public Object dataFilter(Object response) {
				 Log.d( TAG, "dataFilter->" + response);
					return response;
				}
		 };
		 service.setEvent(event);
		 return service;
	}

	public static CategoryService getProduct(String proudctId,Integer key,Number rate){
		Log.d(TAG, "getProduct->proudctId:" + proudctId + ",key:" + key + ",rate:"+rate );
		CategoryService service = new CategoryService();
		//@step
		HashMap params =
				Argument.toMap("route","common/home",
						"path", proudctId,
						"json", "1",
						"int", key,
						"double",rate);

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
