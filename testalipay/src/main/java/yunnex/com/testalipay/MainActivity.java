package yunnex.com.testalipay;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import yunnex.com.testalipay.remote.AlipayServiceRemote;
import yunnex.com.testalipay.remote.BinderPoolService;

public class MainActivity extends AppCompatActivity
{
	private Myconn   mConn;
	private Iservice mBinder;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//		start(null);

		//		测试自启动阿里支付服务
		//		startService(new Intent(this, AlipayServiceRemote.class));
		//		测试自启动连接池服务
		startService(new Intent(this, BinderPoolService.class));
	}

	public void start(View v)
	{
		Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
		startService(new Intent(getApplicationContext(), AliPayService.class));
	}

	public void bind(View v)
	{
		if (mConn == null)
		{
			mConn = new Myconn();
		}
		boolean b = bindService(new Intent(getApplicationContext(), AliPayService.class)//
				, mConn//
				, Context.BIND_AUTO_CREATE);
		if (b)
		{
			Toast.makeText(this, "服务绑定成功", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(this, "服务绑定失败", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * conn为null，或者conn已经断开或者没连接上，都会有异常
	 *
	 * @param view
	 */
	public void unbind(View view)
	{
		if (mConn == null)
		{
			Toast.makeText(this, "已经解绑啦", Toast.LENGTH_SHORT).show();
			return;
		}
		unbindService(mConn);
		mConn = null;
		mBinder = null;
		Toast.makeText(this, "解綁成功", Toast.LENGTH_SHORT).show();
	}

	public void call(View view)
	{
		if (mBinder == null)
		{
			Toast.makeText(this, "您断开连接啦,小秘书不能为您服务了", Toast.LENGTH_SHORT).show();
			return;
		}
		mBinder.callMethodInService(5000);
	}

	/**
	 * 1 采用aidl 其他应用调用自己暴露的stopSrvice要先解除绑定
	 * 2 应用自己自己其他组件stopService也可以
	 * 3 service自杀也可行(灭屏stop，事先也得解除绑定)
	 *
	 * @param view
	 */
	public void stopAlipayServie(View view)
	{
		boolean b = stopService(new Intent(getApplicationContext(), AlipayServiceRemote.class));
		Toast.makeText(this, "stopAlipayServie:" + b, Toast.LENGTH_SHORT).show();
	}

	private class Myconn implements ServiceConnection
	{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			mBinder = (Iservice) service;
			Log.d(AliPayService.TAG, "服务连接了...返回的小秘书是:" + mBinder);
		}

		/**
		 * 該方法什么时候回调？
		 * 类ServiceConnection中的onServiceDisconnected()方法在正常情况下是不被调用的，
		 * 它的调用时机是当Service服务被异外销毁时，例如内存的资源不足时
		 * This is called when the connection with the service
		 * has been unexpectedly disconnected -- that is,
		 * its process crashed. Because it is running in our same process,
		 * we should never see this happen.
		 *
		 * @param name
		 */
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			Log.d(AliPayService.TAG, "服务异常断开连接了...");
			mConn = null;
			mBinder = null;
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		Log.d(AliPayService.TAG, "activity onDestroy");
		super.onDestroy();
	}
}
