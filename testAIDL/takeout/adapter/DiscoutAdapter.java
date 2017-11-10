package com.yunnex.canteen.takeout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunnex.canteen.takeout.base.BaseListViewAdapter;
import com.yunnex.canteen.common.utils.ViewHolderUtil;
import com.yunnex.canteen.R;
import com.yunnex.canteen.takeout.bean.StringConfig;

import java.util.List;

/**
 * Created by Administrator on 2015/12/26.
 */
public class DiscoutAdapter extends BaseListViewAdapter<StringConfig>
{
	public DiscoutAdapter(Context context, List list)
	{
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		StringConfig config = lists.get(position);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_discount, parent, false);
		}
		TextView name = ViewHolderUtil.get(convertView, R.id.discount_name);
		TextView discount = ViewHolderUtil.get(convertView, R.id.discount_price);

		// FIXME: 2016/1/27 优惠字段处理临时方案为：获取优惠字段值为string，且以元为单位
		name.setText(config.getId());
		name.setTextColor(context.getResources().getColor(R.color.black));
		Long disvountValue = Long.parseLong(config.getValue());
		discount.setText("-￥" + disvountValue / 100.0);
		//		discount.setText("-￥" + config.getValue());
		discount.setTextColor(context.getResources().getColor(R.color.red));
		return convertView;
	}
}
