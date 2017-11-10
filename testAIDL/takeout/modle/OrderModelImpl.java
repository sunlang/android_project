package com.yunnex.canteen.takeout.modle;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.yunnex.api.canteen.ApiCanteen;
import com.yunnex.canteen.common.CanteenApplication;
import com.yunnex.canteen.common.CanteenHttpUtil;
import com.yunnex.canteen.common.db.dao.CanteenDao;
import com.yunnex.canteen.common.utils.SharePrefUtil;
import com.yunnex.canteen.common.utils.ToastUtil;
import com.yunnex.canteen.takeout.bean.Order;
import com.yunnex.canteen.takeout.bean.StringConfig;
import com.yunnex.canteen.takeout.http.request.CancelActionRequest;
import com.yunnex.canteen.takeout.http.request.DeliverActionRequest;
import com.yunnex.canteen.takeout.http.request.OrderItemRequest;
import com.yunnex.canteen.takeout.http.request.OrderListReqst;
import com.yunnex.canteen.takeout.http.request.OrderSearchRequest;
import com.yunnex.canteen.takeout.http.response.OrderItemResponse;
import com.yunnex.canteen.takeout.http.response.OrderListResp;
import com.yunnex.canteen.takeout.http.response.OrderSearCondidtionResp;
import com.yunnex.canteen.takeout.http.response.OrderTypeResp;
import com.yunnex.canteen.takeout.mng.DingdMng;
import com.yunnex.canteen.takeout.mng.TakeOutHttpUtils;
import com.yunnex.vpay.lib.http.HttpUtils;
import com.yunnex.vpay.lib.log.output.VLogOutput;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.yunnex.canteen.takeout.base.Constant.urlGetTypeStatus;
import static com.yunnex.canteen.takeout.base.Constant.urlOrderPagelist;
import static com.yunnex.canteen.takeout.base.Constant.urlSearchCondition;
import static com.yunnex.canteen.takeout.base.Constant.urlWmOrderCancel;
import static com.yunnex.canteen.takeout.base.Constant.urlWmOrderCancel2;
import static com.yunnex.canteen.takeout.base.Constant.urlWmOrderDeliverAction;
import static com.yunnex.canteen.takeout.base.Constant.urlWmOrderDeliverAction2;
import static com.yunnex.canteen.takeout.base.Constant.urlWmOrderDetail;
import static com.yunnex.canteen.takeout.base.Constant.urlWmOrderDetail2;
import static com.yunnex.canteen.takeout.base.Constant.urlWmOrderFinishAction;
import static com.yunnex.canteen.takeout.base.Constant.urlWmOrderFinishAction2;
import static com.yunnex.canteen.takeout.base.Constant.urlWmOrderSure;
import static com.yunnex.canteen.takeout.base.Constant.urlWmOrderSure2;

/**
 * Created by sungongyan on 2016/1/8.
 * wechat sun379366152
 */
public class OrderModelImpl implements IOrderModel
{
	private static final String TAG = OrderModelImpl.class.getSimpleName();

	@Override
	public VPayUIRequestV2 orderSearch(Context context, int page, String key, String value, final OrderSearchListener listener)
	{
		final OrderSearchRequest re = new OrderSearchRequest();
		re.setPage(page);
		re.setSearchKey(key);
		re.setSearchValue(value);
		VLogOutput.d(TAG, "page:" + page);
		VLogOutput.d(TAG, "searchKey:" + key);
		VLogOutput.d(TAG, "searchValue:" + value);
		// TODO: 2017/7/20
		String url = CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlOrderPagelist);
		VPayUIRequestV2<?> requestV2 = new VPayUIRequestV2<OrderListResp>(url, re, context, false)
		{
			@Override
			protected boolean onResponse(OrderListResp response)
			{
				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					listener.onSuccess(response);
				}
				else
				{
					listener.onFailed(response.getReason());
				}
				return true;
			}

