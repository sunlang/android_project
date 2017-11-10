package com.yunnex.canteen.takeout.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.yunnex.canteen.R;
import com.yunnex.canteen.common.BaseActivity;
import com.yunnex.canteen.common.CanteenHttpUtil;
import com.yunnex.canteen.common.utils.DialogManager;
import com.yunnex.canteen.takeout.base.BaseItitleCallback;
import com.yunnex.canteen.takeout.bean.Dish;
import com.yunnex.canteen.takeout.http.request.DishItemRequest;
import com.yunnex.canteen.takeout.http.request.StoreNumRequest;
import com.yunnex.canteen.takeout.http.response.DishItemResponse;
import com.yunnex.canteen.takeout.mng.TakeOutUIUtils;
import com.yunnex.canteen.takeout.mng.TakeOutUtils;
import com.yunnex.canteen.takeout.ui.CustomLodingView;
import com.yunnex.canteen.takeout.ui.StoreNumDialog;
import com.yunnex.canteen.takeout.ui.StoreNumLayout;
import com.yunnex.ui.dialog.BaseDialogBuilder;
import com.yunnex.ui.dialog.CustomDialogBuilder;
import com.yunnex.ui.dialog.CustomDialogLayout;
import com.yunnex.vpay.lib.http.HttpUtils;
import com.yunnex.vpay.lib.utils.PriceUtils;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.yunnex.canteen.takeout.base.Constant.urlGoodsDetail;
import static com.yunnex.canteen.takeout.base.Constant.urlUpdateSoldStatus;

/**
 * Created by songdan on 2016/1/5.
 */
public class DishDetailActivity extends BaseActivity
{
	private final String TAG = "DishDetailActivity";
	private Context mContext;
	private String soldoutNum = "0";

	private ImageView      imgDish;
	private TextView       tvStatus;
	private TextView       categoryName;
	private TextView       dishName;
	private TextView       dishPrice;
	private TextView       storeNum;
	private RelativeLayout storeSettingLayout;
	private RelativeLayout dishDetailLayout;    // 全屏布局
	private Button         btn_bottom;

