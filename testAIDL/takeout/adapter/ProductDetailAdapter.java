package com.yunnex.canteen.takeout.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunnex.canteen.takeout.base.BaseExpandAdapter;
import com.yunnex.canteen.takeout.bean.Attach;
import com.yunnex.canteen.takeout.bean.ProductDetail;
import com.yunnex.canteen.common.utils.ViewHolderUtil;
import com.yunnex.canteen.R;

import java.util.List;

/**
 * Created by songdan on 2016/1/20.
 */
public class ProductDetailAdapter extends BaseExpandAdapter<ProductDetail, Attach>
{
	private int[]   flag;
	private Context mContext;

	public ProductDetailAdapter(Context context, List<ProductDetail> lists_group, List<List<Attach>> lists_child, int... flags)
	{
		super(context, lists_group, lists_child);
		flag = flags;
		mContext = context;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		ProductDetail details = lists_group.get(groupPosition);
		if (convertView == null)
		{
			convertView = View.inflate(context, R.layout.item_product, null);
		}
		TextView tv_product_name = ViewHolderUtil.get(convertView, R.id.tv_product_name);
		TextView tv_product_num = ViewHolderUtil.get(convertView, R.id.tv_num);
		TextView tv_product_price = ViewHolderUtil.get(convertView, R.id.tv_price);

		//微信外卖
		if (flag[0] == 4)
		{
			//赠菜
			if (details.getPriceSource() == 999)
			{
				tv_product_name.setText(String.format("%s", "(赠)" + details.getName()));
			}
			else
			{
				tv_product_name.setText(String.format("%s", details.getName()));
			}
			tv_product_num.setText(String.format("x%s", details.getNum()));
			tv_product_price.setText(String.format("￥%s", details.getSingleAmount() / 100.0));
		}
		else
		{
			//add specs,2016.06.30
			String specs = details.getSpecs();

			//add property 2017.10.12
			String property = details.getProperty();

			String concat;
			if (!TextUtils.isEmpty(property))
			{
				concat = TextUtils.isEmpty(specs) ? property : "（" + specs + "," + property + "）";
			}
			else
			{
				concat = TextUtils.isEmpty(specs) ? "" : "（" + specs + "）";
			}
			tv_product_name.setText(String.format("%s", details.getName()) + concat);
			tv_product_num.setText(String.format("x%s", details.getNum()));
			tv_product_price.setText(String.format("￥%s", details.getSingleAmount() / 100.0));
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		Attach attach = lists_child.get(groupPosition).get(childPosition);

		if (convertView == null)
		{
			convertView = View.inflate(context, R.layout.item_attach, null);
		}
		TextView tv_attach_name = ViewHolderUtil.get(convertView, R.id.attach_name);
		TextView tv_attach_num = ViewHolderUtil.get(convertView, R.id.attach_num);
		TextView tv_attach_price = ViewHolderUtil.get(convertView, R.id.attach_price);
		//微信外卖
		if (flag[0] == 4)
		{
			List<Attach.CheckAttr> checkAttrs = attach.getCheckAttrs();

			String name = "--" + attach.getName();
			String str = "";
			if (checkAttrs != null && checkAttrs.size() > 0)
			{
				name = name + "<br/>";
				for (Attach.CheckAttr arr : checkAttrs)
				{
					str += arr.getPname() + "(" + arr.getName() + ")" + "<br/>";
				}
			}
			str = "<font color='" + mContext.getResources().getColor(R.color.grey_2E2E2E) + "'>" + str + "</font>";
			//			tv_attach_name.setText(String.format("%s", Html.fromHtml(name + str)));
			tv_attach_name.setText(Html.fromHtml(name + "   " + str));
		}
		else
		{
			tv_attach_name.setText(String.format("%s", attach.getName()));
		}
		tv_attach_num.setText(String.format("x%s", attach.getNum()));
		tv_attach_price.setText(String.format("￥%s", attach.getSingleAmount() / 100.0));

		//		RelativeLayout rl = (RelativeLayout) convertView.findViewById(R.id.rl_sub);
		//		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rl.getLayoutParams();
		//		lp.height = DensityUtil.dp2px(context, 50);
		//		rl.setLayoutParams(lp);
		return convertView;
	}

}
