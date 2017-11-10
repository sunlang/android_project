package com.yunnex.canteen.takeout.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunnex.canteen.R;
import com.yunnex.canteen.common.utils.DensityUtil;


public class CustomTitle extends LinearLayout
{


	private RelativeLayout mRlDatePlatform;
	private TextView       mTvDate;
	private TextView       mTvPlatform;

	public interface ITitleCallback
	{
		/**
		 * 左边的ImageView点击事件,一般是返回
		 *
		 * @param view
		 */
		void onleftClik(View view);

		/**
		 * 最右边的文本点击事件
		 *
		 * @param view
		 */
		void onRightTVClick(View view);

		/**
		 * 最右边的ImageView点击事件
		 *
		 * @param view
		 */
		void onRightIVClick(View view);

		/**
		 * 从右往左第二个ImageVIew点击事件
		 *
		 * @param view
		 */
		void onRightIV2Click(View view);


		/**
		 * 日期点击
		 *
		 * @param view
		 */
		void onDateClick(View view);

		/**
		 * 平台点击
		 *
		 * @param view
		 */
		void onPlatformClick(View view);
	}


	private RelativeLayout root_layout;
	private ImageView      iv_left;
	private ImageView      iv_left_below;
	private TextView       tv_mid;
	private TextView       tv_right;
	private ImageView      iv_right;
	private ImageView      iv_right2;

	public CustomTitle(Context context)
	{
		super(context);
		initUI(context);
	}

