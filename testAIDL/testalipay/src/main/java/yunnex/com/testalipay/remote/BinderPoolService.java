package yunnex.com.testalipay.remote;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import yunnex.com.testalipay.IAlipayService;
import yunnex.com.testalipay.IBinderPoolService;
import yunnex.com.testalipay.IWxService;
import yunnex.com.testalipay.MainActivity;
import yunnex.com.testalipay.R;


public class BinderPoolService extends Service
{
	//连接池管理的aidl对象
	private IAlipayService mAlipayService;
	private IWxService     mWxService;

//	private Binder mBinderPool = new IBinderPoolService.Stub()
//	{
//
//		@Override
//		public IBinder queryBinder(int binderId) throws RemoteException
//		{
//			IBinder binder = null;
//
//			switch (binderId)
//			{
//				case 1:
//					if (mAlipayService == null)
//					{
//						mAlipayService = new AlipayServiceImpl();
//					}
//					binder = (IBinder) mAlipayService;
//					break;
//				case 2:
//					if (mWxService == null)
//					{
//						mWxService = new WxServiceImpl();
//					}
//					binder = (IBinder) mWxService;
//					break;
//			}
//
//			Log.e("binderPool", "server BinderPoolService queryBinder:" + binder);
//			return binder;
//		}
//	};

	@Override
	public void onCreate()
	{
		Toast.makeText(this, "BinderPoolService onCreat", Toast.LENGTH_SHORT).show();
		super.onCreate();
//		createNotification();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Toast.makeText(this, "BinderPoolService onStartCommand", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		Toast.makeText(this, "BinderPoolService onBind", Toast.LENGTH_SHORT).show();
//		return mBinderPool;
		return null;
	}

	private void createNotification()
	{
		//        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
		Notification notification = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher)    //
				// 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmapicon)
				.setTicker("")// 设置在statusbar上显示的提示文字
				.setContentTitle(getResources().getString(R.string.app_name))// 设置在下拉statusbar后Activity
				.setContentText(getResources().getString(R.string.app_name))// TextView中显示的详细内容
				.setContentIntent(pendingIntent2) // 关联PendingIntent
				.getNotification(); // 需要注意build()是在API level16及之后增加的，在API11中可以使用getNotificatin()来代替
		notification.flags |= Notification.FLAG_INSISTENT;
		//        mNotificationManager.notify(NOTIFICATION_ID, notification);
		startForeground(1, notification);
	}

	@Override
	public void onDestroy()
	{
		Toast.makeText(this, "BinderPoolService onDestroy", Toast.LENGTH_SHORT).show();

		Intent intent = new Intent();
		intent.setAction("BinderPoolServiceBeKilled");
		sendBroadcast(intent);
		super.onDestroy();
	}
}
