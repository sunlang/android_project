package com.yunnex.canteen.takeout.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sungongyan on 2015/12/28.
 * wechat sun379366152
 */
public class FocusedTextView extends TextView
{

	public FocusedTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public FocusedTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public FocusedTextView(Context context)
	{
		super(context);
	}


	@Override
	public boolean isFocused()
	{
		return true;
	}
}
