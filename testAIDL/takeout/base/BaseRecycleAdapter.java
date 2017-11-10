package com.yunnex.canteen.takeout.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * BaseRecycleAdapter使用方法:
 * 1.自定义adapter extends BaseRecycleAdapter
 * 2.自定义ViewHolder作为adapter的内部类，并extends BaseRecycleViewHolder
 * 3.实现的onBindViewHolder方法中，将BaseRecycleViewHolder改为BaseRecycleAdapter.BaseRecycleViewHolder
 *
 * @param <T>
 */
public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleAdapter.BaseRecycleViewHolder>
{

	private List<T>                mDatas;
	public  LayoutInflater         mInflater;
	public  Context                mContext;
	public  OnItemClickListener    mOnItemClickListener;
	public  OnItemLongClickLitener mOnItemLongClickListener;


	public interface OnItemClickListener
	{
		void onItemClick(View view, int position);
	}

	public interface OnItemLongClickLitener
	{
		void onItemLongClick(View view, int position);
	}

	public void setOnItemClickLitener(OnItemClickListener litener)
	{
		mOnItemClickListener = litener;
	}

	public void setOnItemLongClickLitener(OnItemLongClickLitener litener)
	{
		mOnItemLongClickListener = litener;
	}

	public BaseRecycleAdapter(Context context)
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		mDatas = new ArrayList<>();
	}

	/**
	 * 添加一个对象
	 *
	 * @param position 添加对象的位置
	 * @param t        泛型数据
	 */
	public void addData(int position, T t)
	{
		mDatas.add(position, t);
		notifyItemInserted(position);
	}

	/**
	 * 添加批量数据
	 *
	 * @param postionStart 添加的起始位置
	 * @param list         被添加的数据源
	 */
	public void addDataRange(int postionStart, List<T> list)
	{
		mDatas.addAll(postionStart, list);
		if (mDatas.size() == list.size())
		{
			notifyDataSetChanged();
			return;
		}
		notifyItemRangeInserted(postionStart, list.size());
	}

	public BaseRecycleAdapter<T> clear()
	{
		mDatas.clear();
		return this;
	}

	public void removeData(int position)
	{
		mDatas.remove(position);
		notifyItemRemoved(position);
	}

	/**
	 * 获取某个位置数据对象
	 *
	 * @param pos
	 * @return
	 */
	public T getData(int pos)
	{
		if (pos >= mDatas.size())
		{
			return null;//等待视图更新,16ms
		}

		return mDatas.get(pos);
	}

	/**
	 * 返回数据集合
	 *
	 * @return
	 */
	public List<T> getDatas()
	{
		return mDatas;
	}

	@Override
	public int getItemCount()
	{
		return mDatas.size();
	}


	/**
	 * 移除批量数据
	 *
	 * @param postionStart 添加的起始位置
	 * @param itemCount    批量删除的数据个数
	 */
	public void removeDataRange(int postionStart, int itemCount)
	{
		for (int i = postionStart; i < postionStart + itemCount; i++)
		{
			mDatas.remove(postionStart);
		}
		notifyItemRangeRemoved(postionStart, itemCount);
	}

	public abstract class BaseRecycleViewHolder extends ViewHolder
	{
		public BaseRecycleViewHolder(final View itemView)
		{
			super(itemView);
			if (mOnItemClickListener != null)
			{
				itemView.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						mOnItemClickListener.onItemClick(itemView, getLayoutPosition());
					}
				});
			}
			if (mOnItemLongClickListener == null)
			{
				return;
			}
			itemView.setOnLongClickListener(new View.OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View v)
				{
					mOnItemLongClickListener.onItemLongClick(itemView, getLayoutPosition());
					//						getAdapterPosition();
					return false;
				}
			});
		}
	}
}