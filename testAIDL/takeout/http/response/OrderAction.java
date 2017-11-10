/* @author reason
 * @date Feb 14, 2014
 */

package com.yunnex.canteen.takeout.http.response;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.yunnex.canteen.takeout.bean.ProductDetail;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.yunnex.printlib.HardwareUtils;
import com.yunnex.printlib.PrintImageNonViewAware;
import com.yunnex.printlib.PrintUtils;
import com.yunnex.canteen.takeout.bean.Attach;
import com.yunnex.canteen.takeout.bean.Order;
import com.yunnex.canteen.takeout.bean.StringConfig;
import com.yunnex.canteen.takeout.mng.TakeOutUtils;
import com.yunnex.vpay.lib.print.PrintUtil;
import com.yunnex.vpay.lib.utils.PriceUtils;
import com.yunnex.vpay.lib.utils.RegisterUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OrderAction
{
	// =======================================
	// Constants
	// =======================================
	public static final String PRINT_TYPE_BAIDU = "1";
	public static final String PRINT_TYPE_MT    = "2";
	public static final String PRINT_TYPE_ELM   = "3";

	private static final String           CHINESE_REGEX = "[^\\x00-\\xff]";
	private static final java.lang.String PRODER_PRINT  = "vpay_print_3";
	private int length;
	private int lengthSet      = 4;//设置菜品数量长度最少为占4个字符，即两个中文字符
	private int lengthSetForZ5 = 6;//设置菜品数量长度最少为占4个字符，即两个中文字符
	// =======================================
	// Fields
	// =======================================
	private        PrintImageNonViewAware mPrintImageNonViewAware;
	private        Bitmap                 barCodeBm;
	private        String                 printText;
	private        String                 printQrCodeUrl;
	private static OrderAction            mInstance;

	private StringConfig type;

	public static OrderAction getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new OrderAction();
		}
		return mInstance;
	}

	//初始化拿到打印尾部数据
	private void initPrintData(Context context)
	{
		printText = PrintUtil.getPrintBottomText(context, PrintUtil.PRINT_MODEL_TAKEOUT);
		printQrCodeUrl = PrintUtil.getPrintBottomImageUrl(context, PrintUtil.PRINT_MODEL_TAKEOUT);
		if (!TextUtils.isEmpty(printQrCodeUrl))
		{
			ImageSize imageSize = new ImageSize(100, 100);
			mPrintImageNonViewAware = new PrintImageNonViewAware(printQrCodeUrl, imageSize, ViewScaleType.FIT_INSIDE);
			mPrintImageNonViewAware.show(new PrintImageNonViewAware.DefaultImageLoadingListener()
			{
				public void finish(boolean isSuccess)
				{
					if (isSuccess)
					{
						barCodeBm = mPrintImageNonViewAware.getImageBitmap();
					}
				}
			});
		}

	}


	public void getPrintContent(final Order order, final Context context, int times4Shop, int times4Deliver, boolean isConfirmed)
	{
		initPrintData(context);
		type = order.getOrderForm();
		if (type != null)
		{
			switch (type.getId())
			{
				case PRINT_TYPE_BAIDU:
				case PRINT_TYPE_MT:
				case PRINT_TYPE_ELM:
					startActivitySDK(getBaiduPrinterQueue(order, context, times4Shop, times4Deliver, isConfirmed), context);
					break;
				default:
					startActivitySDK(getBaiduPrinterQueue(order, context, times4Shop, times4Deliver, isConfirmed), context);
					break;
			}
		}
	}

	private void startActivitySDK(Queue<PrintUtils.PrintData> printDataQueue, Context context)
	{
		Intent intent = PrintUtils.bindIntent(printDataQueue);
		if (intent != null)
		{
			try
			{
				context.startActivity(intent);
			}
			catch (Exception e)
			{
				Toast.makeText(context, "掌贝打印SDK未安装", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(context, "error: data is null", Toast.LENGTH_LONG).show();
		}
	}


	// 组装打印数据(百度外卖)，默认两张，一张本店留存，一张配送员持有，是同步出现
	//新的需求，本店留存和配送员持有均相互独立，动态配置
	//如果是重印，则不要头， isConfirmed为真表示不要头，添加重印字样
	public Queue<PrintUtils.PrintData> getBaiduPrinterQueue(Order order, final Context context, int times4Shop, int times4Deliver, boolean isConfirmed)
	{
		Queue<PrintUtils.PrintData> printer = new LinkedList<>();

		for (int i = 0; i < times4Shop; i++)
		{
			printer.addAll(getBaiduPrintData(order, context, isConfirmed, 0));
		}

		for (int i = 0; i < times4Deliver; i++)
		{
			//			printer.addAll(getBaiduPrintData(order, context, isConfirmed, 1));
		}

		return printer;
	}

	/**
	 * @param order
	 * @param context
	 * @param isConfirmed 决定是否重印 false第一次，true需要重印
	 * @param times       0本店留存 1 配送员用
	 * @return
	 */
	public Queue<PrintUtils.PrintData> getBaiduPrintData(Order order, final Context context, boolean isConfirmed, int times)
	{
		boolean up5Point2 = "5.2".compareTo(Build.VERSION.RELEASE) <= 0;

		if (HardwareUtils.ZB_Z5_V1.equals(HardwareUtils.getProductModel()) && up5Point2)
		{
			return getBaiduPrintData4Z5(order, context, isConfirmed, times);
		}

		//		if (HardwareUtils.P2000L.equals(HardwareUtils.getProductModel()))
		//		{
		//			return getBaiduPrintData4Z5(order, context, isConfirmed, times);
		//		}

		Queue<PrintUtils.PrintData> printer = new LinkedList<>();
		try
		{
			int textLine = PrintUtils.getLineForTextSize(PrintUtils.TEXTFONT_BIG);
			PrintUtils.PrintData printData;
			StringBuffer sb = new StringBuffer();

			switch (times)
			{
				case 0:
					sb.append(PrintUtils.print("本店存留")).append("\n");
					break;
				case 1:
					sb.append(PrintUtils.print("给配送员专用")).append("\n");
					break;
			}

			//获取订单来源
			int serialNumber = order.getOrderBd();//获取订单来源的序列号
			String serialNumberStr = serialNumber > TakeOutUtils.SERIALNUM_NULL ? "#" + serialNumber : "";
			sb.append(PrintUtils.printCenter(serialNumberStr + order.getOrderForm().getValue(), textLine));

			if (order.getPayType() != null)
			{
				sb.append(PrintUtils.printCenter("[" + order.getPayType().getValue() + "]", textLine));
			}

			printData = PrintUtils.getByte(sb.toString(), PrintUtils.TEXTFONT_BIG);
			printer.add(printData);

			sb = new StringBuffer();
			if (isConfirmed)
			{
				sb.append(PrintUtils.printCenter("【重印】", PrintUtils.getLineForTextSize(PrintUtils.TEXTFONT_SMALL)));
			}
			sb.append(printStarLine(PrintUtils.LINE_2));
			String arriveTime;
			if (!TextUtils.isEmpty(order.getArriveTime()))
			{
				arriveTime = order.getArriveTime();
				//add book order in 2016.07.29
				sb.append("【本单为预订单】\n");
			}
			else
			{
				arriveTime = "立即送达";
			}
			sb.append(PrintUtils.print("期望送达时间：" + arriveTime));
			if (!TextUtils.isEmpty(order.getReceipt()))
			{
				sb.append(PrintUtils.print("发票：" + order.getReceipt()));
			}

			//就餐人数
			if (!TextUtils.isEmpty(order.getDinnersNum()) && !"0".equals(order.getDinnersNum()))
			{
				sb.append(PrintUtils.print("就餐人数: " + order.getDinnersNum()));
			}

			if (!TextUtils.isEmpty(order.getRemark()))
			{
				sb.append(PrintUtils.print("备注: " + order.getRemark()));
			}

			//骑手电话
			if (!TextUtils.isEmpty(order.getDeliveryPhone()))
			{
				sb.append(PrintUtils.print("骑手电话: " + order.getDeliveryPhone()));
			}

			sb.append(printStarLine(PrintUtils.LINE_2));
			//订单号
			sb.append(PrintUtils.printColumns(10, "订单编号:"));
			//			sb.append(PrintUtils.print(order.getPlatformOrderId().trim()));
			sb.append(PrintUtils.print(order.getId().trim()));

			//第三方平台单号
			if (!TextUtils.isEmpty(order.getPlatformOrderId()))
			{
				sb.append(PrintUtils.printColumns(10, "第三方单号:"));
				sb.append(PrintUtils.print(order.getPlatformOrderId()));
			}

			String time = order.getOrderTime();
			if (time != null)
			{
				sb.append(PrintUtils.printColumns(10, "下单时间:"));
				sb.append(PrintUtils.print(time));
			}
			sb.append(printStarLine(PrintUtils.LINE_2));
			printData = PrintUtils.getByte(sb.toString());
			printer.add(printData);

			//订单中商品列表
			sb = new StringBuffer();
			List<ProductDetail> product = order.getProductDetails();
			sb.append(PrintUtils.printColumns(17, "菜品名称"));
			sb.append(PrintUtils.printColumns(7, "数量"));
			sb.append(PrintUtils.printColumns(5, "金额")).append("\n");
			sb.append(PrintUtils.printDottedLine(PrintUtils.LINE_2));
			if (product != null)
			{
				StringConfig orderForm = order.getOrderForm();
				if (orderForm != null && "4".equals(orderForm.getId()))
				{
					//微信外卖
					for (ProductDetail products : product)
					{
						//调整菜品数量的偏移量
						StringBuffer stringBuffer = new StringBuffer();
						String num = products.getNum();
						length = calcStringLength(num);//计算拿到字符的长度
						for (int y = 0; y < (lengthSetForZ5 - length); y++)
						{
							stringBuffer.append(" ");
						}
						stringBuffer.append(num);
						Log.i("dishprint1:", products.getName() + "------" + stringBuffer.toString());

						String singleAmount = PriceUtils.longToCurrency(products.getSingleAmount()).replace("￥", "");

						if (products.getPriceSource() == 999)
						{
							sb.append(PrintUtils.printThree("(赠)" + products.getName(), stringBuffer.toString(), singleAmount, 18, 6, 8, PrintUtils.LINE_2));
						}
						else
						{
							sb.append(PrintUtils.printThree(products.getName(), stringBuffer.toString(), singleAmount, 18, 6, 8, PrintUtils.LINE_2));
						}
						//32->24 18->9

						List<Attach> attach = products.getElemeGarnish();
						if (attach != null)
						{
							for (Attach attach1 : attach)
							{
								StringBuffer stringBuffer1 = new StringBuffer();
								String num_dish = products.getNum();
								length = calcStringLength(num_dish);
								for (int z = 0; z < (lengthSet - length); z++)
								{
									stringBuffer1.append(" ");
								}
								stringBuffer1.append(num_dish);

								String elmSingleAmount = PriceUtils.longToCurrency(attach1.getSingleAmount()).replace("￥", "");

								//套餐子成员
								sb.append(PrintUtils.printThree("--" + attach1.getName(), stringBuffer1.toString(), elmSingleAmount, 18, 6, 8, PrintUtils.LINE_2));

								//套餐成员属性
								List<Attach.CheckAttr> checkAttrs = attach1.getCheckAttrs();
								String arrStr = "";

								if (checkAttrs != null && checkAttrs.size() > 0)
								{
									for (Attach.CheckAttr arr : checkAttrs)
									{
										arrStr += arr.getPname() + "(" + arr.getName() + ")";
										sb.append(PrintUtils.print("  " + arrStr));
									}
								}
							}
						}
					}
				}
				else
				{
					for (int i = 0; i < order.getProductDetails().size(); i++)
					{
						ProductDetail products = order.getProductDetails().get(i);
						//调整菜品数量的偏移量
						StringBuffer stringBuffer = new StringBuffer();
						String num = products.getNum();
						length = calcStringLength(num);//计算拿到字符的长度
						for (int y = 0; y < (lengthSet - length); y++)
						{
							stringBuffer.append(" ");
						}
						stringBuffer.append(num);
						Log.i("dishprint1:", products.getName() + "------" + stringBuffer.toString());

						String property = products.getProperty();
						String specs;
						if (TextUtils.isEmpty(property))
						{
							specs = TextUtils.isEmpty(products.getSpecs()) ? "" : "（" + products.getSpecs() + "）";
						}
						else
						{
							specs = TextUtils.isEmpty(products.getSpecs()) ? "" : "（" + products.getSpecs() + "," + property + "）";
						}

						String singleAmount = PriceUtils.longToCurrency(products.getSingleAmount()).replace("￥", "");

						sb.append(PrintUtils.printThree(products.getName() + specs, stringBuffer.toString(), singleAmount, 18, 6, 8, PrintUtils.LINE_2));

						List<Attach> attach = products.getElemeGarnish();
						if (attach != null)
						{
							for (Attach attach1 : attach)
							{
								StringBuffer stringBuffer1 = new StringBuffer();
								String num_dish = products.getNum();
								length = calcStringLength(num_dish);
								for (int z = 0; z < (lengthSet - length); z++)
								{
									stringBuffer1.append(" ");
								}
								stringBuffer1.append(num_dish);
								Log.i("dishprint2:", attach1.getName() + "--" + stringBuffer1.toString());
								String elmSingleAmount = PriceUtils.longToCurrency(attach1.getSingleAmount()).replace("￥", "");
								sb.append(PrintUtils.printThree(attach1.getName(), stringBuffer1.toString(), elmSingleAmount, 18, 6, 8, PrintUtils.LINE_2));
							}
						}
					}
				}
			}
			if (order.getMealFee() > 0)
			{
				String mealFee = PriceUtils.longToCurrency(order.getMealFee()).replace("￥", "");
				sb.append(PrintUtils.printSide("餐盒费", mealFee, PrintUtils.LINE_2));
			}
			if (order.getDistributionCharge() > 0)
			{
				String distributionCharge = PriceUtils.longToCurrency(order.getDistributionCharge()).replace("￥", "");
				sb.append(PrintUtils.printSide("配 送 费: ", distributionCharge, PrintUtils.LINE_2));
			}
			// 虚线
			sb.append(PrintUtils.printDottedLine(PrintUtils.LINE_2));
			//价格
			String amount = PriceUtils.longToCurrency(order.getAmount()).replace("￥", "");
			sb.append(PrintUtils.printSide("订单总价 ", amount, PrintUtils.LINE_2));

			//显示优惠券(列表)
			if (order.getTakeoutDiscount() != null && order.getTakeoutDiscount().size() > 0)
			{
				List<StringConfig> discounts = order.getTakeoutDiscount();
				if (discounts != null)
				{
					for (StringConfig d : discounts)
					{
						sb.append(PrintUtils.printSide(d.getId(), "-" + Long.parseLong(d.getValue()) / 100.0, PrintUtils.LINE_2));
					}
				}
			}
			//显示优惠总金额
			if (order.getDiscountPrice() > 0)
			{
				String discpuntPrice = PriceUtils.longToCurrency(order.getDiscountPrice()).replace("￥", "");
				sb.append(PrintUtils.printSide("优惠金额", discpuntPrice, PrintUtils.LINE_2));
			}

			//赠品
			dealZengpin(sb, order, "z3");

			//			String actPayment = PriceUtils.longToCurrency(order.getActPayment()).replace("￥", "");
			//			sb.append(PrintUtils.printSide("用户实付", actPayment, PrintUtils.LINE_2));
			//菜品部分调整为2倍高字体
			printData = PrintUtils.getByte(sb.toString(), PrintUtils.TEXTSIZE_DOUBLE_HEIGHT);
			printer.add(printData);

			//			sb = new StringBuffer();
			//			String actAmount = PriceUtils.longToCurrency(order.getActAmount()).replace("￥", "");
			//			sb.append(PrintUtils.printSide("商户实收", actAmount, textLine));
			//			printData = PrintUtils.getByte(sb.toString(), PrintUtils.TEXTSIZE_DOUBLE_WIDTH_HEIGHT);
			//			printer.add(printData);


			//本店留存，关注商户实收
			if (times == 0)
			{
				sb = new StringBuffer();
				String actAmount = PriceUtils.longToCurrency(order.getActAmount()).replace("￥", "");
				sb.append(PrintUtils.printSide("商户实收", actAmount, textLine));
				printData = PrintUtils.getByte(sb.toString(), PrintUtils.TEXTSIZE_DOUBLE_WIDTH_HEIGHT);
				printer.add(printData);
			}


			//配送专用，关注用户实付
			if (times == 1)
			{
				sb = new StringBuffer();
				String actPayment = PriceUtils.longToCurrency(order.getActPayment()).replace("￥", "");
				sb.append(PrintUtils.printSide("用户实付", actPayment, textLine));
				printData = PrintUtils.getByte(sb.toString(), PrintUtils.TEXTSIZE_DOUBLE_WIDTH_HEIGHT);
				printer.add(printData);
			}

			//订单详情
			sb = new StringBuffer();
			String source = order.getPerson();
			String sourceAddress = order.getAddress();
			String mobile = order.getPhone();
			sb.append(printStarLine(PrintUtils.LINE_2));
			if (!TextUtils.isEmpty(source))
			{
				sb.append(PrintUtils.print("姓名: " + source));
			}
			if (!TextUtils.isEmpty(mobile))
			{
				sb.append(PrintUtils.print("电话: " + mobile));
			}
			if (!TextUtils.isEmpty(sourceAddress))
			{
				sb.append(PrintUtils.print("地址: " + sourceAddress));
			}
			sb.append(printStarLine(PrintUtils.LINE_2));

			sb.append(PrintUtils.print(RegisterUtils.getInfo(context, RegisterUtils.SHOP_NAME_KEY_NEW, "")));
			printData = PrintUtils.getByte(sb.toString());
			printer.add(printData);

			switch (times)
			{
				case 0:
					sb = new StringBuffer();
					sb.append(PrintUtils.print(" \n\n\n\n"));
					printData = PrintUtils.getByte(sb.toString());
					printer.add(printData);
					break;
				case 1:
					getBottomPrintData(printer);
					break;
				default:
					getBottomPrintData(printer);
					break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return printer;
	}

	public void getBottomPrintData(Queue<PrintUtils.PrintData> printer)
	{
		try
		{
			PrintUtils.PrintData printData;
			StringBuffer sb;
			if (!TextUtils.isEmpty(printText) || !TextUtils.isEmpty(printQrCodeUrl))
			{
				// 打印尾部图片
				if (barCodeBm != null)
				{
					Log.e("BITMAP", barCodeBm + "图片已下载完毕");
					printData = PrintUtils.getByte(barCodeBm);
					printer.add(printData);
				}
				//				打印尾部文字
				if (!TextUtils.isEmpty(printText))
				{
					sb = new StringBuffer();
					sb.append(PrintUtils.print(" \n"));
					sb.append(PrintUtils.printCenter(printText, PrintUtils.LINE_2));
					sb.append(PrintUtils.print(" \n"));
					printData = PrintUtils.getByte(sb.toString());
					printer.add(printData);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public static String printStarLine(int line)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < line; i++)
		{
			sb.append("*");
		}
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * 计算文字的长度
	 *
	 * @param msg 要打印的内容
	 * @return 填充过的String
	 */
	private static int calcStringLength(String msg)
	{
		int len = msg.length();
		Pattern p = Pattern.compile(CHINESE_REGEX);
		Matcher m = p.matcher(msg);
		while (m.find())
		{
			len++;
		}
		return len;
	}

	public Queue<PrintUtils.PrintData> getBaiduPrintData4Z5(Order order, final Context context, boolean isConfirmed, int times)
	{
		Queue<PrintUtils.PrintData> printer = new LinkedList<>();
		try
		{
			int textLine = PrintUtils.getLineForTextSize(PrintUtils.TEXTFONT_BIG);
			PrintUtils.PrintData printData;
			StringBuffer sb = new StringBuffer();
			switch (times)
			{
				case 0:
					sb.append(PrintUtils.print("本店存留")).append("\n");
					break;
				case 1:
					sb.append(PrintUtils.print("给配送员专用")).append("\n");
					break;
			}

			//获取订单来源
			int serialNumber = order.getOrderBd();//获取订单来源的序列号
			String serialNumberStr = serialNumber > TakeOutUtils.SERIALNUM_NULL ? "#" + serialNumber : "";
			sb.append(PrintUtils.printCenter(serialNumberStr + order.getOrderForm().getValue(), textLine));

			if (order.getPayType() != null)
			{
				sb.append(PrintUtils.printCenter("[" + order.getPayType().getValue() + "]", textLine));
			}

			printData = PrintUtils.getByte(sb.toString(), PrintUtils.TEXTFONT_BIG);
			printer.add(printData);

			sb = new StringBuffer();
			if (isConfirmed)
			{
				sb.append(PrintUtils.printCenter("【重印】", PrintUtils.getLineForTextSize(PrintUtils.TEXTFONT_SMALL)));
			}
			sb.append(printStarLine(PrintUtils.LINE_2));
			String arriveTime;
			if (!TextUtils.isEmpty(order.getArriveTime()))
			{
				arriveTime = order.getArriveTime();
				//add book order in 2016.07.29
				sb.append("【本单为预订单】\n");
			}
			else
			{
				arriveTime = "立即送达";
			}
			sb.append(PrintUtils.print("期望送达时间：" + arriveTime));
			if (!TextUtils.isEmpty(order.getReceipt()))
			{
				sb.append(PrintUtils.print("发票：" + order.getReceipt()));
			}

			//就餐人数
			if (!TextUtils.isEmpty(order.getDinnersNum()) && !"0".equals(order.getDinnersNum()))
			{
				sb.append(PrintUtils.print("就餐人数: " + order.getDinnersNum()));
			}

			if (!TextUtils.isEmpty(order.getRemark()))
			{
				sb.append(PrintUtils.print("备注: " + order.getRemark()));
			}

			//骑手电话
			if (!TextUtils.isEmpty(order.getDeliveryPhone()))
			{
				sb.append(PrintUtils.print("骑手电话: " + order.getDeliveryPhone()));
			}

			sb.append(printStarLine(PrintUtils.LINE_2));
			//订单号
			sb.append(PrintUtils.printColumns(10, "订单编号:"));
			//			sb.append(PrintUtils.print(order.getPlatformOrderId().trim()));
			sb.append(PrintUtils.print(order.getId().trim()));

			//第三方平台单号
			if (!TextUtils.isEmpty(order.getPlatformOrderId()))
			{
				sb.append(PrintUtils.printColumns(10, "第三方单号:"));
				sb.append(PrintUtils.print(order.getPlatformOrderId()));
			}

			String time = order.getOrderTime();
			if (time != null)
			{
				sb.append(PrintUtils.printColumns(10, "下单时间:"));
				sb.append(PrintUtils.print(time));
			}
			sb.append(printStarLine(PrintUtils.LINE_2));
			printData = PrintUtils.getByte(sb.toString());
			printer.add(printData);

			//订单中商品列表
			sb = new StringBuffer();
			List<ProductDetail> product = order.getProductDetails();
			sb.append(PrintUtils.printColumns(12, "菜品名称"));
			sb.append(PrintUtils.printColumns(5, "数量"));
			sb.append(PrintUtils.printColumns(4, "金额")).append("\n");

			sb.append(PrintUtils.printDottedLine(PrintUtils.LINE_3_VERIFONE));
			if (product != null)
			{
				StringConfig orderForm = order.getOrderForm();
				if (orderForm != null && "4".equals(orderForm.getId()))
				{
					//微信外卖
					for (ProductDetail products : product)
					{
						//调整菜品数量的偏移量
						StringBuffer stringBuffer = new StringBuffer();
						String num = products.getNum();
						length = calcStringLength(num);//计算拿到字符的长度
						for (int y = 0; y < (lengthSetForZ5 - length); y++)
						{
							stringBuffer.append(" ");
						}
						stringBuffer.append(num);

						String singleAmount = PriceUtils.longToCurrency(products.getSingleAmount()).replace("￥", "");

						if (products.getPriceSource() == 999)
						{
							sb.append(PrintUtils.printThree("(赠)" + products.getName(), stringBuffer.toString(), singleAmount, 11, 5, 8, PrintUtils.LINE_3_VERIFONE));
						}
						else
						{
							sb.append(PrintUtils.printThree(products.getName(), stringBuffer.toString(), singleAmount, 11, 5, 8, PrintUtils.LINE_3_VERIFONE));
						}
						//32->24 18->9

						List<Attach> attach = products.getElemeGarnish();
						if (attach != null)
						{
							for (Attach attach1 : attach)
							{
								StringBuffer stringBuffer1 = new StringBuffer();
								String num_dish = products.getNum();
								length = calcStringLength(num_dish);
								for (int z = 0; z < (lengthSet - length); z++)
								{
									stringBuffer1.append(" ");
								}
								stringBuffer1.append(num_dish);

								String elmSingleAmount = PriceUtils.longToCurrency(attach1.getSingleAmount()).replace("￥", "");

								//套餐子成员
								sb.append(PrintUtils.printThree("--" + attach1.getName(), stringBuffer1.toString(), elmSingleAmount, 11, 5, 8, PrintUtils.LINE_3_VERIFONE));

								//套餐成员属性
								List<Attach.CheckAttr> checkAttrs = attach1.getCheckAttrs();
								String arrStr = "";

								if (checkAttrs != null && checkAttrs.size() > 0)
								{
									for (Attach.CheckAttr arr : checkAttrs)
									{
										arrStr += arr.getPname() + "(" + arr.getName() + ")";
										sb.append(PrintUtils.print("  " + arrStr));
									}
								}
							}
						}
					}
				}
				else
				{
					for (int i = 0; i < order.getProductDetails().size(); i++)
					{
						ProductDetail products = order.getProductDetails().get(i);
						//调整菜品数量的偏移量
						StringBuffer stringBuffer = new StringBuffer();
						String num = products.getNum();
						length = calcStringLength(num);//计算拿到字符的长度
						for (int y = 0; y < (lengthSetForZ5 - length); y++)
						{
							stringBuffer.append(" ");
						}
						stringBuffer.append(num);
						Log.i("dishprint1:", products.getName() + "------" + stringBuffer.toString());

						/**
						 * 商品增加属性打印，与规格同一括号中
						 */
						String property = products.getProperty();
						String specs;
						if (TextUtils.isEmpty(property))
						{
							specs = TextUtils.isEmpty(products.getSpecs()) ? "" : "（" + products.getSpecs() + "）";
						}
						else
						{
							specs = TextUtils.isEmpty(products.getSpecs()) ? "" : "（" + products.getSpecs() + "," + property + "）";
						}

						String singleAmount = PriceUtils.longToCurrency(products.getSingleAmount()).replace("￥", "");

						sb.append(PrintUtils.printThree(products.getName() + specs, stringBuffer.toString(), singleAmount, 11, 5, 8, PrintUtils.LINE_3_VERIFONE));
						//32->24 18->9

						List<Attach> attach = products.getElemeGarnish();
						if (attach != null)
						{
							for (Attach attach1 : attach)
							{
								StringBuffer stringBuffer1 = new StringBuffer();
								String num_dish = products.getNum();
								length = calcStringLength(num_dish);
								for (int z = 0; z < (lengthSet - length); z++)
								{
									stringBuffer1.append(" ");
								}
								stringBuffer1.append(num_dish);
								Log.i("dishprint2:", attach1.getName() + "--" + stringBuffer1.toString());
								String elmSingleAmount = PriceUtils.longToCurrency(attach1.getSingleAmount()).replace("￥", "");

								sb.append(PrintUtils.printThree(attach1.getName(), stringBuffer1.toString(), elmSingleAmount, 12, 4, 8, PrintUtils.LINE_3_VERIFONE));
							}
						}
					}
				}
			}
			if (order.getMealFee() > 0)
			{
				String mealFee = PriceUtils.longToCurrency(order.getMealFee()).replace("￥", "");
				sb.append(PrintUtils.printSide("餐盒费", mealFee, PrintUtils.LINE_3_VERIFONE));
			}
			if (order.getDistributionCharge() > 0)
			{
				String distributionCharge = PriceUtils.longToCurrency(order.getDistributionCharge()).replace("￥", "");
				sb.append(PrintUtils.printSide("配 送 费: ", distributionCharge, PrintUtils.LINE_3_VERIFONE));
			}
			// 虚线
			sb.append(PrintUtils.printDottedLine(PrintUtils.LINE_3_VERIFONE));
			//价格
			String amount = PriceUtils.longToCurrency(order.getAmount()).replace("￥", "");
			sb.append(PrintUtils.printSide("订单总价 ", amount, PrintUtils.LINE_3_VERIFONE));

			//显示优惠券(列表)
			if (order.getTakeoutDiscount() != null && order.getTakeoutDiscount().size() > 0)
			{
				List<StringConfig> discounts = order.getTakeoutDiscount();
				if (discounts != null)
				{
					for (StringConfig d : discounts)
					{
						sb.append(PrintUtils.printSide(d.getId(), "-" + Long.parseLong(d.getValue()) / 100.0, PrintUtils.LINE_3_VERIFONE));
					}
				}
			}

			//显示优惠总金额
			if (order.getDiscountPrice() > 0)
			{
				String discpuntPrice = PriceUtils.longToCurrency(order.getDiscountPrice()).replace("￥", "");
				sb.append(PrintUtils.printSide("优惠金额", discpuntPrice, PrintUtils.LINE_3_VERIFONE));
			}

			//赠品
			dealZengpin(sb, order, "z5");

			//			String actPayment = PriceUtils.longToCurrency(order.getActPayment()).replace("￥", "");
			//			sb.append(PrintUtils.printSide("用户实付", actPayment, PrintUtils.LINE_2));
			//菜品部分调整为2倍高字体
			printData = PrintUtils.getByte(sb.toString(), PrintUtils.TEXTSIZE_DOUBLE_HEIGHT);
			printer.add(printData);

			//本店留存，关注商户实收
			if (times == 0)
			{
				sb = new StringBuffer();
				String actAmount = PriceUtils.longToCurrency(order.getActAmount()).replace("￥", "");
				sb.append(PrintUtils.printSide("商户实收", actAmount, textLine));
				printData = PrintUtils.getByte(sb.toString(), PrintUtils.TEXTSIZE_DOUBLE_WIDTH_HEIGHT);
				printer.add(printData);
			}


			//配送专用，关注用户实付
			if (times == 1)
			{
				sb = new StringBuffer();
				String actPayment = PriceUtils.longToCurrency(order.getActPayment()).replace("￥", "");
				sb.append(PrintUtils.printSide("用户实付", actPayment, textLine));
				printData = PrintUtils.getByte(sb.toString(), PrintUtils.TEXTSIZE_DOUBLE_WIDTH_HEIGHT);
				printer.add(printData);
			}

			//订单详情
			sb = new StringBuffer();
			String source = order.getPerson();
			String sourceAddress = order.getAddress();
			String mobile = order.getPhone();
			sb.append(printStarLine(PrintUtils.LINE_2));
			if (!TextUtils.isEmpty(source))
			{
				sb.append(PrintUtils.print("姓名: " + source));
			}
			if (!TextUtils.isEmpty(mobile))
			{
				sb.append(PrintUtils.print("电话: " + mobile));
			}
			if (!TextUtils.isEmpty(sourceAddress))
			{
				sb.append(PrintUtils.print("地址: " + sourceAddress));
			}
			sb.append(printStarLine(PrintUtils.LINE_2));

			sb.append(PrintUtils.print(RegisterUtils.getInfo(context, RegisterUtils.SHOP_NAME_KEY_NEW, "")));
			printData = PrintUtils.getByte(sb.toString());
			printer.add(printData);

			switch (times)
			{
				case 0:
					sb = new StringBuffer();
					sb.append(PrintUtils.print(" \n\n\n\n"));
					printData = PrintUtils.getByte(sb.toString());
					printer.add(printData);
					break;
				case 1:
					getBottomPrintData(printer);
					break;
				default:
					getBottomPrintData(printer);
					break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return printer;
	}

	/**
	 * 处理赠品，z3，z5同一个格式
	 *
	 * @param sb
	 * @param order
	 */
	private void dealZengpin(StringBuffer sb, Order order, String device)
	{
		List<String> zengpinList = order.getZengpinList();
		//			if (zengpinList == null)
		//			{
		//				zengpinList = new ArrayList<>();
		//				zengpinList.add("机多宝机多宝机多宝机多宝");
		//				zengpinList.add("王老吉王老吉");
		//				zengpinList.add("和其正和其正和其正和其正和其正和其正和其正和其正正和其正和其正和其正xxxxxxx");
		//			}

		if (zengpinList != null && zengpinList.size() > 0)
		{
			if ("z3".equals(device))
			{
				sb.append(PrintUtils.printDottedLine(PrintUtils.LINE_2));
			}
			else
			{
				//z5 or plus
				sb.append(PrintUtils.printDottedLine(PrintUtils.LINE_3_VERIFONE));
			}

			sb.append(PrintUtils.print("赠品："));

			boolean x = zengpinList.size() > 1;
			for (int i = 0; i < zengpinList.size(); i++)
			{
				sb.append(PrintUtils.print((x ? ((i + 1) + ".") : "") + zengpinList.get(i)));
			}
			if ("z3".equals(device))
			{
				sb.append(PrintUtils.printDottedLine(PrintUtils.LINE_2));
			}
			else
			{
				//z5 or plus
				sb.append(PrintUtils.printDottedLine(PrintUtils.LINE_3_VERIFONE));
			}
		}
	}
}
