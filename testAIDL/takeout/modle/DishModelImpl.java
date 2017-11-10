package com.yunnex.canteen.takeout.modle;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.yunnex.canteen.R;
import com.yunnex.canteen.takeout.http.response.DishItemResponse;
import com.yunnex.canteen.takeout.http.request.DishItemRequest;
import com.yunnex.canteen.takeout.http.request.StoreNumRequest;
import com.yunnex.canteen.takeout.ui.CustomLodingView;
import com.yunnex.canteen.common.utils.DialogManager;
import com.yunnex.canteen.takeout.mng.TakeOutHttpUtils;
import com.yunnex.vpay.lib.http.HttpUtils;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sungongyan on 2016/1/15.
 * wechat sun379366152
 */
public class DishModelImpl implements IDishModel
{
	@Override
	public void getDishDetail(final Context mContext, int dishId, final DishDetailCallBack callBack)
	{
		if (dishId == 0)
		{
			Toast.makeText(mContext, "dishId is null", Toast.LENGTH_LONG).show();
			return;
		}
		DishItemRequest re = new DishItemRequest();
		re.setGoodsId(dishId);
		VPayUIRequestV2<?> requestV2 = new VPayUIRequestV2<DishItemResponse>(TakeOutHttpUtils.getWMProderUrl(TakeOutHttpUtils.FUN_DISH_DETAIL), re, mContext, false)
		{
			@Override
			protected boolean onResponse(DishItemResponse response)
			{
				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					//                    mDish = response.getDish();
					//
					//                    mDetailActivity.updateData(mDish);

					callBack.onGetDish(response.getResponse());

				}
				else
				{
					Toast.makeText(mContext, "请求出错请稍后再试", Toast.LENGTH_LONG).show();
				}
				return false;
			}

			@Override
			protected void onResponseError(VolleyError error, Context context)
			{
				Toast.makeText(mContext, "网络异常请稍后再试", Toast.LENGTH_LONG).show();
				super.onResponseError(error, context);
			}
		};
		requestV2.setShouldCache(false);
		requestV2.send();
	}

	@Override
	public void setDishStoreNum(Context context, int dishId, String storeNum, int actionType, final DishStoreNumCallback callback)
	{
		final CustomLodingView lodingView = new CustomLodingView(context);
		lodingView.setTVText("保存中");
		lodingView.setPbVisible();
		//        DialogManager dialogManager = new DialogManager(context);
		final Dialog dialog = DialogManager.getInstance().
				showDialogWithView(context, 2, lodingView, R.style.dialog_loding_style, false);

		StoreNumRequest request = new StoreNumRequest();
		request.setSoldStatus(actionType);
//		request.setStoreNum(storeNum);
		request.setId(dishId);
		VPayUIRequestV2<?> requestV2 = new VPayUIRequestV2<DishItemResponse>(TakeOutHttpUtils.
				getWMProderUrl(TakeOutHttpUtils.FUN_DISH_STORENUM_SETTING), request, context, false)
		{
			@Override
			protected boolean onResponse(DishItemResponse response)
			{
				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					lodingView.setIVRightVisible();
					lodingView.setTVText("保存成功");
					callback.onSetStoreNumSuccess();
				}
				else
				{
					lodingView.setIVWrongVisible();
					lodingView.setTVText("保存失败");
				}
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
		requestV2.setShouldCache(false);
		requestV2.send();
	}
}
