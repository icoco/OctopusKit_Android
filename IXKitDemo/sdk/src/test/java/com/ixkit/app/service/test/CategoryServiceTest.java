package com.ixkit.app.service.test;

/**
 * Created by icoco on 9/22/15.
 */


import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import com.ixkit.app.service.BuildConfig;
import com.ixkit.app.service.CategoryService;

import org.junit.Test;
import org.junit.runner.RunWith;


import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)

public class CategoryServiceTest extends BaseUnitTestCase {
    public static String TAG = "CategoryServiceTest";

    @Test
    public void testGetCategories() throws Exception {

        CategoryService service = CategoryService.getCategories("");

        Response.Listener<String> onSuccess = new Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d(TAG, "onSuccess->" + s);
                assertThat(s).isNotEmpty();

            }
        };

        Response.ErrorListener onError = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "onError->" + volleyError);
            }
        };

        service.execute(onSuccess, onError);

        this.waitResponse();

    }


}
