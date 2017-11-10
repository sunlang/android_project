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
import com.yunnex.canteen.takeout.adapter.DishCategoryAdapter;
import com.yunnex.canteen.takeout.base.BaseItitleCallback;
import com.yunnex.canteen.takeout.bean.DishCategory;
import com.yunnex.canteen.takeout.http.request.DishCategoryListResponse;
import com.yunnex.vpay.lib.http.HttpUtils;
import com.yunnex.vpay.lib.log.output.VLogOutput;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;
import com.yunnex.vpay.lib.volley.VPayVolleyRequest;

import java.util.List;

import static com.yunnex.canteen.takeout.base.Constant.urlCategoryList;

/**
 * 菜品管理,分类列表
 */
public class DishesManageActivity extends BaseActivity
{
	public static final String TAG = "DishesManageActivity";
	private Context mContext;

	private TextView titleTextView;
//	private RelativeLayout searchWidgetImage;

	private ListView             dishCategoryListview;
	private List<DishCategory>   mDishCategoryList;
	private DishCategoryAdapter  mDishCategoryAdapter;
	private VPayVolleyRequest<?> mRequest;
	private TextView             mTvTotalDishNum;
	private RelativeLayout       mRlNoResult;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dishes_manage);
		mContext = this;
		initView();
	}

	private void initView()
	{
		initTitle();
		titleTextView = (TextView) findViewById(R.id.title);
		mTvTotalDishNum = (TextView) findViewById(R.id.tv_total_dish_num);
		//		searchWidgetImage = (RelativeLayout) findViewById(R.id.searchWidget);
		//		searchWidgetImage.setBackgroundDrawable(DrawableUtils.createRoundRectShape(5, mContext.getResources().getColor(R.color.white)));
		//		searchWidgetImage.setOnClickListener(new View.OnClickListener()
		//		{
		//			@Override
		//			public void onClick(View v)
		//			{
		//				Intent intent = new Intent();
		//				intent.setClass(mContext, DishSearchActivity.class);
		//				startActivity(intent);
		//			}
		//		});
		dishCategoryListview = (ListView) findViewById(R.id.dish_category_listview);
		dishCategoryListview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				DishCategory dishCategory = (DishCategory) mDishCategoryAdapter.getItem(position);
				if (dishCategory != null)
				{
					int categoryId = dishCategory.getId();
					String categoryName = dishCategory.getName();
					Intent intent = new Intent();
					intent.putExtra("categoryId", categoryId);
					intent.putExtra("categoryName", categoryName);
					intent.setClass(mContext, DishesCategoryDetailsActivity.class);

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
//				mDishCategoryList = TestFactory.getDishCategoryList();
		String url = CanteenHttpUtil.getOrderUrl(this, urlCategoryList);
		mRequest = new VPayUIRequestV2<DishCategoryListResponse>(url, null, mContext, true)
		{
			@Override
			protected boolean onResponse(DishCategoryListResponse response)
			{
				switch (response.getCode())
				{
					case HttpUtils.CODE_SUCCESS:
					{
						VLogOutput.i(DishesManageActivity.TAG, "网络请求成功：获取菜品分类列表...");

//						mDishCategoryList = new ArrayList<>();
						mDishCategoryList = response.getResponse();

						int totalDishNum = 0;
						for (int i = 0; i < mDishCategoryList.size(); i++)
						{
							totalDishNum += mDishCategoryList.get(i).getGoodsNum();
						}

						titleTextView.setText("全部分类（" + mDishCategoryList.size() + "）");
//						mTvTotalDishNum.setText("共"+response.getTotalDish()+"个菜品");
						mTvTotalDishNum.setText("共" + totalDishNum + "个菜品");
						if (mDishCategoryList == null || mDishCategoryList.size() == 0)
						{
							mRlNoResult.setVisibility(View.VISIBLE);
							return true;
						}
						mRlNoResult.setVisibility(View.GONE);
						mDishCategoryAdapter = new DishCategoryAdapter(mContext, mDishCategoryList);
						dishCategoryListview.setAdapter(mDishCategoryAdapter);
						return true;
					}
					case HttpUtils.CODE_FAIL:
					{
						VLogOutput.i(DishesManageActivity.TAG, "网络请求失败：获取菜品分类列表！！！");
						return false;
					}
				}
				return false;
			}
		};
		mRequest.setShouldCache(false);
		mRequest.send();
	}

	private void initTitle()
	{
		customTitle.setTitleTxt("菜品管理")//
				.setLeftIVBelowDrawable(arrow_left)//
				.setMidTextOff(titleTextPadingLeft)//
				.setRightText("菜品搜索");

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

	@Override
	protected void onStop()
	{
		super.onStop();
		if (mRequest != null)
		{
			mRequest.cancel();
		}
	}
}
