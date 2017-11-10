package com.yunnex.canteen.takeout.test;

import com.yunnex.canteen.takeout.bean.DishCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lion on 16/1/5.
 */
public class TestFactory
{
	public static List<DishCategory> getDishCategoryList()
	{
		DishCategory dishCategory = new DishCategory();
		dishCategory.setId(1);
		dishCategory.setName("拉面类");
		dishCategory.setGoodsNum(10) ;
		dishCategory.setSoldOutNum(5);

		DishCategory dishCategory_1 = new DishCategory();
		dishCategory_1.setId(2);
		dishCategory_1.setName("炒饭类");
		dishCategory_1.setGoodsNum(10);
		dishCategory_1.setSoldOutNum(0);

		List<DishCategory> dishCategoryList = new ArrayList<>();
		dishCategoryList.add(dishCategory);
		dishCategoryList.add(dishCategory_1);

		return dishCategoryList;
	}

//	public static List<Dish> getDishList()
//	{
//		Dish dish = new Dish();
//		dish.setId("0");
//		dish.setName("骨拉面大份");
//		dish.setIcon("http://images.banma.com/v0/app-feed/soft/icons/_bigIcon1333181096637.jpg");
//		dish.setPrice(3500);
//		dish.setSaleStatus(new StringConfig("2", "剩12份"));
//
//		Dish dish_1 = new Dish();
//		dish_1.setId("1");
//		dish_1.setName("猪扒拉面");
//		dish_1.setIcon("http://pic5.nipic.com/20100104/2701840_091050026202_2.jpg");
//		dish_1.setPrice(3500);
//		dish_1.setSaleStatus(new StringConfig("0", "已售完"));
//
//		Dish dish_2 = new Dish();
//		dish_2.setId("2");
//		dish_2.setName("拉不拉面");
//		dish_2.setPrice(100);
//		dish_2.setSaleStatus(new StringConfig("2", null));
//
//		Dish dish_3 = new Dish();
//		dish_3.setId("3");
//		dish_3.setName("查克拉面");
//		dish_3.setPrice(20000);
//		dish_3.setSaleStatus(new StringConfig("1", "已下架"));
//
//		List<Dish> dishList = new ArrayList<>();
//		dishList.add(dish);
//		dishList.add(dish_1);
//		dishList.add(dish_2);
//		dishList.add(dish_3);
//
//		return dishList;
//	}
}
