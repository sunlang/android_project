package com.yunnex.canteen.takeout.modle;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.yunnex.canteen.R;
import com.yunnex.canteen.common.CanteenApplication;
import com.yunnex.canteen.common.CanteenHttpUtil;
import com.yunnex.canteen.common.db.dao.CanteenDao;
import com.yunnex.canteen.common.polling.WorkTime;
import com.yunnex.canteen.common.utils.DialogManager;
import com.yunnex.canteen.takeout.base.Constant;
import com.yunnex.canteen.takeout.bean.Shop;
import com.yunnex.canteen.takeout.bean.Time;
import com.yunnex.canteen.takeout.http.request.DayBillRequest;
import com.yunnex.canteen.takeout.http.request.NoticePubReq;
import com.yunnex.canteen.takeout.http.request.RunningStatusSetResquest;
import com.yunnex.canteen.takeout.http.request.ShopTimeRequest;
import com.yunnex.canteen.takeout.http.response.DayBillResponse;
import com.yunnex.canteen.takeout.http.response.NoticeGettingRes;
import com.yunnex.canteen.takeout.http.response.ShopResponse;
import com.yunnex.canteen.takeout.http.response.ShopTimeResponse;
import com.yunnex.canteen.takeout.mng.TakeOutHttpUtils;
import com.yunnex.canteen.takeout.ui.CustomLodingView;
import com.yunnex.vpay.lib.http.HttpUtils;
import com.yunnex.vpay.lib.http.ResponseBase;
import com.yunnex.vpay.lib.log.output.VLogOutput;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.yunnex.canteen.takeout.base.Constant.urlDayBill;
import static com.yunnex.canteen.takeout.base.Constant.urlShop;


/**
 * Created by sungongyan on 2016/1/8.
 * wechat sun379366152
 */
public class ShopModleImpl implements IShopModel
{
	private static final String TAG = ShopModleImpl.class.getSimpleName();

