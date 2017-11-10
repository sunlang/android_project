package com.yunnex.canteen.takeout.ui;

import android.content.Context;
import android.view.LayoutInflater;

import com.yunnex.canteen.takeout.bean.Dish;
import com.yunnex.canteen.R;
import com.yunnex.ui.dialog.CustomDialogBuilder;

/**
 * Created by songdan on 2016/1/12.
 */
public class StoreNumDialog extends CustomDialogBuilder
{
	private Context        mContext;
	public  Dish           mDish;
	private StoreNumLayout storeNumLayout;

	public StoreNumDialog(Context context, Dish dish)
	{
		super(context);
		mContext = context;
		mDish = dish;

		storeNumLayout = (StoreNumLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_storenum, null);
		storeNumLayout.num = mDish.getStoreNum();

		if (mDish != null)
		{
			switch (mDish.getSoldStatus())
			{
				// 出售中
				case Dish.DISH_STATUS_SALE:
				{
					// 保持出售
					if (mDish.getStoreNum() == null)
					{
						storeNumLayout.defaultCheckBox();
					}
					// 有库存限制
					else
					{
						String num = mDish.getStoreNum();
						storeNumLayout.setStorenum(num);
					}
					break;
				}
				// 已售完
				case Dish.DISH_STATUS_SOLD_OUT:
				{
					storeNumLayout.defaultCheckBox();
					break;
				}
			}
		}
		setView(storeNumLayout);
	}

	public String getEditText()
	{
		return storeNumLayout.storenum_et.getText().toString();
	}

	public int getStoreStatusChoose()
	{
		return storeNumLayout.storeStatus;
	}

	public void show()
	{
		super.show();
	}


}
