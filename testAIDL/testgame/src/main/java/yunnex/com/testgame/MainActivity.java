package yunnex.com.testgame;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import yunnex.com.testalipay.IAlipayService;
import yunnex.com.testalipay.IBinderPoolService;
import yunnex.com.testalipay.IWxService;

public class MainActivity extends AppCompatActivity
{

	private static final String TAG = "binderPool";
	IBinderPoolService mBinderPool;
	//	private int binderId = 0;//1 zfb,2 wx

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = new Intent();
		//		intent.setAction("com.alibaba.alipay");//使用支付宝支付
		intent.setAction("com.alibaba.alipay.poolservice");//使用连接池
		intent.setPackage("yunnex.com.testalipay");

		Toast.makeText(this, "client onCreat bindService", Toast.LENGTH_SHORT).show();
		//支付服务随用随走，不需他一直存活，因此直接绑定而不调用start

		//如需常驻，先start
//		ComponentName componentName = startService(intent);

		boolean b = bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		Toast.makeText(this, "链接服务:" + b, Toast.LENGTH_SHORT).show();
	}

	public void payWithPool(View view)
	{
		try
		{
			int bindCode = 2;
			if (mBinderPool == null)
			{
				Toast.makeText(this, "mBinderPool is null", Toast.LENGTH_SHORT).show();
				return;
			}
			//			Caused by: java.lang.NullPointerException:
			//			Attempt to invoke interface method
			//			'android.os.IBinder yunnex.com.testalipay.
			//			IBinderPoolService.queryBinder(int)' on a null object reference
			IBinder binder = mBinderPool.queryBinder(bindCode);
			Log.e(TAG, "client payWithPool binder:" + binder);
			switch (bindCode)
			{
				case 1:
					//Caused by: java.lang.ClassCastException:
					//android.os.BinderProxy cannot be cast to
					//yunnex.com.testalipay.IAlipayService
					//((IAlipayService) binder).payMoney(108);
					IAlipayService alipayService = IAlipayService.Stub.asInterface(binder);
					Log.e(TAG, "client payWithPool Service:" + alipayService);
					int money = 102;
					boolean b = alipayService.payMoney(money);
					if (b)
					{
						Toast.makeText(MainActivity.this, "使用支付宝支付成功" + money + "元,得到钻石:" + alipayService.getDiamond().getNum() + "颗", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
					}
					break;

				case 2:
					IWxService wxService = IWxService.Stub.asInterface(binder);
					Log.e(TAG, "client payWithPool Service:" + wxService);
					int money2 = 103;
					boolean b2 = wxService.payMoney(money2);
					if (b2)
					{
						Toast.makeText(MainActivity.this, "使用微信支付成功" + money2 + "元,得到钻石:" + wxService.getDiamond().getNum() + "颗", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
					}
					break;
			}

		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 不使用连接池的
	 *
	 * @param view
	 */
	public void pay(View view)
	{
		//				Log.e(TAG, "client pay Service:" + alipayService);
		//				int money = 108;
		//				boolean b;
		//				try
		//				{
		//					b = alipayService.payMoney(money);
		//					if (b)
		//					{
		//						Toast.makeText(MainActivity.this, "支付成功" + money + "元,得到钻石:" + alipayService.getDiamond().getNum() + "颗", Toast.LENGTH_SHORT).show();
		//					}
		//					else
		//					{
		//						Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
		//					}
		//				}
		//				catch (RemoteException e)
		//				{
		//					e.printStackTrace();
		//				}
	}

	public void close(View view)
	{
		try
		{
			if (mConnection != null)
			{
				Toast.makeText(this, "client unbindService", Toast.LENGTH_SHORT).show();
				unbindService(mConnection);
				mConnection = null;
			}

			/**
			 * 服务自己先启动的，返回true
			 * 服务未自己事先启动，采用绑定的方式开启的，则返回false
			 *
			 * 也就是说，服务自己启动的，别的组件(不分是否同一进程)调用stop能生效
			 * 绑定启动的，unbind就生效了，再stop已经无效了
			 */
			//			Toast.makeText(this, "支付服务关闭" + b, Toast.LENGTH_SHORT).show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	ServiceConnection mConnection = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			Toast.makeText(MainActivity.this, "onServiceConnected", Toast.LENGTH_SHORT).show();
			mBinderPool = IBinderPoolService.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			mBinderPool = null;
			Toast.makeText(MainActivity.this, "onServiceDisconnected", Toast.LENGTH_SHORT).show();
		}
	};

	//	IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient()
	//	{
	//		@Override
	//		public void binderDied()
	//		{
	//			if (alipayService == null)
	//			{
	//				return;
	//			}
	//			alipayService.asBinder().unlinkToDeath(mDeathRecipient, 0);
	//			alipayService = null;
	//			//retry
	//			Intent intent = new Intent();
	//			intent.setAction("com.alibaba.alipay");
	//			intent.setPackage("yunnex.com.testalipay");
	//			//支付服务随用随走，不需他一直存活，因此直接绑定而不调用start
	//			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	//		}
	//	};
}
