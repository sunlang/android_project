package com.yunnex.canteen.takeout.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.yunnex.canteen.R;
import com.yunnex.canteen.common.BaseFragment;
import com.yunnex.canteen.common.HomeActivity;
import com.yunnex.canteen.common.urlrouter.UrlRouter;
import com.yunnex.canteen.common.utils.ViewHolderUtil;
import com.yunnex.canteen.takeout.activity.NoticePubActivity;
import com.yunnex.canteen.takeout.activity.ShopTimeSettingActivity;
import com.yunnex.canteen.takeout.base.BaseListViewAdapter;
import com.yunnex.canteen.takeout.bean.Shop;
import com.yunnex.canteen.takeout.bean.Time;
import com.yunnex.canteen.takeout.mng.TakeOutUIUtils;
import com.yunnex.canteen.takeout.presenter.IShopPresenter;
import com.yunnex.canteen.takeout.presenter.ShopInfoPresenter;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.yunnex.canteen.common.RouteConst.URI_TAKEOUT_BILLNOTE;
import static com.yunnex.canteen.common.RouteConst.URI_TAKEOUT_DAYBILL;
import static com.yunnex.canteen.common.RouteConst.URI_TAKEOUT_DISHESMANAGE;
import static com.yunnex.canteen.common.RouteConst.URI_TAKEOUT_SHOPRUNCHANGE;

/**
 * 菜品管理模块
 * Created by yanxin on 2015/12/10.
 */
public class SettingFragment extends BaseFragment implements ShopInfoPresenter.IShopView
{
	private View               rootView;
	private CircleImageView    ivIcon;
	private TextView           tvShopName;
	private TextView           tvShopAddress;
	private TextView           tvShopTime;
	private SwipyRefreshLayout swipyrefresh;
	private VPayUIRequestV2    mRequestV2;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (rootView == null)
		{
			rootView = inflater.inflate(R.layout.fragment3_manage, null);
			initView(rootView);
			initData();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null)
		{
			parent.removeView(rootView);
		}
		return rootView;
	}

	public void initView(View rootView)
	{
		GridView gridView = (GridView) rootView.findViewById(R.id.gv);

		final List<Data> list_data = new ArrayList<>();
		list_data.add(new Data(2, "菜品管理", R.mipmap.dish_mng));
		list_data.add(new Data(0, "外卖营业状态", R.mipmap.running_status));
//		list_data.add(new Data(1, "营业时间", R.mipmap.running_time));

		//		list_data.add(new Data(3, "发布公告", R.mipmap.notice_pub));
		list_data.add(new Data(4, "日结账单", R.mipmap.day_bill));
		list_data.add(new Data(5, "订单提醒", R.mipmap.notice_pub));
		gridView.setAdapter(new InnerAdapter4GridView(getActivity().getApplicationContext(), list_data));

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				switch (list_data.get(position).id)
				{
					case 0:
						// 营业状态
//						((HomeActivity) getActivity()).startActivity(ShopRunChangeActivity.class);
						UrlRouter.from(getActivity()).jump(URI_TAKEOUT_SHOPRUNCHANGE);
						break;
					case 1:
						//营业时间
						((HomeActivity) getActivity()).startActivity(ShopTimeSettingActivity.class);
						break;
					case 2:
						//菜品管理
//						((HomeActivity) getActivity()).startActivity(DishesManageActivity.class);
						UrlRouter.from(getActivity()).jump(URI_TAKEOUT_DISHESMANAGE);
						break;
					case 3:
						//发布公告
						((HomeActivity) getActivity()).startActivity(NoticePubActivity.class);
						break;
					case 4:
						//日结账单
//						((HomeActivity) getActivity()).startActivity(DayBillActivity.class);
						UrlRouter.from(getActivity()).jump(URI_TAKEOUT_DAYBILL);
						break;
					case 5:
						//订单提醒
//						((HomeActivity) getActivity()).startActivity(BillNoteActivity.class);
						UrlRouter.from(getActivity()).jump(URI_TAKEOUT_BILLNOTE);
						break;
					default:
						break;
				}

			}
		});


		ivIcon = (CircleImageView) rootView.findViewById(R.id.iv_icon);
		tvShopName = (TextView) rootView.findViewById(R.id.tv_shop_name);
		tvShopAddress = (TextView) rootView.findViewById(R.id.tv_shop_address);
		tvShopTime = (TextView) rootView.findViewById(R.id.tv_shop_time);

		/**
		 * click 5 times in 1.2 seconds,you will get a big gift
		 */
//		ScreenUtil.multiClick(ivIcon, 3, 1200);

		swipyrefresh = (SwipyRefreshLayout) rootView.findViewById(R.id.swipyrefresh);

		swipyrefresh.setDirection(SwipyRefreshLayoutDirection.TOP);
		swipyrefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.purple);

		swipyrefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction)
			{
				initData();
				//				new WeakHandler().postDelayed(new Runnable()
				//				{
				//					@Override
				//					public void run()
				//					{
				//						swipyrefresh.setRefreshing(false);
				//					}
				//				}, 1500);
				Observable.timer(500, TimeUnit.MILLISECONDS)//
						.observeOn(AndroidSchedulers.mainThread())//
						.subscribe(new Action1<Long>()
						{
							@Override
							public void call(Long aLong)
							{
								swipyrefresh.setRefreshing(false);
							}
						});
			}
		});
	}


	/**
	 * get data from server and fill the data into ui
	 */
	public void initData()
	{
		IShopPresenter shopPresenter = new ShopInfoPresenter(this);
		mRequestV2 = shopPresenter.getShop(getContext(), false);
	}

	@Override
	public void fillData(Shop shop)
	{
		tvShopName.setText(shop.getName() + "");
		tvShopAddress.setText(shop.getAddress() + "");
		TakeOutUIUtils.displayImageView(getActivity(), shop.getIcon(), ivIcon);

		List<Time> list_time = shop.getRunningTimes();

		if (list_time == null)
		{
			return;
		}

		//		if (list_time == null)
		//		{
		//			list_time = new ArrayList<>();
		//		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list_time.size(); i++)
		{
			sb.append(list_time.get(i).getStartTime() + "-" + list_time.get(i).getEndTime() + ";");
		}


		String sb_str = list_time.size() == 0 ? sb.substring(0, sb.length()) : sb.substring(0, sb.length() - 1);

		tvShopTime.setText(sb_str);
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

			ImageView iv = ViewHolderUtil.get(convertView, R.id.iv_pic);
			TextView tv_name = ViewHolderUtil.get(convertView, R.id.tv_name);
			TextView tv_right_vertical = ViewHolderUtil.get(convertView, R.id.tv_right_vertical);


			if (position == 2)
			{
				tv_right_vertical.setVisibility(View.GONE);
			}

			iv.setImageResource(data.imageResouce);
			tv_name.setText(data.name);
			return convertView;
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
}
