package com.yunnex.canteen.takeout.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.yunnex.canteen.R;
import com.yunnex.canteen.catering.data.domain.jsonbean.ExtraDataTwo;
import com.yunnex.canteen.catering.ui.activity.OrderDetailActivity;
import com.yunnex.canteen.common.BaseFragment;
import com.yunnex.canteen.common.RouteConst;
import com.yunnex.canteen.common.urlrouter.UrlRouter;
import com.yunnex.canteen.common.utils.DateUtils;
import com.yunnex.canteen.common.utils.FragmentTabUtil;
import com.yunnex.canteen.common.utils.ScreenUtil;
import com.yunnex.canteen.common.utils.SharePrefUtil;
import com.yunnex.canteen.common.utils.ToastUtil;
import com.yunnex.canteen.takeout.adapter.OrderDtoAdapter;
import com.yunnex.canteen.takeout.base.BaseRecycleAdapter;
import com.yunnex.canteen.takeout.base.Constant;
import com.yunnex.canteen.takeout.bean.OrderDto;
import com.yunnex.canteen.takeout.bean.OrderDtoSub;
import com.yunnex.canteen.takeout.bean.OrderStatu;
import com.yunnex.canteen.takeout.bean.OrderType;
import com.yunnex.canteen.takeout.bean.Page;
import com.yunnex.canteen.takeout.http.response.OrderListResp;
import com.yunnex.canteen.takeout.http.response.OrderTypeResp;
import com.yunnex.canteen.takeout.mng.PopuWindowMng;
import com.yunnex.canteen.takeout.modle.IOrderModel;
import com.yunnex.canteen.takeout.modle.OrderModelImpl;
import com.yunnex.canteen.takeout.presenter.IOrderPresenter;
import com.yunnex.canteen.takeout.presenter.OrderListPresenter;
import com.yunnex.canteen.takeout.ui.CustomDateDialog;
import com.yunnex.canteen.takeout.ui.DividerItemDecoration;
import com.yunnex.vpay.lib.http.HttpUtils;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
public class AllOrderFragment extends BaseFragment implements OnClickListener, OrderListPresenter.IOrderListView
{
	public static final String TAG = AllOrderFragment.class.getSimpleName();
	private TextView tvDate;
	private TextView tvType;

	private TabLayout mTabLayout;

	private SwipyRefreshLayout swipyrefresh;
	private RecyclerView       recyclerview;

	private OrderDtoAdapter adapter;

	private int currentPage  = 1;
	private int currentType  = 0;
	private int currentStatu = 0;

	private SwipyRefreshLayoutDirection currentDirection = SwipyRefreshLayoutDirection.TOP;

	private boolean isHasNext = true;//默认有
	private VPayUIRequestV2  mRequestV2;
	private CustomDateDialog mDateDialog;

