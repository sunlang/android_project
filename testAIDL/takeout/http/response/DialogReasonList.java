/**
 * @author reason
 * @date 2014-12-25
 */

package com.yunnex.canteen.takeout.http.response;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yunnex.canteen.common.CanteenHttpUtil;
import com.yunnex.canteen.takeout.bean.StringConfig;
import com.yunnex.canteen.takeout.mng.TakeOutHttpUtils;
import com.yunnex.canteen.R;
import com.yunnex.canteen.takeout.http.request.CancelReasonRequest;
import com.yunnex.vpay.lib.http.HttpUtils;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.util.List;

public class DialogReasonList extends RelativeLayout
{

	// =======================================
	// Constants
	// =======================================

	// =======================================
	// Fields
	// =======================================
	private ListView mList;
	private List<StringConfig> mData = null;
	private int type;
	private int setScreenHeight;

	// =======================================
	// Constructors
	// =======================================
	public DialogReasonList(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		setScreenHeight = wm.getDefaultDisplay().getHeight();
	}

	// =======================================
	// Setters/Getters
	// =======================================

	// =======================================
	// Methods from SuperClass/Interfaces
	// =======================================
	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		mList = (ListView) findViewById(R.id.list);
		mList.setItemsCanFocus(false);
		mList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	// =======================================
	// Methods
	// =======================================

	public void setType(int type)
	{
		this.type = type;
	}

	public void sendRequest()
	{

		CancelReasonRequest req = new CancelReasonRequest();
		req.setId(type);


		VPayUIRequestV2<?> request = new VPayUIRequestV2<ReasonListResponse>(CanteenHttpUtil.getProderUrl(CanteenHttpUtil.FUN_CANCEL_REASON), req, getContext(), true)
		{
			@Override
			public boolean onResponse(ReasonListResponse response)
			{
				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					ReasonListResponse.InnerResponse innerResponse = response.getResponse();
					if (innerResponse != null)
					{
						mData = innerResponse.getReasonList();
					}
					if (mData == null)
					{
						Toast.makeText(getContext(), "未获取到获取订单取消原因", Toast.LENGTH_SHORT).show();
						return false;
					}
					mList.setAdapter(new ArrayAdapter<StringConfig>(getContext(), android.R.layout.simple_list_item_single_choice, mData));
					setListViewHeightBasedOnChildren(mList, setScreenHeight / 3);//设置ListView高度为当前屏幕高度的1/3
					return true;
				}
				return false;
			}
		};
		request.setShouldCache(false);
		request.send();
	}

	public String getReason()
	{
		int pos = mList.getCheckedItemPosition();
		if (mData == null || pos < 0 || pos >= mData.size())
		{
			return null;
		}
		return mData.get(pos).getId();
	}

	//设置ListView的高度
	public static void setListViewHeightBasedOnChildren(ListView listView, int maxHeight)
	{
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
		{
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++)
		{
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		if (totalHeight > maxHeight)
		{
			params.height = maxHeight;
			listView.setLayoutParams(params);
		}
	}

	// =======================================
	// Inner Classes/Interfaces
	// =======================================

}
