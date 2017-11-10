package com.yunnex.canteen.takeout.mng;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yunnex.canteen.R;
import com.yunnex.canteen.common.utils.DensityUtil;
import com.yunnex.canteen.common.utils.SharePrefUtil;
import com.yunnex.canteen.common.utils.ViewHolderUtil;
import com.yunnex.canteen.takeout.base.BaseListViewAdapter;
import com.yunnex.canteen.takeout.base.Constant;
import com.yunnex.canteen.takeout.bean.OrderType;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sungongyan on 2015/12/17.
 * wechat sun379366152
 * 该类中的方法大部分与业务耦合，并非通用，do not modify
 */
public class PopuWindowMng<T>
{


	public interface OnPopuWindowDismissListener
	{
		void onDismiss(Object flag, int position_left, int positon_right);
	}

	private Context                     mContext;
	private PopupWindow                 popupWindow;
	private OnPopuWindowDismissListener listener;
	//	private boolean                     isClicking;//是否处于一次点击事件触发中，如是，屏蔽再次事件触发

	public PopuWindowMng(Context mContext, OnPopuWindowDismissListener listener)
	{
		this.mContext = mContext;
		this.listener = listener;
	}

	/**
	 * popuwindow包含的视图为ListView
	 *
	 * @param id
	 * @param anchor
	 * @param width
	 * @param xoff
	 * @param yoff
	 * @return
	 */
	public PopupWindow showPopuWindowWithListViewObj(int id, View anchor, List<T> data, int width, int xoff, int yoff)
	{
		final ListView contentView = new ListView(mContext);
		contentView.setDividerHeight(1);
		popupWindow = new PopupWindow(width, LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(false);
		if (popupWindow != null && popupWindow.isShowing())
		{
			return popupWindow;
		}

		switch (id)
		{
			case 0:// 上部
				//				AnimationController.slideInFromTop(contentView, 200, 0);
				ListPopuwindowObjAdapter adapter = new ListPopuwindowObjAdapter(data);
				contentView.setAdapter(adapter);
				popupWindow.showAsDropDown(anchor, xoff, yoff);
				break;

			case 1:// 中间
				break;

			case 2://下部
				break;

			default:
				break;
		}
		return popupWindow;
	}


	/**
	 * 次适配器专为本项目中fragmetn2中的订单来源和状态服务
	 */
	private class ListPopuwindowObjAdapter extends BaseListViewAdapter<T>
	{

		public ListPopuwindowObjAdapter(List<T> lists)
		{
			super(lists);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			final int selectedSourcePosition = SharePrefUtil.getInt(mContext, Constant.selectedSourcePosition, 0);
			final Object obj = lists.get(position);
			if (convertView == null)
			{
				convertView = View.inflate(mContext, R.layout.item_popuwindow, null);
			}
			final TextView tv = ViewHolderUtil.get(convertView, R.id.tv);
			final CheckBox cb = ViewHolderUtil.get(convertView, R.id.cb);

			RxView.clicks(convertView)//
					.throttleFirst(800, TimeUnit.MILLISECONDS)//
					.subscribe(new Action1<Void>()
					{
						@Override
						public void call(Void aVoid)
						{
							if (obj instanceof OrderType)
							{
								if (selectedSourcePosition == position)
								{
									popupWindow.dismiss();
									return;
								}

								SharePrefUtil.saveInt(mContext, Constant.selectedSourcePosition, Integer.MAX_VALUE);
								notifyDataSetChanged();
								cb.setChecked(true);
								SharePrefUtil.saveInt(mContext, Constant.selectedSourcePosition, position);
							}
							tv.setTextColor(mContext.getResources().getColor(R.color.blue_sky));

							Observable.timer(300, TimeUnit.MILLISECONDS)//
									.observeOn(AndroidSchedulers.mainThread())//
									.subscribe(new Action1<Long>()
									{
										@Override
										public void call(Long aLong)
										{
											popupWindow.dismiss();
											listener.onDismiss(obj, position, selectedSourcePosition);
										}
									});
						}
					});

			if (obj instanceof OrderType)
			{
				OrderType item = (OrderType) obj;
				tv.setText(item.getName());
				boolean ischecked;
				cb.setChecked(ischecked = selectedSourcePosition == position);
				if (ischecked)
				{
					tv.setTextColor(mContext.getResources().getColor(R.color.blue_sky));
				}
				else
				{
					tv.setTextColor(mContext.getResources().getColor(R.color.black));
				}
			}
			RelativeLayout rl = (RelativeLayout) convertView.findViewById(R.id.rl_sub);
			LayoutParams lp = (LayoutParams) rl.getLayoutParams();
			lp.height = DensityUtil.dp2px(mContext, 50);
			rl.setLayoutParams(lp);
			return convertView;
		}
	}
}
