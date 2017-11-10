package com.yunnex.canteen.takeout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunnex.canteen.takeout.base.BaseListViewAdapter;
import com.yunnex.canteen.takeout.bean.DayBill;
import com.yunnex.canteen.common.utils.ViewHolderUtil;
import com.yunnex.canteen.R;

import java.util.List;

/**
 * 适配内容
 * Created by Administrator on 2015/9/28.
 */
public class DayBillAdapter extends BaseListViewAdapter<DayBill>
{

	public DayBillAdapter(Context context, List<DayBill> lists)
	{
		super(context, lists);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		DayBill item = lists.get(position);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_day_bill, parent, false);
		}

		TextView tv_name = ViewHolderUtil.get(convertView, R.id.name);
		TextView tv_orderNum = ViewHolderUtil.get(convertView, R.id.order_num);
		TextView tv_sum = ViewHolderUtil.get(convertView, R.id.sum);

		tv_name.setText(String.valueOf(item.getSourceName() == null ? "" : item.getSourceName()));
		tv_orderNum.setText(item.getCount() + "笔");
		tv_sum.setText(com.yunnex.vpay.lib.utils.PriceUtils.longToCurrency(item.getRealFee()) + "");

		return convertView;
	}
}
