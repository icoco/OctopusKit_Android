package com.ixkit.app.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ixkit.app.util.ExtDate;
import com.ixkit.app.R;
import com.ixkit.app.model.RefectionEngine;
import com.ixkit.app.util.JSONHelper;

import com.ixkit.app.base.IDataProvider;

import net.ixkit.octopus.core.WebService;

import com.ixkit.app.util.StringHelper;
import com.ixkit.app.util.Tracer;

import java.util.Date;

public class WebRequestAdapter<T> extends BaseAdapter {

	public static final java.lang.String TAG = "WebRequestAdapter";

	public Activity ownerActivity;
	private JSONArray items ;

	public WebService service;


	public class ViewHolder {
		public String id = "";
		public TextView title;
		public ImageView icon;
	}


		@Override
	public int getCount() {
		return null== items ? 0 : items.length();
	}

	@Override
	public Object getItem(int position) {
		try {
			return null== items ? null : items.get(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//@step
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private final static int indexOfId = 0;
	private final static int indexOfTitle = 1;
	private final static int indexOfImage = 2;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// @step
 		View rowView = convertView;
		// @step
		
		LayoutInflater inflater = this.ownerActivity.getLayoutInflater();

		ViewHolder holder = null;

		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_row, null);
			holder = new ViewHolder();
			rowView.setTag(holder);
		}

		holder = (ViewHolder) rowView.getTag();
		holder.title = (TextView) rowView.findViewById(R.id.text_title);
		holder.icon = (ImageView) rowView.findViewById(R.id.icon_left);

		// @step
		JSONObject  record = (JSONObject) getItem(position);
		Tracer.json(record);

		try {

			String buf = JSONHelper.toPairString(record);
			holder.title.setText(buf);
		
		} catch (Exception e) {

			 Tracer.e(e,"Error");
		}
		// holder.icon.setBackgroundResource(this.getResourceIdByIndex(position));
		holder.id = "" + position ;

		return rowView;
	}

	public void loadData(IDataProvider provider) {
		// @step
		if (null != provider) {
			provider.loadData(onSuccess, onError);
		}

	}

	private ExtDate timer = new ExtDate();
	public long elapsedTime(){
		return timer.durationMilliSecond();
	}

	public void loadData(JSONObject item){
		timer.from = new Date();
		Object service = RefectionEngine.invoke(item,this.onSuccess,this.onError);


		this.service = (WebService) service;
	}

	private void setData(JSONArray  list){
		items = list;

		this.notifyDataSetChanged();
	}
	/*
	 *
	 *
	 */
	protected Response.Listener<String> onSuccess = new Response.Listener<String>() {

		@Override
		public void onResponse(String response) {
			timer.to = new Date();
			 Tracer.t(TAG).d("onSuccess",response);
			try {
				JSONArray list = JSONHelper.safeJSONArry(response);

				setData(list);

			} catch (Exception e) {

				Tracer.e(e,"onSuccess");

			}
		}
	};

	protected Response.ErrorListener onError = new Response.ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			timer.to = new Date();
			Tracer.t(TAG).d("onError",error);
 			//@step
			String msg =  error.getLocalizedMessage();
			try {

				msg = StringHelper.fromatAsJsonString("error",msg);
				JSONArray list = JSONHelper.safeJSONArry(msg);

				setData(list);

			}catch (Exception e){
				Tracer.t(TAG).e(e,"error while wrap json");
			}
		}
	};
}