			@Override
			protected void onResponseError(VolleyError error, Context context)
			{
				listener.onFailed(error.getMessage());
				super.onResponseError(error, context);
			}
		};
		requestV2.setShouldCache(false);
		requestV2.send();
		return requestV2;
	}

		@Override
		public VPayUIRequestV2 orderDetail(Context context, String orderId, final OrderDetailListener listener)
		{
			if (orderId == null)
			{
				Toast.makeText(context, "订单号为空", Toast.LENGTH_SHORT).show();
				return null;
			}

			ToastUtil.showTestToast(context, "orderId:" + orderId);

			String url = CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlWmOrderDetail);

			StringConfig stringConfig = new StringConfig();
			stringConfig.setId(orderId);
			VPayUIRequestV2<?> requestV2 = new VPayUIRequestV2<OrderItemResponse>(url, stringConfig, context, true, null)
			{
				@Override
				protected boolean onResponse(OrderItemResponse response)
				{
					if (response.getCode() == HttpUtils.CODE_SUCCESS)
					{
						listener.onSuccess(response.getResponse().getOrder());
					}
					return false;
				}
			};
			requestV2.setShouldCache(false);
			requestV2.send();
			return requestV2;
		}

	/**
	 * @param context
	 * @param orderId
	 * @param listener
	 * @return
	 */
	@Override
	public VPayUIRequestV2 orderDetail2(Context context, String orderId, final OrderDetailListener listener)
	{
		if (orderId == null)
		{
			Toast.makeText(context, "订单号为空", Toast.LENGTH_SHORT).show();
			return null;
		}

		String url2 = CanteenHttpUtil.getOrderUrl2(CanteenApplication.getmApplication(), urlWmOrderDetail2);

		StringConfig stringConfig = new StringConfig();
		stringConfig.setId(orderId);
		VPayUIRequestV2<?> requestV2 = new VPayUIRequestV2<OrderItemResponse>(url2, stringConfig, context, true, null)
		{
			@Override
			protected boolean onResponse(OrderItemResponse response)
			{
				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					listener.onSuccess(response.getResponse().getOrder());
				}
				return false;
			}
		};
		requestV2.setShouldCache(false);
		requestV2.send();
		return requestV2;
	}

	@Override
	public VPayUIRequestV2 orderConfirm(final Context context, final String orderId, final OrderConfirmListener listener)
	{
		OrderItemRequest req = new OrderItemRequest();
		req.setId(orderId);
		VPayUIRequestV2<?> request = new VPayUIRequestV2<OrderItemResponse>(CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlWmOrderSure), req, context, true)
		{
			@Override
			public boolean onResponse(OrderItemResponse response)
			{
				String endTime = new SimpleDateFormat("yy-MM-dd:hh:mm:ss", Locale.CHINA).format(new Date(System.currentTimeMillis()));

				VLogOutput.e(TAG, orderId + ":orderConfirm end:" + endTime);

				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					ApiCanteen.stopRing(context);
					//					ApiVPay.stopRing2(context);
					listener.onSuccess();
					return true;
				}
				else if ((response.getCode() == HttpUtils.ORDER_CANCELED))
				{
					listener.onFailed("订单已取消");
				}
				else if (response.getCode() == HttpUtils.ORDER_CONFIRMED)
				{
					listener.onFailed("订单重复确认");
				}
				return false;
			}

			@Override
			public void finish(boolean result)
			{
				super.finish(result);
			}
		};
		request.setShouldCache(false);
		RetryPolicy policy = new DefaultRetryPolicy(12000, 1, 1.0f);
		request.setRetryPolicy(policy);
		request.send();
		String startTime = new SimpleDateFormat("yy-MM-dd:hh:mm:ss", Locale.CHINA).format(new Date(System.currentTimeMillis()));

		VLogOutput.e(TAG, orderId + ":orderConfirm start:" + startTime);
		//确认处理的订单，更改订单表记录状态
		CanteenDao shopDao = new CanteenDao(context);
		Order order = new Order();
		order.setId(orderId);
		order.setDealTime(startTime);
		order.setDealCount(1);
		shopDao.add(order);
		//更新打卡时间,逻辑上此后的一段时间都不会用bugly上报数据
		SharePrefUtil.saveLong(context, DingdMng.LAST_DING_TIME, System.currentTimeMillis());
		return request;
	}

	@Override
	public VPayUIRequestV2 orderConfirm2(final Context context, final String orderId, final OrderConfirmListener listener)
	{
		OrderItemRequest req = new OrderItemRequest();
		req.setId(orderId);
		VPayUIRequestV2<?> request = new VPayUIRequestV2<OrderItemResponse>(CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlWmOrderSure2), req, context, true)
		{
			@Override
			public boolean onResponse(OrderItemResponse response)
			{
				String endTime = new SimpleDateFormat("yy-MM-dd:hh:mm:ss", Locale.CHINA).format(new Date(System.currentTimeMillis()));

				VLogOutput.e(TAG, orderId + ":orderConfirm end:" + endTime);

				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					ApiCanteen.stopRing(context);
					//					ApiVPay.stopRing2(context);
					listener.onSuccess();
					return true;
				}
				else if ((response.getCode() == HttpUtils.ORDER_CANCELED))
				{
					listener.onFailed("订单已取消");
				}
				else if (response.getCode() == HttpUtils.ORDER_CONFIRMED)
				{
					listener.onFailed("订单重复确认");
				}
				return false;
			}

			@Override
			public void finish(boolean result)
			{
				super.finish(result);
			}
		};
		request.setShouldCache(false);
		RetryPolicy policy = new DefaultRetryPolicy(12000, 1, 1.0f);
		request.setRetryPolicy(policy);
		request.send();
		String startTime = new SimpleDateFormat("yy-MM-dd:hh:mm:ss", Locale.CHINA).format(new Date(System.currentTimeMillis()));

		VLogOutput.e(TAG, orderId + ":orderConfirm start:" + startTime);
		//确认处理的订单，更改订单表记录状态
		CanteenDao shopDao = new CanteenDao(context);
		Order order = new Order();
		order.setId(orderId);
		order.setDealTime(startTime);
		order.setDealCount(1);
		shopDao.add(order);
		//更新打卡时间,逻辑上此后的一段时间都不会用bugly上报数据
		SharePrefUtil.saveLong(context, DingdMng.LAST_DING_TIME, System.currentTimeMillis());
		return request;
	}

	@Override
	public VPayUIRequestV2 orderSend(Context context, String sdcId, String deliverNo, String orderId, final OrderSendListener listener)
	{
		//        if (TextUtils.isEmpty(sdcId)) {
		//            return;
		//        }
		DeliverActionRequest req = new DeliverActionRequest();
		req.setId(orderId);
		req.setSdcId(sdcId);
		req.setDeliverNo(deliverNo);

		VPayUIRequestV2<?> request = new VPayUIRequestV2<OrderItemResponse>(CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlWmOrderDeliverAction), req, context, true)
		{
			@Override
			public boolean onResponse(OrderItemResponse response)
			{
				listener.onResponse();
				return false;
			}

			@Override
			public void finish(boolean result)
			{
				super.finish(result);
			}
		};
		request.setShouldCache(false);
		request.send();
		return request;
	}

	public VPayUIRequestV2 orderSend2(Context context, String sdcId, String deliverNo, String orderId, final OrderSendListener listener)
	{
		DeliverActionRequest req = new DeliverActionRequest();
		req.setId(orderId);
		req.setSdcId(sdcId);
		req.setDeliverNo(deliverNo);

		VPayUIRequestV2<?> request = new VPayUIRequestV2<OrderItemResponse>(CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlWmOrderDeliverAction2), req, context, true)
		{
			@Override
			public boolean onResponse(OrderItemResponse response)
			{
				listener.onResponse();
				return false;
			}

			@Override
			public void finish(boolean result)
			{
				super.finish(result);
			}
		};
		request.setShouldCache(false);
		request.send();
		return request;
	}

	@Override
	public VPayUIRequestV2 orderCancle(final Context context, String reasonId, String mOrderId, final OrderCancleListener listener)
	{
		if (TextUtils.isEmpty(reasonId))
		{
			Toast.makeText(context, "请选择取消原因", Toast.LENGTH_SHORT).show();
			return null;
		}

		CancelActionRequest req = new CancelActionRequest();
		req.setId(mOrderId);
		req.setReasonId(reasonId);
		VPayUIRequestV2<?> request = new VPayUIRequestV2<OrderItemResponse>(CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlWmOrderCancel), req, context, true)
		{
			@Override
			public boolean onResponse(OrderItemResponse response)
			{
				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{

					//取消成功也需要停止铃声 zhaolifan begin
					ApiCanteen.stopRing(context);
					//					ApiVPay.stopRing2(context);
					//取消成功也需要停止铃声 end
					listener.onResponse();
					return true;
				}
				else if ((response.getCode() == HttpUtils.ORDER_CANCELED) || (response.getCode() == HttpUtils.ORDER_CONFIRMED))
				{
					listener.onResponse();
				}
				return false;
			}

			@Override
			public void finish(boolean result)
			{
				super.finish(result);
			}
		};
		request.setShouldCache(false);
		request.send();
		return request;
	}

	@Override
	public VPayUIRequestV2 orderCancle2(final Context context, String reasonId, String mOrderId, final OrderCancleListener listener)
	{
		if (TextUtils.isEmpty(reasonId))
		{
			Toast.makeText(context, "请选择取消原因", Toast.LENGTH_SHORT).show();
			return null;
		}

		CancelActionRequest req = new CancelActionRequest();
		req.setId(mOrderId);
		req.setReasonId(reasonId);
		VPayUIRequestV2<?> request = new VPayUIRequestV2<OrderItemResponse>(CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlWmOrderCancel2), req, context, true)
		{
			@Override
			public boolean onResponse(OrderItemResponse response)
			{
				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{

					//取消成功也需要停止铃声 zhaolifan begin
					ApiCanteen.stopRing(context);
					//					ApiVPay.stopRing2(context);
					//取消成功也需要停止铃声 end
					listener.onResponse();
					return true;
				}
				else if ((response.getCode() == HttpUtils.ORDER_CANCELED) || (response.getCode() == HttpUtils.ORDER_CONFIRMED))
				{
					listener.onResponse();
				}
				return false;
			}

			@Override
			public void finish(boolean result)
			{
				super.finish(result);
			}
		};
		request.setShouldCache(false);
		request.send();
		return request;
	}

	@Override
	public VPayUIRequestV2 orderArrived(Context context, String mOrderId, final OrderArrivedListener listener)
	{
		StringConfig stringConfig = new StringConfig();
		stringConfig.setId(mOrderId);
		VPayUIRequestV2<?> requestV2 = new VPayUIRequestV2<OrderItemResponse>(CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlWmOrderFinishAction), stringConfig, context, false)
		{
			@Override
			protected boolean onResponse(OrderItemResponse response)
			{
				listener.onResponse();
				return false;
			}
		};
		requestV2.setShouldCache(false);
		requestV2.send();
		return requestV2;
	}

	@Override
	public VPayUIRequestV2 orderArrived2(Context context, String mOrderId, final OrderArrivedListener listener)
	{
		StringConfig stringConfig = new StringConfig();
		stringConfig.setId(mOrderId);
		VPayUIRequestV2<?> requestV2 = new VPayUIRequestV2<OrderItemResponse>(CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlWmOrderFinishAction2), stringConfig, context, false)
		{
			@Override
			protected boolean onResponse(OrderItemResponse response)
			{
				listener.onResponse();
				return false;
			}
		};
		requestV2.setShouldCache(false);
		requestV2.send();
		return requestV2;
	}

	@Override
	public VPayUIRequestV2 orderDelay(final Context context, String mOrderId, final OrderDelayListener listener)
	{
		OrderItemRequest req = new OrderItemRequest();
		req.setId(mOrderId);
		VPayUIRequestV2<?> request = new VPayUIRequestV2<OrderItemResponse>(TakeOutHttpUtils.
				getWMProderUrl(TakeOutHttpUtils.FUN_ACTION_OrderDelay), req, context, true)
		{
			@Override
			public boolean onResponse(OrderItemResponse response)
			{
				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					ApiCanteen.stopRing(context);
					//					ApiVPay.stopRing2(context);
					listener.onSuccess();
					return true;
				}
				else if ((response.getCode() == HttpUtils.ORDER_CANCELED))
				{
					listener.onFailed("该延迟订单已取消");
				}
				else if (response.getCode() == HttpUtils.ORDER_CONFIRMED)
				{
					listener.onFailed("该延迟订单重复确认");
				}
				return false;
			}

			@Override
			public void finish(boolean result)
			{
				super.finish(result);
			}
		};
		request.setShouldCache(false);
		request.send();
		return request;
	}

	@Override
	public VPayUIRequestV2 getOrderListNew(Context context, int newOrder, int page, final OrderListCallback callback)
	{
		Log.e(TAG, "getOrderListNew");
		OrderListReqst reqst = new OrderListReqst();
		reqst.setNewOrder(newOrder);
		reqst.setPage(page);
		String url = CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlOrderPagelist);
		VPayUIRequestV2 requestV2 = new VPayUIRequestV2<OrderListResp>(url, reqst, context, false)
		{
			@Override
			protected boolean onResponse(OrderListResp response)
			{
				Log.e(OrderModelImpl.TAG, "getOrderListNew onResponse:" + response);
				callback.onGetOrderListNew(response);
				return true;
			}

			@Override
			protected void onResponseError(VolleyError error, Context context)
			{
				Log.e(OrderModelImpl.TAG, "getOrderListNew onResponseError:" + error);
				super.onResponseError(error, context);
			}
		};
		requestV2.setShouldCache(false);
		requestV2.send();
		return requestV2;
	}

	@Override
	public VPayUIRequestV2 getOrderList(Context context, long date, int type, int status, int page, final OrderListCallback callback)
	{
		Log.e(TAG, "getOrderList");
		Log.e(TAG, "date:" + com.yunnex.vpay.lib.utils.DateUtils.getDate3(date));
		Log.e(TAG, "type:" + type);
		Log.e(TAG, "status:" + status);
		Log.e(TAG, "page:" + page);
		OrderListReqst re = new OrderListReqst();
		re.setDate(date);
		re.setOrderType(type);
		re.setOrderStatus(status);
		re.setPage(page);
		re.setNewOrder(0);//订单列表过滤新订单

		//		String url = TakeOutHttpUtils.getWMProderUrl(TakeOutHttpUtils.FUN_ORDER_LIST);
		String url = CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlOrderPagelist);
		VPayUIRequestV2 requestV2 = new VPayUIRequestV2<OrderListResp>(url, re, context, false)
		{
			@Override
			protected boolean onResponse(OrderListResp response)
			{
				Log.e(OrderModelImpl.TAG, "getOrderList onResponse:" + response);
				callback.onGetOrderList(response);
				return true;
			}

			@Override
			protected void onResponseError(VolleyError error, Context context)
			{
				Log.e(OrderModelImpl.TAG, "getOrderList onResponseError:" + error);
				super.onResponseError(error, context);
			}
		};
		requestV2.setShouldCache(false);
		requestV2.send();
		return requestV2;
	}

	@Override
	public VPayUIRequestV2 getTypeStatus(Context context, final OrderTypeCallback callback)
	{

		VPayUIRequestV2 requestV2 = new VPayUIRequestV2<OrderTypeResp>(CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlGetTypeStatus), null, context, false)
		{
			@Override
			protected boolean onResponse(OrderTypeResp response)
			{
				Log.e(OrderModelImpl.TAG, "getTypeStatus onResponse:" + response);
				callback.onGetOrderType(response);
				return true;
			}

			@Override
			protected void onResponseError(VolleyError error, Context context)
			{
				Log.e(OrderModelImpl.TAG, "getTypeStatus onResponseError:" + error);
				super.onResponseError(error, context);
			}
		};
		requestV2.setShouldCache(true);
		requestV2.send();
		return requestV2;
	}

	@Override
	public VPayUIRequestV2 getSearCondition(Context context, final OrderSearConditionCallback callback)
	{
		String url = CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlSearchCondition);
		VPayUIRequestV2 requestV2 = new VPayUIRequestV2<OrderSearCondidtionResp>(url, null, context, false)
		{
			@Override
			protected boolean onResponse(OrderSearCondidtionResp response)
			{
				Log.e(OrderModelImpl.TAG, "getSearCondition onResponse:" + response);
				callback.onGetCondition(response);
				return true;
			}

			@Override
			protected void onResponseError(VolleyError error, Context context)
			{
				Log.e(OrderModelImpl.TAG, "getSearCondition onResponseError:" + error);
				super.onResponseError(error, context);
			}
		};
		requestV2.setShouldCache(true);
		requestV2.send();
		return requestV2;
	}
}
