package com.yunnex.canteen.takeout.ui;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by sungongyan on 2017/8/25.
 * qq 379366152
 */

public class AccurateHeightTextView extends android.support.v7.widget.AppCompatTextView
{
	private Context context;

	public AccurateHeightTextView(Context context)
	{
		super(context);
		this.context = context;
	}

	public AccurateHeightTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
	}

	public AccurateHeightTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		Layout layout = getLayout();
		if (layout != null)
		{
			int height = (int) Math.ceil(getMaxLineHeight(this.getText().toString())) + getCompoundPaddingTop() + getCompoundPaddingBottom();
			int width = getMeasuredWidth();
			setMeasuredDimension(width, height);
		}
	}

	private float getMaxLineHeight(String str)
	{
		float height;
		float screenW = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		float paddingLeft = ((LinearLayout) this.getParent()).getPaddingLeft();
		float paddingReft = ((LinearLayout) this.getParent()).getPaddingRight();
		int line = (int) Math.ceil((this.getPaint().measureText(str) / (screenW - paddingLeft - paddingReft)));
		height = (this.getPaint().getFontMetrics().descent - this.getPaint().getFontMetrics().ascent) * line;
		return height;
	}
}
