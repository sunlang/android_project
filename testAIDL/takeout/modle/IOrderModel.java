package com.yunnex.canteen.takeout.modle;

import android.content.Context;

import com.yunnex.canteen.takeout.bean.Order;
import com.yunnex.canteen.takeout.http.response.OrderListResp;
import com.yunnex.canteen.takeout.http.response.OrderSearCondidtionResp;
import com.yunnex.canteen.takeout.http.response.OrderTypeResp;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

/**
 * Created by sungongyan on 2016/1/8.
 * wechat sun379366152
 */
public interface IOrderModel
{

	interface OrderSearchListener
	{

		void onSuccess(OrderListResp resp);

		void onFailed(String message);
	}

	interface OrderDetailListener
	{

		void onSuccess(Order order);
	}


	interface OrderConfirmListener
	{

		void onSuccess();

		void onFailed(String reason);
	}

	interface OrderSendListener
	{

		void onResponse();

	}

	interface OrderCancleListener
	{

		void onResponse();

	}

	interface OrderArrivedListener
	{

		void onResponse();
	}

	/**
	 * 订单列表回调
	 */
	interface OrderListCallback
	{
		void onGetOrderList(OrderListResp response);

		void onGetOrderListNew(OrderListResp response);
	}

	/**
	 * 订单类型回调
	 */
	interface OrderTypeCallback
	{
		void onGetOrderType(OrderTypeResp response);
	}

	/**
	 * 订单搜索条件回调
	 */
	interface OrderSearConditionCallback
	{
		void onGetCondition(OrderSearCondidtionResp response);
	}

	interface OrderDelayListener
	{

		void onSuccess();

		void onFailed(String reason);
	}


	/**
	 * 订单搜索
	 *
	 * @param context
	 * @param key
	 * @param listener
	 * @return
	 */
	VPayUIRequestV2 orderSearch(Context context, int page, String key, String value, OrderSearchListener listener);

	VPayUIRequestV2 orderDetail(Context context, String orderId, OrderDetailListener listener);
	VPayUIRequestV2 orderDetail2(Context context, String orderId, OrderDetailListener listener);

	VPayUIRequestV2 orderConfirm(Context context, String orderId, OrderConfirmListener listener);

	VPayUIRequestV2 orderConfirm2(Context context, String orderId, OrderConfirmListener listener);

	VPayUIRequestV2 orderSend(Context context, String sdcId, String deliverNo, String orderId, OrderSendListener listener);

	VPayUIRequestV2 orderSend2(Context context, String sdcId, String deliverNo, String orderId, OrderSendListener listener);

	VPayUIRequestV2 orderCancle(Context context, String reasonId, String mOrderId, OrderCancleListener listener);

	VPayUIRequestV2 orderCancle2(Context context, String reasonId, String mOrderId, OrderCancleListener listener);

	VPayUIRequestV2 orderArrived(Context context, String mOrderId, OrderArrivedListener listener);

	VPayUIRequestV2 orderArrived2(Context context, String mOrderId, OrderArrivedListener listener);


	VPayUIRequestV2 orderDelay(Context context, String mOrderId, OrderDelayListener listener);

	/**
	 * 新订单列表
	 */
	VPayUIRequestV2 getOrderListNew(Context context, int newOrder, int page, OrderListCallback callback);

	/**
	 * 订单管理列表
	 *
	 * @param context
	 * @param date
	 * @param type
	 * @param status
	 * @param page
	 * @param callback
	 * @return
	 */
	VPayUIRequestV2 getOrderList(Context context, long date, int type, int status, int page, OrderListCallback callback);

	/**
	 * 订单筛选的类型状态值
	 *
	 * @param context
	 * @param callback
	 * @return
	 */
	VPayUIRequestV2 getTypeStatus(Context context, OrderTypeCallback callback);


	/**
	 * 订单搜索条件
	 *
	 * @param context
	 * @param callback
	 * @return
	 */
	VPayUIRequestV2 getSearCondition(Context context, OrderSearConditionCallback callback);


}
