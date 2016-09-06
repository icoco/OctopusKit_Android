package com.ixkit.app.service.test;

/**
 * Created by icoco on 9/22/15.
 */


import com.ixkit.app.service.ServiceConfig;

import org.junit.After;
import org.junit.Before;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.util.Scheduler;


public class BaseUnitTestCase
{

    @Before
    public void setup() {
        ShadowLog.stream = System.out;

        ServiceConfig.setup(RuntimeEnvironment.application);

    }

    @After
    public void tearDown() {

    }

    public void testGetCategories() throws Exception {
        //调用请求方法后 volley 会开启后台线程去做真正的请求， 请求完毕后会在主线程上
        //调用Listener.onResponse方法通知请求完毕
        //但是主线程是一个有Handler的线程，Robolectric框架让主线程不轮询消息队列
        //必须在测试方法里主动驱动主线程轮询消息队列，针对消息进行处理
        //否则永远不会在UI线程上通知请求完毕

//        CategoryService service = CategoryService.getCategories("");
//
//        Response.Listener<String> onSuccess = new Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Log.d(TAG, "onSuccess->" + s);
//                assertThat(s).isNotEmpty();
//
//            }
//        };
//
//        Response.ErrorListener onError = new ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.d(TAG, "onError->" + volleyError);
//            }
//        };
//
//        service.execute(onSuccess, onError);

        //获取主线程的消息队列的调度者，通过它可以知道消息队列的情况
        //并驱动主线程主动轮询消息队列
        Scheduler scheduler = Robolectric.getForegroundThreadScheduler();
        //因为调用请求方法后 后台线程请求需要一段时间才能请求完毕，然后才会通知主线程
        // 所以在这里进行等待，直到消息队列里存在消息
        while (scheduler.size() == 0) {
            Thread.sleep(500);
        }
        //轮询消息队列，这样就会在主线程进行通知
        scheduler.runOneTask();

    }

    //@purpose waitResponse or else test case will be exit immediately
    public void waitResponse() throws Exception {
        //调用请求方法后 volley 会开启后台线程去做真正的请求， 请求完毕后会在主线程上
        //调用Listener.onResponse方法通知请求完毕
        //但是主线程是一个有Handler的线程，Robolectric框架让主线程不轮询消息队列
        //必须在测试方法里主动驱动主线程轮询消息队列，针对消息进行处理
        //否则永远不会在UI线程上通知请求完毕

        //@step
        // service.execute(onSuccess, onError);

        //获取主线程的消息队列的调度者，通过它可以知道消息队列的情况
        //并驱动主线程主动轮询消息队列
        Scheduler scheduler = Robolectric.getForegroundThreadScheduler();
        //因为调用请求方法后 后台线程请求需要一段时间才能请求完毕，然后才会通知主线程
        // 所以在这里进行等待，直到消息队列里存在消息
        while (scheduler.size() == 0) {
            Thread.sleep(500);
        }
        //轮询消息队列，这样就会在主线程进行通知
        scheduler.runOneTask();
    }
}
