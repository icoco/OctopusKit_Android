package com.ixkit.app.adapter;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;

public class ParametersAdapter extends JsonDataAdapter  {

   public ParametersAdapter(Context context, JSONArray items){
        this.ownerActivity = (Activity) context;
        this.setData(items);
    }
}
