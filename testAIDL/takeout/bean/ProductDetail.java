package com.yunnex.canteen.takeout.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/23.
 */
public class ProductDetail
{

	private String       name;
	private String       num;
	private long         singleAmount;
	private List<Attach> elemeGarnish;//菜品浇头，后面理解为组下面的附属
	private String       specs;//规格名称 add 2016.06.30
	private String       property;
	private int          priceSource;// "priceSource": 1, //标识是不是赠菜，赠菜为999

	private int                        id;//groupId
	private int                        packageGoodsId;//子id
	private int                        isPackage;//判断是否是group
	private List<Attach.CheckAttr> checkAttrs;

	public List<Attach.CheckAttr> getCheckAttrs()
	{
		return checkAttrs;
	}

	public void setCheckAttrs(List<Attach.CheckAttr> checkAttrs)
	{
		this.checkAttrs = checkAttrs;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getPackageGoodsId()
	{
		return packageGoodsId;
	}

	public void setPackageGoodsId(int packageGoodsId)
	{
		this.packageGoodsId = packageGoodsId;
	}

	public int getIsPackage()
	{
		return isPackage;
	}

	public void setIsPackage(int isPackage)
	{
		this.isPackage = isPackage;
	}

	public int getPriceSource()
	{
		return priceSource;
	}

	public void setPriceSource(int priceSource)
	{
		this.priceSource = priceSource;
	}

	public String getProperty()
	{
		return property;
	}

	public void setProperty(String property)
	{
		this.property = property;
	}

	public String getSpecs()
	{
		return specs;
	}

	public void setSpecs(String specs)
	{
		this.specs = specs;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNum()
	{
		return num;
	}

	public void setNum(String num)
	{
		this.num = num;
	}

	public long getSingleAmount()
	{
		return singleAmount;
	}

	public void setSingleAmount(long singleAmount)
	{
		this.singleAmount = singleAmount;
	}

	public List<Attach> getElemeGarnish()
	{
		return elemeGarnish;
	}

	public void setElemeGarnish(List<Attach> elemeGarnish)
	{
		this.elemeGarnish = elemeGarnish;
	}

	@Override
	public String toString()
	{
		final StringBuffer sb = new StringBuffer("ProductDetail{");
		sb.append("name='").append(name).append('\'');
		sb.append(", num='").append(num).append('\'');
		sb.append(", singleAmount=").append(singleAmount);
		sb.append(", elemeGarnish=").append(elemeGarnish);
		sb.append(", specs='").append(specs).append('\'');
		sb.append(", property='").append(property).append('\'');
		sb.append(", priceSource=").append(priceSource);
		sb.append(", id=").append(id);
		sb.append(", packageGoodsId=").append(packageGoodsId);
		sb.append(", isPackage=").append(isPackage);
		sb.append('}');
		return sb.toString();
	}
}
