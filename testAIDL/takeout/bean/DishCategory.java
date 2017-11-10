package com.yunnex.canteen.takeout.bean;

/**
 * Created by lion on 16/1/5.
 */

/**
 * 菜品类别
 */
public class DishCategory
{
	private int id;  //菜品分类Id
	private String name;  //菜品分类名
	private int    goodsNum;          //该分类下的菜品数量（包括上架的和下架的）
	private int    soldOutNum;        //该分类下的已售完菜品数量

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

	public int getGoodsNum()
	{
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum)
	{
		this.goodsNum = goodsNum;
	}

	public int getSoldOutNum()
	{
		return soldOutNum;
	}

	public void setSoldOutNum(int soldOutNum)
	{
		this.soldOutNum = soldOutNum;
	}
}
