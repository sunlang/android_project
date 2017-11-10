package com.yunnex.canteen.takeout.base;

import android.content.Context;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

public abstract class BaseExpandAdapter<G, C> extends BaseExpandableListAdapter
{

	public Context       context;
	public List<G>       lists_group;
	public List<List<C>> lists_child;

	public BaseExpandAdapter(Context context, List<G> lists_group, List<List<C>> lists_child)
	{
		super();
		this.context = context;
		this.lists_group = lists_group;
		this.lists_child = lists_child;
	}

	@Override
	public int getGroupCount()
	{
		return lists_group.size();
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		return lists_child.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return lists_group.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return lists_child.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return false;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}
}
