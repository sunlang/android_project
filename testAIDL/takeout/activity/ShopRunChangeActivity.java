package com.yunnex.canteen.takeout.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunnex.canteen.takeout.base.BaseItitleCallback;
import com.yunnex.canteen.common.db.dao.CanteenDao;
import com.yunnex.canteen.R;
import com.yunnex.canteen.common.BaseActivity;
import com.yunnex.canteen.takeout.bean.Shop;
import com.yunnex.canteen.takeout.presenter.IShopPresenter;
import com.yunnex.canteen.takeout.presenter.ShopRunChangePresenter;
import com.yunnex.canteen.common.utils.DialogManager;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

/**
 * 店铺营业状态
 */
public class ShopRunChangeActivity extends BaseActivity implements View.OnClickListener, ShopRunChangePresenter.IShopRunChangeView
{
	private RelativeLayout  llBd;
	private TextView        tvBd;
	private RelativeLayout  llMt;
	private TextView        tvMt;
	private RelativeLayout  llEleme;
	private TextView        tvEleme;
	private VPayUIRequestV2 mRequestV2;
	private IShopPresenter  mPresenter;

	/**
	 * 除1 3 0外，其他状态不可操作
	 */
	private static final int STATUS_IN_BUSNISS = 1;//营业中
	private static final int STATUS_OFF_LINE   = 2;//下线
	private static final int STATUS_CLOSE      = 3;//暂停营业
	private static final int STATUS_NOT_OPEN   = 4;//未开通
	private static final int STATUS_DISABLE    = 5;//停用
	private static final int STATUS_AUDITING   = -1;//审核中
	private static final int STATUS_DEFAULT    = 0;//兼容上一版本，上版本中0代表CLOSE

