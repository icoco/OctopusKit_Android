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


import com.ixkit.app.R;
import com.ixkit.app.util.JSONHelper;
import com.ixkit.app.util.Tracer;


public class JsonDataAdapter<T> extends BaseAdapter {

	public static final java.lang.String TAG = "JsonDataAdapter";

	public Activity ownerActivity;
	private JSONArray items ;


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

		
		try {

			String buf = JSONHelper.toPairString(record);
			holder.title.setText(buf);
		
		} catch (Exception e) {
			Tracer.e(e,"");
		}
		// holder.icon.setBackgroundResource(this.getResourceIdByIndex(position));
		holder.id = "" + position ;

		return rowView;
	}

	public void loadData() {


	}

	public void setData(JSONArray  list){
		items = list;
		
		this.notifyDataSetChanged();
	}


}
