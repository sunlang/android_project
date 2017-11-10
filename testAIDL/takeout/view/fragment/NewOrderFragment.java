package com.yunnex.canteen.takeout.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.yunnex.api.canteen.ApiCanteen;
import com.yunnex.canteen.R;
import com.yunnex.canteen.catering.data.domain.jsonbean.ExtraDataTwo;
import com.yunnex.canteen.catering.ui.activity.OrderDetailActivity;
import com.yunnex.canteen.common.BaseFragment;
import com.yunnex.canteen.common.Functions;
import com.yunnex.canteen.common.HomeActivity;
import com.yunnex.canteen.common.RouteConst;
import com.yunnex.canteen.common.db.dao.CanteenDao;
import com.yunnex.canteen.common.urlrouter.UrlRouter;
import com.yunnex.canteen.common.utils.FragmentTabUtil;
import com.yunnex.canteen.takeout.adapter.OrderDtoAdapter;
import com.yunnex.canteen.takeout.base.BaseRecycleAdapter;
import com.yunnex.canteen.takeout.bean.Order;
import com.yunnex.canteen.takeout.bean.OrderDto;
import com.yunnex.canteen.takeout.bean.OrderDtoSub;
import com.yunnex.canteen.takeout.http.response.OrderListResp;
import com.yunnex.canteen.takeout.presenter.IOrderPresenter;
import com.yunnex.canteen.takeout.presenter.OrderListPresenter;
import com.yunnex.canteen.takeout.ui.DividerItemDecoration;
import com.yunnex.vpay.lib.http.HttpUtils;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.yunnex.canteen.catering.ui.activity.OrderDetailActivity.ADD_ORDER;
import static com.yunnex.canteen.catering.ui.activity.OrderDetailActivity.NEW_ORDER;
import static com.yunnex.canteen.common.RouteConst.ACTIOIN_WM_ORDERTAIL;
import static com.yunnex.canteen.takeout.bean.OrderDtoSub.SOURCE_BD;
import static com.yunnex.canteen.takeout.bean.OrderDtoSub.SOURCE_CPD;
import static com.yunnex.canteen.takeout.bean.OrderDtoSub.SOURCE_ELE;
import static com.yunnex.canteen.takeout.bean.OrderDtoSub.SOURCE_MT;
import static com.yunnex.canteen.takeout.bean.OrderDtoSub.SOURCE_POS;
import static com.yunnex.canteen.takeout.bean.OrderDtoSub.SOURCE_WX;
import static com.yunnex.canteen.takeout.bean.OrderDtoSub.SOURCE_WXWM;

/**
 * Created by sungongyan on 2015/12/10.
 * wechat sun379366152
 */
public class NewOrderFragment extends BaseFragment implements OnClickListener, OrderListPresenter.IOrderListView
{

	public static final  String TAG          = NewOrderFragment.class.getSimpleName();
	private static final int    RESET_HANDLE = 1000;
	private SwipyRefreshLayout swipyrefresh;
	private RecyclerView       recyclerview;

	private OrderDtoAdapter adapter;

	private int currentPage = 1;

	private boolean isHasNext = true;

	private View            remind;//add by wzd at 2015-12-26
	private VPayUIRequestV2 mRequestV2;
	private SwipyRefreshLayoutDirection currentDirection = SwipyRefreshLayoutDirection.TOP;
	private static boolean isHandle;

