package com.yunnex.canteen.takeout.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunnex.canteen.R;

/**
 * Created by songdan on 2016/1/7.
 */
public class StoreNumLayout extends LinearLayout
{
	private Context mContext;

	private LinearLayout storenum_layout;
	public  EditText     storenum_et;
	public  TextView     storenum_remark;
	public  CheckBox     ch_keepsale, ch_sorenum;

	public String num;

	// 库存状态
	public int storeStatus;
	public static final int STATUS_KEEP_SALE = 0;
	public static final int STATUS_SET_NUM   = 1;


	public StoreNumLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		storenum_layout = (LinearLayout) findViewById(R.id.storenum_layout);
		storenum_et = (EditText) findViewById(R.id.storenum_et);
		storenum_et.addTextChangedListener(mTextWatcher);
		storenum_remark = (TextView) findViewById(R.id.storenum_remark);

		ch_keepsale = (CheckBox) findViewById(R.id.ch_keepsale);
		ch_sorenum = (CheckBox) findViewById(R.id.ch_setnum);

		//点击保持出售状态
		ch_keepsale.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				defaultCheckBox();
			}
		});

		//点击设置库存
		ch_sorenum.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				setStorenum(num);
			}
		});
	}

	// 勾选 保持出售状态
	public void defaultCheckBox()
	{
		storeStatus = STATUS_KEEP_SALE;

		ch_keepsale.setChecked(true);
		ch_sorenum.setChecked(false);
		storenum_et.setVisibility(GONE);
		storenum_remark.setVisibility(GONE);
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(storenum_et.getWindowToken(), 0);
	}

	// 勾选 设置库存
	public void setStorenum(final String num)
	{
		storeStatus = STATUS_SET_NUM;

		if (num != null)
			if (!num.equals("0"))
				storenum_et.setText(num);

		ch_sorenum.setChecked(true);
		ch_keepsale.setChecked(false);
		storenum_et.setVisibility(VISIBLE);
		storenum_layout.setBackgroundResource(R.drawable.shap_storenum_corner);
		storenum_remark.setVisibility(VISIBLE);
		storenum_et.requestFocus();
		InputMethodManager imm = (InputMethodManager) storenum_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
	}

	TextWatcher mTextWatcher = new TextWatcher()
	{
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
		}

		@Override
		public void afterTextChanged(Editable s)
		{
			//设置输入数字首位不能为0
			String text = s.toString();
			int len = s.toString().length();
			if (len == 1 && text.equals("0"))
			{
				s.clear();
			}
		}
	};

}
