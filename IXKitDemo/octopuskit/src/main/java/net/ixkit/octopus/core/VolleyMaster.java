/**
 * VolleyMaster provide HTTP request feature based on Volley
 * <p/>
 *
 * For more information, visit the project page:
 * https://github.com/icoco/ixkit
 *
 * @author Robin Cheung <iRobinCheung@hotmail.com>
 * @version 1.0.1
 */
package net.ixkit.octopus.core;

import android.app.ActivityManager;
import android.content.Context;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.util.concurrent.Executors;


public class VolleyMaster {
	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;

	public  static Context mContext;
	
	private VolleyMaster() {
		// no instances
	}

	static void init(Context context) {
		if (null != mRequestQueue) {
			return ;
			//throw new IllegalStateException("RequestQueue has been initialized");

		}
		 mContext = context;
		mRequestQueue = Volley.newRequestQueue(context);

		int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		int cacheSize = 1024 * 1024 * memClass / 8;
		// @ mImageLoader = new ImageLoader(mRequestQueue, new
		// BitmapLruCache(cacheSize));
	}

	public static RequestQueue getMainRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} 
		synchronized (DataManager.class) {
			if (null != mContext){
				mRequestQueue = Volley.newRequestQueue(mContext);

				return mRequestQueue;
			}
			throw new IllegalStateException("RequestQueue not initialized");
		}
		 
	}

	private static RequestQueue getTestQequestQueue(Context context){
		HttpStack stack = new HurlStack();
//        HttpStack stack = new HttpClientStack(new DefaultHttpClient());

		Network network = new BasicNetwork(stack);

		ResponseDelivery responseDelivery = new ExecutorDelivery(Executors.newSingleThreadExecutor());

		RequestQueue queue = new RequestQueue(new NoCache(), network, 4, responseDelivery);

		queue.start();
		return queue;
	}

	public static RequestQueue newRequestQueueForTest(final Context context) {
		final File cacheDir = new File(context.getCacheDir(), "volley");
		HttpStack stack = new HurlStack();
		final Network network = new BasicNetwork(stack);

		final ResponseDelivery responseDelivery = new ExecutorDelivery(Executors.newSingleThreadExecutor());

		final RequestQueue queue =
				new RequestQueue(
						new DiskBasedCache(cacheDir),
						network,
						4,
						responseDelivery);

		queue.start();

		return queue;
	}

	/**
	 * Returns instance of ImageLoader initialized with {@see FakeImageCache}
	 * which effectively means that no memory caching is used. This is useful
	 * for images that you know that will be show only once.
	 * 
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}
}