	private int bdStatus;
	private int mtStatus;
	private int eleStatus;
	private int wxStatus;
	private RelativeLayout mRlWx;
	private TextView mTvWx;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_running);
		initView();
		initData();
	}

	private void initData()
	{
		mPresenter = new ShopRunChangePresenter(this);
		mRequestV2 = mPresenter.getShop(mContext, true);
	}

	private void initView()
	{
		customTitle.setTitleTxt("营业状态").
				setMidTextOff(titleTextPadingLeft).
				setIsTVRightVisible(false);

		customTitle.onclick(new BaseItitleCallback()
		{
			@Override
			public void onleftClik(View view)
			{
				finish();
			}
		});

		((RelativeLayout) findViewById(R.id.title_container)).addView(customTitle);
		llBd = (RelativeLayout) findViewById(R.id.ll_bd);
		tvBd = (TextView) findViewById(R.id.tv_bd);

		llMt = (RelativeLayout) findViewById(R.id.ll_mt);
		tvMt = (TextView) findViewById(R.id.tv_mt);

		llEleme = (RelativeLayout) findViewById(R.id.ll_eleme);
		tvEleme = (TextView) findViewById(R.id.tv_eleme);

		mRlWx = (RelativeLayout) findViewById(R.id.ll_wx);
		mTvWx = (TextView) findViewById(R.id.tv_wx);

		llBd.setOnClickListener(this);
		llMt.setOnClickListener(this);
		llEleme.setOnClickListener(this);
		mRlWx.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.ll_bd:
				handleStatus(bdStatus,1);
				break;
			case R.id.ll_mt:
				handleStatus(mtStatus,2);
				break;
			case R.id.ll_eleme:
				handleStatus(eleStatus,3);
				break;
			case R.id.ll_wx:
				handleStatus(wxStatus,4);
				break;
		}
	}

	private void handleStatus(int platformStatus,int platform)
	{
		if (platformStatus == STATUS_IN_BUSNISS || platformStatus == STATUS_CLOSE || platformStatus == STATUS_DEFAULT)
		{
			showDialogChangeStatus(platform);
		}
		else
		{
			Toast.makeText(mContext, "该状态不可操作", Toast.LENGTH_SHORT).show();
		}
	}


	/**
	 *
	 * @param platform
	 * 1 baidu
	 * 2 mt
	 * 3 eleme
	 * 4 wx
	 */
	private void showDialogChangeStatus(final int platform)
	{
		View view = View.inflate(getApplicationContext(), R.layout.dialog_change_status, null);
		final Dialog dialog = DialogManager.getInstance().showDialogWithView(mContext, 2, view, true);
		TextView tv1 = (TextView) view.findViewById(R.id.tv1);
		TextView tv2 = (TextView) view.findViewById(R.id.tv2);
		tv1.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
				mPresenter.changeRunningStatus(mContext, platform, STATUS_IN_BUSNISS);
			}
		});
		tv2.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
				//				mPresenter.changeRunningStatus(mContext, platform, 0);
				mPresenter.changeRunningStatus(mContext, platform, STATUS_CLOSE);
			}
		});
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
	public void fillData(Shop shop)
	{
		bdStatus = shop.getbDRunning();
		mtStatus = shop.getmTRunning();
		eleStatus = shop.getEleRunning();
		wxStatus = shop.getWxRunning();

		bindData4View(bdStatus, tvBd);
		bindData4View(mtStatus, tvMt);
		bindData4View(eleStatus, tvEleme);
		bindData4View(wxStatus, mTvWx);

		setLoopRuning(bdStatus,mtStatus,eleStatus,wxStatus);
	}

	/**
	 * 由店铺营业状态设置轮询服务的起用或者停用
	 * @param bdStatus
	 * @param mtStatus
	 * @param eleStatus
	 * @param wxStatus
	 */
	private void setLoopRuning(int bdStatus, int mtStatus, int eleStatus, int wxStatus)
	{
		boolean isBaiduAble=bdStatus == STATUS_IN_BUSNISS
				|| bdStatus == STATUS_CLOSE
				|| bdStatus == STATUS_DEFAULT;
		boolean isMtAble=mtStatus == STATUS_IN_BUSNISS
				|| bdStatus == STATUS_CLOSE
				|| bdStatus == STATUS_DEFAULT;
		boolean isElmAble=eleStatus == STATUS_IN_BUSNISS
				|| bdStatus == STATUS_CLOSE
				|| bdStatus == STATUS_DEFAULT;
		boolean isWxAble=wxStatus == STATUS_IN_BUSNISS
				|| bdStatus == STATUS_CLOSE
				|| bdStatus == STATUS_DEFAULT;

		CanteenDao shopDao = new CanteenDao(getApplicationContext());
		if (isBaiduAble||isMtAble||isElmAble||isWxAble)
		{
			//need loop
//			PullQueryServiceRequest.getInstance().setNeedLoop(true);
			shopDao.upDateLooper("1");
		}else
		{
			//need no loop
//			PullQueryServiceRequest.getInstance().setNeedLoop(false);
			shopDao.upDateLooper("0");
		}

		//test
//		Random random = new Random();
//		int i = random.nextInt(2);
//		if (i==0)
//		{
//			PullQueryServiceRequest.getInstance().setNeedLoop(false);
//		}else
//		{
//			PullQueryServiceRequest.getInstance().setNeedLoop(true);
//		}
	}

	private void bindData4View(int status, TextView v)
	{
		switch (status)
		{
			case STATUS_IN_BUSNISS:
				v.setText("营业中");
				v.setBackgroundResource(R.drawable.shap_blue_sky);

				break;
			case STATUS_OFF_LINE:
				v.setText("下线");
				v.setBackgroundResource(R.drawable.shape_gray);
				break;
			case STATUS_CLOSE:
				v.setText("暂停营业");
				v.setBackgroundResource(R.drawable.shape_pink);
				break;
			case STATUS_NOT_OPEN:
				v.setText("未开通");
				v.setBackgroundResource(R.drawable.shape_gray);
				break;
			case STATUS_DISABLE:
				v.setText("停用");
				v.setBackgroundResource(R.drawable.shape_gray);
				break;
			case STATUS_AUDITING:
				v.setText("审核中");
				v.setBackgroundResource(R.drawable.shape_gray);
				break;
			default:
				v.setText("暂停营业");
				v.setBackgroundResource(R.drawable.shape_pink);
				break;
		}
	}
}