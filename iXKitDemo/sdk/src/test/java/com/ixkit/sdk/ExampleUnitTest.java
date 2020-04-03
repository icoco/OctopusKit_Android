package com.ixkit.sdk;


import com.google.common.truth.Truth;
import com.ixkit.octopus.event.WebEvent;
import com.ixkit.octopus.response.JsonBucket;
import com.ixkit.sdk.service.AppService;
import com.ixkit.sdk.service.BaseUnitTestCase;
import com.ixkit.sdk.util.TestHelper;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowLog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by icoco on 08/06/2018.
 */

@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest  {

    //@Before
    public void setup() {

        ShadowLog.stream = System.out;

        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d(this.toString(), "setup" );

        String url = "http://www.ixkit.com/api/";
        ServiceConfig.setup(url, RuntimeEnvironment.application);

    }

    @After
    public void tearDown() {

    }

    @Test
    public void usage() throws Exception{
        setup();
        testList();
    }

    public void testList() throws Exception {
        String filter = null;
        int pageIndex = 1;
        //@step
        WebEvent<JsonBucket> event = new WebEvent<JsonBucket>() {
            @Override
            public void onResponse(JsonBucket response) {
                Logger.d("onResponse %s",response);
                String key = "data.total";
                String value = response.getJsonData().pickString(key);
                Logger.d("[%s]=[%s]",key,value);
                Truth.assertThat(value.length() >0 ).isTrue();
                key = "data.list";
                Object list = response.getJsonData().pick(key);
                Logger.d("[%s]=[%s]",key,list);
            }
            @Override
            public void onFailure( Throwable t) {
                Logger.e("onFailure %s",t.getMessage());
            }
        };
        //@step
        AppService.list(filter,pageIndex).execute(event);

        TestHelper.waitResponse();

    }


}
