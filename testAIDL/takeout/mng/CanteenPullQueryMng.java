package com.yunnex.canteen.takeout.mng;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.yunnex.framework.utils.Log;
import com.yunnex.vpay.api.inner.ApiVPay;
import com.yunnex.vpay.service.IPullQueryActivity;
import com.yunnex.vpay.service.IPullQueryService;
import com.yunnex.vpay.service.PullQueryResponse;
import com.yunnex.vpay.service.PullQueryResponseParcel;
import com.yunnex.vpay.utils.UtilsVPayPkgName;

/**
 * 作者：wzd on 2015年11月23日 15:21
 * 邮箱：wangzhenduo@yunnex.com
 */
public class CanteenPullQueryMng
{
	private static       CanteenPullQueryMng canteenPullQueryMng  = null;
	private static final String              TAG                  = "CanteenPullQueryMng";
	private static final String              FLAG_APP_NAME        = UtilsVPayPkgName.PACKAGE_SMART_CANTEEN;
	private              IPullQueryService   mPullQueryService    = null;
	private              UpdateOrderListener mUpdateOrderListener = null;
	private              boolean             isBound              = false;

	private CanteenPullQueryMng()
	{
	}

	public static CanteenPullQueryMng getInstance()
	{
		if (canteenPullQueryMng == null)
		{
			synchronized (CanteenPullQueryMng.class)
			{
				if (canteenPullQueryMng == null)
				{
					canteenPullQueryMng = new CanteenPullQueryMng();
				}
			}
		}
		return canteenPullQueryMng;
	}

	public void setmUpdateOrder(UpdateOrderListener mUpdateOrderListener)
	{
		this.mUpdateOrderListener = mUpdateOrderListener;
	}

	public boolean bindService(Context context)
	{
		//		isBound = ApiVPay.bindLooperService(context,mRemoteConnection);
		isBound = ApiVPay.bindLooperServiceNew(context, mRemoteConnection);
		android.util.Log.e(TAG, TAG + " canteen 绑定远程服务:" + isBound);
		return isBound;
	}

	public void unbindService(Context context)
	{
		Log.d(TAG, "canteen unbindService 解除绑定");
		try
		{
			Log.d(TAG, "canteen stopService 停止服务,解除回调！！");
			mPullQueryService.removeCallback(FLAG_APP_NAME, mPullQueryActivity);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
			Log.d(TAG, "canteen stopService 停止服务,解除回调失败");
		}
		catch (NullPointerException e)
		{
			Log.d(TAG, "canteen unbindService 服务为空！");
		}
		try
		{
			if (isBound)
			{
				context.unbindService(mRemoteConnection);
			}
		}
		catch (RuntimeException e)
		{
			Log.w(TAG, "canteen unbindService 异常，没有先绑定！");
		}

	}

	/**
	 * 更新时间戳接口
	 *
	 * @param timeStamp 时间
	 */
	public void updateTimeStamp(long timeStamp)
	{
		if (mPullQueryService != null)
		{
			try
			{
				mPullQueryService.updateTimeStamp(FLAG_APP_NAME, timeStamp);
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 请求轮询
	 */
	public void requestPollQuery()
	{
		if (mPullQueryService != null)
		{
			try
			{
				mPullQueryService.requestPollQuery();
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}
	}

	private IPullQueryActivity.Stub mPullQueryActivity = new IPullQueryActivity.Stub()
	{
		@Override
		public void updateNewOrderState(PullQueryResponseParcel response) throws RemoteException
		{
			if (mUpdateOrderListener != null)
			{
				mUpdateOrderListener.updateNewOrderState(response.getPullQueryResponse());
			}
		}
	};

	public ServiceConnection mRemoteConnection = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			Log.d(TAG, "canteen onServiceConnected");

			mPullQueryService = IPullQueryService.Stub.asInterface(service);
			try
			{
				Log.d(TAG, "mPullQueryService 为" + FLAG_APP_NAME + "注册mPullQueryActivity");
				mPullQueryService.addCallback(FLAG_APP_NAME, mPullQueryActivity);
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
				Log.d(TAG, "mPullQueryService 为" + FLAG_APP_NAME + "注册mPullQueryActivity注册失败");
			}
			catch (NullPointerException e)
			{
				Log.d(TAG, "mPullQueryService 为" + FLAG_APP_NAME + "注册mPullQueryActivity注册失败,服务为空！");
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			Log.d(TAG, "onServiceDisconnected 断开服务");
			mPullQueryService = null;
		}
	};

	public void setNeedLoop(boolean b)
	{
		try
		{
			mPullQueryService.setNeedLoop(b);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	public interface UpdateOrderListener
	{
		/**
		 * 轮询看是否有新订单通知
		 *
		 * @param response 查询结果
		 */
		void updateNewOrderState(PullQueryResponse response);
	}
}
