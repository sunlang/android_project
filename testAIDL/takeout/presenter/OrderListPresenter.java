package com.yunnex.canteen.takeout.presenter;

import android.content.Context;

import com.yunnex.canteen.takeout.http.response.OrderListResp;
import com.yunnex.canteen.takeout.modle.IOrderModel;
import com.yunnex.canteen.takeout.modle.OrderModelImpl;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

/**
 * Created by sungongyan on 2016/4/15.
 * qq 379366152
 */
public class OrderListPresenter extends BaseOrderPresenter
{
	public interface IOrderListView
	{
		void fillData(OrderListResp orderListResponse);
		void fillDataNew(OrderListResp orderListResponse);
	}

	private IOrderListView mOrderListView;
	private IOrderModel    mOrderModel;

	public OrderListPresenter(IOrderListView orderListView)
	{
		mOrderListView = orderListView;
		mOrderModel = new OrderModelImpl();
	}

	@Override
	public VPayUIRequestV2 getOrderList(Context context, long date,int type, int status, int page)
	{
		return mOrderModel.getOrderList(context, date,type, status, page, new IOrderModel
				.OrderListCallback()
		{
			@Override
			public void onGetOrderList(OrderListResp response)
			{
				mOrderListView.fillData(response);
			}

			@Override
			public void onGetOrderListNew(OrderListResp response)
			{

			}
		});
	}

	@Override
	public VPayUIRequestV2 getOrderListNew(Context context, int orderNew, int page)
	{

		return mOrderModel.getOrderListNew(context,orderNew,page, new IOrderModel.OrderListCallback()
		{
			@Override
			public void onGetOrderList(OrderListResp response)
			{

			}

			@Override
			public void onGetOrderListNew(OrderListResp response)
			{
				mOrderListView.fillDataNew(response);
			}
		});
	}
}
