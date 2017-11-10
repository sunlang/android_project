package com.yunnex.canteen.takeout.modle;

import android.content.Context;

import com.yunnex.canteen.takeout.bean.Dish;

/**
 * Created by sungongyan on 2016/1/15.
 * wechat sun379366152
 */
public interface IDishModel
{

	interface DishDetailCallBack
	{

		void onGetDish(Dish dish);

	}

	interface DishStoreNumCallback
	{
		void onSetStoreNumSuccess();
	}

	void getDishDetail(Context context, int dishId, DishDetailCallBack callBack);

	void setDishStoreNum(Context context, int dishId, String storeNum, int actionType, DishStoreNumCallback callback);
}
