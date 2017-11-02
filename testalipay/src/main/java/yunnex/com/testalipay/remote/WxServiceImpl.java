package yunnex.com.testalipay.remote;

import android.os.RemoteException;

import yunnex.com.testalipay.Diamond;
import yunnex.com.testalipay.IWxService;

/**
 * Created by sungongyan on 2017/7/7.
 * qq 379366152
 */

public class WxServiceImpl extends IWxService.Stub
{
	private int mMoney;

	@Override
	public boolean payMoney(int money) throws RemoteException
	{
		mMoney = money;
		return money > 0;
	}

	@Override
	public Diamond getDiamond() throws RemoteException
	{
		Diamond diamond=new Diamond();
		diamond.setNum(mMoney);
		diamond.setOtherArr("wx pay");
		return diamond;
	}

	@Override
	public boolean stopLoop() throws RemoteException
	{
		return false;
	}
}
