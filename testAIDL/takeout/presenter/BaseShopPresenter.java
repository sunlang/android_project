package com.yunnex.canteen.takeout.presenter;

import android.content.Context;

import com.yunnex.canteen.takeout.bean.Time;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.util.List;

/**
 * Created by sungongyan on 2016/4/15.
 * qq 379366152
 */
public class BaseShopPresenter implements IShopPresenter
{
	@Override
	public VPayUIRequestV2 getShop(Context context,boolean isShow)
	{
		return null;
	}

	@Override
	public VPayUIRequestV2 changeRunningStatus(Context context, int platform, int actionType)
	{
		return null;
	}

	@Override
	public VPayUIRequestV2 getShopRunningTime(Context context)
	{
		return null;
	}

	@Override
	public VPayUIRequestV2 setShopRunningTime(Context context, List<Time> mShopTimeList)
	{
		return null;
	}

	@Override
	public VPayUIRequestV2 getShopDayBill(Context context, String date)
	{
		return null;
	}
}
