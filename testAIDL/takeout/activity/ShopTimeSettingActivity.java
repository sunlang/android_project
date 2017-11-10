package com.yunnex.canteen.takeout.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunnex.canteen.takeout.base.BaseItitleCallback;
import com.yunnex.canteen.R;
import com.yunnex.canteen.common.BaseActivity;
import com.yunnex.canteen.takeout.bean.Time;
import com.yunnex.canteen.takeout.modle.IShopModel;
import com.yunnex.canteen.takeout.modle.ShopModleImpl;
import com.yunnex.canteen.takeout.ui.PickerTimeLayout;
import com.yunnex.ui.dialog.BaseDialogBuilder;
import com.yunnex.ui.dialog.CustomDialogBuilder;
import com.yunnex.ui.dialog.CustomDialogLayout;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xing.Zheng
 * @version 1.0
 * @Description: 门店营业时间设置
 * @created at 2016/1/11 14:45
 * @Copyright (c) 2015云移科技-版权所有
 */

public class ShopTimeSettingActivity extends BaseActivity
{

	private static final String TAG = "TakeOut/ShopTimeSetting";

	private LayoutInflater mInflater;
	private TextView       mAddTimeTxt, mTitleHint;
	private ListView mShopTimeListView;

	private boolean             isChange             = false;
	// For shop time list
	private List<Time>          mShopTimeList        = new ArrayList<Time>();
	private ShopTimeListAdapter mShopTimeListAdapter = null;
	private Dialog          dialog;
	private VPayUIRequestV2 mRequestV2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_time_setting);
		initView();
		getShopTime();
	}

	private void initView()
	{
		((RelativeLayout) findViewById(R.id.title_container)).addView(customTitle);
//		customTitle.setTitleTxt("营业时间").
//				setMidTextOff(titleTextPadingLeft).
//				setRightTVBackground(R.drawable.selector_title).
//				setRightText("        保存       ").setIsTVRightVisible(false);

		customTitle.setTitleTxt("营业时间")//
				.setMidTextOff(titleTextPadingLeft)//
				.setIsIV2RightVisible(false)//
				.setIsIVRightVisible(false)//
				.setIsTVRightVisible(false)//
				.setRightText("");


		customTitle.onclick(new BaseItitleCallback()
		{
			@Override
			public void onleftClik(View view)
			{
				if (isChange)
				{
					showDialogCommon(R.drawable.icon_warning, null, "", "直接返回将不保存修改内容\n 确定返回？", new BaseCommonDialogListener()
					{
						@Override
						public void onConfirm(CustomDialogLayout layout)
						{
							finish();
						}
					});

				}
				else
				{
					finish();
				}
			}

			@Override
			public void onRightTVClick(View view)
			{
				if (mShopTimeList.size() == 0)
					return;
				if (checkTime())
				{
					setShopTime(mShopTimeList);
				}
				else
				{
					Toast.makeText(ShopTimeSettingActivity.this, R.string.shop_time_error_hint2, Toast.LENGTH_SHORT).show();
				}
			}
		});
		mTitleHint = (TextView) findViewById(R.id.title_hint);
		mAddTimeTxt = (TextView) findViewById(R.id.add_time_txt);
		mAddTimeTxt.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showPickerTimeDialog(8, 0, 22, 0, -1);
			}
		});
	}

	private void initListView()
	{
		customTitle.setIsTVRightVisible(true);
//		mTitleHint.setVisibility(View.VISIBLE);
		if (mShopTimeList.size() > 2)
		{
			mAddTimeTxt.setVisibility(View.INVISIBLE);
		}
		else
		{
			mAddTimeTxt.setVisibility((mShopTimeList.size() == 0) ? View.INVISIBLE : View.VISIBLE);
		}
		mShopTimeListView = (ListView) findViewById(R.id.shop_time_list);
		//mShopTimeListView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		mShopTimeListAdapter = new ShopTimeListAdapter(this);
		mShopTimeListView.setAdapter(mShopTimeListAdapter);
		mShopTimeListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Time time = mShopTimeList.get(position);
				showPickerTimeDialog(readTimeH(time.getStartTime()), readTimeM(time.getStartTime()), readTimeH(time.getEndTime()), readTimeM(time.getEndTime()), position);
			}
		});
	}


	private void getShopTime()
	{
		IShopModel shopModel = new ShopModleImpl();
		mRequestV2 = shopModel.getShopRunningTime(this, new IShopModel.ShopTimeGettingListener()
		{
			@Override
			public void onGetTime(List<Time> times)
			{
				mShopTimeList = times;
				initListView();
			}
		});
	}

	private void setShopTime(List<Time> mShopTimeList)
	{

		IShopModel shopModel = new ShopModleImpl();
		 mRequestV2= shopModel.setShopRunningTime(this, mShopTimeList, new IShopModel
			.ShopTimeSettingListener()
		{
			@Override
			public void onSetting(boolean b)
			{
				if (isChange)
				{
					isChange = !b;
				}
			}
		});
	}


	public void showPickerTimeDialog(int sh, int sm, int eh, int em, final int id)
	{
		final PickerTimeLayout pickerTimeLayout = new PickerTimeLayout(ShopTimeSettingActivity.this);
		if (id >= 0)
			pickerTimeLayout.setDefaultValue(sh, sm, eh, em);
		CustomDialogBuilder builder = new CustomDialogBuilder(ShopTimeSettingActivity.this);
		builder.setTitle("选择时间段");
		builder.setView(pickerTimeLayout);
		builder.setMessage("直接返回将不保存修改内容\n 确定返回？");
		builder.setNegativeButton(android.R.string.cancel, new BaseDialogBuilder.OnClickListener()
		{
			@Override
			public void onClick(AlertDialog alertDialog, CustomDialogLayout customDialogLayout, View view)
			{
				alertDialog.dismiss();
			}
		});
		builder.setPositiveButton(android.R.string.ok, new BaseDialogBuilder.OnClickListener()
		{
			@Override
			public void onClick(AlertDialog alertDialog, CustomDialogLayout customDialogLayout, View view)
			{

				savePickerTime(alertDialog, pickerTimeLayout.sh, pickerTimeLayout.sm, pickerTimeLayout.eh, pickerTimeLayout.em, id);
			}
		});
		builder.createDialog();
		builder.createView();
		builder.show();
	}

	private void savePickerTime(AlertDialog alertDialog, int sh, int sm, int eh, int em, final int id)
	{
		int sZong = sh * 3600 + sm * 60;
		int eZong = eh * 3600 + em * 60;
		if (eZong <= sZong)
		{
			Toast.makeText(ShopTimeSettingActivity.this, R.string.shop_time_error_hint1, Toast.LENGTH_SHORT).show();
			return;
		}
		alertDialog.dismiss();
		Time time = new Time();
		time.setStartTime(timeFomart(sh + ":" + sm));
		time.setEndTime(timeFomart(eh + ":" + em));

		if (id == -1)
		{
			mShopTimeList.add(time);
		}
		else
		{
			mShopTimeList.set(id, time);
		}

		mShopTimeListView.setAdapter(mShopTimeListAdapter);

		if (mShopTimeList.size() > 2)
		{
			mAddTimeTxt.setVisibility(View.INVISIBLE);
		}
		else
		{
			mAddTimeTxt.setVisibility(View.VISIBLE);
		}
		isChange = true;
	}

	private class ShopTimeListAdapter extends BaseAdapter
	{

		private Activity activity;

		public class ViewHolder
		{
			public TextView startTimeTxt, endTimeTxt;
			public ImageView deleteItemBtn;
			public View      line;
		}

		public ShopTimeListAdapter(Context context)
		{
			this.activity = (ShopTimeSettingActivity) context;
			mInflater = activity.getLayoutInflater();
		}

		@Override
		public int getCount()
		{
			return mShopTimeList.size();
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(final int position, View view, ViewGroup parent)
		{
			ViewHolder viewHolder = null;
			if (view == null)
			{
				viewHolder = new ViewHolder();
				view = mInflater.inflate(R.layout.shop_time_list_item, null);
				view.setPadding(20, 0, 20, 0);
				viewHolder.startTimeTxt = (TextView) view.findViewById(R.id.startTime_txt);
				viewHolder.endTimeTxt = (TextView) view.findViewById(R.id.endTime_txt);
				viewHolder.deleteItemBtn = (ImageView) view.findViewById(R.id.delete_btn);
				viewHolder.line = view.findViewById(R.id.divider_line);
				view.setTag(viewHolder);
			}
			else
			{
				viewHolder = (ViewHolder) view.getTag();
				if (viewHolder == null)
				{
					viewHolder = new ViewHolder();
					viewHolder.startTimeTxt = (TextView) view.findViewById(R.id.startTime_txt);
					viewHolder.endTimeTxt = (TextView) view.findViewById(R.id.endTime_txt);
					viewHolder.deleteItemBtn = (ImageView) view.findViewById(R.id.delete_btn);
					viewHolder.line = view.findViewById(R.id.divider_line);
					view.setTag(viewHolder);
				}
			}

			Time time = mShopTimeList.get(position);
			viewHolder.startTimeTxt.setText(time.getStartTime());
			viewHolder.endTimeTxt.setText(time.getEndTime());
			if (position == 0)
			{
				viewHolder.deleteItemBtn.setVisibility(View.INVISIBLE);
			}
			if (position != (mShopTimeList.size() - 1))
			{
				viewHolder.line.setBackgroundResource(R.drawable.list_dotted_line);
			}
			viewHolder.deleteItemBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mShopTimeList.remove(position);
					mShopTimeListAdapter.notifyDataSetChanged();
					isChange = true;
					if (mShopTimeList.size() > 2)
					{
						mAddTimeTxt.setVisibility(View.INVISIBLE);
					}
					else
					{
						mAddTimeTxt.setVisibility(View.VISIBLE);
					}
				}
			});
			return view;
		}
	}

	private String timeFomart(String time)
	{

		SimpleDateFormat _24time = new SimpleDateFormat("HH:mm");
		//SimpleDateFormat _12time = new SimpleDateFormat("hh:mm");
		try
		{
			String[] array = time.split(":");
			if (Integer.parseInt(array[0]) < 0 || Integer.parseInt(array[0]) > 23)
			{
				return null;
			}
			if (Integer.parseInt(array[1]) < 0 || Integer.parseInt(array[1]) > 59)
			{
				return null;
			}
			return _24time.format(_24time.parse(time));

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private int readTimeH(String value)
	{
		return Integer.parseInt(value.substring(0, 2));
	}

	private int readTimeM(String value)
	{
		return Integer.parseInt(value.substring(3, 5));
	}


	private boolean checkTime()
	{

		for (int i = (mShopTimeList.size() - 1); i >= 0; i--)
		{
			String time = mShopTimeList.get(i).getStartTime();
			String[] my = time.split(":");
			int hour = Integer.parseInt(my[0]);
			int min = Integer.parseInt(my[1]);

			int zong = hour * 3600 + min * 60;

			for (int j = 0; j < (mShopTimeList.size()); j++)
			{
				if (i == j)
					continue;
				String startTime = mShopTimeList.get(j).getStartTime();
				String endTime = mShopTimeList.get(j).getEndTime();

				String[] stime = startTime.split(":");
				int shour = Integer.parseInt(stime[0]);
				int smin = Integer.parseInt(stime[1]);
				int startZong = shour * 3600 + smin * 60;

				String[] etime = endTime.split(":");
				int ehour = Integer.parseInt(etime[0]);
				int emin = Integer.parseInt(etime[1]);
				int endZong = ehour * 3600 + emin * 60;

				if (zong >= startZong && zong < endZong)
				{
					return false;
				}
			}
		}
		return true;
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
}
