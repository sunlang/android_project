package com.yunnex.canteen.takeout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunnex.canteen.R;
import com.yunnex.canteen.takeout.bean.Dish;
import com.yunnex.canteen.takeout.mng.TakeOutUIUtils;
import com.yunnex.vpay.lib.utils.PriceUtils;

import java.util.ArrayList;
import java.util.List;

import static com.yunnex.canteen.takeout.bean.Dish.DISH_STATUS_SALE;
import static com.yunnex.canteen.takeout.bean.Dish.DISH_STATUS_SOLD_OUT;

/**
 * Created by lion on 16/1/6.
 */
public class DishIntroAdapter extends BaseAdapter
{
	private Context        mContext;
	private LayoutInflater mLayoutInflater;
	private List<Dish>     mDishList;

	public DishIntroAdapter(Context context, List<Dish> dishList)
	{
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (dishList != null)
		{
			mDishList = dishList;
		}
		else
		{
			mDishList = new ArrayList<>();
		}
	}

	@Override
	public int getCount()
	{
		return mDishList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mDishList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view;
		ViewHolder holder = null;

		if (convertView == null)
		{
			view = mLayoutInflater.inflate(R.layout.item_dish_intro, null);

			holder = new ViewHolder();
			holder.dishIconImageView = (ImageView) view.findViewById(R.id.dish_icon);
			holder.dishNameTextView = (TextView) view.findViewById(R.id.dish_name);
			holder.dishPriceTextView = (TextView) view.findViewById(R.id.dish_price);
			holder.dishNumTextView = (TextView) view.findViewById(R.id.dish_num);

			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		Dish dish = (Dish) getItem(position);
		if (dish != null)
		{
			String dishIcon = dish.getIcon();
			TakeOutUIUtils.displayImageView(mContext, dishIcon, holder.dishIconImageView);

			String dishName = dish.getName();
			if (dishName != null)
				holder.dishNameTextView.setText(dishName);
			else
				holder.dishNameTextView.setText("");

			holder.dishPriceTextView.setText(PriceUtils.longToCurrency(dish.getPrice()));

			// 下架
			if (dish.getOutOfStock())
			{
				holder.dishNameTextView.setTextColor(mContext.getResources().getColor(R.color.grey));
				holder.dishPriceTextView.setTextColor(mContext.getResources().getColor(R.color.grey));
				holder.dishNumTextView.setTextColor(mContext.getResources().getColor(R.color.grey));
				holder.dishNumTextView.setText("已下架");
			}
			// 没下架
			else
			{
				holder.dishNameTextView.setTextColor(mContext.getResources().getColor(R.color.black));
				holder.dishPriceTextView.setTextColor(mContext.getResources().getColor(R.color.black));

				int saleStatus = dish.getSoldStatus();
				switch (saleStatus)
				{
					// 设置颜色
					// 已售完
					case DISH_STATUS_SOLD_OUT:
					{
						holder.dishNumTextView.setTextColor(mContext.getResources().getColor(R.color.red));
						holder.dishNumTextView.setText("估清");
						break;
					}
					// 正常出售中
					case DISH_STATUS_SALE:
					{
						holder.dishNumTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
						holder.dishNumTextView.setText("出售中");
						break;
					}
					default:
					{
						holder.dishNumTextView.setTextColor(mContext.getResources().getColor(R.color.black));
					}
				}
			}
		}

		return view;
	}

	public static class ViewHolder
	{
		public ImageView dishIconImageView;
		public TextView  dishNameTextView;
		public TextView  dishPriceTextView;
		public TextView  dishNumTextView;

	}
}
