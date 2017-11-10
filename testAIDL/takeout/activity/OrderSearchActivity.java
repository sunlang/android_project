package com.yunnex.canteen.takeout.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.yunnex.canteen.R;
import com.yunnex.canteen.catering.data.domain.jsonbean.ExtraDataTwo;
import com.yunnex.canteen.common.BaseActivity;
import com.yunnex.canteen.common.RouteConst;
import com.yunnex.canteen.common.urlrouter.UrlRouter;
import com.yunnex.canteen.common.utils.ToastUtil;
import com.yunnex.canteen.common.utils.ViewHolderUtil;
import com.yunnex.canteen.takeout.adapter.OrderDtoAdapter;
import com.yunnex.canteen.takeout.base.BaseItitleCallback;
import com.yunnex.canteen.takeout.base.BaseListViewAdapter;
import com.yunnex.canteen.takeout.base.BaseRecycleAdapter;
import com.yunnex.canteen.takeout.bean.OrderDto;
import com.yunnex.canteen.takeout.bean.OrderDtoSub;
import com.yunnex.canteen.takeout.bean.StringConfig;
import com.yunnex.canteen.takeout.http.response.OrderListResp;
import com.yunnex.canteen.takeout.http.response.OrderSearCondidtionResp;
import com.yunnex.canteen.takeout.modle.IOrderModel;
import com.yunnex.canteen.takeout.modle.OrderModelImpl;
import com.yunnex.canteen.takeout.ui.SpaceItemDecoration;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.util.ArrayList;
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
import static com.yunnex.vpay.lib.cookie.CookieMngWrapper.getContext;

public class OrderSearchActivity extends BaseActivity
{
	private static final String TAG = OrderSearchActivity.class.getSimpleName();
	private OrderDtoAdapter adapter;

