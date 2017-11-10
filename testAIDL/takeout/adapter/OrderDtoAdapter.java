package com.yunnex.canteen.takeout.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunnex.canteen.R;
import com.yunnex.canteen.takeout.base.BaseRecycleAdapter;
import com.yunnex.canteen.takeout.bean.OrderDto;
import com.yunnex.canteen.takeout.bean.OrderDtoSub;
import com.yunnex.canteen.takeout.bean.StringConfig;
import com.yunnex.vpay.lib.utils.DateUtils;

import java.util.List;

/**
 * Created by sungongyan on 2015/12/23
 * wechat sun379366152
 */
public class OrderDtoAdapter extends BaseRecycleAdapter<OrderDto>
{
	private static final int    ITEM_TYPE_NO_DADA = 0;
	private static final int    ITEM_TYPE_DADA    = 1;
	private static final String TAG_F1            = "NewOrderFragment";
	private static final String TAG_F2            = "AllOrderFragment";
	private String  tag;
	private Context mContext;
	private String TAG = OrderDtoAdapter.class.getSimpleName();

	public OrderDtoAdapter(Context context, String tag)
	{
		super(context);
		this.tag = tag;
		mContext = context;
	}

	@Override
	public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		if (viewType == ITEM_TYPE_NO_DADA)
		{
			return new MyViewHolderNodata(mInflater.inflate(R.layout.item_order_no_data, parent, false));
		}
		return new MyViewHolder(mInflater.inflate(R.layout.item_order_new, parent, false));
	}

	@Override
	public void onBindViewHolder(BaseRecycleAdapter.BaseRecycleViewHolder holder, int position)
	{
		if (!(holder instanceof MyViewHolder))
		{
			return;
		}

		MyViewHolder holder1 = (MyViewHolder) holder;

		OrderDto order = getData(position);

		List<StringConfig> contactInfoVo = order.getContactInfoVo();
		String str = "";
		for (StringConfig sc : contactInfoVo)
		{
			String value = "<font color='" + mContext.getResources().getColor(R.color.grey_2E2E2E) + "'>" + sc.getValue() + "</font>";
			str += sc.getId() + value + "<br/>";
		}

		//动态数据
		holder1.mTv_dynamic_data.setText(Html.fromHtml(str));
		OrderDtoSub orderDto = order.getCyWmOrderDto();

		//首先这是什么平台订单？
		int orderType = orderDto.getOrderType();
		int sources = orderDto.getSources();

		//logo处理
		switch (sources)
		{
			case OrderDtoSub.SOURCE_BD:
				holder1.mIvLogo.setImageResource(R.mipmap.baiduwaimai);
				break;
			case OrderDtoSub.SOURCE_CPD:
				holder1.mIvLogo.setImageResource(R.drawable.tangshi);
				break;
			case OrderDtoSub.SOURCE_ELE:
				holder1.mIvLogo.setImageResource(R.mipmap.eleme);
				break;
			case OrderDtoSub.SOURCE_MT:
				holder1.mIvLogo.setImageResource(R.mipmap.meituanwaimai);
				break;
			case OrderDtoSub.SOURCE_POS:
				holder1.mIvLogo.setImageResource(R.drawable.tangshi);
				break;
			case OrderDtoSub.SOURCE_WX:
				holder1.mIvLogo.setImageResource(R.drawable.tangshi);
				break;
			case OrderDtoSub.SOURCE_WXWM:
				holder1.mIvLogo.setImageResource(R.mipmap.weixin);
				break;
			default:
				holder1.mIvLogo.setImageResource(R.drawable.ic_launcher);
				break;
		}

		//序列号
		int serialNo = orderDto.getSerialNo();
		holder1.mTvSerial.setText("#" + serialNo);

		//		[200,300)
		if (orderType >= OrderDtoSub.TYPE_WM && orderType < 300)
		{
			//外卖单
			int waimaiStatus = orderDto.getWaimaiStatus();
			switch (waimaiStatus)
			{
				case OrderDtoSub.STATU_WM_DQR:
					holder1.mTvStatus.setText("待确认");
					holder1.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
					break;
				case OrderDtoSub.STATU_WM_DPS:
					holder1.mTvStatus.setText("待配送");
					holder1.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
					break;
				case OrderDtoSub.STATU_WM_DPS_WFK:
					holder1.mTvStatus.setText("待配送(未付款)");
					holder1.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
					break;
				case OrderDtoSub.STATU_WM_DSH:
					holder1.mTvStatus.setText("待收货");
					holder1.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
					break;
				case OrderDtoSub.STATU_WM_DSH_WFK:
					holder1.mTvStatus.setText("待收货(未付款)");
					holder1.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
					break;
				case OrderDtoSub.STATU_WM_GB:
					holder1.mTvStatus.setText("关闭");
					holder1.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
					break;
				case OrderDtoSub.STATU_WM_WC:
					holder1.mTvStatus.setText("完成");
					holder1.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.fbutton_color_green_sea));
					break;
				default:
					holder1.mTvStatus.setText("");
					break;
			}
		}
		//		[100,200)
		else if (orderType < 200 && orderType >= OrderDtoSub.TYPE_TS)
		{
			//堂食单
			//堂食状态
			int status = orderDto.getOrderStatus();
			switch (status)
			{
				case OrderDtoSub.STATU_TS_DFK:
					holder1.mTvStatus.setText("待支付");
					holder1.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
					break;
				case OrderDtoSub.STATU_TS_YXD:
					holder1.mTvStatus.setText("已下单");
					holder1.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.fbutton_color_green_sea));
					break;
				case OrderDtoSub.STATU_TS_GB:
					holder1.mTvStatus.setText("关闭");
					holder1.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
					break;
				case OrderDtoSub.STATU_TS_WC:
					holder1.mTvStatus.setText("完成");
					holder1.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.fbutton_color_green_sea));
					break;
				case OrderDtoSub.STATU_TS_DSH://待审核
					holder1.mTvStatus.setText("待确认");
					holder1.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
					break;
				default:
					holder1.mTvStatus.setText("");
					break;
			}
		}
		//价格
		long payFee = orderDto.getPayFee();
		holder1.mTvPrice.setText("￥" + payFee / 100.0);
		holder1.mTvPrice.setTextSize(16);
		holder1.mTvPrice.setTextColor(mContext.getResources().getColor(R.color.black_alpha9));
		holder1.mTvPrice.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		//商品数量
		int amount = orderDto.getGoodsAmount();
		holder1.mTvNum.setText("(" + amount + "件商品)");
		//时间
		long createTime = orderDto.getCreateTime();
		holder1.mTvTime.setText(DateUtils.getSimpleTime(createTime));
		//角标处理
		holder1.mIv_book.setVisibility(View.INVISIBLE);
		//预订单,只在外卖单出现
		int sendImmediately = orderDto.getSendImmediately();
		//外卖单，且不立即送达，则为预定单
		//sendImmediately 1立即送达 2 预定单
		if (orderType >= OrderDtoSub.TYPE_WM && orderType < 300 && sendImmediately == 2)
		{
			holder1.mIv_book.setImageResource(R.drawable.yuding);
			holder1.mIv_book.setVisibility(View.VISIBLE);
		}

		/**
		 * 订单管理页的说明；
		 * 4.每条订单信息以独立卡片形式展示，
		 * 卡片内容参照上文（无“已付”角标，无“加菜角标”，
		 * 其他与上文相同）
		 */
		//只在新订单页面中出现,是否是加菜单，同时还有堂食完成单
		Log.d(TAG, "tag:" + tag);
		if (tag.equals(TAG_F1))
		{
			int isAddDish = orderDto.getIsAddDish();
			Log.d(TAG, "isAddDish:" + isAddDish);
			if (isAddDish == 1)
			{
				//加菜，待确认
				holder1.mTvStatus.setText("待确认");
				holder1.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
				holder1.mIv_book.setImageResource(R.drawable.jiacai);
				holder1.mIv_book.setVisibility(View.VISIBLE);
			}

			//订单状态是堂食完成单，（完成）
			int orderStatus = orderDto.getOrderStatus();
			if (orderType < 200 && orderType >= OrderDtoSub.TYPE_TS && orderStatus == OrderDtoSub.STATU_TS_WC)
			{
				holder1.mIv_book.setImageResource(R.drawable.yifu3x);
				holder1.mIv_book.setVisibility(View.VISIBLE);
			}
		}


		// 最后统一处理异常单
		//0 defalut,1 ing,2 failed,3,success
		int pushStatus = orderDto.getPushStatus();
		if (pushStatus == 2)
		{
			holder1.mTvStatus.setVisibility(View.INVISIBLE);
			holder1.mIvPushException.setVisibility(View.VISIBLE);
			holder1.mIvPushException.setImageResource(R.mipmap.push_exception);
			holder1.mIv_book.setVisibility(View.INVISIBLE);
		}
		else
		{
			holder1.mIvPushException.setVisibility(View.INVISIBLE);
			holder1.mTvStatus.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public int getItemCount()
	{
		if (getDatas().size() == 0)
		{
			return 1;
		}
		return super.getItemCount();
	}

	@Override
	public int getItemViewType(int position)
	{
		if (getDatas().size() == 0)
		{
			return ITEM_TYPE_NO_DADA;
		}
		return ITEM_TYPE_DADA;
	}

	private class MyViewHolder extends BaseRecycleViewHolder
	{

		private final ImageView mIvLogo;
		private final TextView  mTvSerial;
		private final TextView  mTvStatus;
		private final TextView  mTvPrice;
		private final TextView  mTvNum;
		private final TextView  mTvTime;
		private final TextView  mTv_dynamic_data;
		private final ImageView mIv_book;
		private final ImageView mIvPushException;

		private MyViewHolder(View itemView)
		{
			super(itemView);
			mIvLogo = (ImageView) itemView.findViewById(R.id.iv_logo);
			mIvPushException = (ImageView) itemView.findViewById(R.id.iv_push_exception);
			mTvSerial = (TextView) itemView.findViewById(R.id.tv_serial);
			mTvStatus = (TextView) itemView.findViewById(R.id.tv_status);
			mTvPrice = (TextView) itemView.findViewById(R.id.tv_price);
			mTvNum = (TextView) itemView.findViewById(R.id.tv_num);
			mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
			mTv_dynamic_data = (TextView) itemView.findViewById(R.id.tv_dynamic_data);
			mIv_book = (ImageView) itemView.findViewById(R.id.iv_book);
		}
	}

	private class MyViewHolderNodata extends BaseRecycleViewHolder
	{
		private MyViewHolderNodata(View itemView)
		{
			super(itemView);
		}
	}
}
