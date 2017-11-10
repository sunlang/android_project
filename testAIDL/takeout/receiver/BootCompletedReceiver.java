package com.yunnex.canteen.takeout.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yunnex.canteen.common.db.dao.CanteenDao;
import com.yunnex.canteen.takeout.service.BootCompleteService;
import com.yunnex.vpay.lib.log.output.VLogOutput;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class BootCompletedReceiver extends BroadcastReceiver
{
	private static final String TAG = BootCompletedReceiver.class.getSimpleName();

	public BootCompletedReceiver()
	{
	}

	@Override
	public void onReceive(final Context context, Intent intent)
	{
		VLogOutput.e(TAG, "外卖接受到开机广播:");
		//		Observable.just(0).//
		//				subscribeOn(Schedulers.io())//
		//				.observeOn(Schedulers.io())//
		//				.subscribe(new Action1<Integer>()
		//				{
		//					@Override
		//					public void call(Integer integer)
		//					{
		//						IShopPresenter mPresenter = new ShopRunChangePresenter(null);
		//						mPresenter.getShop(context);
		//					}
		//				});


		//		Observable.timer(20000, TimeUnit.MILLISECONDS)//
		//				.subscribeOn(Schedulers.io())//
		//				.observeOn(AndroidSchedulers.mainThread())//
		//				.subscribe(new Action1<Long>()
		//				{
		//					@Override
		//					public void call(Long aLong)
		//					{
		//						VLogOutput.e(TAG, "20s后请求门店数据:");
		//						IShopPresenter mPresenter = new ShopRunChangePresenter(null);
		//						mPresenter.getShop(context);
		//					}
		//				});

		//		IShopPresenter mPresenter = new ShopRunChangePresenter(null);
		//		mPresenter.getShop(context);

		//如果上一次退出shop数据库表未还原，即application执行的onterminate回调执行失败，则再次清除
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
							dao.deleteShopById("1");
						}
						BootCompleteService.startActionFoo(context, "", "");
					}
				});
	}
}
