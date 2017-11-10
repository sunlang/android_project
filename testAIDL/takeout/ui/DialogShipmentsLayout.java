/**
 * @author reason
 * @date 2014-12-25
 */

package com.yunnex.canteen.takeout.ui;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.yunnex.canteen.R;
import com.yunnex.canteen.takeout.bean.DeliverStringConfig;
import com.yunnex.canteen.takeout.bean.Order;
import com.yunnex.canteen.takeout.http.response.SdcListResponse;
import com.yunnex.canteen.takeout.mng.TakeOutHttpUtils;
import com.yunnex.vpay.lib.http.HttpUtils;
import com.yunnex.vpay.lib.volley.VPayUIRequestV2;

import java.util.List;

public class DialogShipmentsLayout extends TableLayout
{

	// =======================================
	// Constants
	// =======================================
	private static final String DefaultSdcId = "0";

	// =======================================
	// Fields
	// =======================================
	private Spinner  mSpinner;
	private EditText mEditText;
	private List<DeliverStringConfig> mData = null;
	private ArrayAdapter<DeliverStringConfig> mSpinnerAdapter;
	public static final char[] CHARACTERS = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',};

	// =======================================
	// Constructors
	// =======================================
	public DialogShipmentsLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
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
		mSpinner = (Spinner) findViewById(R.id.spinner);
		mEditText = (EditText) findViewById(R.id.edit);
		mSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		mEditText.setEnabled(true);
		mEditText.setKeyListener(new NumberKeyListener()
		{
			protected char[] getAcceptedChars()
			{
				return CHARACTERS;
			}

			@Override
			public int getInputType()
			{
				return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
			}
		});
		VPayUIRequestV2<?> request = new VPayUIRequestV2<SdcListResponse>(TakeOutHttpUtils.getProderUrl(TakeOutHttpUtils.FUN_ORDER_SDC_LIST), null, getContext(), true)
		{
			@Override
			public boolean onResponse(SdcListResponse response)
			{
				if (response.getCode() == HttpUtils.CODE_SUCCESS)
				{
					mData = response.getExpressList();
					mSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, mData);
					mSpinner.setAdapter(mSpinnerAdapter);
					return true;
				}
				return false;
			}
		};
		request.setShouldCache(false);
		request.send();
	}

	// =======================================
	// Methods
	// =======================================
	public String getSdcId()
	{
		int pos = mSpinner.getSelectedItemPosition();
		if (mData == null || pos < 0 || pos >= mData.size())
		{
			return null;
		}
		return mData.get(pos).getId();
	}

	public String getDeliverNo()
	{
		int pos = mSpinner.getSelectedItemPosition();
		if (mData == null || pos < 0 || pos >= mData.size())
		{
			return null;
		}
		String s = null;
		if (TextUtils.isEmpty(mEditText.getText().toString()) && (mData.get(pos).getFlagInt() ==
				Order.ORDER_SDC_SHOP_DELIVER))
		{
			s = DefaultSdcId;
		}
		else
		{
			s = mEditText.getText().toString();
		}
		return s;
	}

	// =======================================
	// Inner Classes/Interfaces
	// =======================================
	class SpinnerSelectedListener implements OnItemSelectedListener
	{

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			if (mData != null)
			{
				//				mEditText.setEnabled(mData.get(arg2).getFlagInt() != Order.ORDER_SDC_SHOP_DELIVER);
				if (mData.get(arg2).getFlagInt() == Order.ORDER_SDC_SHOP_DELIVER)
					mEditText.setHint("可不填或填入配送人员手机");
				else
					mEditText.setHint("配送单号应为字母或数字");

			}
		}

		public void onNothingSelected(AdapterView<?> arg0)
		{
		}
	}
}
