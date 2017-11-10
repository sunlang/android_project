package com.yunnex.canteen.takeout.presenter;

import android.content.Context;

import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

/**
 * Created by sungongyan on 2016/4/15.
 * qq 379366152
 */

public interface IOrderPresenter
{
	VPayUIRequestV2 getOrderListNew(Context context, int orderNew, int page);

	VPayUIRequestV2 getOrderList(Context context, long date, int type, int status, int page);


	VPayUIRequestV2 orderDetail(Context context, String orderId);
	VPayUIRequestV2 orderDetail2(Context context, String orderId);

	VPayUIRequestV2 orderSearch(Context context, String key);

	VPayUIRequestV2 orderConfirm(Context context, String orderId);
	VPayUIRequestV2 orderConfirm2(Context context, String orderId);

	VPayUIRequestV2 orderSend(Context context, String sdcId, String deliverNo, String orderId);
	VPayUIRequestV2 orderSend2(Context context, String sdcId, String deliverNo, String orderId);

	VPayUIRequestV2 orderCancle(Context context, String reasonId, String mOrderId);
	VPayUIRequestV2 orderCancle2(Context context, String reasonId, String mOrderId);

	VPayUIRequestV2 orderArrived(Context context, String mOrderId);
	VPayUIRequestV2 orderArrived2(Context context, String mOrderId);

	VPayUIRequestV2 orderDelay(Context context, String mOrderId);


}
