package com.yunnex.canteen.takeout.base;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BaseListViewAdapter<T> extends BaseAdapter
{

	public Context context;

	public List<T> lists;

	public BaseListViewAdapter(List<T> lists)
	{
		super();
		this.lists = lists;
	}

	public BaseListViewAdapter(Context context, List<T> lists)
	{
		super();
		this.context = context;
		this.lists = lists;
	}

	@Override
	public int getCount()
	{
		return lists.size();
	}

	@Override
	public Object getItem(int position)
	{
		return lists.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public BaseListViewAdapter clear()
	{
		lists.clear();
		return this;
	}

	public void addAll(List<T> datas)
	{
		lists.addAll(datas);
		notifyDataSetChanged();
	}
}
