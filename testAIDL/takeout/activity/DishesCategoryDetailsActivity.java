package com.yunnex.canteen.takeout.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunnex.canteen.R;
import com.yunnex.canteen.common.BaseActivity;
import com.yunnex.canteen.common.CanteenHttpUtil;
import com.yunnex.canteen.takeout.adapter.DishIntroAdapter;
import com.yunnex.canteen.takeout.base.BaseItitleCallback;
import com.yunnex.canteen.takeout.bean.Dish;
import com.yunnex.canteen.takeout.http.request.DishIntroListRequest;
import com.yunnex.canteen.takeout.http.request.DishIntroListResponse;
import com.yunnex.ui.actionbar.DrawerArrowDrawable;
import com.yunnex.vpay.lib.http.HttpUtils;
import com.yunnex.vpay.lib.log.output.VLogOutput;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;
import com.yunnex.vpay.lib.volley.VPayVolleyRequest;

import java.util.List;

import static com.yunnex.canteen.takeout.base.Constant.urlGoodsList;

/**
 * 某个分类列表详细菜品
 */
public class DishesCategoryDetailsActivity extends BaseActivity
{
	public static final String TAG = "DishesCategoryDetails";
	private Context mContext;

	private TextView       titleTextView;
//	private RelativeLayout searchWidgetImage;
	private ListView       dishIntroListview;

	private List<Dish>           mDishIntroList;
	private DishIntroAdapter     mDishIntroAdapter;
	private VPayVolleyRequest<?> mRequest;
	private TextView             mTvTotalDishNum;
	private RelativeLayout       mRlNoResult;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dishes_category_details);
		mContext = this;

		initView();
	}

	private void initView()
	{
		initTitle();
		titleTextView = (TextView) findViewById(R.id.title);
		mTvTotalDishNum = (TextView) findViewById(R.id.tv_total_dish_num);
		dishIntroListview = (ListView) findViewById(R.id.dish_intro_listview);
		dishIntroListview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Dish dish = (Dish) mDishIntroAdapter.getItem(position);
				if (dish != null)
				{
					// 如果商品下架了，不跳转
					if (dish.getOutOfStock() == true)
						return;

					int dishId = dish.getId();
					Intent intent = new Intent();
					intent.putExtra("dishId", dishId);
					intent.setClass(mContext, DishDetailActivity.class);

					startActivity(intent);
				}
			}
		});

		mRlNoResult = (RelativeLayout) findViewById(R.id.rl_no_result);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// 测试用
		//		mDishIntroList = TestFactory.getDishList();

		int categoryId = getIntent().getIntExtra("categoryId",0);

		DishIntroListRequest dishIntroListRequest = new DishIntroListRequest();
		dishIntroListRequest.setCategoryId(categoryId);
		String url =  CanteenHttpUtil.getOrderUrl(this, urlGoodsList);
		mRequest = new VPayUIRequestV2<DishIntroListResponse>(url, dishIntroListRequest, mContext, true)
		{
			@Override
			protected boolean onResponse(DishIntroListResponse response)
			{
				switch (response.getCode())
				{
					case HttpUtils.CODE_SUCCESS:
					{
						VLogOutput.i(DishesCategoryDetailsActivity.TAG, "网络请求成功：获取某分类下的菜品列表...");

						mDishIntroList = response.getResponse();
						//test
//						mDishIntroList=new ArrayList<>();

						String categoryName = getIntent().getStringExtra("categoryName");
						if (categoryName != null)
						{
							titleTextView.setText(categoryName + "(" + mDishIntroList.size() + ")");
						}
						mTvTotalDishNum.setText("共"+mDishIntroList.size()+"个菜品");

						if (mDishIntroList != null&&mDishIntroList.size()>0)
						{
							mDishIntroAdapter = new DishIntroAdapter(mContext, mDishIntroList);
							dishIntroListview.setAdapter(mDishIntroAdapter);
						}else
						{
							mRlNoResult.setVisibility(View.VISIBLE);
						}
						return true;
					}
					case HttpUtils.CODE_FAIL:
					{
						VLogOutput.i(DishesCategoryDetailsActivity.TAG, "网络请求失败：获取某分类下的菜品列表！！！");
						return false;
					}
				}
				return false;
			}
		};
		mRequest.setShouldCache(false);
		mRequest.send();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		if (mRequest != null)
		{
			mRequest.cancel();
		}
	}

	private void initTitle()
	{
		DrawerArrowDrawable arrow_left = new DrawerArrowDrawable(getApplicationContext())
		{
			@Override
			public boolean isLayoutRtl()
			{
				return false;
			}
		};
		arrow_left.setProgress(1f);

		customTitle.setTitleTxt("菜品管理").
				setLeftIVBelowDrawable(arrow_left).
				setMidTextOff(titleTextPadingLeft).
				setRightText("菜品搜索");

		customTitle.onclick(new BaseItitleCallback()
		{
			@Override
			public void onleftClik(View view)
			{
				finish();
			}

			@Override
			public void onRightTVClick(View view)
			{
				Intent intent = new Intent();
				intent.setClass(mContext, DishSearchActivity.class);
				startActivity(intent);
			}
		});

		((RelativeLayout) findViewById(R.id.title_container)).addView(customTitle);
	}
}
