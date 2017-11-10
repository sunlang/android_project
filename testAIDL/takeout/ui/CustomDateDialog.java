package com.yunnex.canteen.takeout.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by yasin
 * Email zhengyanxin@yunnex.com
 * 2015/12/14 15:49
 * <p/>
 * 用于显示最近45天的时间控件
 */
public class CustomDateDialog extends DatePickerDialog
{
	private final CharSequence       titleContent;
	private       Calendar           calendar;
	private final OnSetRightListener mOnSetRightListener;

	public CustomDateDialog(Context context, OnSetRightListener callBack, Calendar calendar)
	{
		super(context, null, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

		SpannableString s = new SpannableString("选择日期         可选最近45天");
		s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		titleContent = s;
		setTitle(titleContent);

		mOnSetRightListener = callBack;
		this.calendar = calendar;
		initDatePickerDialog();
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day)
	{
		super.onDateChanged(view, year, month, day);
		setTitle(titleContent);
	}

	/**
	 * 初始化日期控件
	 */
	private DatePickerDialog initDatePickerDialog()
	{
		Calendar calendarNew = Calendar.getInstance();
		calendarNew.add(Calendar.DAY_OF_YEAR, -44);
		getDatePicker().setMinDate(calendarNew.getTimeInMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		getDatePicker().setMaxDate(calendar.getTimeInMillis());

		setButton(DialogInterface.BUTTON_POSITIVE, "确定", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				DatePicker datePicker = getDatePicker();
				int year = datePicker.getYear();
				int month = datePicker.getMonth();
				int day = datePicker.getDayOfMonth();
				calendar.set(year, month, day);
				mOnSetRightListener.onSetRight(calendar.getTimeInMillis());
			}
		});
		return this;
	}


	public interface OnSetRightListener
	{

		void onSetRight(Long time);
	}
}
