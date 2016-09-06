package com.ixkit.app.base;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

public class BaseActivity extends Activity {

    public TextView findTextView(int id){
        return (TextView) this
                .findViewById(id);
    }

    private String getIntentStringValue(String name){
        Intent intent = getIntent();
        return intent.getStringExtra(name);
    }
}
