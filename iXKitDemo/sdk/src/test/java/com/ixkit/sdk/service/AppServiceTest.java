package com.ixkit.sdk.service;



import com.google.common.truth.Truth;
import com.ixkit.octopus.event.WebEvent;
import com.ixkit.octopus.response.JsonBucket;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by icoco on 08/06/2018.
 */

@RunWith(RobolectricTestRunner.class)
public class AppServiceTest extends BaseUnitTestCase {

    @Test
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

        this.waitResponse();

    }

    @Test
    public void testDetail() throws Exception {
        final String id = "13";
        //@step
        WebEvent<JsonBucket> event = new WebEvent<JsonBucket>() {
            @Override
            public void onResponse(JsonBucket response) {
                Logger.d("onResponse %s",response);
                String key = "id";
                String value =  response.getJsonData().pickString(key);
                Logger.d("[%s]=[%s]",key,value);
                Truth.assertThat(id.equals(value) ).isTrue();

            }
            @Override
            public void onFailure( Throwable t) {
                Logger.e("onFailure %s",t.getMessage());
            }
        };

        //@step
        // responseClass default is JsonBucket or
        // special with customise class mapping to json data
        AppService.detail(id,JsonBucket.class).execute(event);

        this.waitResponse();

    }
    private String getCurrentTime(){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String result = sdf.format(new Date());
        return result;
    }

    @Test
    public void testCreate() throws Exception {
        final String name = "Android Demo Test@" + getCurrentTime();
        //@step
        WebEvent<JsonBucket> event = new WebEvent<JsonBucket>() {
            @Override
            public void onResponse(JsonBucket response) {
                Logger.d("onResponse %s",response);
                String key = "data.name";
                String value =  response.getJsonData().pickString(key);

                Logger.d("[%s]=[%s]",key,value);
                Truth.assertThat(name.equals(value)).isTrue();

            }
            @Override
            public void onFailure( Throwable t) {
                Logger.e("onFailure %s",t.getMessage());
            }
        };

        //@step
        // responseClass default is JsonBucket or
        // special with customise class mapping to json data
        AppService.create(name).execute(event);

        this.waitResponse();

    }

    @Test
    public void testDelete() throws Exception {
        final String id = "20";
        //@step
        WebEvent<JsonBucket> event = new WebEvent<JsonBucket>() {
            @Override
            public void onResponse(JsonBucket response) {
                Logger.d("onResponse %s",response);
                String key = "data.id";
                String value =  response.getJsonData().pickString(key);

                Logger.d("[%s]=[%s]",key,value);

                //Truth.assertThat(id.equals(value)).isTrue();

            }
            @Override
            public void onFailure( Throwable t) {
                Logger.e("onFailure %s",t.getMessage());
            }
        };

        //@step
        // responseClass default is JsonBucket or
        // special with customise class mapping to json data
        AppService.delete(id).execute(event);

        this.waitResponse();

    }
}
