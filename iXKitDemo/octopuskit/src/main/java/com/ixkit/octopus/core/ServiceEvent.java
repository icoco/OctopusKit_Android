/**
 * ServiceEvent wrap the Service Execute events
 * <p/>
 *
 * For more information, visit the project page:
 * https://github.com/icoco/ixkit
 *
 * @author Robin Cheung <iRobinCheung@hotmail.com>
 * @version 1.0.1
 */
package com.ixkit.octopus.core;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ServiceEvent<T> implements Event<T> {
	public static final String TAG = "ServiceEvent";
	@Override
	public Object beforeSend(T sender) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object dataFilter(T response) {
	 
		return response;
	}

	@Override
	public Object success(T response) {

		return response;
	}

	@Override
	public Exception error(Exception error) {
		 
		return error;
	}

	@Override
	public Object complete(T response) {
		 
		return response;
	}
	
	public static <T> Callback<?> wrapListener(
            final Event listener, final Callback<?> callback) {
		Callback<?> result = new Callback<T>() {
			private Event<T> mEvent = listener;
			private final Callback<T> mListener = (Callback<T>) callback;

			@Override
			public void onResponse(Call<T> call, Response<T> response) {
				Log.d(TAG,"onResponse->"+ response);
				Object filterResponse = this.mEvent.dataFilter((T)response);
				Object successResponse = this.mEvent.success((T) filterResponse);
				this.deliverResponse(call, (Response<T>)successResponse);
				this.mEvent.complete((T) successResponse);
			}

			@Override
			public void onFailure(Call<T> call,
						   Throwable t){
				Exception ex = new Exception(t);
				Exception exception = this.mEvent.error(ex);
			    this.deliverError(call,t);
				this.mEvent.complete((T) exception);

			}


			private void deliverResponse(Call<T> call,Response<T> filterResponse) {

				mListener.onResponse(call, filterResponse);

			}
			private void deliverError(Call<T> call,Throwable t) {
				mListener.onFailure(call,t);
			}
		};
		return result;
	}
//	public static ErrorListener wrapErrorListener(
//			final Event listener, final ErrorListener  onError) {
//		ErrorListener result = new ErrorListener () {
//			private Event   mEvent = listener;
//			private final ErrorListener  mListener = (ErrorListener ) onError;
//
//			@Override
//			 public void onErrorResponse(VolleyError error) {
//
//				Exception exception = this.mEvent.error(error);
//
//				this.deliverError((VolleyError)exception);
//				this.mEvent.complete( exception);
//			}
//
//			private void deliverError(VolleyError error) {
//				mListener.onErrorResponse( error);
//			}
//		};
//		return result;
//	}
}
