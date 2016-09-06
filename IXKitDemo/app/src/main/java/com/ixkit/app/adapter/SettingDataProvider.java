package com.ixkit.app.adapter;

import java.util.ArrayList;

import android.app.Activity;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.ixkit.app.base.IDataProvider;
import com.ixkit.app.AppManager;
import com.ixkit.app.util.FileHelper;

public class SettingDataProvider implements IDataProvider {

	@Override
	public ArrayList loadData() {
		// @step
		String filePath = "settings.def";
		// @step
		ArrayList items = null;// new ArrayList<String>();
		//@step
		Activity activity = (Activity) AppManager.getInstance().getContext();
		items = (ArrayList) FileHelper.readCsv(activity, filePath);
		return items;
	}

	@Override
	public ArrayList<?> loadData(Listener<String> onSuccessCallback,
			ErrorListener onErrorCallBack) {
		// TODO Auto-generated method stub
		
		return this.loadData();
	}

	@Override
	public Object getData(Listener<String> onSuccessCallback,
			ErrorListener onErrorCallBack) {
		// TODO Auto-generated method stub
		return null;
	}

}