	private void initUI(Context context)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.ui_titile, this);

		root_layout = (RelativeLayout) v.findViewById(R.id.root_layout);

		iv_left = (ImageView) v.findViewById(R.id.iv_left);
		iv_left_below = (ImageView) v.findViewById(R.id.iv_left_below);
		tv_mid = (TextView) v.findViewById(R.id.tv_mid);
		tv_right = (TextView) v.findViewById(R.id.tv_right);
		iv_right = (ImageView) v.findViewById(R.id.iv_right);
		iv_right2 = (ImageView) v.findViewById(R.id.iv_right2);

		mRlDatePlatform = (RelativeLayout) findViewById(R.id.rl_date_platform);
		mTvDate = (TextView) findViewById(R.id.tv_date);
		mTvPlatform = (TextView) findViewById(R.id.tv_platform);
	}

	public CustomTitle(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initUI(context);
	}

	/**
	 * 设置该公共头中各个控件的点击事件
	 *
	 * @param callback
	 */
	public void onclick(final ITitleCallback callback)
	{
		/**
		 * 左边的ImageView,一般是返回键点击事件
		 */
		iv_left.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				callback.onleftClik(v);
			}
		});

		/**
		 * 右边的TextView,文本点击事件
		 */
		tv_right.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				callback.onRightTVClick(v);
			}
		});

		/**
		 * 右边的ImageView点击事件
		 */
		iv_right.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				callback.onRightIVClick(v);
			}
		});

		/**
		 * 从右到左第二个ImageView点击事件
		 */
		iv_right2.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				callback.onRightIV2Click(v);
			}
		});

		mTvDate.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				callback.onDateClick(v);
			}
		});

		mTvPlatform.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				callback.onPlatformClick(v);
			}
		});

	}


	/**
	 * 设置日期文字
	 *
	 * @param txt
	 * @return
	 */
	public CustomTitle setDateText(String txt)
	{
		if (!TextUtils.isEmpty(txt))
		{
			mTvDate.setText(txt);
		}
		return this;
	}

	/**
	 * 设置平台文字
	 *
	 * @param txt
	 * @return
	 */
	public CustomTitle setPlatformText(String txt)
	{
		if (!TextUtils.isEmpty(txt))
		{
			mTvPlatform.setText(txt);
		}
		return this;
	}

	/**
	 * 設置日期平台組合的显示与否
	 *
	 * @param visible
	 * @return
	 */
	public CustomTitle setIsDateAndPlatformVisible(boolean visible)
	{
		if (visible)
		{
			mRlDatePlatform.setVisibility(View.VISIBLE);
		}
		else
		{
			mRlDatePlatform.setVisibility(View.GONE);
		}
		return this;
	}



	/**
	 * 设置中间控件的文本
	 */
	public CustomTitle setTitleTxt(String txt)
	{
		tv_mid.setText(txt + "");
		return this;
	}

	public CustomTitle setTitleTxt(int resId)
	{
		tv_mid.setText(resId);
		return this;
	}

	/**
	 * 设置右边TextView控件的文字
	 */
	public CustomTitle setRightText(String string)
	{
		tv_right.setText(string);
		return this;
	}

	/**
	 * /** 设置左边返回控件的隐藏与否 图片展示，则文本控件隐藏
	 */
	public CustomTitle setIsLeftVisible(boolean b)
	{
		if (b)
		{
			iv_left.setVisibility(View.VISIBLE);
			setIsIVRightVisible(!b);
		}
		else
		{
			iv_left.setVisibility(View.INVISIBLE);
		}
		return this;
	}


	/**
	 * 设置最右边文本控件的隐藏与否 文本展示，必然图片隐藏
	 */
	public CustomTitle setIsTVRightVisible(boolean b)
	{
		if (b)
		{
			tv_right.setVisibility(View.VISIBLE);
			setIsIVRightVisible(!b);
		}
		else
		{
			tv_right.setVisibility(View.INVISIBLE);

		}
		return this;
	}

	/**
	 * 设置最右边图片控件的隐藏与否 图片展示，必然右边文本隐藏
	 */
	public CustomTitle setIsIVRightVisible(boolean b)
	{
		if (b)
		{
			iv_right.setVisibility(View.VISIBLE);
			setIsTVRightVisible(!b);
		}
		else
		{
			iv_right.setVisibility(View.INVISIBLE);
		}
		return this;
	}


	/**
	 * 设置从右往左第二个ImageView控件的隐藏与否
	 */
	public CustomTitle setIsIV2RightVisible(boolean b)
	{
		if (b)
		{
			iv_right2.setVisibility(View.VISIBLE);
		}
		else
		{
			iv_right2.setVisibility(View.INVISIBLE);
		}
		return this;
	}

	/**
	 * 右边的ImageView图片的设置
	 */
	public CustomTitle setRightIvResource(int id)
	{
		iv_right.setImageResource(id);
		return this;
	}

	/**
	 * 设置从右往左第二个ImageView控件的图片的设置
	 */
	public CustomTitle setRightIv2Resource(int id)
	{
		iv_right2.setImageResource(id);
		return this;
	}


	/**
	 * 设置中间文本距离左边偏移量,单位dp,内部自动转为px
	 */
	public CustomTitle setMidTextOff(int left)
	{
		tv_mid.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		tv_mid.setPadding(DensityUtil.dp2px(getContext(), left), 0, 0, 0);
		return this;
	}

	/**
	 * 设置左边底部图片资源Resource
	 */
	public CustomTitle setLeftIVBelowResource(int id)
	{
		iv_left_below.setImageResource(id);
		return this;
	}

	/**
	 * 设置左边底部图片Drawable
	 */
	public CustomTitle setLeftIVBelowDrawable(Drawable drawable)
	{
		iv_left_below.setImageDrawable(drawable);
		return this;
	}

	/**
	 * 获取左边ImageView
	 */
	public ImageView getLeftIV()
	{
		return iv_left;
	}

	/**
	 * 设置整个头布局的颜色
	 *
	 * @return
	 */
	public CustomTitle setBackGroundColor(int color)
	{
		root_layout.setBackgroundColor(color);
		return this;
	}

	/**
	 * 设置右边textVIew的颜色
	 *
	 * @return
	 */
	public CustomTitle setRightTVBackground(int r)
	{
		tv_right.setBackgroundResource(r);
		return this;
	}

	/**
	 * 显示设置左边ImageView的大小
	 * 单位dp，内部自动转为px
	 *
	 * @return
	 */
	public CustomTitle setLeftIVLayoutParams(int width, int height)
	{
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) iv_left.getLayoutParams();
		lp.width = DensityUtil.dp2px(getContext(), width);
		lp.height = DensityUtil.dp2px(getContext(), height);
		iv_left.setLayoutParams(lp);
		return this;
	}
}
