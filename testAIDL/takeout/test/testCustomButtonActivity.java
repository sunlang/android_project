package com.yunnex.canteen.takeout.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yunnex.canteen.takeout.bean.StringConfig;
import com.yunnex.canteen.takeout.ui.CustomButton;
import com.yunnex.canteen.R;

import java.util.ArrayList;
import java.util.List;

public class testCustomButtonActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_custom_button);

		CustomButton customButton = (CustomButton) findViewById(R.id.custom_button);


		customButton.onclick(new CustomButton.IButtonCallback()
		{
			@Override
			public void onTVxClick(View view, int i)
			{
			}
		});

		List<StringConfig> list_action=new ArrayList<>();
		list_action.add(new StringConfig("1","确认订单"));
		list_action.add(new StringConfig("2","取消订单"));
		list_action.add(new StringConfig("3","已送达"));

		List<StringConfig> actions_valed = customButton.setTextAndFlag(list_action);
		customButton.setBgUI(actions_valed.size());
		customButton.setWeight(actions_valed.size());
		customButton.setMargin(actions_valed.size());
	}
}
