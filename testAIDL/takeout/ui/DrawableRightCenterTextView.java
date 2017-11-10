package com.yunnex.canteen.takeout.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by sungongyan on 2017/7/24.
 * qq 379366152
 */

public class DrawableRightCenterTextView extends AppCompatTextView
{

	public DrawableRightCenterTextView(Context context)
	{
		super(context);
	}

	public DrawableRightCenterTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public DrawableRightCenterTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		Drawable[] drawables = getCompoundDrawables();//left,top,right,bottom
		Drawable drawableRight = drawables[2];
		if (drawableRight != null)
		{
			float textWidth = getPaint().measureText(getText().toString());
			int drawablePadding = getCompoundDrawablePadding();
			int drawableWidth;
			drawableWidth = drawableRight.getIntrinsicWidth();
			float bodyWidth = textWidth + drawableWidth + drawablePadding;
			setPadding(0, 0, (int) (getWidth() - bodyWidth), 0);
			canvas.translate((getWidth() - bodyWidth) / 2, 0);
		}
		super.onDraw(canvas);
	}
}
