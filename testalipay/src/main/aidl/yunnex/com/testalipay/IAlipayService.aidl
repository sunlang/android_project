package yunnex.com.testalipay;
import yunnex.com.testalipay.Diamond;
/**
 * Created by sungongyan on 2017/5/31.
 * qq 379366152
 */

interface IAlipayService
{
	 boolean payMoney(int money);
	 Diamond getDiamond();
	 boolean stopLoop();
}
