package com.yunnex.canteen.takeout.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunnex.canteen.R;


public class CustomLodingView extends LinearLayout
{

	private LinearLayout root_layout;
	private ProgressBar  progressBar;
	private ImageView    iv_right;
	private ImageView    iv_wrong;
	private TextView     tv_text;

	public CustomLodingView(Context context)
	{
		this(context, null);
	}

	public CustomLodingView(Context context, AttributeSet attrs)
	{
		this(context, null, 0);
	}

	public CustomLodingView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		initUI(context);
	}

	private void initUI(Context context)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_common, this);

		root_layout = (LinearLayout) v.findViewById(R.id.root_layout);

		progressBar = (ProgressBar) v.findViewById(R.id.pb);

		iv_right = (ImageView) v.findViewById(R.id.iv_right);
		iv_wrong = (ImageView) v.findViewById(R.id.iv_wrong);

		tv_text = (TextView) v.findViewById(R.id.tv_text);


	}


	/**
	 * pb visible
	 *
	 * @return
	 */
	public CustomLodingView setPbVisible()
	{
		iv_right.setVisibility(View.INVISIBLE);
		iv_wrong.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.VISIBLE);
		return this;
	}


	/**
	 * setIVRightVisible
	 *
	 * @return
	 */
	public CustomLodingView setIVRightVisible()
	{
		iv_right.setVisibility(View.VISIBLE);
		iv_wrong.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
		return this;
	}

	/**
	 * setIVWrongVisible
	 *
	 * @return
	 */
	public CustomLodingView setIVWrongVisible()
	{
		iv_right.setVisibility(View.INVISIBLE);
		iv_wrong.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
		return this;
	}

	/**
	 * set text 4 textview
	 *
	 * @param text
	 * @return
	 */
	public CustomLodingView setTVText(String text)
	{
		tv_text.setText(text + "");
		return this;
	}
}
