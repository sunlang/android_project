package com.yunnex.canteen.takeout.presenter;

import android.content.Context;

import com.yunnex.canteen.takeout.bean.Shop;
import com.yunnex.canteen.takeout.modle.IShopModel;
import com.yunnex.canteen.takeout.modle.ShopModleImpl;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

/**
 * Created by sungongyan on 2016/4/15.
 * qq 379366152
 */
public class ShopRunChangePresenter extends BaseShopPresenter
{

	public interface IShopRunChangeView
	{
		void fillData(Shop shop);
	}

	private final IShopModel         mShopModel;
	private final IShopRunChangeView mShopRunChangeView;

	public ShopRunChangePresenter(IShopRunChangeView ishopView)
	{
		mShopRunChangeView = ishopView;
		mShopModel = new ShopModleImpl();
	}

	@Override
	public VPayUIRequestV2 getShop(Context context,boolean isShow)
	{
		return mShopModel.getShop(context,isShow, new IShopModel.OnGetShopLisener()
		{
			@Override
			public void onSuccess(Shop shop)
			{
				if (mShopRunChangeView != null)
				{
					mShopRunChangeView.fillData(shop);
				}
			}
		});
	}

	@Override
	public VPayUIRequestV2 changeRunningStatus(final Context context, int platform, int actionType)
	{
		return mShopModel.changeRunningStatus(context, platform, actionType, new IShopModel.ChangeRunningStatusListener()
		{
			@Override
			public void onSuccess()
			{
				getShop(context,true);
			}

			@Override
			public void onFailed()
			{

			}
		});
	}
}
