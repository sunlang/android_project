package com.yunnex.canteen.takeout.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunnex.canteen.takeout.base.BaseItitleCallback;
import com.yunnex.canteen.takeout.ui.CustomButton;
import com.yunnex.canteen.R;
import com.yunnex.canteen.takeout.adapter.DiscoutAdapter;
import com.yunnex.canteen.takeout.adapter.ProductDetailAdapter;
import com.yunnex.canteen.common.BaseActivity;
import com.yunnex.canteen.takeout.bean.Attach;
import com.yunnex.canteen.takeout.bean.Order;
import com.yunnex.canteen.takeout.bean.ProductDetail;
import com.yunnex.canteen.takeout.bean.StringConfig;
import com.yunnex.canteen.takeout.http.response.DialogReasonList;
import com.yunnex.canteen.takeout.http.response.OrderAction;
import com.yunnex.canteen.takeout.presenter.IOrderPresenter;
import com.yunnex.canteen.takeout.presenter.OrderDetailPresenter;
import com.yunnex.canteen.common.utils.AdapterViewUtils;
import com.yunnex.canteen.takeout.mng.TakeOutUtils;
import com.yunnex.canteen.common.utils.ToastUtil;
import com.yunnex.canteen.takeout.ui.MapTextView;
import com.yunnex.ui.dialog.CustomDialogLayout;
import com.yunnex.ui.linearlistview.LinearListView;
import com.yunnex.vpay.lib.print.PrintUtil;
import com.yunnex.vpay.lib.utils.PriceUtils;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Songdan
 * 该类对外提供使用，因此单独防在这
 */
public class OrderDetailActivity extends BaseActivity implements OrderDetailPresenter.IOrderDetailView
{
	private static final String TAG = "OrderDetailActivity";
	private CustomButton customButton;
	private String       orderId;
	private Order        mOrder;
	public OrderDetailActivity mInstance = null;

	private TextView orderFrom;
	private TextView orderStatus;
	private TextView contact, text_contact;
	private TextView address, text_address;
	private TextView deliveryTime, text_deliveryTime;
	private TextView payType, text_payType;
	private TextView receipt, text_receipt;
	private TextView remark, text_remark;
	private TextView mealFee, text_mealFee;
	private TextView deliveryFee, text_deliveryFee;
	private TextView       amount;
	private TextView       actAmount;
	private TextView       order_num;
	private TextView       order_time;
	private TextView       discount_price_total;
	private TextView       actPayment;
	private RelativeLayout layoutDiscount;


	private ProductDetailAdapter mProductDetailAdapter;
	private ExpandableListView   product_list;
	private LinearListView       discount_list;
	//	private DiscoutAdapter       mDiscoutAdapter;
	private VPayUIRequestV2      mRequestV2;
	private IOrderPresenter      mOrderPresenter;
	//	private OrderModelImpl       mOrderModel;
	private TextView             mTvDeliveryPhone;
	private TextView             mTvCancelReason;
	private ImageView            mIvBook;
	private LinearLayout         mLinearFapiao;
	private MapTextView          mTvZengpin;
	private TextView             mTvDinnerNum;
	private TextView             mOrder_num_third;
	private int                  apiVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mInstance = this;
		setContentView(R.layout.activity_order_detail);
		initView();
		orderId = getIntent().getExtras().getString("orderId");

		apiVersion = getIntent().getExtras().getInt("apiVersion");
		//		orderId = getIntent().getStringExtra("orderId");
		if (orderId == null)
		{
			orderId = getIntent().getStringExtra("extra_eshop_id");//orderId from other apps
		}
		if (orderId == null)
		{
			orderId = getIntent().getStringExtra("extra");//orderId from other apps
		}

