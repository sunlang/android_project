package com.yunnex.canteen.takeout.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.yunnex.canteen.R;
import com.yunnex.canteen.takeout.bean.StringConfig;
import com.yunnex.ui.textview.FButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

public class CustomButton extends LinearLayout
{
	public interface IButtonCallback
	{
		void onTVxClick(View view, int i);
	}

	private static final int     MAX_BUTTON_NUM = 3;
	private              FButton tv[]           = new FButton[MAX_BUTTON_NUM];
	private              int     flag_tv[]      = new int[MAX_BUTTON_NUM];
	private int colorBlueSky;
	private int colorWhite;
	private boolean isCalling;

	public CustomButton(Context context)
	{
		super(context);
		initUI(context);
	}

	private void initUI(Context context)
	{
		View v = LayoutInflater.from(context).inflate(R.layout.ui_button, this);
		tv[0] = (FButton) v.findViewById(R.id.tv1);
		tv[1] = (FButton) v.findViewById(R.id.tv2);
		tv[2] = (FButton) v.findViewById(R.id.tv3);
		colorBlueSky = context.getResources().getColor(R.color.blue_light);
		colorWhite = context.getResources().getColor(R.color.white);
	}

	public CustomButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initUI(context);
	}

	/**
	 * 设置点击事件
	 *
	 * @param callback IButtonCallback
	 */
	public void onclick(final IButtonCallback callback)
	{
		for (int i = 0; i < tv.length; i++)
		{
			final int finalI = i;
			RxView.clicks(tv[i]).
					throttleFirst(2000, TimeUnit.MILLISECONDS).
					subscribe(new Action1<Void>()
					{
						@Override
						public void call(Void aVoid)
						{
							if (isCalling)
							{
								Observable.timer(1000,TimeUnit.MILLISECONDS)//狀態還原時間
								.subscribe(new Action1<Long>()
								{
									@Override
									public void call(Long aLong)
									{
										isCalling=false;
									}
								});
								return;
							}
							isCalling=true;
							Observable.timer(1000,TimeUnit.MILLISECONDS)//互斥時間
									.subscribe(new Action1<Long>()
									{
										@Override
										public void call(Long aLong)
										{
											isCalling=false;
										}
									});
							callback.onTVxClick(tv[finalI], flag_tv[finalI]);
						}
					});
		}
	}

	private void setTVTextColor(int index, int color)
	{
		tv[index].setTextColor(color);
	}

	/**
	 * 整体UI控制，与业务相关
	 *
	 * @param size size
	 */
	public void setBgUI(int size)
	{
		switch (size)
		{
			case 0:
				setVisibility(View.INVISIBLE);
				break;
			case 1:
				setIsTVVisible(0, true);
				setTVBackground(0, colorWhite);
				setTVTextColor(0, colorBlueSky);
				break;
			case 2:
				setIsTVVisible(0, true);
				setIsTVVisible(1, true);

				this.setTVBackground(0, colorWhite);
				this.setTVTextColor(0, colorBlueSky);

				this.setTVBackground(1, colorBlueSky);
				this.setTVTextColor(1, colorWhite);
				break;
			case 3:

				setIsTVVisible(0, true);
				setIsTVVisible(1, true);
				setIsTVVisible(2, true);

				this.setTVBackground(0, colorWhite);
				this.setTVTextColor(0, colorBlueSky);

				this.setTVBackground(1, colorWhite);
				this.setTVTextColor(1, colorBlueSky);

				this.setTVBackground(2, colorBlueSky);
				this.setTVTextColor(2, colorWhite);
				break;
		}

	}


	private void setIsTVVisible(int index, boolean b)
	{
		if (b)
		{
			tv[index].setVisibility(View.VISIBLE);
		}
		else
		{
			tv[index].setVisibility(View.INVISIBLE);
		}
	}


	private void setTVBackground(int index, int r)
	{
		tv[index].setButtonColor(r);
	}

	/**
	 * 整体权重控制，与业务相关
	 *
	 * @param size size
	 */
	public void setWeight(int size)
	{
		switch (size)
		{
			case 0:
				break;
			case 1:
				setTVWeight(0, 3);
				break;
			case 2:
				setTVWeight(0, 1);
				setTVWeight(1, 2);
				break;
			case 3:
				setTVWeight(0, 1);
				setTVWeight(1, 1);
				setTVWeight(2, 1);
				break;
		}
	}


	private void setTVWeight(int index, int weight)
	{
		LinearLayout.LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT, weight);
		tv[index].setLayoutParams(lp);
	}

	/**
	 * 整体边距控制，与业务相关
	 *
	 * @param size
	 */
	public void setMargin(int size)
	{

		//还原
		LinearLayout.LayoutParams ll1 = (LayoutParams) tv[0].getLayoutParams();
		LinearLayout.LayoutParams ll2 = (LayoutParams) tv[1].getLayoutParams();
		LinearLayout.LayoutParams ll3 = (LayoutParams) tv[2].getLayoutParams();

		ll1.setMargins(0, 0, 0, 0);
		ll2.setMargins(0, 0, 0, 0);
		ll3.setMargins(0, 0, 0, 0);

		int margin = 12;
		switch (size)
		{
			case 0:

				break;
			case 1:
				ll1.setMargins(margin, 0, margin, 0);
				tv[0].setLayoutParams(ll1);
				break;
			case 2:
				ll1.setMargins(margin, 0, margin, 0);
				tv[0].setLayoutParams(ll1);

				ll2.setMargins(margin, 0, margin, 0);
				tv[1].setLayoutParams(ll2);
				break;
			case 3:

				ll1.setMargins(margin, 0, margin, 0);
				tv[0].setLayoutParams(ll1);

				ll2.setMargins(margin, 0, margin, 0);
				tv[1].setLayoutParams(ll2);

				ll3.setMargins(margin, 0, margin, 0);
				tv[2].setLayoutParams(ll3);
				break;
		}
	}

	/**
	 * set the text and click flag
	 *
	 * @param list_action list_action
	 * @return List<StringConfig>
	 */
	public List<StringConfig> setTextAndFlag(List<StringConfig> list_action)
	{
		List<StringConfig> actions_valed = new ArrayList<>();

		if (list_action == null || 0 == list_action.size())
		{
			return actions_valed;
		}

		for (int i = 0; i < list_action.size(); i++)
		{
			if (list_action.get(i) != null)
			{
				actions_valed.add(list_action.get(i));
			}
		}

		/**
		 * the size of the valid list must >=1
		 */
		for (int i = 0; i < actions_valed.size(); i++)
		{
			initTvFromData(i, actions_valed.get(i));
		}
		return actions_valed;
	}

	private void initTvFromData(int index, StringConfig sc)
	{
		setTVFlag(index, Integer.parseInt(sc.getId()));
		//		setTVText(index, sc.getId()+":"+sc.getValue());
		setTVText(index, sc.getValue());
	}

	private void setTVFlag(int i, int value)
	{
		flag_tv[i] = value;
	}

	private void setTVText(int index, String string)
	{
		tv[index].setText(string);
	}
}