	//	private static List<OrderType> typesDef = new ArrayList<>();
	private static List<OrderType> types = new ArrayList<>();


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment2, null);

		initView(rootView);

		return rootView;
	}

	public void initData(final SwipyRefreshLayoutDirection direction, int orderType, int orderStatus)
	{
		initDataPre();

		currentDirection = direction;
		swipyrefresh.setRefreshing(true);
		switch (direction)
		{
			case TOP:
				currentPage = 1;
				break;
			case BOTTOM:
				currentPage++;
				break;
		}


		IOrderPresenter orderPresenter = new OrderListPresenter(this);

		long time = SharePrefUtil.getLong(getContext(), Constant.requestTime, 0L);
		ToastUtil.showTestToast(getActivity(), "orderType:" + orderType + ",orderStatus:" + orderStatus + ",page:" + currentPage);

		mRequestV2 = orderPresenter.getOrderList(getActivity(),//
				time, //
				orderType, //
				orderStatus, //
				currentPage);

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
						//王彬 add   点击堂食订单，跳转至堂食订单详情 OrderDetailActivity

						OrderDto order = adapter.getData(position);
						OrderDtoSub cyWmOrderDto = order.getCyWmOrderDto();
						Log.d(TAG, "order:" + order);
						if (order == null || cyWmOrderDto == null)
						{
							return;
						}

						switch (cyWmOrderDto.getSources())
						{
							case SOURCE_POS:
							case SOURCE_CPD:
							case SOURCE_WX:

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

	/**
	 * 订单类型选择
	 *
	 * @param
	 */
	public void onChooseType(final View anchor)
	{
		if (types.size() == 0)
		{
			getTypeAndStatu();
			Toast.makeText(getContext(), "正在初始化数据,请稍后", Toast.LENGTH_SHORT).show();
			return;
		}

		/**
		 [OrderType [id=1, typeName=全部类型, status=null],
		 OrderType [id=2, typeName=全部堂食, status=[OrderStatu [id=1, value=待付款], OrderStatu [id=2, value=已下单], OrderStatu [id=3, value=交易完成], OrderStatu [id=4, value=交易关闭]]],
		 OrderType [id=3, typeName=全部外卖, status=[OrderStatu [id=1, value=待配送], OrderStatu [id=2, value=待收货], OrderStatu [id=3, value=交易完成], OrderStatu [id=4, value=交易关闭]]],
		 OrderType [id=4, typeName=百度, status=[OrderStatu [id=1, value=待配送], OrderStatu [id=2, value=待收货], OrderStatu [id=3, value=交易完成], OrderStatu [id=4, value=交易关闭]]],
		 OrderType [id=5, typeName=美团, status=[OrderStatu [id=1, value=待配送], OrderStatu [id=2, value=待收货], OrderStatu [id=3, value=交易完成], OrderStatu [id=4, value=交易关闭]]],
		 OrderType [id=6, typeName=饿了么, status=[OrderStatu [id=1, value=待配送], OrderStatu [id=2, value=待收货], OrderStatu [id=3, value=交易完成], OrderStatu [id=4, value=交易关闭]]]]
		 */

		PopuWindowMng<OrderType> manager = new PopuWindowMng<>(getActivity().getApplicationContext(), new PopuWindowMng.OnPopuWindowDismissListener()
		{
			@Override
			public void onDismiss(Object item, int position_left, int position_right)
			{
				//				if (anchor instanceof CustomTitle)
				//				{
				//					((CustomTitle) anchor).setPlatformText(((OrderType) item).getName() + " ");
				//				}
				currentType = ((OrderType) item).getOrderType();
				tvType.setText(((OrderType) item).getName());
				resetTab(((OrderType) item));
			}
		});

		manager.showPopuWindowWithListViewObj(0, anchor, types, ScreenUtil.getScreenWidth(getActivity()), 0, 0).setOnDismissListener(new PopupWindow.OnDismissListener()
		{
			@Override
			public void onDismiss()
			{
				ScreenUtil.setBackgroundAlpha(getActivity(), 1.0f);
			}
		});
		ScreenUtil.setBackgroundAlpha(getActivity(), 0.7f);

	}

	//	static
	//	{
	//		getOrderTypesDef();
	//	}

	//	@NonNull
	//	private static List<OrderType> getOrderTypesDef()
	//	{
	//		OrderType ot1 = new OrderType();
	//		ot1.setOrderType(0);
	//		ot1.setName("所有类型");
	//		ot1.setStatus(new ArrayList<OrderStatu>());//status []
	//
	//		OrderType ot2 = new OrderType();
	//		ot2.setOrderType(100);
	//		ot2.setName("所有堂食");
	//		List<OrderStatu> statusTs = new ArrayList<>();
	//		OrderStatu sc0 = new OrderStatu();
	//		sc0.setId("0");
	//		sc0.setValue("所有状态");
	//		OrderStatu sc1 = new OrderStatu();
	//		sc1.setId("1");
	//		sc1.setValue("待付款");
	//		OrderStatu sc2 = new OrderStatu();
	//		sc2.setId("2");
	//		sc2.setValue("已下单");
	//		OrderStatu sc3 = new OrderStatu();
	//		sc3.setId("3");
	//		sc3.setValue("完成");
	//		OrderStatu sc4 = new OrderStatu();
	//		sc4.setId("4");
	//		sc4.setValue("关闭");
	//		statusTs.add(sc0);
	//		statusTs.add(sc1);
	//		statusTs.add(sc2);
	//		statusTs.add(sc3);
	//		statusTs.add(sc4);
	//		ot2.setStatus(statusTs);
	//
	//		OrderType ot3 = new OrderType();
	//		ot3.setOrderType(200);
	//		ot3.setName("所有外卖");
	//		List<OrderStatu> statusWm = new ArrayList<>();
	//		OrderStatu oswm0 = new OrderStatu();
	//		oswm0.setId("0");
	//		oswm0.setValue("所有外卖");
	//		OrderStatu oswm1 = new OrderStatu();
	//		oswm1.setId("4");
	//		oswm1.setValue("待配送");
	//		OrderStatu oswm2 = new OrderStatu();
	//		oswm2.setId("6");
	//		oswm2.setValue("待收货");
	//		OrderStatu oswm3 = new OrderStatu();
	//		oswm3.setId("100");
	//		oswm3.setValue("交易完成");
	//		OrderStatu oswm4 = new OrderStatu();
	//		oswm4.setId("90");
	//		oswm4.setValue("交易关闭");
	//		statusWm.add(oswm0);
	//		statusWm.add(oswm1);
	//		statusWm.add(oswm2);
	//		statusWm.add(oswm3);
	//		statusWm.add(oswm4);
	//		ot3.setStatus(statusWm);
	//
	//
	//		OrderType ot4 = new OrderType();
	//		ot4.setOrderType(201);
	//		ot4.setName("百度");
	//		ot4.setStatus(statusWm);
	//
	//		OrderType ot5 = new OrderType();
	//		ot5.setOrderType(202);
	//		ot5.setName("美团");
	//		ot5.setStatus(statusWm);
	//
	//		OrderType ot6 = new OrderType();
	//		ot6.setOrderType(203);
	//		ot6.setName("饿了么");
	//		ot6.setStatus(statusWm);
	//
	//		typesDef.add(ot1);
	//		typesDef.add(ot2);
	//		typesDef.add(ot3);
	//		typesDef.add(ot4);
	//		typesDef.add(ot5);
	//		typesDef.add(ot6);
	//		return typesDef;
	//	}

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
		mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
		mTabLayout.setVisibility(View.GONE);
		tvDate = (TextView) view.findViewById(R.id.tv_date);
		tvDate.setTextColor(getResources().getColor(R.color.blue_sky));

		String text1;
		String date_str = DateUtils.time2SimpleStr(SharePrefUtil.getLong(getContext(), Constant.requestTime, 0L), DateUtils.MD);

		if (date_str.equals(DateUtils.currentTime2Str(DateUtils.MD)))
		{
			text1 = "今天 ";
		}
		else
		{
			text1 = date_str + " ";
		}
		tvDate.setText(text1);

		tvType = (TextView) view.findViewById(R.id.tv_type);
		tvType.setTextColor(getResources().getColor(R.color.blue_sky));
		tvType.setText("所有类型");

		swipyrefresh = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefresh);
		recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

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
						initData(direction, currentType, currentStatu);
						break;
					case BOTTOM:
						if (isHasNext)
						{
							initData(direction, currentType, currentStatu);
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

		//控制方向以及数据反转
		recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
		recyclerview.setItemAnimator(new DefaultItemAnimator());
		recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

		tvDate.setOnClickListener(this);
		tvType.setOnClickListener(this);

		getTypeAndStatu();
	}

	private void getTypeAndStatu()
	{
		OrderModelImpl orderModel = new OrderModelImpl();
		orderModel.getTypeStatus(getContext(), new IOrderModel.OrderTypeCallback()
		{
			@Override
			public void onGetOrderType(OrderTypeResp response)
			{
				types = response.getResponse();
				Log.d(AllOrderFragment.TAG, "typeAndStatu:" + types);
			}
		});
	}

	/**
	 * 重置tab
	 *
	 * @param orderType
	 */
	private void resetTab(OrderType orderType)
	{
		final List<OrderStatu> status = orderType.getStatus();
		if (status == null || status.size() == 0)
		{
			mTabLayout.setVisibility(View.GONE);
			initData(SwipyRefreshLayoutDirection.TOP, currentType, 0);
			return;
		}
		mTabLayout.setVisibility(View.VISIBLE);
		mTabLayout.removeAllTabs();
		for (OrderStatu s : status)
		{
			mTabLayout.addTab(mTabLayout.newTab().setText(s.getValue()));
		}
		if (mTabLayout.getTabCount() >= 4)
		{
			mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
		}
		else
		{
			mTabLayout.setTabMode(TabLayout.MODE_FIXED);
		}

		mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
		{
			@Override
			public void onTabSelected(TabLayout.Tab tab)
			{
				currentStatu = Integer.parseInt(status.get(tab.getPosition()).getId());
				initData(SwipyRefreshLayoutDirection.TOP, currentType, currentStatu);
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab)
			{

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab)
			{
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.tv_date:
				showDateDialog();
				break;
			case R.id.tv_type:
				onChooseType(tvType);
				break;
		}
	}

	@Override
	public void onResume()
	{
		if (getHomeActivity().getFragmentTabUtil() != null && getHomeActivity().getFragmentTabUtil().getCurrentTab() == FragmentTabUtil.FRAGMENT_ORDER_LIST_MANAGER)
		{
			isHasNext = true;
			initData(SwipyRefreshLayoutDirection.TOP, currentType, currentStatu);
		}
		super.onResume();
	}

	@Override
	public void fillDataNew(OrderListResp response)
	{
	}

	@Override
	public void fillData(OrderListResp response)
	{
		OrderListResp.MyResponse response1 = response.getResponse();
		Log.e(TAG, "fillData:" + response1);
		swipyrefresh.setRefreshing(false);
		if (recyclerview.getAdapter() == null)
		{
			recyclerview.setAdapter(adapter);
		}
		if (!isAdded())
		{
			return;
		}

		if (response.getCode() == HttpUtils.CODE_SUCCESS)
		{
			Page page = response1.getPage();
			if (page != null)
			{
				isHasNext = page.isHasNextPage();
			}
			String text1;

			String date_str = DateUtils.time2SimpleStr(SharePrefUtil.getLong(getContext(), Constant.requestTime, 0L), DateUtils.MD);

			if (date_str.equals(DateUtils.currentTime2Str(DateUtils.MD)))
			{
				text1 = "今天 ";
			}
			else
			{
				text1 = date_str + " ";
			}

			tvDate.setText(text1);

			List<OrderDto> ordersLists = response1.getList();

			if (ordersLists == null || ordersLists.size() == 0)
			{
				adapter.clear().notifyDataSetChanged();
				return;
			}
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

	/**
	 * 日期选择
	 *
	 * @param
	 */
	public void showDateDialog()
	{
		{
			if (mDateDialog != null)
			{
				mDateDialog.show();
				return;
			}
			mDateDialog = new CustomDateDialog(getActivity(), new CustomDateDialog.OnSetRightListener()
			{
				@Override
				public void onSetRight(Long time)
				{
					SharePrefUtil.saveLong(getActivity().getApplicationContext(), Constant.requestTime, time);
					initData(SwipyRefreshLayoutDirection.TOP, currentType, currentStatu);
				}
			}, Calendar.getInstance());
			mDateDialog.show();
		}
	}
}
