package com.yunnex.canteen.takeout.presenter;

import android.content.Context;

import com.yunnex.canteen.takeout.bean.Shop;
import com.yunnex.canteen.takeout.modle.IShopModel;
import com.yunnex.canteen.takeout.modle.ShopModleImpl;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

/**
 * Created by sungongyan on 2016/4/15.
 * qq 379366152
 * <p>
 */
public class ShopInfoPresenter extends BaseShopPresenter
{
	public interface IShopView
	{
		void fillData(Shop shop);
	}

	private final IShopModel mShopModel;
	private final IShopView  mIshopView;

	public ShopInfoPresenter(IShopView ishopView)
	{
		mIshopView = ishopView;
		mShopModel = new ShopModleImpl();
	}

	@Override
	public VPayUIRequestV2 getShop(Context context,boolean isShow)
	{
		return mShopModel.getShop(context, isShow,new IShopModel.OnGetShopLisener()
		{
			@Override
			public void onSuccess(Shop shop)
			{
				mIshopView.fillData(shop);
			}
		});
	}
}
