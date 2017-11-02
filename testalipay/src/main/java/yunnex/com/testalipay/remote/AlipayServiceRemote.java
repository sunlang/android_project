package yunnex.com.testalipay.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import yunnex.com.testalipay.Diamond;
import yunnex.com.testalipay.IAlipayService;

/**
 * aidl 远程调用的服务service
 */
public class AlipayServiceRemote extends Service
{
	private static final String TAG = AlipayServiceRemote.class.getSimpleName();
	private int mMoney;

	IAlipayService.Stub mAlipayService = new IAlipayService.Stub()
	{
		@Override
		public boolean payMoney(int money) throws RemoteException
		{
			mMoney = money;
			Toast.makeText(AlipayServiceRemote.this, "有人支付了:"+money, Toast.LENGTH_SHORT).show();
			if (money > 0)
			{
				return true;
			}
			return false;
		}

		@Override
		public Diamond getDiamond() throws RemoteException
		{
			return new Diamond(mMoney * 10);
		}

		/**
		 * 有人和该服务绑定时关不掉的
		 * @return
		 * @throws RemoteException
		 */
		@Override
		public boolean stopLoop() throws RemoteException
		{
			return stopService(new Intent(getApplicationContext(), AlipayServiceRemote.class));
		}
	};

	@Override
	public IBinder onBind(Intent intent)
	{
		Toast.makeText(this, "alipay onBind", Toast.LENGTH_SHORT).show();
		return mAlipayService;
	}

	@Override
	public void onCreate()
	{
		Toast.makeText(this, "alipay onCreate", Toast.LENGTH_SHORT).show();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Toast.makeText(this, "alipay onStartCommand", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		Toast.makeText(this, "alipay onDestroy", Toast.LENGTH_SHORT).show();
		Log.d(TAG, TAG + " alipay onDestroy");
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		Toast.makeText(this, "alipay onUnbind", Toast.LENGTH_SHORT).show();
		return super.onUnbind(intent);
	}
}
