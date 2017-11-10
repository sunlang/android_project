package com.yunnex.canteen.takeout.mng;

/**
 * Created by sungongyan on 2017/6/6.
 * qq 379366152
 */

import android.content.Context;
import android.util.Singleton;

import com.yunnex.canteen.takeout.base.Constant;
import com.yunnex.canteen.common.db.dao.CanteenDao;
import com.yunnex.canteen.common.exception.ReportExcepton;
import com.yunnex.canteen.common.utils.GsonUtil;
import com.yunnex.canteen.common.utils.SharePrefUtil;
import com.yunnex.framework.utils.Log;
import com.yunnex.vpay.lib.bugly.VPayBuglyUtils;

/**
 * 钉钉打卡
 */
public class DingdMng
{
	private static final String TAG      = DingdMng.class.getSimpleName();
	private              int    duration = 24;//默认打卡周期
	private int deviation;//误差
	public static String LAST_DING_TIME = "last_ding_time";

	public DingdMng setDuration(int duration)
	{
		this.duration = duration;
		return this;
	}

	public DingdMng setDeviation(int deviation)
	{
		this.deviation = deviation;
		return this;
	}

	/**
	 * @param context 打卡,计算本次打卡距离上次数据重置的时间，或者累计打卡时间(单位:h)
	 */
	public void ding(Context context)
	{
		long hour=60*60*1000;
		long min=60*1000;

		long currentTime = System.currentTimeMillis();
		Long lastTime = SharePrefUtil.getLong(context, LAST_DING_TIME, 0L);
		long deltaTime = (currentTime - lastTime) / 1000 / 3600;
		//				int min = (int) ((currentTime - lastTime) / 1000 / 60);
		Log.d(Constant.TAG,"ding 累计打卡时间:" + deltaTime);
		if (duration - deviation <= deltaTime)
		{
			String data = GsonUtil.Obj2JsonStr(new CanteenDao(context).getUnhandleOrders());
			Log.d(Constant.TAG,"上报数据:" + data);

			String timeStr = "";
			if(deltaTime>hour){
				long remainMin=(deltaTime-deltaTime/hour*3600*1000)/min;
				timeStr=deltaTime/hour+"h,"+remainMin+"min";
			}else if(deltaTime>min){
				timeStr=deltaTime/min+"min";
			}

			//test 47064 ,normal 46972
			VPayBuglyUtils.uploadLog(context,//
					46972,//
					VPayBuglyUtils.D,//
					TAG,//
					new String[]{"data:" + data, "duration time from last report:" + timeStr},//
					new ReportExcepton("unused Exception"));

			SharePrefUtil.saveLong(context, LAST_DING_TIME, currentTime);
			new CanteenDao(context).clear();
		}
	}

	public static DingdMng getInstance()
	{
		return mSingleton.get();
	}

	private static Singleton<DingdMng> mSingleton=new Singleton<DingdMng>()
	{
		@Override
		protected DingdMng create()
		{
			return new DingdMng();
		}
	};
}
