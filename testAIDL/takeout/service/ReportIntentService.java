package com.yunnex.canteen.takeout.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.yunnex.canteen.takeout.base.Constant;
import com.yunnex.framework.utils.Log;
import com.yunnex.canteen.takeout.mng.DingdMng;
import com.yunnex.vpay.lib.bugly.VPayBuglyUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ReportIntentService extends Service
{
	private static final String TAG = ReportIntentService.class.getSimpleName();
	private static Subscription mSubscription;

	public ReportIntentService()
	{
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		handleActionStart(getApplicationContext(), 1);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	/**
	 * 服务启动，初始化参数
	 *
	 * @param context 上下文
	 */
	public static void startAction(Context context)
	{
		Intent intent = new Intent(context, ReportIntentService.class);
		context.startService(intent);
	}

	/**
	 * Handle action in the provided background thread with the provided
	 * parameters.
	 */
	private static void handleActionStart(final Context context, final long period)
	{
		Log.d(Constant.TAG,"时间间隔:" + period + "hours");

		if (mSubscription == null || mSubscription.isUnsubscribed())
		{
			mSubscription = Observable.interval(period, period, TimeUnit.HOURS)//
					.map(new Func1<Long, Long>()
					{
						@Override
						public Long call(Long aLong)
						{
							return aLong * period;
						}
					})//
					.subscribeOn(Schedulers.io())//
					.observeOn(Schedulers.io())//
					.subscribe(new Subscriber<Long>()
					{
						@Override
						public void onCompleted()
						{
						}

						@Override
						public void onError(Throwable e)
						{
							Log.e(Constant.TAG,"onError:"+e);
							VPayBuglyUtils.postCatchedException(e, context);
						}

						@Override
						public void onNext(Long aLong)
						{
							//打卡
							DingdMng.getInstance()//
									.setDuration(24)//
									.setDeviation(0)//
									.ding(context);
						}
					});
		}
	}

	@Override
	public void onDestroy()
	{
		if (mSubscription != null && !mSubscription.isUnsubscribed())
		{
			mSubscription.unsubscribe();
		}
		super.onDestroy();
	}

	public static void stopAction(Context context)
	{
		Intent intent = new Intent(context, ReportIntentService.class);
		context.stopService(intent);
	}


	/**
	 * restar the  task
	 *
	 * @param newPerid
	 */
	public static void handleActionRestar(Context context, long newPerid)
	{
		if (mSubscription != null && !mSubscription.isUnsubscribed())
		{
			mSubscription.unsubscribe();
			mSubscription = null;
		}
		handleActionStart(context, newPerid);
	}
}
