package com.yunnex.canteen.takeout.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunnex.canteen.R;
import com.yunnex.canteen.common.BaseActivity;
import com.yunnex.canteen.common.utils.PrintUtilsManager;
import com.yunnex.canteen.takeout.adapter.DayBillAdapter;
import com.yunnex.canteen.takeout.base.BaseItitleCallback;
import com.yunnex.canteen.takeout.bean.DayBill;
import com.yunnex.canteen.takeout.http.response.DayBillResponse;
import com.yunnex.canteen.takeout.presenter.IShopPresenter;
import com.yunnex.canteen.takeout.presenter.ShopDayBillPresenter;
import com.yunnex.canteen.takeout.ui.CustomDateDialog;
import com.yunnex.vpay.lib.utils.PriceUtils;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.text.format.DateUtils.isToday;

/**
 * 日结账单
 */
public class DayBillActivity extends BaseActivity implements ShopDayBillPresenter.IShopDayBillView
{

	private static final SimpleDateFormat formatter         = new SimpleDateFormat("yyyy-MM-dd " + "HH:mm");
	public static final  SimpleDateFormat request_formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static final  SimpleDateFormat print_formatter   = new SimpleDateFormat("MM-dd " + "HH:mm");
	private static final SimpleDateFormat title_formatter   = new SimpleDateFormat("MM-dd ");

	private LinearLayout headerView;
	private TextView     footView;
	private ImageView    dateView;
	private TextView     timeView;
	private TextView     timeTwoView;
	private TextView     totalFeeView;
	private TextView     realFeeView;
	private TextView     realFeeTwoView;
	private TextView     countView;
	private TextView     countTwoView;
	private TextView     discountFeeView;

	private ListView                             lv_daybill;
	private DayBillAdapter                       dayBillAdapter;
	private DayBillResponse.DayBillResponseDeail dayBillResponse;
	private VPayUIRequestV2                      mRequestV2;

	private CustomDateDialog mDateDialog;
	private boolean isToday = true;
	private String checkedTime;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_bill2);
		initView();
		initData(Calendar.getInstance().getTimeInMillis());
	}

	private void initView()
	{
		initBottomBar();

		LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		headerView = (LinearLayout) lif.inflate(R.layout.head_view_day_bill, null);
		footView = (TextView) lif.inflate(R.layout.foot_view_day_bill, null);
		mBottomBar.addOrUpdateButton(1, "打印");
		mBottomBar.setListen(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (dayBillResponse != null)
					PrintUtilsManager.getInstance().printDayBillDetail(DayBillActivity.this, dayBillResponse);
				else
					Toast.makeText(DayBillActivity.this, "没有打印数据，稍后再试", Toast.LENGTH_SHORT).show();
			}
		});
		((RelativeLayout) findViewById(R.id.title_container)).addView(customTitle);

		customTitle.setTitleTxt("日结")//
				.setMidTextOff(titleTextPadingLeft)//
				.setIsTVRightVisible(false)//
				.setRightText("");

		customTitle.onclick(new BaseItitleCallback()
		{
			@Override
			public void onleftClik(View view)
			{
				finish();
			}

		});


		dateView = (ImageView) headerView.findViewById(R.id.date);
		dateView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showDateDialog();
			}
		});

		timeView = (TextView) headerView.findViewById(R.id.time);
		timeTwoView = (TextView) headerView.findViewById(R.id.time_two);
		totalFeeView = (TextView) headerView.findViewById(R.id.tv_totalFee);
		realFeeView = (TextView) headerView.findViewById(R.id.tv_realFee);
		realFeeTwoView = (TextView) headerView.findViewById(R.id.tv_realFee_two);
		countView = (TextView) headerView.findViewById(R.id.tv_count);
		countTwoView = (TextView) headerView.findViewById(R.id.tv_count_two);
		discountFeeView = (TextView) headerView.findViewById(R.id.tv_discountFee);
		lv_daybill = (ListView) findViewById(R.id.list);
		lv_daybill.addHeaderView(headerView);
		lv_daybill.addFooterView(footView);
		lv_daybill.setDivider(new ColorDrawable(getResources().getColor(R.color.window_bg)));
		lv_daybill.setDividerHeight(1);
	}

	private void showDateDialog()
	{
		{
			if (mDateDialog != null)
			{
				mDateDialog.show();
				return;
			}
			mDateDialog = new CustomDateDialog(this, new CustomDateDialog.OnSetRightListener()
			{
				@Override
				public void onSetRight(Long time)
				{
					isToday = isToday(time);
					if (isToday)
					{
						initData(Calendar.getInstance().getTimeInMillis());
					}
					else
					{
						initData(time);
					}
				}
			}, Calendar.getInstance());
			mDateDialog.show();
		}
	}

	/**
	 * 获取数据
	 *
	 * @param date 指定获取时间
	 */
	public void initData(long date)
	{
		checkedTime = request_formatter.format(date);
		IShopPresenter iShopPresenter = new ShopDayBillPresenter(this);
		mRequestV2 = iShopPresenter.getShopDayBill(mContext, checkedTime);
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

	@Override
	public void fillData(DayBillResponse.DayBillResponseDeail response)
	{
		response.setCheckedTime(checkedTime);
//		response.setStartTime(getDayBegin(response.getStartTime()));
//		response.setEndTime(getDayEnd(response.getEndTime()));
//		if (isToday)
//		{
//			//如果是“今天”，修改下时间结束数据
//			response.setEndTime(System.currentTimeMillis());
//		}
		dayBillResponse = response;

		timeView.setText(checkedTime + "营业概况");
		totalFeeView.setText(PriceUtils.longToCurrency(response.getTotalFee()));
		realFeeView.setText(PriceUtils.longToCurrency(response.getRealFee()));
		realFeeTwoView.setText(PriceUtils.longToCurrency(response.getRealFee()));
		countView.setText("" + response.getCount());
		countTwoView.setText(response.getCount() + "笔");
		discountFeeView.setText(PriceUtils.longToCurrency(response.getTotalDiscountFee()));
		timeTwoView.setText(response.getStartTime() + "-" + response.getEndTime());

		if (response.getSourceStatResult() == null)
			response.setSourceStatResult(new ArrayList<DayBill>());

		dayBillAdapter = new DayBillAdapter(getApplicationContext(), response.getSourceStatResult());
		lv_daybill.setAdapter(dayBillAdapter);

	}

	/**
	 * 时间戳所在天的起始时间
	 *
	 * @return
	 */
	public long getDayBegin(long timeStamp)
	{
//		Calendar cal = Calendar.getInstance();
//		cal.setTimeInMillis();
//		cal.set(Calendar.HOUR_OF_DAY, 0);
//		cal.set(Calendar.SECOND, 0);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.MILLISECOND, 001);
//		return new Timestamp(cal.getTimeInMillis());
		Calendar cal = Calendar.getInstance();
		Date date = new Date(timeStamp);
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime().getTime();
	}

	/**
	 * 时间戳所在天的结束时间
	 *
	 * @param timeStamp
	 * @return
	 */
	public long getDayEnd(long timeStamp)
	{
		Calendar cal = Calendar.getInstance();
		Date date = new Date(timeStamp);
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime().getTime();
	}
}
