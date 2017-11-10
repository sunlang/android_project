package com.yunnex.canteen.takeout.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.yunnex.canteen.R;
import com.yunnex.canteen.common.utils.AdapterViewUtils;
import com.yunnex.canteen.takeout.base.BaseListViewAdapter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sungongyan on 2017/8/24.
 * qq 379366152
 */

public class MapTextView extends LinearLayout
{

	private ListView mLlContainer;

	public MapTextView(Context context)
	{
		this(context, null);
	}

	public MapTextView(Context context, @Nullable AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public MapTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		initUI(context);
	}

	private void initUI(Context context)
	{
		View v = LayoutInflater.from(context).inflate(R.layout.ui_map_tview, this);
		mLlContainer = (ListView) v.findViewById(R.id.ll_container);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public MapTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
	{
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public void create(List<Map<String, String>> stringMaplist)
	{
		mLlContainer.setAdapter(new InnerAdapter(stringMaplist));
		AdapterViewUtils.setListViewHeightBasedOnChildren(mLlContainer);
	}

	private class InnerAdapter extends BaseListViewAdapter<Map<String, String>>
	{

		private InnerAdapter(List<Map<String, String>> lists)
		{
			super(lists);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = View.inflate(getContext(), R.layout.item_textview2, null);
			}

			TextView mTvLeft = (TextView) convertView.findViewById(R.id.tv_left);
			TextView mTvRight = (TextView) convertView.findViewById(R.id.tv_right);

			Map<String, String> map = lists.get(position);
			Iterator<Map.Entry<String, String>> entryIterator = map.entrySet().iterator();
			Map.Entry<String, String> next = entryIterator.next();

			//			String key = "<font color='" + mContext.getResources().getColor(R.color.colorAccent) + "'>" + next.getKey() + "</font>";

			mTvLeft.setText(Html.fromHtml(next.getKey()));
			mTvRight.setText(String.valueOf(next.getValue()));

			return convertView;
		}
	}
}