	private SwipyRefreshLayout swipyrefresh;
	private RecyclerView       recyclerview;
	private LinearLayout       ll_error;
	private VPayUIRequestV2    mRequestV2;
	private LinearLayout       mLl_search_condition;
	private GridView           mGv_search_condition;
	private AppCompatSpinner   mSpinner;
	private String             currentSelect;
	private List<StringConfig> conditions = new ArrayList<>();
	private boolean isHasNext;
	private int currentPage = 1;
	private EditText mEt_search;
	private TextView mTv_note;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_search);
		initView();
	}

	private void initView()
	{
		((RelativeLayout) findViewById(R.id.title_container)).addView(customTitle);

		TextView tv_search = (TextView) findViewById(R.id.tv_search);
		mEt_search = (EditText) findViewById(R.id.et_search);

		tv_search.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (TextUtils.isEmpty(mEt_search.getEditableText()))
				{
					Toast.makeText(mContext, "输入搜索内容", Toast.LENGTH_SHORT).show();
					return;
				}
				initData(SwipyRefreshLayoutDirection.TOP, currentSelect, mEt_search.getEditableText().toString());

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});

		mTv_note = (TextView) findViewById(R.id.tv_note);

		mSpinner = (AppCompatSpinner) findViewById(R.id.spinner);
		swipyrefresh = (SwipyRefreshLayout) findViewById(R.id.swipyrefresh);
		recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
		ll_error = (LinearLayout) findViewById(R.id.ll);

		//search condition
		mLl_search_condition = (LinearLayout) findViewById(R.id.ll_search_condition);
		mGv_search_condition = (GridView) findViewById(R.id.gv_search_condition);
		mGv_search_condition.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				mSpinner.setSelection(position);
			}
		});
		mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				view.findViewById(R.id.view_line).setVisibility(View.INVISIBLE);
				view.findViewById(R.id.iv_triangle).setVisibility(View.VISIBLE);
				currentSelect = conditions.get(position).getId();
				ToastUtil.showTestToast(mContext, "selected:" + currentSelect);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
		mSpinner.setPopupBackgroundResource(R.color.white);
		mSpinner.setDropDownWidth(150);
		mSpinner.setBackgroundResource(R.drawable.shape_white_corner);
		mSpinner.setDropDownVerticalOffset(120);

		customTitle.setLeftIVBelowDrawable(arrow_left)//
				.setTitleTxt("订单搜索")//
				.setMidTextOff(50)//
				.setIsTVRightVisible(false);

		customTitle.onclick(new BaseItitleCallback()
		{
			@Override
			public void onleftClik(View view)
			{
				finish();
			}
		});


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
						initData(direction, currentSelect, mEt_search.getEditableText().toString());
						break;
					case BOTTOM:
						if (isHasNext)
						{
							initData(direction, currentSelect, mEt_search.getEditableText().toString());
						}
						else
						{
							Toast.makeText(getApplicationContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
							swipyrefresh.setRefreshing(false);
						}
						break;
				}
			}
		});

		recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
		recyclerview.setItemAnimator(new DefaultItemAnimator());
		int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
		recyclerview.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

		getSearCondition();
	}

	//test
	private void getSearConditionLocal()
	{
		OrderSearCondidtionResp resp = new OrderSearCondidtionResp();
		fillData(resp);
	}

	private void getSearCondition()
	{
		OrderModelImpl orderModel = new OrderModelImpl();
		orderModel.getSearCondition(getApplicationContext(), new IOrderModel.OrderSearConditionCallback()
		{
			@Override
			public void onGetCondition(OrderSearCondidtionResp response)
			{
				fillData(response);
			}
		});
	}

	private void fillData(OrderSearCondidtionResp response)
	{
		Log.d(TAG, "fillData:" + response);
		conditions = response.getResponse();
		showSearConditon();

		int[] resource = new int[]{R.mipmap.dingdanhao,//
				R.mipmap.lianxiren,//
				R.mipmap.shoujihao,//
				R.mipmap.zhuohao,//
				R.mipmap.qucanhao,//
				R.mipmap.liushuihao};

		final List<Data> list_data4gv = new ArrayList<>();
		List<String> list4spin = new ArrayList<>();

		if (conditions == null)
		{
			conditions = new ArrayList<>();
			//test
			//			conditions.add(new StringConfig("1", "?"));
			//			conditions.add(new StringConfig("2", "?"));
			//			conditions.add(new StringConfig("3", "?"));
			//			conditions.add(new StringConfig("4", "?"));
			//			conditions.add(new StringConfig("5", "?"));
			//			conditions.add(new StringConfig("6", "?"));
		}

		for (int i = 0; i < conditions.size(); i++)
		{
			String name = conditions.get(i).getValue();
			list_data4gv.add(new Data(i, name, resource[i]));
			list4spin.add(name);
		}
		mGv_search_condition.setAdapter(new InnerAdapter4GridView(this, list_data4gv));
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, R.id.tv_item_name, list4spin);
		mSpinner.setAdapter(adapter);
	}


	private void initData(final SwipyRefreshLayoutDirection direction, String key, String value)
	{
		initDataPre();
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
		OrderModelImpl orderModel = new OrderModelImpl();
		mRequestV2 = orderModel.orderSearch(this, currentPage, key, value, new IOrderModel.OrderSearchListener()
		{
			@Override
			public void onSuccess(OrderListResp resp)
			{
				List<OrderDto> ordersLists = resp.getResponse().getList();

				isHasNext = resp.getResponse().getPage().isHasNextPage();
				swipyrefresh.setRefreshing(false);
				if (recyclerview.getAdapter() == null)
				{
					recyclerview.setAdapter(adapter);
				}
				if (ordersLists == null || ordersLists.size() == 0)
				{
					showNoData();
					return;
				}
				showData();
				switch (direction)
				{
					case TOP:
						adapter.clear().addDataRange(0, ordersLists);
						break;
					case BOTTOM:
						adapter.addDataRange(adapter.getItemCount(), ordersLists);
						break;
				}
			}

			@Override
			public void onFailed(String message)
			{
				swipyrefresh.setRefreshing(false);
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void showData()
	{
		mLl_search_condition.setVisibility(View.GONE);
		swipyrefresh.setVisibility(View.VISIBLE);
		ll_error.setVisibility(View.GONE);
		mTv_note.setVisibility(View.GONE);
	}

	private void showNoData()
	{
		mLl_search_condition.setVisibility(View.GONE);
		swipyrefresh.setVisibility(View.GONE);
		ll_error.setVisibility(View.VISIBLE);
		mTv_note.setVisibility(View.GONE);
	}

	private void showSearConditon()
	{
		mLl_search_condition.setVisibility(View.VISIBLE);
		swipyrefresh.setVisibility(View.GONE);
		ll_error.setVisibility(View.GONE);
		mTv_note.setVisibility(View.VISIBLE);
	}

	private void initDataPre()
	{
		if (adapter == null)
		{
			adapter = new OrderDtoAdapter(getApplicationContext(), TAG);
			adapter.setOnItemClickLitener(new BaseRecycleAdapter.OnItemClickListener()
			{
				@Override
				public void onItemClick(View view, int position)
				{
					OrderDto order = adapter.getData(position);
					if (order == null)
					{
						return;
					}
					OrderDtoSub cyWmOrderDto = order.getCyWmOrderDto();
					Log.d(TAG, "order:" + order);
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
							bundle.putSerializable(com.yunnex.canteen.catering.ui.activity.OrderDetailActivity.EXTRA, extraDataTwo);
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
			});
		}
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		if (mRequestV2 != null)
		{
			mRequestV2.cancel();
		}
	}

	private class Data
	{
		public int    id;
		public String name;
		public int    imageResouce;

		public Data(int id, String name, int imageResouce)
		{
			this.id = id;
			this.name = name;
			this.imageResouce = imageResouce;
		}
	}

	private class InnerAdapter4GridView extends BaseListViewAdapter<Data>
	{

		public InnerAdapter4GridView(Context context, List<Data> lists)
		{
			super(context, lists);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			Data data = lists.get(position);

			if (convertView == null)
			{
				convertView = View.inflate(context, R.layout.item__in_fragment3_manage, null);
			}

			convertView.setBackgroundColor(getResources().getColor(R.color.window_bg));
			ImageView iv = ViewHolderUtil.get(convertView, R.id.iv_pic);
			TextView tv_name = ViewHolderUtil.get(convertView, R.id.tv_name);
			TextView tv_right_vertical = ViewHolderUtil.get(convertView, R.id.tv_right_vertical);
			ViewHolderUtil.get(convertView, R.id.tv_line).setVisibility(View.GONE);

			tv_right_vertical.setVisibility(View.GONE);

			iv.setImageResource(data.imageResouce);
			tv_name.setText(data.name);
			tv_name.setTextColor(context.getResources().getColor(R.color.grey));
			return convertView;
		}
	}
}
