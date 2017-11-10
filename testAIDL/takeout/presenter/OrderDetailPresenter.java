package com.yunnex.canteen.takeout.presenter;

import android.content.Context;
import android.widget.Toast;

import com.yunnex.canteen.common.utils.ToastUtil;
import com.yunnex.canteen.takeout.bean.Order;
import com.yunnex.canteen.takeout.modle.IOrderModel;
import com.yunnex.canteen.takeout.modle.OrderModelImpl;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

/**
 * Created by sungongyan on 2016/4/19.
 * qq 379366152
 */
public class OrderDetailPresenter extends BaseOrderPresenter
{

	public interface IOrderDetailView
	{
		void fillData(Order order);

		void fillData2(Order order);

		void onConfirmSuccess(boolean b);

		void onConfirmFailed(String reason);
	}

	private IOrderDetailView mIOrderDetailView;
	//	private IOrderModel      mOrderModel;
	private OrderModelImpl   mOrderModel;

	public OrderDetailPresenter(IOrderDetailView IOrderDetailView)
	{
		mIOrderDetailView = IOrderDetailView;
		mOrderModel = new OrderModelImpl();
	}

	@Override
	public VPayUIRequestV2 orderDetail(final Context context, String orderId)
	{
		return mOrderModel.orderDetail(context, orderId, new IOrderModel.OrderDetailListener()
		{
			@Override
			public void onSuccess(Order order)
			{
				if (order == null)
				{
					Toast.makeText(context, "订单详情数据为空", Toast.LENGTH_SHORT).show();
					return;
				}
				mIOrderDetailView.fillData(order);
			}
		});
	}

	@Override
	public VPayUIRequestV2 orderDetail2(final Context context, String orderId)
	{
		return mOrderModel.orderDetail2(context, orderId, new IOrderModel.OrderDetailListener()
		{
			@Override
			public void onSuccess(Order order)
			{
				if (order == null)
				{
					Toast.makeText(context, "订单详情数据为空", Toast.LENGTH_SHORT).show();
					return;
				}
				//判断接口版本，老的走原来的，新的走新的
				//				int apiVersion = order.getApiVersion();
				//				switch (apiVersion)
				//				{
				//					case 0://old
				//						mIOrderDetailView.fillData(order);
				//						break;
				//
				//					case 1:
				mIOrderDetailView.fillData2(order);
				//						break;
				//					default:
				//						Toast.makeText(context, "未知订单api版本", Toast.LENGTH_SHORT).show();
				//						break;
				//				}
			}
		});
	}

	@Override
	public VPayUIRequestV2 orderSend(final Context context, String sdcId, String deliverNo, final String orderId)
	{
		return mOrderModel.orderSend(context, sdcId, deliverNo, orderId, new IOrderModel.OrderSendListener()
		{
			@Override
			public void onResponse()
			{
				orderDetail(context, orderId);
			}
		});
	}

	@Override
	public VPayUIRequestV2 orderSend2(final Context context, String sdcId, String deliverNo, final String orderId)
	{
		return mOrderModel.orderSend2(context, sdcId, deliverNo, orderId, new IOrderModel.OrderSendListener()
		{
			@Override
			public void onResponse()
			{
				orderDetail2(context, orderId);
			}
		});
	}

	@Override
	public VPayUIRequestV2 orderConfirm(final Context context, final String orderId)
	{
		return mOrderModel.orderConfirm(context, orderId, new IOrderModel.OrderConfirmListener()
		{
			@Override
			public void onSuccess()
			{
				ToastUtil.showToast(context, "确认订单成功");
				orderDetail(context, orderId);
				//false表示订单之前未确认，是新订单,为其后打印服务
				mIOrderDetailView.onConfirmSuccess(false);
			}

			@Override
			public void onFailed(String reason)
			{
				mIOrderDetailView.onConfirmFailed(reason);
			}
		});
	}

	@Override
	public VPayUIRequestV2 orderConfirm2(final Context context, final String orderId)
	{
		return mOrderModel.orderConfirm2(context, orderId, new IOrderModel.OrderConfirmListener()
		{
			@Override
			public void onSuccess()
			{
				ToastUtil.showToast(context, "确认订单成功");
				orderDetail2(context, orderId);
				//false表示订单之前未确认，是新订单,为其后打印服务
				mIOrderDetailView.onConfirmSuccess(false);
			}

			@Override
			public void onFailed(String reason)
			{
				mIOrderDetailView.onConfirmFailed(reason);
			}
		});
	}

	@Override
	public VPayUIRequestV2 orderArrived(final Context context, final String mOrderId)
	{
		return mOrderModel.orderArrived(context, mOrderId, new IOrderModel.OrderArrivedListener()
		{
			@Override
			public void onResponse()
			{
				orderDetail(context, mOrderId);
			}
		});
	}

	@Override
	public VPayUIRequestV2 orderArrived2(final Context context, final String mOrderId)
	{
		return mOrderModel.orderArrived2(context, mOrderId, new IOrderModel.OrderArrivedListener()
		{
			@Override
			public void onResponse()
			{
				orderDetail2(context, mOrderId);
			}
		});
	}

	@Override
	public VPayUIRequestV2 orderCancle(final Context context, String reasonId, final String mOrderId)
	{
		return mOrderModel.orderCancle(context, reasonId, mOrderId, new IOrderModel.OrderCancleListener()
		{
			@Override
			public void onResponse()
			{
				orderDetail(context, mOrderId);
			}
		});
	}

	@Override
	public VPayUIRequestV2 orderCancle2(final Context context, String reasonId, final String mOrderId)
	{
		return mOrderModel.orderCancle2(context, reasonId, mOrderId, new IOrderModel.OrderCancleListener()
		{
			@Override
			public void onResponse()
			{
				orderDetail2(context, mOrderId);
			}
		});
	}

	@Override
	public VPayUIRequestV2 orderDelay(final Context context, final String mOrderId)
	{
		return mOrderModel.orderDelay(context, mOrderId, new IOrderModel.OrderDelayListener()
		{
			@Override
			public void onSuccess()
			{
				orderDetail(context, mOrderId);
				//false表示订单之前未确认，是新订单,为其后打印服务
				mIOrderDetailView.onConfirmSuccess(false);
			}

			@Override
			public void onFailed(String reason)
			{
				mIOrderDetailView.onConfirmFailed(reason);
			}
		});
	}
}
