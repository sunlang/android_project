package yunnex.com.testalipay.remote;

import android.os.RemoteException;

import yunnex.com.testalipay.Diamond;
import yunnex.com.testalipay.IAlipayService;

/**
 * Created by sungongyan on 2017/7/7.
 * qq 379366152
 */

public class AlipayServiceImpl extends IAlipayService.Stub
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
		return new Diamond(mMoney * 10);
	}

	@Override
	public boolean stopLoop() throws RemoteException
	{
		return false;
	}
}
