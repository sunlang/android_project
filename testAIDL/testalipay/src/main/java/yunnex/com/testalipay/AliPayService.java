package yunnex.com.testalipay;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 非远程调用服务
 */
public class AliPayService extends Service
{
	public static final String TAG = AliPayService.class.getSimpleName();

	private class MyBinder extends Binder implements Iservice
	{
		@Override
		public boolean callMethodInService(int money)
		{
			return methodInservice(money);
		}
	}

	/**
	 * 服务中的方法
	 */
	private boolean methodInservice(int money)
	{
		Log.d(TAG, "4 小秘书调用服务中的方法...");
		Log.d(TAG, "5 小秘书检查条件");
		if (money > 3000)
		{
			Log.d(TAG, "6 条件符合，服务中的方法被调用了...");
			return true;
		}
		else
		{
			Log.d(TAG, "6 钱太少了，多来点钱吧");
			return false;
		}
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		/**
		 * 服务只要先启动后绑定，服务就会一直存活而与调用者无关
		 * 当再次绑定时，new MyBinder()返回的依然是服务中原来已经存在的小秘书
		 */
		MyBinder myBinder = new MyBinder();
		Log.d(TAG, "3 服务被绑定了..." + myBinder);
		return myBinder;
	}


	@Override
	public void onCreate()
	{
		Log.d(TAG, "1 服务被创建了...");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.d(TAG, "2 服务启动了...");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		Log.d(TAG, "服务被销毁了...");
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		Log.d(TAG, "服务被解绑了...");
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent)
	{
		Log.d(TAG, "服务被重新綁定了...");
		super.onRebind(intent);
	}
}
