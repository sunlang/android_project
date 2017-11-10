package com.yunnex.canteen.takeout.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.yunnex.canteen.R;
import com.yunnex.canteen.common.BaseActivity;
import com.yunnex.canteen.common.CanteenHttpUtil;
import com.yunnex.canteen.takeout.adapter.DishIntroAdapter;
import com.yunnex.canteen.takeout.base.BaseItitleCallback;
import com.yunnex.canteen.takeout.bean.Dish;
import com.yunnex.canteen.takeout.http.request.DishIntroSearchListRequest;
import com.yunnex.canteen.takeout.http.request.DishIntroSearchListResponse;
import com.yunnex.framework.utils.Log;
import com.yunnex.vpay.lib.http.HttpUtils;
import com.yunnex.vpay.lib.ui.widget.search.SearchWidget;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;
import com.yunnex.vpay.lib.volley.VPayVolleyRequest;

import java.util.List;

import static com.yunnex.canteen.takeout.base.Constant.urlGoodsList;

/**
 * 菜品搜索
 */
public class DishSearchActivity extends BaseActivity
{
	public static final String TAG = "DishSearchActivity";
	private Context mContext;

	private SearchWidget searchWidget;

	private ListView         dishIntroListview;
	private List<Dish>       mDishIntroList;
	private DishIntroAdapter mDishIntroAdapter;

	private RelativeLayout       searchResultEmptyLayout;
	private VPayVolleyRequest<?> mRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dish_search);
		mContext = this;
		initView();
	}

	private void initView()
	{
		initTitle();

		searchWidget = (SearchWidget) findViewById(R.id.searchWidget);
		searchWidget.setSearchEditTextOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				searchResultEmptyLayout.setVisibility(View.GONE);
			}
		});
		searchWidget.setSearchTVOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String searchKey = searchWidget.getEditText();
				DishIntroSearchListRequest dishIntroSearchListRequest = new DishIntroSearchListRequest();
				dishIntroSearchListRequest.setGoodsName(searchKey);
				String url =  CanteenHttpUtil.getOrderUrl(getApplication(), urlGoodsList);
				mRequest = new VPayUIRequestV2<DishIntroSearchListResponse>(url, dishIntroSearchListRequest, mContext, true)
				{
					@Override
					protected boolean onResponse(DishIntroSearchListResponse response)
					{
						switch (response.getCode())
						{
							case HttpUtils.CODE_SUCCESS:
							{
								Log.i(DishSearchActivity.TAG, "网络请求成功：菜品搜索...");

								mDishIntroList = response.getResponse();
								if (mDishIntroList != null)
								{
									if (mDishIntroList.isEmpty())
									{
										searchResultEmptyLayout.setVisibility(View.VISIBLE);
									}
									else
									{
										searchResultEmptyLayout.setVisibility(View.GONE);
									}

									mDishIntroAdapter = new DishIntroAdapter(mContext, mDishIntroList);
									dishIntroListview.setAdapter(mDishIntroAdapter);
								}

								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

								return true;
							}
							case HttpUtils.CODE_FAIL:
							{
								Log.i(DishSearchActivity.TAG, "网络请求失败：菜品搜索！！！");
								return false;
							}
						}
						return false;
					}
				};
				mRequest.send();
			}
		});
		searchWidget.setEditTextHint("输入菜品名称关键字");

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

		searchResultEmptyLayout = (RelativeLayout) findViewById(R.id.searchResultEmptyLayout);
	}

	private void initTitle()
	{
		customTitle.setTitleTxt(" 菜品搜索 ").
				setLeftIVBelowDrawable(arrow_left).
				setMidTextOff(titleTextPadingLeft).
				setIsTVRightVisible(false);

		customTitle.onclick(new BaseItitleCallback()
		{
			@Override
			public void onleftClik(View view)
			{
				finish();
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
