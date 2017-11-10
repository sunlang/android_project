package com.yunnex.canteen.takeout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunnex.canteen.R;
import com.yunnex.canteen.takeout.bean.DishCategory;
import com.yunnex.vpay.lib.log.output.VLogOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lion on 16/1/5.
 */
public class DishCategoryAdapter extends BaseAdapter
{
	private static final String TAG = "DishCategoryAdapter";

	private Context            mContext;
	private LayoutInflater     mLayoutInflater;
	private List<DishCategory> mDishCategoryList;

	public DishCategoryAdapter(Context context, List<DishCategory> dishCategoryList)
	{
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (dishCategoryList != null)
		{
			mDishCategoryList = dishCategoryList;
		}
		else
		{
			mDishCategoryList = new ArrayList<>();
		}
	}

	@Override
	public int getCount()
	{
		return mDishCategoryList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mDishCategoryList.get(position);
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
			view = mLayoutInflater.inflate(R.layout.item_dish_category, null);

			holder = new ViewHolder();
			holder.dishCategoryNameTextView = (TextView) view.findViewById(R.id.dish_category_name);
			holder.dishNumTextView = (TextView) view.findViewById(R.id.dish_num);
			holder.dishNumSoldOutTextView = (TextView) view.findViewById(R.id.dish_num_sold_out);
			holder.rightArrowImageView = (ImageView) view.findViewById(R.id.right_arrow);

			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		DishCategory dishCategory = (DishCategory) getItem(position);
		if (dishCategory != null)
		{
			String name = dishCategory.getName();
			if (name != null)
				holder.dishCategoryNameTextView.setText(name);
			else
				holder.dishCategoryNameTextView.setText("");

			int dishNum = dishCategory.getGoodsNum();
			int dishNumSoldOut = dishCategory.getSoldOutNum();

			if (dishNumSoldOut == 0)
			{
				holder.dishNumTextView.setText(String.valueOf(dishNum) + "个菜品");
			}
			else
			{
				holder.dishNumTextView.setText(String.valueOf(dishNum) + "个菜品，");
			}

			if (dishNumSoldOut != 0)
				holder.dishNumSoldOutTextView.setText(String.valueOf(dishNumSoldOut) + "个沽清");
			else
				holder.dishNumSoldOutTextView.setText("");
		}

		VLogOutput.i(TAG, "position : " + position + "  " +
				"name : " + dishCategory.getName() + " " +
				"saleNum : " + dishCategory.getGoodsNum() + " " +
				"soldOutNum : " + dishCategory.getSoldOutNum());
		return view;
	}

	public static class ViewHolder
	{
		public TextView  dishCategoryNameTextView;
		public TextView  dishNumTextView;
		public TextView  dishNumSoldOutTextView;
		public ImageView rightArrowImageView;
	}
}
