package com.yunnex.canteen.takeout.presenter;

import android.content.Context;

import com.yunnex.canteen.takeout.http.response.DayBillResponse;
import com.yunnex.canteen.takeout.modle.IShopModel;
import com.yunnex.canteen.takeout.modle.ShopModleImpl;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

/**
 * Created by sungongyan on 2016/4/20.
 * qq 379366152
 */
public class ShopDayBillPresenter extends BaseShopPresenter
{
	public interface IShopDayBillView
	{
		void fillData(DayBillResponse.DayBillResponseDeail response);
	}

	private IShopDayBillView mShopDayBillView;
	private IShopModel       mShopModel;

	public ShopDayBillPresenter(IShopDayBillView shopDayBillView)
	{
		mShopDayBillView = shopDayBillView;
		mShopModel = new ShopModleImpl();
	}

	@Override
	public VPayUIRequestV2 getShopDayBill(Context context, String date)
	{
		return mShopModel.getShopDayBill(context, date, new IShopModel.DayBillCallback()
		{
			@Override
			public void onGetDayBill(DayBillResponse.DayBillResponseDeail response)
			{
				mShopDayBillView.fillData(response);
			}
		});
	}
}
