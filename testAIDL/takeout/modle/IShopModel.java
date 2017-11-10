package com.yunnex.canteen.takeout.modle;

import android.content.Context;

import com.yunnex.canteen.takeout.bean.Shop;
import com.yunnex.canteen.takeout.bean.Time;
import com.yunnex.canteen.takeout.http.response.DayBillResponse;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.util.List;

/**
 * Created by sungongyan on 2016/1/8.
 * wechat sun379366152
 */
public interface IShopModel
{
	interface OnGetShopLisener
	{
		void onSuccess(Shop shop);
	}

	interface ShopTimeGettingListener
	{
		void onGetTime(List<Time> times);
	}

	interface ShopTimeSettingListener
	{
		void onSetting(boolean b);
	}

	interface ChangeRunningStatusListener
	{
		void onSuccess();

		void onFailed();
	}

	interface DayBillCallback
	{
		void onGetDayBill(DayBillResponse.DayBillResponseDeail response);
	}


	interface NoticePubListener
	{

		void onPubSuccess();

		void onPubFailed();
	}

	interface NoticeGettingListener
	{
		void onGettingNotice(String notice);
	}


	/**
	 * 获取门店数据
	 *
	 * @param context
	 * @param listener
	 */
	VPayUIRequestV2 getShop(Context context,boolean isShow,OnGetShopLisener listener);

	/**
	 * 改变门店营业状态
	 *
	 * @param context
	 * @param platform
	 * @param actionType
	 * @param listener
	 */
	VPayUIRequestV2 changeRunningStatus(Context context, int platform, int actionType,
										 ChangeRunningStatusListener listener);


	/**
	 * 获取店铺营业时间
	 *
	 * @param context
	 * @param listener
	 */
	VPayUIRequestV2 getShopRunningTime(Context context, ShopTimeGettingListener listener);


	/**
	 * 设置店铺营业时间
	 *
	 * @param context
	 * @param listener
	 */
	VPayUIRequestV2 setShopRunningTime(Context context, List<Time> mShopTimeList,
										ShopTimeSettingListener listener);


	/**
	 * 获取门店日结账单
	 *
	 * @param context
	 * @param date
	 * @param callback
	 */
	VPayUIRequestV2 getShopDayBill(Context context, String date, DayBillCallback callback);


	/**
	 * 发布公告,由于该功能已屏蔽，暂不修改了
	 *
	 * @param context
	 * @param notice
	 * @param listener
	 */
	void publishNOtice(Context context, String notice, NoticePubListener listener);

	/**
	 * 获取公告
	 *
	 * @param context
	 * @param listener
	 */
	void getNotice(Context context, NoticeGettingListener listener);
}
