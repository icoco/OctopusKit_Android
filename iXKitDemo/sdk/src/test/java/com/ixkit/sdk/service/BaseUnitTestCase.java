package com.ixkit.sdk.service;

/**
 * Created by icoco on 9/22/15.
 */


import com.ixkit.sdk.ServiceConfig;

import org.junit.After;
import org.junit.Before;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowLog;

import org.robolectric.util.Scheduler;

import com.ixkit.sdk.util.TestHelper;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class BaseUnitTestCase
{

    @Before
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


    //@purpose waitResponse or else test case will be exit immediately
    public void waitResponse() throws Exception {
        TestHelper.waitResponse();
    }
}
