package com.ixkit.app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ixkit.app.base.IndexPath;
import com.ixkit.app.model.ApiModel;
import com.ixkit.app.R;

import com.ixkit.app.util.ActivityHelper;
import com.ixkit.app.Resource;
import com.ixkit.app.util.JSONHelper;
import com.ixkit.app.util.Tracer;

public class GroupListAdapter extends BaseExpandableListAdapter {

	private final Context mContext;
	private final LayoutInflater mLayoutInflater;

	private JSONArray getGroups(){
		return ApiModel.getInstance().getModules();
	}


	private JSONArray getSubItems(int section){
		JSONArray  items = this.getGroups();
		if (null == items ) return null;
		JSONArray subItems = null;
		try {
			JSONObject item = items.getJSONObject(section);
			subItems =  item.getJSONArray("apis");

		}catch(Exception e){
			Tracer.e(e, "Failed getSubItems");
		}

		return subItems ;
	}

	private JSONObject wrapModuleInfo(JSONObject item, int section){
		try {
			JSONObject module = (JSONObject) this.getGroup(section);
			String name = JSONHelper.getString(module, "title");

			item.put("groupName", name);
		}catch (Exception e){
			Tracer.e(e,"wrapModuleInfo",item,section);
		}
		return item;
	}

	private JSONObject getSubItemByIndexPath(int section, int row){
		JSONObject result = null;

		JSONArray subItems = this.getSubItems(section);

		try {
			result = subItems.getJSONObject(row);

			result = this.wrapModuleInfo(result,section);
		}catch (Exception e){
			Tracer.e(e,"getSubItemByIndexPath");
		}
		return result;
	}

	public GroupListAdapter(Context context) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getGroupCount() {

		return this.getGroups().length();
	}

	@Override
	public Object getGroup(int groupPosition) {
		Object result = null;
		try {
			result = this.getGroups().get(groupPosition);
		}catch (Exception e){
			Tracer.e(e,"getGroup");
		}

		return result;

	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.sample_activity_list_group_item, parent, false);
		}

		//final ImageView image = (ImageView) convertView.findViewById(R.id.sample_activity_list_group_item_image);
		//image.setImageResource(mGroupDrawables[groupPosition]);

		final TextView text = (TextView) convertView.findViewById(R.id.sample_activity_list_group_item_text);

		//@step
		String title = null;
		try {
			JSONObject json = this.getGroups().getJSONObject(groupPosition);
			title = json.getString("title");
		} catch (JSONException e) {
			e.printStackTrace();
		}
 		text.setText(title);

		//@step
		final ImageView expandedImage = (ImageView) convertView.findViewById(R.id.sample_activity_list_group_expanded_image);
		final int resId = isExpanded ? R.drawable.minus : R.drawable.plus;
		expandedImage.setImageResource(resId);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		JSONArray items = this.getSubItems(groupPosition);

		return  null == items ? 0 : items.length();

	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this.getSubItemByIndexPath(groupPosition,childPosition);

	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.sample_activity_list_child_item, parent, false);
		}
		
		final TextView text = (TextView) convertView.findViewById(R.id.sample_activity_list_child_item_text);

		JSONObject item = this.getSubItemByIndexPath(groupPosition,childPosition);

		String title = null;

		try {
			title = item.getString("title");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		text.setText(title);

		//@step
		IndexPath indexPath = new IndexPath(groupPosition,childPosition);
		convertView.setTag(indexPath);
		this.attachedLisenter(convertView);

		return convertView;
	}

	private void attachedLisenter(View view){
		final GroupListAdapter adapter = this;
		view.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						System.out.println("onClick " + v + ",tag:" + v.getTag());
						IndexPath indexPath = (IndexPath)v.getTag();
						adapter.onSelectedRow(indexPath);
					}
				});
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}


	//@step
	private void onSelectedRow (IndexPath indexPath){

		Tracer.d("onSelectedRow",indexPath);

		int section = indexPath.getSection();
		int row = indexPath.getRow();
		JSONObject item = this.getSubItemByIndexPath(section,row);

		Bundle bundle = new Bundle();
		bundle.putString("item",item.toString());
		ActivityHelper.startActivity(this.mContext,Resource.packageActivity("DetailActivity") ,bundle);

	}
}
