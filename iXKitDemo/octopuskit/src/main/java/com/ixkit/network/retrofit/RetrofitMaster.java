package com.ixkit.network.retrofit;

import com.ixkit.octopuskit.BuildConfig;
import com.orhanobut.logger.Logger;

import net.retrofit.cookie.PersistentCookieJar;
import net.retrofit.cookie.cache.SetCookieCache;
import net.retrofit.cookie.persistence.SharedPrefsCookiePersistor;
import net.retrofit.intercepter.FilterFastRequestInterceptor;
import com.ixkit.octopus.util.DeviceUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import android.content.Context;
import net.retrofit.intercepter.LogIntercepter;

public class RetrofitMaster  {
    //缓存的默认大小 5M 根据需要修改
    private static final int DEFAULT_CACHE_SIZE = 1024 * 1024 * 5;

    //缓存的默认文件夹
    private static final String DEFAULT_CACHE_FILE = "okhttp_ixkit_cache";

    private volatile static Retrofit retrofit = null;
    private static RetrofitMaster master = new RetrofitMaster() ;

    protected Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
    protected OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
    private String baseUrl;
    private HashMap headers;

    public static RetrofitMaster getInstance(){
        if (null == master.retrofitBuilder){
//            master.retrofitBuilder = new Retrofit.Builder();
//            String baseUrl =  WebServiceConfig.getInstance().getApiRootUrl();
//            master.setup(baseUrl,null);
        }
        return master;
    }


    private PersistentCookieJar _cookieJar;

    private void setCookie(CookieJar jar){
        _cookieJar = (PersistentCookieJar)jar;
    }
    public void cleanCache(){

        if (null != _cookieJar){
            _cookieJar.clear();
        }
        clearHeader();
    }

    public void setup(String baseUrl,Context ctx) {
        Logger.d("setup %s, %s",baseUrl, ctx);
        this.baseUrl = baseUrl;


        retrofitBuilder = new Retrofit.Builder();



        OkHttpClient client = getOKClient(ctx);// getClient();
        this.setCookie(client.cookieJar());

        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
//                        .setLenient()
//                        .create()
//                ))
                //  .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //.client(httpBuilder.build())
                .client(client)
                .baseUrl(baseUrl);


    }

    public void addHeader(String key, String value){
        if (null == headers){
            headers = new HashMap();
        }
        headers.put(key,value);
        HttpLoggingInterceptor.Logger.DEFAULT.log("addHeader:" +key + "=" + value);
    }

    public void removeHeader(String key){
        if (null == headers){return;}
        headers.remove(key);
    }
    private void clearHeader(){
        if (null == headers){
            return ;
        }
        headers.clear();
    }
    public void packHeaders(Request.Builder builder){
        if (null == headers){return;}


        Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String name = entry.getKey();
            String value = entry.getValue();
            builder.addHeader(name,value);

        }

        Logger.d("packHeaders->%s" , headers);
    }

    private Request getRequest(Request original){
        final String agent = DeviceUtils.getDefaultUserAgent() + ";" + "Android ixkit 1.0";
        String accept =  "application/json, text/javascript, */*; q=0.01";
        Request.Builder builder = original.newBuilder()
                .header("User-Agent",agent )
                .header("Accept", accept)
                .method(original.method(), original.body());

        //@step
        packHeaders(builder);

        Request request = builder.build();
        return  request;
    }


         /**
     * 初始化 okhttpClient
     *
     * @param context
     */
    private OkHttpClient getOKClient(Context context) {
        SharedPrefsCookiePersistor  cookiePersistor = new SharedPrefsCookiePersistor(context);
        PersistentCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), cookiePersistor);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                //支持自动持久化cookie和自动添加cookie
                .cookieJar(cookieJar)
                //错误重连
                .retryOnConnectionFailure(true)
                //没有网络，加载缓存(仅限GET)
             //   .addInterceptor(new ForceCacheInterceptor(context))
                //添加请求头(用时注意打开)
                .addInterceptor(getHeaderInterceptor())
                //过滤频繁请求，5s为缓存时间，单位秒,5秒之内反复请求，取缓存，超出5秒，取服务器数据
                .addNetworkInterceptor(new FilterFastRequestInterceptor(5))
                //缓存
                .cache(new Cache(new File(context.getCacheDir(), DEFAULT_CACHE_FILE), DEFAULT_CACHE_SIZE));
        //打印请求日志
        if (BuildConfig.DEBUG) {
             builder.addInterceptor(new LogIntercepter());
        }
        OkHttpClient client = builder.build();


        return client;
    }

    public Interceptor getHeaderInterceptor(){
        final String agent = DeviceUtils.getDefaultUserAgent() + ";" + "Android ixkit 1.0";
        Interceptor result = new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                String accept =  "application/json, text/javascript, */*; q=0.01";

                Request.Builder builder = original.newBuilder()
                        .header("User-Agent",agent )

                        .header("Accept", accept)
                        .method(original.method(), original.body());

                //@step
                packHeaders(builder);

                Request request = builder.build();

                return chain.proceed(request);
            }
        };
        return result;
    }

    private OkHttpClient getClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        final String agent = DeviceUtils.getDefaultUserAgent() + ";" + "Android ixkit 1.0";

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                String accept =  "application/json, text/javascript, */*; q=0.01";

                Request.Builder builder = original.newBuilder()
                        .header("User-Agent",agent )

                        .header("Accept", accept)
                        .method(original.method(), original.body());

                //@step
                packHeaders(builder);

                Request request = builder.build();

                return chain.proceed(request);
            }
        });

        builder.addInterceptor(getLoggerInterceptor());

        OkHttpClient client = builder.build();

        return client;
    }



    public void _reset(){
        master.retrofitBuilder = null;
    }
    /**
     * 构建retroft
     *
     * @return Retrofit对象
     */

    public Retrofit getRetrofit() {
        if (retrofit == null) {
            //锁定代码块
            synchronized (RetrofitMaster.class) {
                if (retrofit == null) {
                    retrofit = retrofitBuilder.build(); //创建retrofit对象
                }
            }
        }

        return retrofit;

    }



    public OkHttpClient.Builder setInterceptor(Interceptor interceptor) {
        return httpBuilder.addInterceptor(interceptor);
    }


    public Retrofit.Builder setConverterFactory(Converter.Factory factory) {
        return retrofitBuilder.addConverterFactory(factory);
    }

    /**
     * 日志拦截器
     * 将你访问的接口信息
     *
     * @return 拦截器
     */
    public HttpLoggingInterceptor getLoggerInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BASIC;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {

                Logger.d("RetrofitMaster->%s",message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}