	private static Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Log.d(TAG, "message what:" + msg.what);
			switch (msg.what)
			{
				case RESET_HANDLE:
					isHandle = false;
					break;
			}
		}
	};
	private CanteenDao mShopDao;

	//	private static Handler mHandler = new HandlerUtils.MyHandler(new HandlerUtils.IHandlerIntent()
	//	{
	//		@Override
	//		public void handlerIntent(Message message)
	//		{
	//			Log.d(TAG, "message what:"+message.what);
	//			switch (message.what)
	//			{
	//				case RESET_HANDLE:
	//					isHandle = false;
	//					break;
	//			}
	//		}
	//	});


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View rootView = inflater.inflate(R.layout.fragment_new_order, null);

		initView(rootView);

		return rootView;
	}

	private void initData(final SwipyRefreshLayoutDirection direction, int orderNew)
	{
		initDataPre();

		swipyrefresh.setRefreshing(true);
		currentDirection = direction;
		switch (direction)
		{
			case TOP:
				currentPage = 1;
				break;
			case BOTTOM:
				currentPage++;
				break;
		}
		IOrderPresenter iOrderPresenter = new OrderListPresenter(this);
		mRequestV2 = iOrderPresenter.getOrderListNew(getActivity(), orderNew, currentPage);
	}

	private void initDataPre()
	{
		if (adapter == null)
		{
			adapter = new OrderDtoAdapter(getContext(), TAG);
			adapter.setOnItemClickLitener(new BaseRecycleAdapter.OnItemClickListener()
			{
				@Override
				public void onItemClick(View view, int position)
				{
					if (adapter.getDatas().size() > 0)
					{
						OrderDto order = adapter.getData(position);
						Log.d(TAG, "order:" + order);
						if (order == null)
						{
							return;
						}
						OrderDtoSub cyWmOrderDto = order.getCyWmOrderDto();
						if (cyWmOrderDto == null)
						{
							return;
						}

						switch (cyWmOrderDto.getSources())
						{
							case SOURCE_POS:
							case SOURCE_WX:
							case SOURCE_CPD:

								ExtraDataTwo extraDataTwo = new ExtraDataTwo();
								extraDataTwo.setOrderId(cyWmOrderDto.getOrderId());
								extraDataTwo.setOrderType(cyWmOrderDto.getIsAddDish() == 0 ? NEW_ORDER : ADD_ORDER);
								Bundle bundle = new Bundle();
								bundle.putSerializable(OrderDetailActivity.EXTRA, extraDataTwo);
								UrlRouter.from(getContext())//
										.params(bundle)//
										.jump(RouteConst.URI_CATERING_ORDERDETAIL);
								break;
							case SOURCE_MT:
							case SOURCE_BD:
							case SOURCE_ELE:
							case SOURCE_WXWM:
								//								String orderIdTakeout = order.getCyWmOrderDto().getWaimaiOrderId();

								int apiVersion = order.getCyWmOrderDto().getApiVersion();
								String orderId;
								if (apiVersion == 1)
								{
									orderId = order.getCyWmOrderDto().getOrderId();
								}
								else
								{
									orderId = order.getCyWmOrderDto().getWaimaiOrderId();
								}

								Bundle bT = new Bundle();
								bT.putString("orderId", orderId);
								bT.putInt("apiVersion", apiVersion);
								UrlRouter.from(getContext())//
										.action(ACTIOIN_WM_ORDERTAIL).params(bT)//
										.jump(RouteConst.URI_TAKEOUT_ORDERDETAIL);
								break;
						}

					}
				}
			});
			recyclerview.setAdapter(adapter);
		}
	}

	@Override
	public void onStop()
	{
		super.onStop();
		if (mRequestV2 != null)
		{
			mRequestV2.cancel();
		}
	}

	private void initView(View view)
	{
		remind = view.findViewById(R.id.remind);//add by wzd
		swipyrefresh = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefresh);
		recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

		remind.setOnClickListener(this);//add by wzd
		swipyrefresh.setDirection(SwipyRefreshLayoutDirection.BOTH);
		swipyrefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.purple);
		swipyrefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction)
			{
				switch (direction)
				{
					case TOP:
						isHasNext = true;
						initData(direction, 1);
						break;
					case BOTTOM:
						if (isHasNext)
						{
							initData(direction, 1);
						}
						else
						{
							Toast.makeText(getContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
							swipyrefresh.setRefreshing(false);
						}
						break;
				}
			}
		});

		recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
		recyclerview.setItemAnimator(new DefaultItemAnimator());
		recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

		Functions.getInstance().registFunc(new Functions.FunctionNPNR("newOrderRefresh")
		{
			@Override
			public void function()
			{
				initData(SwipyRefreshLayoutDirection.TOP, 1);
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.remind:
				isHandle = true;
				mHandler.sendEmptyMessageDelayed(RESET_HANDLE, 30000);
				remind.setVisibility(View.GONE);
				initData(SwipyRefreshLayoutDirection.TOP, 1);
				break;
		}
	}

	public void setRemindVisible(int visible)
	{
		if (isHandle)
		{
			return;
		}
		if (null != remind)
		{
			remind.setVisibility(visible);
		}
	}

	@Override
	public void onResume()
	{
		if (getHomeActivity().getFragmentTabUtil() != null && getHomeActivity().getFragmentTabUtil().getCurrentTab() == FragmentTabUtil.FRAGMENT_NEW_ORDER_LIST)
			initData(SwipyRefreshLayoutDirection.TOP, 1);
		super.onResume();
	}

	@Override
	public void fillDataNew(OrderListResp response)
	{
		swipyrefresh.setRefreshing(false);
		OrderListResp.MyResponse myResponse = response.getResponse();
		if (response.getCode() == HttpUtils.CODE_SUCCESS)
		{
			isHasNext = myResponse.getPage().isHasNextPage();
			List<OrderDto> ordersLists = myResponse.getList();
			HomeActivity homeActivity = getHomeActivity();
			if (ordersLists == null || ordersLists.size() == 0)
			{
				if (getContext() != null)
				{
					ApiCanteen.stopRing(getContext());
					//					ApiVPay.stopRing2(getContext());
				}
				adapter.clear().notifyDataSetChanged();
				if (homeActivity != null)
				{
					homeActivity.setNewOrderCount(0);
				}
				return;
			}
			if (homeActivity != null)
			{
				homeActivity.setNewOrderCount(response.getResponse().getPage().getTotalRows());
			}
			persist(ordersLists);
			switch (currentDirection)
			{
				case TOP:
					adapter.clear().addDataRange(0, ordersLists);
					break;
				case BOTTOM:
					adapter.addDataRange(adapter.getItemCount(), ordersLists);
					break;
			}
		}
	}

	@Override
	public void fillData(OrderListResp orderListResponse)
	{

	}

	private void persist(List<OrderDto> ordersLists)
	{
		if (mShopDao == null)
		{
			mShopDao = new CanteenDao(getContext());
		}
		final List<Order> orderList = new ArrayList<>();
		Observable.from(ordersLists)//
				//.subscribeOn(Schedulers.io())//
				.observeOn(Schedulers.io())//
				.subscribe(new Subscriber<OrderDto>()
				{
					@Override
					public void onCompleted()
					{
						mShopDao.add(orderList);
					}

					@Override
					public void onError(Throwable e)
					{

					}

					@Override
					public void onNext(OrderDto orderDto)
					{
						OrderDtoSub dto = orderDto.getCyWmOrderDto();
						Order or = new Order();
						or.setId(dto.getOrderId());
						or.setOrderTime(dto.getCreateTime() + "");
						orderList.add(or);
					}
				});
	}

	@Override
	public void onDestroy()
	{
		mHandler.removeMessages(RESET_HANDLE);
		super.onDestroy();
	}
}
