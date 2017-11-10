package com.yunnex.canteen.takeout.bean;

/**
 * Created by lion on 16/1/5.
 */

/**
 * 菜品类
 */
public class Dish
{
	//菜品售卖状态
	public static final int DISH_STATUS_SOLD_OUT = 1;//已售完
	public static final int DISH_STATUS_SALE     = 2;//出售中

	private int  id; // 菜品Id
	private String  name;  // 该菜品名
	private String  icon;  // 该菜品图片
	private long    price;  // 该菜品价格
	private boolean outOfStock;        // true : 已下架，false：未下架
	private int     soldStatus;        // 该菜品销售状态  （1 已售完，2正常出售中）
	private String  saleStatusLabel;        // 对应不同的 菜品销售状态，返回不同的文字标签
	private String  storeNum; // 库存数
	private String  categoryId;  // 该菜品分类Id
	private String  categoryName;  // 该菜品分类名

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public long getPrice()
	{
		return price;
	}

	public void setPrice(long price)
	{
		this.price = price;
	}

	public boolean getOutOfStock()
	{
		return outOfStock;
	}

	public void setOutOfStock(boolean outOfStock)
	{
		this.outOfStock = outOfStock;
	}

	public int getSoldStatus()
	{
		return soldStatus;
	}

	public void setSoldStatus(int soldStatus)
	{
		this.soldStatus = soldStatus;
	}

	public String getSaleStatusLabel()
	{
		return saleStatusLabel;
	}

	public void setSaleStatusLabel(String saleStatusLabel)
	{
		this.saleStatusLabel = saleStatusLabel;
	}

	public String getStoreNum()
	{
		return storeNum;
	}

	public void setStoreNum(String storeNum)
	{
		this.storeNum = storeNum;
	}

	public String getCategoryId()
	{
		return categoryId;
	}

	public void setCategoryId(String categoryId)
	{
		this.categoryId = categoryId;
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}
}