		Log.d("OrderDetailActivity", "orderId:" + orderId + ",apiVersion:" + apiVersion);
		initData(orderId);
	}

	private void initData(String orderId)
	{
		if (mOrderPresenter == null)
		{
			mOrderPresenter = new OrderDetailPresenter(this);
		}

		if (apiVersion == 1)
		{
			mOrderPresenter.orderDetail2(mContext, orderId);
			return;
		}

		mOrderPresenter.orderDetail(mContext, orderId);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		if (mRequestV2 != null)
		{
			mRequestV2.cancel();
		}
	}

	private void initView()
	{
		orderFrom = (TextView) findViewById(R.id.orderfrom);
		orderStatus = (TextView) findViewById(R.id.order_status);
		contact = (TextView) findViewById(R.id.persopn);
		address = (TextView) findViewById(R.id.address);
		deliveryTime = (TextView) findViewById(R.id.delivery_time);
		payType = (TextView) findViewById(R.id.payType);
		receipt = (TextView) findViewById(R.id.fapiao);
		remark = (TextView) findViewById(R.id.remak);
		mealFee = (TextView) findViewById(R.id.mealFee);
		deliveryFee = (TextView) findViewById(R.id.deliveryFee);
		amount = (TextView) findViewById(R.id.amount);
		actAmount = (TextView) findViewById(R.id.act_amount);
		order_num = (TextView) findViewById(R.id.order_num);
		order_time = (TextView) findViewById(R.id.order_time);
		text_contact = (TextView) findViewById(R.id.text_persopn);
		text_address = (TextView) findViewById(R.id.text_address);
		text_deliveryTime = (TextView) findViewById(R.id.text_delivery_time);
		text_payType = (TextView) findViewById(R.id.text_payType);
		text_receipt = (TextView) findViewById(R.id.text_fapiao);
		text_remark = (TextView) findViewById(R.id.text_remak);
		text_mealFee = (TextView) findViewById(R.id.text_mealFee);
		text_deliveryFee = (TextView) findViewById(R.id.text_deliveryFee);

		//        product_list = (LinearListView) findViewById(R.id.product_list);
		product_list = (ExpandableListView) findViewById(R.id.product_list);
		product_list.setGroupIndicator(null);//设置隐藏箭头
		product_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
		{
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
			{
				return true;
			}
		});
		discount_list = (LinearListView) findViewById(R.id.discount_list);
		discount_price_total = (TextView) findViewById(R.id.discount_price_total);
		layoutDiscount = (RelativeLayout) findViewById(R.id.discount_layout);
		actPayment = (TextView) findViewById(R.id.text_payment);

		((RelativeLayout) findViewById(R.id.title_container)).addView(customTitle);
		customTitle.setTitleTxt("订单详情")//
				.setMidTextOff(50)//
				.setIsTVRightVisible(false);

		customTitle.onclick(new BaseItitleCallback()
		{
			@Override
			public void onleftClik(View view)
			{
				finish();
			}
		});

		customButton = (CustomButton) findViewById(R.id.custom_button);
		customButton.onclick(new CustomButton.IButtonCallback()
		{
			@Override
			public void onTVxClick(View view, int i)
			{
				ToastUtil.showTestToast(mInstance, "flag" + ":" + i);

				onTVclickByFlag(i);
			}
		});

		//增加骑手电话和取消原因于2016.06.27
		mTvDeliveryPhone = (TextView) findViewById(R.id.tv_deliveryPhone);
		mTvCancelReason = (TextView) findViewById(R.id.tv_cancelReason);
		//增加订单预约图标 2016.7.29
		mIvBook = (ImageView) findViewById(R.id.iv_book);

		mLinearFapiao = (LinearLayout) findViewById(R.id.linear_fapiao);

		//赠品
		mTvZengpin = (MapTextView) findViewById(R.id.zengpin);
		//就餐人数
		mTvDinnerNum = (TextView) findViewById(R.id.tv_dinner_num);

		//第三方订单号
		mOrder_num_third = (TextView) findViewById(R.id.order_num_third);
	}

	private void onTVclickByFlag(final int flag)
	{
		switch (flag)
		{
			case 1:
				//确认订单操作
				orderConfirm();
				break;
			case 2:
				//开始配送
				orderSend();
				break;
			case 3:
				//已送达
				orderArrived();
				break;
			case 4:
				//取消订单
				orderCancel();
				break;
			case 5:
				//打印
				orderPrint(true);//true表示订单已确认
				break;
			case 6:
				//延迟订单，version5.2,2017.3.28,处理并打印,按照订单确认处理
				//				orderDelay();
				orderConfirm();
				break;
			case 20:
				orderReaded();
				break;
		}
	}

	/**
	 * 微信外卖的已读处理（走订单确认流程）
	 */
	private void orderReaded()
	{
		orderConfirm();
	}

	/**
	 * 延迟订单处理
	 * 因后台最初想新建接口处理，后面又回滚采用订单确认逻辑，因此该方法废弃
	 */

	@Deprecated
	private void orderDelay()
	{
		mOrderPresenter.orderDelay(mContext, orderId);
	}

	private void orderArrived()
	{
		if (apiVersion == 1)
		{
			mOrderPresenter.orderArrived2(mContext, orderId);
			return;
		}

		mOrderPresenter.orderArrived(mContext, orderId);
	}

	private void orderSend()
	{
		if (apiVersion == 1)
		{
			mOrderPresenter.orderSend2(mContext, null, null, orderId);
			return;
		}
		mOrderPresenter.orderSend(mContext, null, null, orderId);
	}

	private void orderConfirm()
	{
		if (apiVersion == 1)
		{
			mOrderPresenter.orderConfirm2(mContext, orderId);
			return;
		}
		mOrderPresenter.orderConfirm(mContext, orderId);
	}

	/**
	 * 打印
	 */
	private void orderPrint(boolean isConfirmed)
	{
		if (mOrder != null)
		{
			//判断当前订单的状态,如果是已取消，则截断打印
			//			if (Order.ORDER_STATUS_YQX == mOrder.getOrderStatus().getIdInteger())
			//			{
			//				Toast.makeText(this, "该订单已关闭", Toast.LENGTH_SHORT).show();
			//				return;
			//			}

			int times4Shop = PrintUtil.getTimes4Shop(mInstance, 3);
			int times4Deliver = PrintUtil.getTimes4Deliver(mInstance, 3);
			//如果isConfirmed为false，即订单未确认，此时如果打印的门店配置和配送员持有小票配置均为零，截断打印
			if (0 == times4Shop && 0 == times4Deliver)
			{
				Toast.makeText(this, "所有打印配置张数均为零", Toast.LENGTH_SHORT).show();
				return;
			}
			ToastUtil.showTestToast(mInstance, "shop:" + times4Shop + ",deliver:" + times4Deliver);

			OrderAction.getInstance().getPrintContent(mOrder, OrderDetailActivity.this, times4Shop, times4Deliver, isConfirmed);
		}
		else
		{
			Toast.makeText(OrderDetailActivity.this, "order is null", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 取消订单
	 */
	private void orderCancel()
	{
		DialogReasonList list = (DialogReasonList) LayoutInflater.from(mInstance).inflate(R.layout.dialog_reason_list, (ViewGroup) null);
		list.setType(1);//外卖中设置为1
		list.sendRequest();
		showDialogCommon(0, list, "选择订单取消的原因", "", new BaseCommonDialogListener()
		{
			@Override
			public void onConfirm(CustomDialogLayout layout)
			{
				DialogReasonList rl = (DialogReasonList) layout.getCustomView();
				cancelAction(rl.getReason(), orderId);
			}

			@Override
			public void onCancel()
			{
				super.onCancel();
			}
		});
	}

	/**
	 * 订单取消的真正操作
	 *
	 * @param reasonId
	 * @param mOrderId
	 */
	public void cancelAction(String reasonId, final String mOrderId)
	{
		if (apiVersion == 1)
		{
			mOrderPresenter.orderCancle2(mContext, reasonId, mOrderId);
			return;
		}
		mOrderPresenter.orderCancle(mContext, reasonId, mOrderId);
	}

	/**
	 * 获取菜品详情列表
	 *
	 * @param productDetailsList 菜品列表
	 * @param attchlist          浇头列表
	 */
	private void fillDataProduct(List<ProductDetail> productDetailsList, List<List<Attach>> attchlist, int platform)
	{
		if (mProductDetailAdapter == null)
		{
			mProductDetailAdapter = new ProductDetailAdapter(getApplicationContext(), productDetailsList, attchlist, platform);
			product_list.setAdapter(mProductDetailAdapter);
		}
		mProductDetailAdapter.notifyDataSetChanged();
		//默认展开子View
		for (int i = 0; i < mProductDetailAdapter.getGroupCount(); i++)
		{
			product_list.expandGroup(i);
		}
		AdapterViewUtils.setListViewHeightBasedOnChildren(product_list);

	}

	/**
	 * 更新详情整页数据
	 *
	 * @param order 订单
	 *              三方订单apiVersion==0，
	 *              无套餐概念，此处渲染数据
	 */
	@Override
	public void fillData(Order order)
	{
		mOrder = order;
		findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
		customButton.setVisibility(View.VISIBLE);

		List<StringConfig> list_action = order.getAction();

		ToastUtil.showTestToast(getApplicationContext(), list_action + "");
		List<List<Attach>> list_child = new ArrayList<>();

		if (null != order.getProductDetails())
		{
			for (int i = 0; i < order.getProductDetails().size(); i++)
			{
				if (order.getProductDetails().get(i).getElemeGarnish() == null)
				{
					List<Attach> list_temp = new ArrayList<>();
					list_child.add(list_temp);
				}
				else
				{
					list_child.add(order.getProductDetails().get(i).getElemeGarnish());// add null
				}
			}
			fillDataProduct(order.getProductDetails(), list_child, 0);
		}
		else
		{
			Toast.makeText(mContext, "订单暂无商品信息", Toast.LENGTH_SHORT).show();
		}

		if (order.getDiscountPrice() > 0)
			discount_price_total.setText("-" + PriceUtils.longToCurrency(order.getDiscountPrice()));
		else
			layoutDiscount.setVisibility(View.GONE);
		if (order.getOrderForm() != null)
		{
			// FIXME: 2016/1/28 订单来源序列号后台获取值类型为int（2.3时修改为string类型处理）
			int serialNumber = order.getOrderBd();//获取订单来源的序列号
			String serialNumberStr = serialNumber > TakeOutUtils.SERIALNUM_NULL ? "#" + serialNumber : "";
			orderFrom.setText(order.getOrderForm().getValue() + serialNumberStr);
			switch (order.getOrderForm().getId())
			{
				case OrderAction.PRINT_TYPE_BAIDU:
					orderFrom.setBackgroundResource(R.drawable.shap_red_corner);
					break;
				case OrderAction.PRINT_TYPE_MT:
					orderFrom.setBackgroundResource(R.drawable.shap_yellow_corner);
					break;
				case OrderAction.PRINT_TYPE_ELM:
					orderFrom.setBackgroundResource(R.drawable.shap_blue_corner);
					break;
				default:
					orderFrom.setBackgroundResource(R.drawable.shap_red_corner);
					break;
			}
		}
		switch (order.getOrderStatus().getIdInteger())
		{
			case Order.ORDER_STATUS_DQR:
				orderStatus.setTextColor(Color.RED);
				break;
			case Order.ORDER_STATUS_DPS:
				orderStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.blue));
				break;
			case Order.ORDER_STATUS_DSH:
			case Order.ORDER_STATUS_YWC:
			case Order.ORDER_STATUS_YQX:
				orderStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.grey));
				break;
			default:
				orderStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.grey));
				break;
		}
		orderStatus.setText(order.getOrderStatus().getValue() + "");
		if (!TextUtils.isEmpty(order.getPerson()) || !TextUtils.isEmpty(order.getPhone()))
		{
			contact.setText(order.getPerson().toString() + order.getPhone().toString());
			text_contact.setVisibility(View.VISIBLE);
			contact.setVisibility(View.VISIBLE);
		}

		if (!TextUtils.isEmpty(order.getAddress()))
		{
			address.setText(order.getAddress());
			text_address.setVisibility(View.VISIBLE);
			address.setVisibility(View.VISIBLE);
		}
		String arriveTime = order.getArriveTime() == null ? "立即送达" : order.getArriveTime();

		if (order.getDelivery() != null)
		{
			deliveryTime.setText(arriveTime + "(" + order.getDelivery().getValue() + ")");
		}

		//add 2016.7.29,is book order
		if (order.getArriveTime() == null)
		{
			mIvBook.setVisibility(View.GONE);
		}
		else
		{
			mIvBook.setVisibility(View.VISIBLE);
		}


		text_deliveryTime.setVisibility(View.VISIBLE);
		deliveryTime.setVisibility(View.VISIBLE);

		if (!TextUtils.isEmpty(order.getPayType().getValue()))
		{
			payType.setText(order.getPayType() + "");
			text_payType.setVisibility(View.VISIBLE);
			payType.setVisibility(View.VISIBLE);
		}
		if (!TextUtils.isEmpty(order.getReceipt()))
		{
			mLinearFapiao.setVisibility(View.VISIBLE);
			receipt.setText(order.getReceipt() + "");
			text_receipt.setVisibility(View.VISIBLE);
			receipt.setVisibility(View.VISIBLE);
		}
		else
		{
			mLinearFapiao.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(order.getRemark()))
		{
			remark.setText(order.getRemark() + "");
			text_remark.setVisibility(View.VISIBLE);
			remark.setVisibility(View.VISIBLE);
		}
		if ((order.getMealFee() > 0))
		{
			text_mealFee.setVisibility(View.VISIBLE);
			mealFee.setText(PriceUtils.longToCurrency(order.getMealFee()));
			mealFee.setVisibility(View.VISIBLE);
		}
		if (order.getDistributionCharge() > 0)
		{
			text_deliveryFee.setVisibility(View.VISIBLE);
			deliveryFee.setText(PriceUtils.longToCurrency(order.getDistributionCharge()));
			deliveryFee.setVisibility(View.VISIBLE);
		}

		if (!TextUtils.isEmpty(order.getDeliveryPhone()))
		{
			mTvDeliveryPhone.setVisibility(View.VISIBLE);
			mTvDeliveryPhone.setText("骑手电话:" + order.getDeliveryPhone());
		}
		else
		{
			mTvDeliveryPhone.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(order.getCancelReason()))
		{
			mTvCancelReason.setVisibility(View.VISIBLE);
			mTvCancelReason.setText("取消原因:" + order.getCancelReason());
		}
		else
		{
			mTvCancelReason.setVisibility(View.GONE);
		}


		amount.setText(PriceUtils.longToCurrency(order.getAmount()));
		actPayment.setText(PriceUtils.longToCurrency(order.getActPayment()));//用户实付金额
		actAmount.setText(PriceUtils.longToCurrency(order.getActAmount()));//商户实收金额
		TextPaint textPaint = actAmount.getPaint();
		textPaint.setFakeBoldText(true);

		order_num.setText("订  单  号：" + order.getId());
		if (!TextUtils.isEmpty(order.getPlatformOrderId()))
		{
			mOrder_num_third.setVisibility(View.VISIBLE);
			mOrder_num_third.setText("第三方单号：" + order.getPlatformOrderId());
		}
		else
		{
			mOrder_num_third.setVisibility(View.GONE);
		}
		order_time.setText("下单时间：" + order.getOrderTime());

		//赠品
		List<String> zengpinList = order.getZengpinList();
		if (zengpinList == null)
		{
			zengpinList = new ArrayList<>();
		}

		List<Map<String, String>> list = new ArrayList<>();


		for (int i = 0; i < zengpinList.size(); i++)
		{
			Map<String, String> map = new LinkedHashMap<>();

			String strI = "<font color='" + mContext.getResources().getColor(R.color.alpha0) + "'>" + i + "</font>";

			map.put("赠品:" + strI, zengpinList.get(i));
			list.add(map);
		}

		if (list.size() > 0)
		{
			mTvZengpin.setVisibility(View.VISIBLE);
			mTvZengpin.create(list);
		}
		else
		{
			mTvZengpin.setVisibility(View.GONE);
		}

		//String，不为null，不为“0”
		if (!TextUtils.isEmpty(order.getDinnersNum()) && !"0".equals(order.getDinnersNum()))
		{
			String dinnerNum = "就餐人数：<font color='" + mContext.getResources().getColor(R.color.black) + "'>" + order.getDinnersNum() + "</font>";
			mTvDinnerNum.setVisibility(View.VISIBLE);
			mTvDinnerNum.setText(Html.fromHtml(dinnerNum));
		}
		else
		{
			mTvDinnerNum.setVisibility(View.GONE);
		}

		//优惠券列表
		List<StringConfig> configList = order.getTakeoutDiscount();
		//		mDiscoutAdapter = new DiscoutAdapter(mContext,configList);
		discount_list.setAdapter(new DiscoutAdapter(mContext, configList));

		List<StringConfig> actions_valed = customButton.setTextAndFlag(list_action);
		customButton.setBgUI(actions_valed.size());
		customButton.setWeight(actions_valed.size());
		customButton.setMargin(actions_valed.size());
	}

	/**
	 * 微信订单有套餐概念，如果apiversion=1
	 * 此处渲染数据
	 *
	 * @param order
	 */
	@Override
	public void fillData2(Order order)
	{
		Log.d(TAG, "fillData2");
		findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
		customButton.setVisibility(View.VISIBLE);

		List<StringConfig> list_action = order.getAction();

		ToastUtil.showTestToast(getApplicationContext(), list_action + "");
		List<List<Attach>> list_child = new ArrayList<>();

		List<ProductDetail> productDetailList = order.getProductDetails();
		StringConfig orderForm = order.getOrderForm();
		if (null != productDetailList)
		{
			//微信外卖
			if (orderForm != null && "4".equals(orderForm.getId()))
			{
				mOrder = order = handleProductDetails(order, productDetailList);
			}
			else
			{
				for (ProductDetail p : productDetailList)
				{
					if (p.getElemeGarnish() == null)
					{
						list_child.add(new ArrayList<Attach>());
					}
					else
					{
						list_child.add(p.getElemeGarnish());
					}
				}
				fillDataProduct(productDetailList, list_child, 0);
			}
		}
		else
		{
			Toast.makeText(mContext, "订单暂无商品信息", Toast.LENGTH_SHORT).show();
		}

		if (order.getDiscountPrice() > 0)
			discount_price_total.setText("-" + PriceUtils.longToCurrency(order.getDiscountPrice()));
		else
			layoutDiscount.setVisibility(View.GONE);
		if (orderForm != null)
		{
			// FIXME: 2016/1/28 订单来源序列号后台获取值类型为int（2.3时修改为string类型处理）
			int serialNumber = order.getOrderBd();//获取订单来源的序列号
			String serialNumberStr = serialNumber > TakeOutUtils.SERIALNUM_NULL ? "#" + serialNumber : "";
			orderFrom.setText(orderForm.getValue() + serialNumberStr);
			switch (orderForm.getId())
			{
				case OrderAction.PRINT_TYPE_BAIDU:
					orderFrom.setBackgroundResource(R.drawable.shap_red_corner);
					break;
				case OrderAction.PRINT_TYPE_MT:
					orderFrom.setBackgroundResource(R.drawable.shap_yellow_corner);
					break;
				case OrderAction.PRINT_TYPE_ELM:
					orderFrom.setBackgroundResource(R.drawable.shap_blue_corner);
					break;
				default:
					orderFrom.setBackgroundResource(R.drawable.shap_red_corner);
					break;
			}
		}
		switch (order.getOrderStatus().getIdInteger())
		{
			case Order.ORDER_STATUS_DQR:
				orderStatus.setTextColor(Color.RED);
				break;
			case Order.ORDER_STATUS_DPS:
				orderStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.blue));
				break;
			case Order.ORDER_STATUS_DSH:
			case Order.ORDER_STATUS_YWC:
			case Order.ORDER_STATUS_YQX:
				orderStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.grey));
				break;
			default:
				orderStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.grey));
				break;
		}
		orderStatus.setText(order.getOrderStatus().getValue() + "");
		if (!TextUtils.isEmpty(order.getPerson()) || !TextUtils.isEmpty(order.getPhone()))
		{
			contact.setText(order.getPerson().toString() + order.getPhone().toString());
			text_contact.setVisibility(View.VISIBLE);
			contact.setVisibility(View.VISIBLE);
		}

		if (!TextUtils.isEmpty(order.getAddress()))
		{
			address.setText(order.getAddress());
			text_address.setVisibility(View.VISIBLE);
			address.setVisibility(View.VISIBLE);
		}
		String arriveTime = order.getArriveTime() == null ? "立即送达" : order.getArriveTime();

		if (order.getDelivery() != null)
		{
			deliveryTime.setText(arriveTime + "(" + order.getDelivery().getValue() + ")");
		}

		//add 2016.7.29,is book order
		if (order.getArriveTime() == null)
		{
			mIvBook.setVisibility(View.GONE);
		}
		else
		{
			mIvBook.setVisibility(View.VISIBLE);
		}


		text_deliveryTime.setVisibility(View.VISIBLE);
		deliveryTime.setVisibility(View.VISIBLE);

		if (!TextUtils.isEmpty(order.getPayType().getValue()))
		{
			payType.setText(order.getPayType() + "");
			text_payType.setVisibility(View.VISIBLE);
			payType.setVisibility(View.VISIBLE);
		}
		if (!TextUtils.isEmpty(order.getReceipt()))
		{
			mLinearFapiao.setVisibility(View.VISIBLE);
			receipt.setText(order.getReceipt() + "");
			text_receipt.setVisibility(View.VISIBLE);
			receipt.setVisibility(View.VISIBLE);
		}
		else
		{
			mLinearFapiao.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(order.getRemark()))
		{
			remark.setText(order.getRemark() + "");
			text_remark.setVisibility(View.VISIBLE);
			remark.setVisibility(View.VISIBLE);
		}
		if ((order.getMealFee() > 0))
		{
			text_mealFee.setVisibility(View.VISIBLE);
			mealFee.setText(PriceUtils.longToCurrency(order.getMealFee()));
			mealFee.setVisibility(View.VISIBLE);
		}
		if (order.getDistributionCharge() > 0)
		{
			text_deliveryFee.setVisibility(View.VISIBLE);
			deliveryFee.setText(PriceUtils.longToCurrency(order.getDistributionCharge()));
			deliveryFee.setVisibility(View.VISIBLE);
		}

		if (!TextUtils.isEmpty(order.getDeliveryPhone()))
		{
			mTvDeliveryPhone.setVisibility(View.VISIBLE);
			mTvDeliveryPhone.setText("骑手电话:" + order.getDeliveryPhone());
		}
		else
		{
			mTvDeliveryPhone.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(order.getCancelReason()))
		{
			mTvCancelReason.setVisibility(View.VISIBLE);
			mTvCancelReason.setText("取消原因:" + order.getCancelReason());
		}
		else
		{
			mTvCancelReason.setVisibility(View.GONE);
		}


		amount.setText(PriceUtils.longToCurrency(order.getAmount()));
		actPayment.setText(PriceUtils.longToCurrency(order.getActPayment()));//用户实付金额
		actAmount.setText(PriceUtils.longToCurrency(order.getActAmount()));//商户实收金额
		TextPaint textPaint = actAmount.getPaint();
		textPaint.setFakeBoldText(true);

		order_num.setText("订  单  号：" + order.getId());
		if (!TextUtils.isEmpty(order.getPlatformOrderId()))
		{
			mOrder_num_third.setVisibility(View.VISIBLE);
			mOrder_num_third.setText("第三方单号：" + order.getPlatformOrderId());
		}
		else
		{
			mOrder_num_third.setVisibility(View.GONE);
		}
		order_time.setText("下单时间：" + order.getOrderTime());

		//赠品
		/**
		 * 新接口的赠品不是直接取，需要从 productDetails[]对象中的priceSource取
		 */
		mTvZengpin.setVisibility(View.VISIBLE);
		List<String> zengpinList = order.getZengpinList();
		if (zengpinList == null)
		{
			zengpinList = new ArrayList<>();
		}

		//origin productDetailList
		List<ProductDetail> productDetails = productDetailList;
		if (productDetails != null && productDetails.size() > 0)
		{
			for (ProductDetail d : productDetails)
			{
				int priceSource = d.getPriceSource();
				if (priceSource == 999)
				{
					zengpinList.add(d.getName());
				}
			}
		}

		List<Map<String, String>> list = new ArrayList<>();

		for (int i = 0; i < zengpinList.size(); i++)
		{
			Map<String, String> map = new LinkedHashMap<>();

			String strI = "<font color='" + mContext.getResources().getColor(R.color.alpha0) + "'>" + i + "</font>";

			map.put("赠品:" + strI, zengpinList.get(i));
			list.add(map);
		}

		if (list.size() > 0)
		{
			mTvZengpin.setVisibility(View.VISIBLE);
			mTvZengpin.create(list);
		}
		else
		{
			mTvZengpin.setVisibility(View.GONE);
		}
		//另外如果是微信外卖就gong掉
		if (orderForm != null && "4".equals(orderForm.getId()))
		{
			mTvZengpin.setVisibility(View.GONE);
		}

		//String，不为null，不为“0”
		if (!TextUtils.isEmpty(order.getDinnersNum()) && !"0".equals(order.getDinnersNum()))
		{
			String dinnerNum = "就餐人数：<font color='" + mContext.getResources().getColor(R.color.black) + "'>" + order.getDinnersNum() + "</font>";
			mTvDinnerNum.setVisibility(View.VISIBLE);
			mTvDinnerNum.setText(Html.fromHtml(dinnerNum));
		}
		else
		{
			mTvDinnerNum.setVisibility(View.GONE);
		}

		//优惠券列表
		List<StringConfig> configList = order.getTakeoutDiscount();
		//		mDiscoutAdapter = new DiscoutAdapter(mContext,configList);
		discount_list.setAdapter(new DiscoutAdapter(mContext, configList));

		List<StringConfig> actions_valed = customButton.setTextAndFlag(list_action);
		customButton.setBgUI(actions_valed.size());
		customButton.setWeight(actions_valed.size());
		customButton.setMargin(actions_valed.size());
	}

	/**
	 * isPackage==1&&packageGoodsId==0代表套餐组
	 * isPackage==1&&packageGoodsId==某个组的id 代表套餐组的成员
	 * isPackage==0 代表普通菜品
	 * priceSource=999 代表赠菜
	 */
	@NonNull
	private Order handleProductDetails(Order order, List<ProductDetail> productDetailList)
	{
		List<ProductDetail> group = new ArrayList<>();
		List<List<Attach>> list_child = new ArrayList<>();
		for (ProductDetail g : productDetailList)
		{
			int isPackage = g.getIsPackage();
			int packageGoodsId = g.getPackageGoodsId();
			Log.d(TAG, "package:" + isPackage + ",packageGoodsId:" + packageGoodsId);

			//普通菜品也当做组
			if (packageGoodsId == 0)
			{
				group.add(g);
			}
		}
		Log.d(TAG, "productDetailList size:" + productDetailList.size());
		Log.d(TAG, "group size:" + group.size());

		for (ProductDetail g : group)
		{
			int gId = g.getId();
			List<ProductDetail> child = new ArrayList<>();
			for (ProductDetail item : productDetailList)
			{
				int packageGoodsId = item.getPackageGoodsId();
				if (packageGoodsId == gId)
				{
					child.add(item);
				}
			}
			Log.d(TAG, "group:" + gId + "的成员个数:" + child.size());
			List<Attach> t = new ArrayList<>();
			for (ProductDetail c : child)
			{
				Log.d(TAG, "group:" + gId + "的成员分别是:" + c.getId());
				Attach attach = new Attach();
				attach.setName(c.getName());
				attach.setNum(Integer.parseInt(c.getNum()));
				attach.setCheckAttrs(c.getCheckAttrs());
				t.add(attach);
			}
			g.setElemeGarnish(t);

			list_child.add(t);
		}
		order.setProductDetails(group);
		Log.d(TAG, "group:" + group);
		Log.d(TAG, "groupChild:" + list_child);
		fillDataProduct(group, list_child, 4);
		return order;
	}

	@Override
	public void onConfirmSuccess(boolean b)
	{
		orderPrint(b);
	}

	@Override
	public void onConfirmFailed(String reason)
	{
		Toast.makeText(OrderDetailActivity.this, reason, Toast.LENGTH_SHORT).show();
	}
}
