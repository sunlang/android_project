package com.yunnex.canteen.takeout.presenter;

import android.content.Context;

import com.yunnex.canteen.takeout.bean.Time;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.util.List;

/**
 * Created by sungongyan on 2016/4/15.
 * qq 379366152
 */

/**
 * 关于门店业务的总接口
 * 为减少接口爆发，就实现IShopPresenter这一个总接口，只复写与其业务有关的方法
 */
public interface IShopPresenter
{

	/**
	 * 获取门店数据
	 */
	VPayUIRequestV2 getShop(Context context,boolean isShow);

	/**
	 * 展示更改营业状态的dialog
	 *
	 * @param platform，要更改的平台，例如，0 美团，1百度，2饿了么
	 *
	 * 注意，这是错误的mvp使用，与model无关的控制不必交给presenter，
	 * 否则会导致类似V->P->V->P->V这种无用功
	 */
	//	void showDialogChangeStatus(int platform);

	/**
	 * 改变某个平台的营业状态
	 *
	 * @param platform   平台
	 * @param actionType 0 关闭，1 营业
	 */
	VPayUIRequestV2 changeRunningStatus(Context context, int platform, int actionType);


	/**
	 * 获取店铺营业时间
	 *
	 * @param context
	 */
	VPayUIRequestV2 getShopRunningTime(Context context);


	/**
	 * 设置店铺营业时间
	 *
	 * @param context
	 */
	VPayUIRequestV2 setShopRunningTime(Context context, List<Time> mShopTimeList);


	VPayUIRequestV2 getShopDayBill(Context context, String date);

}
