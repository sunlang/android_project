package com.yunnex.canteen.takeout.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.yunnex.canteen.R;
import com.yunnex.canteen.takeout.base.BaseItitleCallback;
import com.yunnex.canteen.common.db.dao.CanteenDao;
import com.yunnex.canteen.common.BaseActivity;

public class BillNoteActivity extends BaseActivity
{
	private CanteenDao   mShopDao;
	private ToggleButton mToggleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_note);

		mShopDao = new CanteenDao(getApplicationContext());

		initTitle();

		initView();
	}

	private void initView()
	{
		mToggleButton = (ToggleButton) findViewById(R.id.toggle_btn);

		mToggleButton.setChecked(mShopDao.getLooperStatus("1"));

		mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					mShopDao.upDateLooper("1");
//					PullQueryServiceRequest.getInstance().setNeedLoop(true);
				}else
				{
					mShopDao.upDateLooper("0");
//					PullQueryServiceRequest.getInstance().setNeedLoop(false);
				}
			}
		});
	}

	private void initTitle()
	{
		((RelativeLayout) findViewById(R.id.title_container)).addView(customTitle);

		customTitle.setTitleTxt("订单提醒")//
				.setMidTextOff(titleTextPadingLeft)//
				.setIsTVRightVisible(false);

		customTitle.onclick(new BaseItitleCallback()
		{
			@Override
			public void onleftClik(View view)
			{
				finish();
			}
		});
	}
}