	@Override
	public VPayUIRequestV2 getShop(final Context context, boolean isShow, final OnGetShopLisener listener)
	{
		VLogOutput.e(TAG, "ShopModleImpl getShop");

		VPayUIRequestV2 vPayUIRequestV2 = new VPayUIRequestV2<ShopResponse>(CanteenHttpUtil.getOrderUrl(context, urlShop), null, context, isShow)
		{
			@Override
			protected boolean onResponse(ShopResponse response)
			{
				VLogOutput.e(TAG, "ShopModleImpl getShop response code:" + response.getCode());


				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					final Shop shop = response.getResponse().getShop();
					final CanteenDao dao = new CanteenDao(context);

					VLogOutput.e(TAG, "ShopModleImpl getShop response succcess:" + shop);

					Observable.just("1").
							flatMap(new Func1<String, Observable<Boolean>>()
							{
								@Override
								public Observable<Boolean> call(String s)
								{
									return dao.isExsist(s);
								}
							}).
							subscribeOn(Schedulers.io()).
							observeOn(Schedulers.io()).
							subscribe(new Action1<Boolean>()
							{
								@Override
								public void call(Boolean aBoolean)
								{
									if (aBoolean)
									{
										//										dao.upDate(shop);
										dao.upDateWithoutRunTime(shop);
									}
									else
									{
										//构建一个id为1的shop对象
										Shop shop = new Shop();
										shop.setId("1");
										shop.setRunningTimes(shop.getRunningTimes());
										shop.setWmOpen(1);
										dao.add(shop);
									}
								}
							});
					listener.onSuccess(shop);
					return true;
				}
				return false;
			}
		};
		vPayUIRequestV2.setShouldCache(false);
		vPayUIRequestV2.send();
		return vPayUIRequestV2;
	}


	/**
	 * 在model层中看似有ui，但这里的ui并不存在于xml中，因此位置可灵活放置
	 *
	 * @param context
	 * @param platfomr
	 * @param actionType
	 * @param listener
	 * @return
	 */
	@Override
	public VPayUIRequestV2 changeRunningStatus(Context context, int platfomr, int actionType, final ChangeRunningStatusListener listener)
	{
		final CustomLodingView lodingView = new CustomLodingView(context);
		lodingView.setTVText("保存中...");
		lodingView.setPbVisible();
		final Dialog dialog = DialogManager.getInstance().
				showDialogWithView(context, 2, lodingView, R.style.dialog_loding_style, false);
		RunningStatusSetResquest re = new RunningStatusSetResquest();
		re.setPlatform(platfomr);
		re.setRunningStatus(actionType);
		VPayUIRequestV2 vPayUIRequestV2 = new VPayUIRequestV2<ResponseBase>(TakeOutHttpUtils.getWMProderUrl(TakeOutHttpUtils.FUN_RUNNING_STATUS_SETTING), re, context, false)
		{
			@Override
			protected boolean onResponse(ResponseBase response)
			{
				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					lodingView.setIVRightVisible();
					lodingView.setTVText("保存成功");
					listener.onSuccess();
				}
				else
				{
					lodingView.setIVWrongVisible();
					lodingView.setTVText("保存失败");
					listener.onFailed();
				}

				Observable.timer(500, TimeUnit.MILLISECONDS)//
						.subscribeOn(Schedulers.io())//
						.observeOn(AndroidSchedulers.mainThread())//
						.subscribe(new Action1<Long>()
						{
							@Override
							public void call(Long aLong)
							{
								if (dialog != null)
								{
									dialog.dismiss();
								}
							}
						});
				return false;
			}

			@Override
			protected void onResponseError(VolleyError error, Context context)
			{
				lodingView.setTVText("保存失败");
				lodingView.setIVWrongVisible();
				dialog.dismiss();
				listener.onFailed();
				super.onResponseError(error, context);
			}
		};
		vPayUIRequestV2.setShouldCache(false);
		vPayUIRequestV2.send();
		return vPayUIRequestV2;
	}

	/**
	 * 营业时间的获取分为两步：
	 * 1 获取原来老的外卖的时间
	 * 2 智慧餐厅的营业时间
	 * 将这两个数据源合并就是需要的时间
	 * 至于为什么这样搞，后台说数据不在同一个库
	 */
	@Override
	public VPayUIRequestV2 getShopRunningTime(final Context context, final ShopTimeGettingListener listener)
	{
		VPayUIRequestV2 vPayUIRequestV2 = new VPayUIRequestV2<ShopTimeResponse>(TakeOutHttpUtils.
				getWMProderUrl(TakeOutHttpUtils.FUN_TIME_GETTING), null, context, true)
		{
			@Override
			protected boolean onResponse(final ShopTimeResponse response)
			{
				switch (response.getCode())
				{
					case HttpUtils.CODE_SUCCESS:

						Log.w(Constant.TAG, "---->营业时间1获取成功。");
						getShopRunningTime2(response.getRunningTimes(), context, listener);
						return true;
					case HttpUtils.CODE_FAIL:
						Log.w(Constant.TAG, "---->营业时间获取失败。");
						return true;
				}
				return false;
			}
		};
		vPayUIRequestV2.setShouldCache(false);
		vPayUIRequestV2.send();
		return vPayUIRequestV2;
	}


	private VPayUIRequestV2 getShopRunningTime2(final List<Time> times, final Context context, final ShopTimeGettingListener listener)
	{
		String url = CanteenHttpUtil.getShopRunningTime(context, CanteenHttpUtil.FUN_Running_Time);
		VPayUIRequestV2 vPayUIRequestV2 = new VPayUIRequestV2<ShopTimeResponse>(url, null, context, false)
		{
			@Override
			protected boolean onResponse(final ShopTimeResponse response)
			{
				switch (response.getCode())
				{
					case HttpUtils.CODE_SUCCESS:

						Log.e(ShopModleImpl.TAG, "times1:" + times);
						List<Time> times2 = null;
						ShopTimeResponse.ShopHoursResponse hoursResponse = response.getResponse();
						if (hoursResponse != null)
						{
							times2 = hoursResponse.getShopHours();
						}

						Log.e(ShopModleImpl.TAG, "times2:" + times2);
						if (times2 != null)
						{
							times.addAll(times2);
						}

						//						Time--> WorkTime
						//						List<WorkTime> workTimes1 = new ArrayList<>();
						//						for (Time time : times)
						//						{
						//							WorkTime wt = new WorkTime();
						//							wt.startTime = time.getStartTime();
						//							wt.endTime = time.getEndTime();
						//							wt.change();
						//							workTimes1.add(wt);
						//						}
						//
						//						List<WorkTime> workTimes2 = new ArrayList<>();
						//						for (Time time : times2)
						//						{
						//							WorkTime wt = new WorkTime();
						//							wt.startTime = time.getStartTime();
						//							wt.endTime = time.getEndTime();
						//							wt.change();
						//							workTimes2.add(wt);
						//						}

						Log.w(Constant.TAG, "---->营业时间2获取成功。");
						final CanteenDao dao = new CanteenDao(context);
						Observable.just("1").
								flatMap(new Func1<String, Observable<Boolean>>()
								{
									@Override
									public Observable<Boolean> call(String s)
									{
										return dao.isExsist(s);
									}
								}).
								subscribeOn(Schedulers.io()).
								observeOn(Schedulers.io()).
								subscribe(new Action1<Boolean>()
								{
									@Override
									public void call(Boolean aBoolean)
									{
										if (aBoolean)
										{
											dao.upDateTime(times);
										}
										else
										{
											//构建一个id为1的shop对象
											Shop shop = new Shop();
											shop.setId("1");
											shop.setRunningTimes(times);
											shop.setWmOpen(1);
											dao.add(shop);
										}
									}
								});
						if (listener != null)
						{
							listener.onGetTime(times);
						}
						return true;
					case HttpUtils.CODE_FAIL:
						Log.w(Constant.TAG, "---->营业时间获取失败。");
						return true;
				}
				return false;
			}
		};
		vPayUIRequestV2.setShouldCache(false);
		vPayUIRequestV2.send();
		return vPayUIRequestV2;
	}


	@Override
	public VPayUIRequestV2 setShopRunningTime(final Context context, final List<Time> mShopTimeList, final ShopTimeSettingListener listener)
	{
		final CustomLodingView lodingView = new CustomLodingView(context);
		lodingView.setTVText("保存中");
		lodingView.setPbVisible();
		final Dialog dialog = DialogManager.getInstance().
				showDialogWithView(context, 2, lodingView, R.style.dialog_loding_style, false);
		ShopTimeRequest shopTimeRequest = new ShopTimeRequest();
		shopTimeRequest.setTime(mShopTimeList);
		VPayUIRequestV2 vPayUIRequestV2 = new VPayUIRequestV2<ShopTimeResponse>(TakeOutHttpUtils.
				getWMProderUrl(TakeOutHttpUtils.FUN_TIME_SETTING), shopTimeRequest, context, false)
		{
			@Override
			protected boolean onResponse(final ShopTimeResponse response)
			{
				boolean isSuccess = false;
				switch (response.getCode())
				{
					case HttpUtils.CODE_SUCCESS:
						Log.i(Constant.TAG, "---->营业时间设置成功。");
						lodingView.setTVText("保存成功");
						lodingView.setIVRightVisible();
						final CanteenDao dao = new CanteenDao(context);

						Observable.just("1").
								flatMap(new Func1<String, Observable<Boolean>>()
								{
									@Override
									public Observable<Boolean> call(String s)
									{
										return dao.isExsist(s);
									}
								}).
								subscribeOn(Schedulers.io()).
								observeOn(Schedulers.io()).
								subscribe(new Action1<Boolean>()
								{
									@Override
									public void call(Boolean aBoolean)
									{
										if (aBoolean)
										{
											dao.upDateTime(mShopTimeList);
										}
										else
										{
											//构建一个id为1的shop对象
											Shop shop = new Shop();
											shop.setId("1");
											shop.setRunningTimes(mShopTimeList);
											dao.add(shop);
										}
									}
								});

						listener.onSetting(true);
						break;
					case HttpUtils.CODE_FAIL:
						Log.w(Constant.TAG, "---->营业时间设置失败。");
						lodingView.setTVText("保存失败");
						lodingView.setIVWrongVisible();
						listener.onSetting(isSuccess);
						break;
					default:
						Log.w(Constant.TAG, "---->response.getCode()= " + response.getCode());
						break;
				}

				Observable.timer(500, TimeUnit.MILLISECONDS)//
						.subscribeOn(Schedulers.io())//
						.observeOn(AndroidSchedulers.mainThread())//
						.subscribe(new Action1<Long>()
						{
							@Override
							public void call(Long aLong)
							{
								if (dialog != null)
								{
									dialog.dismiss();
								}
							}
						});

				return isSuccess;
			}

			@Override
			protected void onResponseError(VolleyError error, Context context)
			{
				dialog.dismiss();
				super.onResponseError(error, context);
			}
		};
		vPayUIRequestV2.setShouldCache(false);
		vPayUIRequestV2.send();
		return vPayUIRequestV2;
	}

	@Override
	public VPayUIRequestV2 getShopDayBill(final Context context, String date, final DayBillCallback callback)
	{
		DayBillRequest body = new DayBillRequest();
		body.setCheckingDate(date);


		final VPayUIRequestV2<?> requestV2 = new VPayUIRequestV2<DayBillResponse>(CanteenHttpUtil.getOrderUrl(CanteenApplication.getmApplication(), urlDayBill), body, context, false)
		{
			@Override
			public boolean onResponse(DayBillResponse response)
			{
				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					callback.onGetDayBill(response.getResponse());
					return true;
				}
				else
				{
					Toast.makeText(context, response.getReason(), Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		};
		requestV2.setShouldCache(false);
		requestV2.send();
		return requestV2;
	}

	@Override
	public void publishNOtice(Context context, String notice, final NoticePubListener listener)
	{
		if (TextUtils.isEmpty(notice))
		{
			Toast.makeText(context, "输入公告内容", Toast.LENGTH_SHORT).show();
			return;
		}

		if (notice.length() >= 251)
		{
			Toast.makeText(context, "字数超过250个了", Toast.LENGTH_SHORT).show();
			return;
		}

		final CustomLodingView lodingView = new CustomLodingView(context);
		lodingView.setTVText("正在保存...");
		lodingView.setPbVisible();

		final Dialog dialog = DialogManager.getInstance().
				showDialogWithView(context, 2, lodingView, R.style.dialog_loding_style, false);


		NoticePubReq req = new NoticePubReq();
		req.setNotice(notice);

		VPayUIRequestV2 vPayUIRequestV2 = new VPayUIRequestV2<ResponseBase>(TakeOutHttpUtils.getWMProderUrl(TakeOutHttpUtils.FUN_NOTICE_SETTING), req, context, false)
		{

			@Override
			protected boolean onResponse(ResponseBase response)
			{

				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					Observable.timer(500, TimeUnit.MILLISECONDS)//
							.subscribeOn(Schedulers.io())//
							.observeOn(AndroidSchedulers.mainThread())//
							.subscribe(new Action1<Long>()
							{
								@Override
								public void call(Long aLong)
								{
									if (dialog != null)
									{
										dialog.dismiss();
										listener.onPubSuccess();
									}
								}
							});

					lodingView.setTVText("保存成功");
					lodingView.setIVRightVisible();
				}
				else
				{
					Observable.timer(500, TimeUnit.MILLISECONDS)//
							.subscribeOn(Schedulers.io())//
							.observeOn(AndroidSchedulers.mainThread())//
							.subscribe(new Action1<Long>()
							{
								@Override
								public void call(Long aLong)
								{
									if (dialog != null)
									{
										dialog.dismiss();
										listener.onPubFailed();
									}
								}
							});

					lodingView.setTVText("保存失败");
					lodingView.setIVWrongVisible();
					lodingView.setIVWrongVisible();
				}
				return false;
			}

			@Override
			protected void onResponseError(VolleyError error, Context context)
			{
				lodingView.setTVText("保存失败");
				lodingView.setIVWrongVisible();
				dialog.dismiss();
				super.onResponseError(error, context);
			}
		};
		vPayUIRequestV2.setShouldCache(false);
		vPayUIRequestV2.send();
	}

	@Override
	public void getNotice(Context context, final NoticeGettingListener listener)
	{
		VPayUIRequestV2 vPayUIRequestV2 = new VPayUIRequestV2<NoticeGettingRes>(TakeOutHttpUtils.getWMProderUrl(TakeOutHttpUtils.FUN_NOTICE_Getting), null, context, true)
		{

			@Override
			protected boolean onResponse(NoticeGettingRes response)
			{

				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					listener.onGettingNotice(response.getNotice());
				}
				else
				{
					listener.onGettingNotice(null);
				}
				return false;
			}
		};
		vPayUIRequestV2.setShouldCache(false);
		vPayUIRequestV2.send();
	}
}
