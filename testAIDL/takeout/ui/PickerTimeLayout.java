package com.yunnex.canteen.takeout.ui;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.yunnex.canteen.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xing.Zheng
 * @version 1.0
 * @Description: PickerTiem Dialog Layout
 * @created at 2016/1/11 14:44
 * @Copyright (c) 2015云移科技-版权所有
 */

public class PickerTimeLayout extends RelativeLayout
{
	public int sh, sm, eh, em;
	TimePicker mStartTimePicker;
	TimePicker mEndTimePicker;

	public PickerTimeLayout(Context context)
	{

		super(context);
		LayoutInflater mInflater = LayoutInflater.from(context);
		View myView = mInflater.inflate(R.layout.picker_time_layout, null);
		addView(myView);

		mStartTimePicker = (TimePicker) findViewById(R.id.startTimePicker);
		mStartTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
		mStartTimePicker.setIs24HourView(true);
		mStartTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener()
		{
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
			{
				sh = hourOfDay;
				sm = minute;
			}
		});
		sh = mStartTimePicker.getCurrentHour();
		sm = mStartTimePicker.getCurrentMinute();
		resizePikcer(mStartTimePicker);


		mEndTimePicker = (TimePicker) findViewById(R.id.endTimePicker);
		mEndTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
		mEndTimePicker.setIs24HourView(true);
		mEndTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener()
		{
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
			{
				eh = hourOfDay;
				em = minute;
			}
		});
		eh = mEndTimePicker.getCurrentHour();
		em = mEndTimePicker.getCurrentMinute();
		resizePikcer(mEndTimePicker);
	}

	public PickerTimeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PickerTimeLayout(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}


	public void setDefaultValue(int sh, int sm, int eh, int em)
	{
		mStartTimePicker.setCurrentHour(sh);
		mStartTimePicker.setCurrentMinute(sm);
		mEndTimePicker.setCurrentHour(eh);
		mEndTimePicker.setCurrentMinute(em);
	}

	private void resizePikcer(FrameLayout tp)
	{
		List<NumberPicker> npList = findNumberPicker(tp);
		for (NumberPicker np : npList)
		{
			resizeNumberPicker(np);
		}
	}

	private List<NumberPicker> findNumberPicker(ViewGroup viewGroup)
	{
		List<NumberPicker> npList = new ArrayList<NumberPicker>();
		View child = null;
		if (null != viewGroup)
		{
			for (int i = 0; i < viewGroup.getChildCount(); i++)
			{
				child = viewGroup.getChildAt(i);
				if (child instanceof NumberPicker)
				{
					npList.add((NumberPicker) child);
				}
				else if (child instanceof LinearLayout)
				{
					List<NumberPicker> result = findNumberPicker((ViewGroup) child);
					if (result.size() > 0)
					{
						return result;
					}
				}
			}
		}
		return npList;
	}

	private void resizeNumberPicker(NumberPicker np)
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((Build.VERSION.SDK_INT >= 21) ? 100 : 80, LayoutParams.WRAP_CONTENT);
		np.setLayoutParams(params);
	}

}