	private Dish               mDish;
	private StoreNumDialog     storeNumDialog;
	private VPayUIRequestV2<?> mRequestV2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dish_detail);
		mContext = this;
		initTitle();
		init();
		getDishDetailHttp();
	}

	// 获取最新菜品详情（成功获取刷新数据）
	private void getDishDetailHttp()
	{
		DishItemRequest re = new DishItemRequest();
		int dishId = getIntent().getIntExtra("dishId",0);
		re.setGoodsId(dishId);
		String url = CanteenHttpUtil.getOrderUrl(this, urlGoodsDetail);
		mRequestV2 = new VPayUIRequestV2<DishItemResponse>(url, re, mContext, false)
		{
			@Override
			protected boolean onResponse(DishItemResponse response)
			{
				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					mDish = response.getResponse();
					if (mDish != null)
						refreshData();
					return true;
				}
				else
				{
					Toast.makeText(mContext, R.string.error_http, Toast.LENGTH_LONG).show();
					return false;
				}
			}

			@Override
			protected void onResponseError(VolleyError error, Context context)
			{
				Toast.makeText(mContext, R.string.error_network, Toast.LENGTH_LONG).show();
				super.onResponseError(error, context);
			}
		};
		mRequestV2.setShouldCache(false);
		mRequestV2.send();
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

	// 刷新数据
	private void refreshData()
	{
		// 显示界面
		dishDetailLayout.setVisibility(View.VISIBLE);

		switch (mDish.getSoldStatus())
		{
			case Dish.DISH_STATUS_SOLD_OUT:
				tvStatus.setText(R.string.dish_status_slod_out);
				tvStatus.setBackgroundResource(R.drawable.remark_sold_out_red);
				storeSettingLayout.setClickable(false);
				storeNum.setText(mDish.getStoreNum());
				storeNum.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
				break;
			case Dish.DISH_STATUS_SALE:
				tvStatus.setText(R.string.dish_status_sale);
				tvStatus.setBackgroundResource(R.drawable.remark_sale);
				storeSettingLayout.setClickable(true);
				if (mDish.getStoreNum() == null)//当库存数为null时，则默认显示为保持出售状态
				{
					storeNum.setText(R.string.status_keep_sale);
					storeSettingLayout.setBackgroundResource(R.color.white);
					storeNum.setTextColor(getApplicationContext().getResources().getColor(R.color.blue));
				}
				else
				{
					storeNum.setText(mDish.getStoreNum());
					storeSettingLayout.setBackgroundResource(R.color.white);
					storeNum.setTextColor(getApplicationContext().getResources().getColor(R.color.blue));
				}
				break;
		}

		TakeOutUIUtils.displayImageView(DishDetailActivity.this, mDish.getIcon(), imgDish);
		categoryName.setText(mDish.getCategoryName());
		dishName.setText(mDish.getName());
		dishPrice.setText(PriceUtils.longToCurrency(mDish.getPrice()));

		// 底部按钮 初始化
		switch (mDish.getSoldStatus())
		{
			case Dish.DISH_STATUS_SALE:
				btn_bottom.setText(R.string.btn_status_slodout2);
				btn_bottom.setTextColor(getApplicationContext().getResources().getColor(R.color.blue_btn_weight));
				btn_bottom.setBackgroundResource(R.drawable.shap_button_sw);
				break;
			case Dish.DISH_STATUS_SOLD_OUT:
				btn_bottom.setBackgroundResource(R.drawable.shap_button_hf);
				btn_bottom.setText(R.string.btn_status_restart);
				btn_bottom.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
				break;
		}
	}

	// 初始化
	private void init()
	{
		imgDish = (ImageView) findViewById(R.id.image_dish);
		tvStatus = (TextView) findViewById(R.id.image_dishstatus);
		categoryName = (TextView) findViewById(R.id.categoryName);
		dishName = (TextView) findViewById(R.id.dishName);
		dishPrice = (TextView) findViewById(R.id.dishPrice);
		storeNum = (TextView) findViewById(R.id.storeNum);
		storeSettingLayout = (RelativeLayout) findViewById(R.id.layout_storenum);
		//屏蔽库存栏
		storeSettingLayout.setVisibility(View.GONE);
		dishDetailLayout = (RelativeLayout) findViewById(R.id.layout_detail);
		btn_bottom = (Button) findViewById(R.id.btn_bottom);

		// 库存栏 点击事件
		storeSettingLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showStoreNumDialog(mDish, TakeOutUtils.ACTION_SET_STORE_NUM);
			}
		});

		// 底部按钮 点击事件
		btn_bottom.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				switch (mDish.getSoldStatus())
				{
					case Dish.DISH_STATUS_SALE:
					{
						showWarningDialog();
						break;
					}
					case Dish.DISH_STATUS_SOLD_OUT:
					{
						//						showStoreNumDialog(mDish, TakeOutUtils.ACTION_RESTORE);
						//屏蔽设置库存的操作，默认修改为“保持出售状态”
						setStoreNumHttp(TakeOutUtils.ACTION_RESTORE, null);
						break;
					}
					default:
				}
			}
		});
	}

	/**
	 * 显示 设置库存弹出框
	 *
	 * @param dish 菜品
	 */
	private void showStoreNumDialog(final Dish dish, final int actionType)
	{
		storeNumDialog = new StoreNumDialog(mContext, dish);
		storeNumDialog.setTitle(R.string.status_num_set);
		storeNumDialog.setPositiveButton(android.R.string.ok, new BaseDialogBuilder.OnClickListener()
		{
			@Override
			public void onClick(AlertDialog alertDialog, CustomDialogLayout customDialogLayout, View view)
			{
				String num_et = storeNumDialog.getEditText();
				switch (storeNumDialog.getStoreStatusChoose())
				{
					case StoreNumLayout.STATUS_KEEP_SALE:
						alertDialog.dismiss();
						setStoreNumHttp(actionType, null);
						break;
					case StoreNumLayout.STATUS_SET_NUM:
						if (TextUtils.isEmpty(num_et))
						{
							return;
						}
						else
						{
							alertDialog.dismiss();
							setStoreNumHttp(actionType, num_et);
						}
						break;
				}
			}
		});
		storeNumDialog.setNegativeButton(android.R.string.cancel, new BaseDialogBuilder.OnClickListener()
		{
			@Override
			public void onClick(AlertDialog alertDialog, CustomDialogLayout customDialogLayout, View view)
			{
				alertDialog.dismiss();
				getDishDetailHttp();
			}
		});
		storeNumDialog.createDialog();
		storeNumDialog.createView();
		storeNumDialog.show();
	}

	private void initTitle()
	{
		customTitle.setTitleTxt("菜品详情").
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

	public void showWarningDialog()
	{
		CustomDialogBuilder builder = new CustomDialogBuilder(DishDetailActivity.this);
		builder.setIcon(R.drawable.icon_warning);
//		builder.setMessage(R.string.dialog_warnning);
		builder.setMessage(R.string.dialog_warnning2);
		builder.setNegativeButton(android.R.string.cancel, new BaseDialogBuilder.OnClickListener()
		{
			@Override
			public void onClick(AlertDialog alertDialog, CustomDialogLayout customDialogLayout, View view)
			{
				alertDialog.cancel();
			}
		});
		builder.setPositiveButton(android.R.string.ok, new BaseDialogBuilder.OnClickListener()
		{
			@Override
			public void onClick(AlertDialog alertDialog, CustomDialogLayout customDialogLayout, View view)
			{
				//触发售完操作，库存默认设置为0
				setStoreNumHttp(TakeOutUtils.ACTION_SOLD_OUT, soldoutNum);
				alertDialog.cancel();
			}
		});
		builder.createDialog().setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				Log.i(TAG, "warning dialog onCancel.....");
				getDishDetailHttp();
			}
		});
		builder.createView();
		builder.show();
	}

	// 库存设置
	private void setStoreNumHttp(int actionType, String storeNum)
	{
		final CustomLodingView lodingView = new CustomLodingView(mContext);
		lodingView.setTVText("保存中");
		lodingView.setPbVisible();
		//		DialogManager dialogManager = new DialogManager(mContext);
		final Dialog dialog = DialogManager.getInstance().showDialogWithView(this, 2, lodingView, R.style.dialog_loding_style, false);

		StoreNumRequest request = new StoreNumRequest();
		request.setId(mDish.getId());
//		request.setStoreNum(storeNum);
		request.setSoldStatus(actionType);
		String url =  CanteenHttpUtil.getOrderUrl(this, urlUpdateSoldStatus);
		mRequestV2 = new VPayUIRequestV2<DishItemResponse>(url, request, mContext, false)
		{
			@Override
			protected boolean onResponse(DishItemResponse response)
			{
				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					lodingView.setIVRightVisible();
					lodingView.setTVText("保存成功");
				}
				else
				{
					lodingView.setIVWrongVisible();
					lodingView.setTVText("保存失败");
				}
				getDishDetailHttp();

				//				new WeakHandler().postDelayed(new Runnable()
				//				{
				//					@Override
				//					public void run()
				//					{
				//						dialog.dismiss();
				//					}
				//				}, 2000);
				Observable.timer(2000, TimeUnit.MILLISECONDS)//
						.observeOn(AndroidSchedulers.mainThread())//
						.subscribe(new Action1<Long>()
						{
							@Override
							public void call(Long aLong)
							{
								dialog.dismiss();
							}
						});
				return false;
			}

			@Override
			protected void onResponseError(VolleyError error, Context context)
			{
				lodingView.setIVWrongVisible();
				lodingView.setTVText("保存失败");
				dialog.dismiss();
				super.onResponseError(error, context);
			}
		};
		mRequestV2.setShouldCache(false);
		mRequestV2.send();
	}

}