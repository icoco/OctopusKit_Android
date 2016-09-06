package com.ixkit.app.ui;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

import com.ixkit.app.R;
import com.ixkit.app.base.BaseActivity;

import com.ixkit.app.adapter.WebRequestAdapter;
import com.ixkit.app.util.JSONHelper;
import com.ixkit.app.adapter.ParametersAdapter;



import net.ixkit.octopus.core.WebService;
import com.ixkit.app.util.Tracer;


public class DetailActivity extends BaseActivity {
	public static final String TAG = "DetailActivity";

	private PullToRefreshListView listView;
	private WebRequestAdapter adapter;


	private JSONObject getJsonObjectValue(String name){

		JSONObject result = null;
		try {
			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();
			String jsonString =  bundle.getString(name);
			result = new JSONObject(jsonString);
		}catch (Exception e){
			Tracer.e(e,"Failed getJsonObjectValue");
		}
		return result;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail_activity);
		
		this.setupListView();
	}
	
	private void setupListView( ) {

		adapter = new WebRequestAdapter();
				
		listView = (PullToRefreshListView) this
				.findViewById(R.id.pull_to_refresh_listview);

		// OPTIONAL: Disable scrolling when list is refreshing
		// listView.setLockScrollWhileRefreshing(false);

		// OPTIONAL: Uncomment this if you want the Pull to Refresh header to
		// show the 'last updated' time
		// listView.setShowLastUpdatedText(true);

		// OPTIONAL: Uncomment this if you want to override the date/time format
		// of the 'last updated' field
		// listView.setLastUpdatedDateFormat(new
		// SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));

		// OPTIONAL: Uncomment this if you want to override the default strings
		//listView.setTextPullToRefresh("");
		//listView.setTextReleaseToRefresh("");
		//listView.setTextRefreshing("refreshing...");
		listView.setTextColor(Color.BLACK);
		// MANDATORY: Set the onRefreshListener on the list. You could also use
		// listView.setOnRefreshListener(this); and let this Activity
		// implement OnRefreshListener.
		listView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// Your code to refresh the list contents goes here

				// for example:
				// If this is a webservice call, it might be asynchronous so
				// you would have to call listView.onRefreshComplete(); when
				// the webservice returns the data
				
				startRefresh();

		 
			}

		});

		// @step
	 
		adapter.ownerActivity = this;
		try{
			adapter.unregisterDataSetObserver(this.mObserver);
			
		}catch(Exception e){
			//@step e.printStackTrace();
		}finally{
			adapter.registerDataSetObserver(this.mObserver);
		}
		
		listView.setAdapter(adapter);
		
		// Request the adapter to load the data
		// @adapter.loadData();
		this.startRefresh();

		listView.setRefreshing();

		// click listener
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
			}
		});

	}
	
	public void startRefresh() {

		this.findTextView(R.id.label_time).setText("");

		JSONObject item = this.getJsonObjectValue("item");

		this.renderData(item);

		this.adapter.loadData(item);

		 
	}



	private void renderData (JSONObject data){
		String groupName = JSONHelper.getString(data,"groupName");
		String title = JSONHelper.getString(data,"title");

		this.findTextView(R.id.sample_activity_list_group_item_text).setText(groupName);

		this.findTextView(R.id.label_title).setText(title);
		String url = JSONHelper.getString(data,"url");
		this.findTextView(R.id.label_url).setText(url);
		String method = JSONHelper.getString(data,"method");
		this.findTextView(R.id.label_method).setText(method);
        //@step
		ListView paramsView = (ListView)this.findViewById(R.id.listView_params);
		try {
			JSONArray params = data.getJSONArray("parameters");
			ParametersAdapter paramsAdapter = new ParametersAdapter(this, params);
			paramsView.setAdapter(paramsAdapter);
		}catch (Exception e){
			Tracer.e(e,"renderData");
		}
	}

	public void onRefreshDone() {
		// @step
		listView.onRefreshComplete();
		//@step
		if (null != this.adapter.service){
			this.renderWebServiceInfo(this.adapter.service);
		}

	}
	private void renderWebServiceInfo(WebService service){
		String url = service.getUrl();
		this.findTextView(R.id.label_url).setText(url);
		String method = service.getMethod();
		this.findTextView(R.id.label_method).setText(method);

		String time = "Elapsed time:" + this.adapter.elapsedTime() + "ms";
		this.findTextView(R.id.label_time).setText(time);
	}

	DataSetObserver mObserver = new DataSetObserver(){
		public void onChanged(){
			onRefreshDone();
		}
	};
}
