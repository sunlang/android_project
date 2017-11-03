package com.sun.myeventbuslib;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by sungongyan on 2017/8/7.
 * qq 379366152
 */

/**
 * UI组件交互通信
 */
public class Functions
{
	private static HashMap<String, Function> mFunctionHashMap = new HashMap<>();
	private static Functions                 instance         = new Functions();

	private Functions()
	{
	}

	public static Functions getInstance()
	{
		//		return mSingleton.get();
		return instance;
	}

	//	private static Singleton<Functions> mSingleton = new Singleton<Functions>()
	//	{
	//		@Override
	//		protected Functions create()
	//		{
	//			return new Functions();
	//		}
	//	};

	/**
	 * 对接口的抽象
	 */
	public static abstract class Function
	{
		private String mFunctionName;

		public String getFunctionName()
		{
			return mFunctionName;
		}

		private Function(String functionName)
		{
			mFunctionName = functionName;
		}
	}


	/**
	 * no params no result
	 */
	public static abstract class FunctionNPNR extends Function
	{

		public FunctionNPNR(String functionName)
		{
			super(functionName);
		}

		public abstract void function();
	}

	/**
	 * no params with result
	 *
	 * @param <Result>
	 */
	public static abstract class FunctionNPWR<Result> extends Function
	{

		public FunctionNPWR(String functionName)
		{
			super(functionName);
		}

		public abstract Result function();
	}


	/**
	 * no result with params
	 */
	public static abstract class FunctionNRWP extends Function
	{

		public FunctionNRWP(String functionName)
		{
			super(functionName);
		}

		public abstract void function(Map<String, String> params);
	}

	/**
	 * with result with params
	 */
	public static abstract class FunctionWRWP<Result> extends Function
	{

		public FunctionWRWP(String functionName)
		{
			super(functionName);
		}

		public abstract Result function(Map<String, String> params);
	}

	/**
	 * regist function in place where your business need
	 *
	 * @param f
	 */
	public void registFunc(Function f)
	{
		mFunctionHashMap.put(f.getFunctionName(), f);
	}

	/**
	 * unregist function
	 *
	 * @param fctionName
	 */
	public void unRegistFunc(String fctionName)
	{
		mFunctionHashMap.remove(fctionName);
	}

	/**
	 * trigger the function you have registed in everywhere you want
	 *
	 * @param funcName
	 * @param clazz
	 * @param params
	 * @param <T>
	 * @return
	 */
	public <T> T invokeFunc(String funcName, Class<T> clazz, Map<String, String> params)
	{
		Function function = mFunctionHashMap.get(funcName);
		if (function == null)
		{
			throw new IllegalArgumentException("no such function:" + funcName + ",have you regist " + "this function somewhere first?");
		}

		if (clazz != null)
		{
			//1 no params with result
			if (params == null)
			{
				if (function instanceof FunctionNPWR)
				{
					return clazz.cast(((FunctionNPWR) function).function());
				}
				else
				{
					return throwException(function, FunctionNPWR.class);
				}
			}
			else
			{
				//2 with params with result
				if (function instanceof FunctionWRWP)
				{
					return clazz.cast(((FunctionWRWP) function).function(params));
				}
				else
				{
					return throwException(function, FunctionWRWP.class);
				}

			}
		}
		else
		{
			//1 no params no result
			if (params == null)
			{
				if (function instanceof FunctionNPNR)
				{
					((FunctionNPNR) function).function();
				}
				else
				{
					return throwException(function, FunctionNPNR.class);
				}
			}
			else
			{
				//2 with params no result
				((FunctionNRWP) function).function(params);
			}
		}
		return null;
	}

	/**
	 * 当调用参数不匹配时，抛出异常
	 *
	 * @param function
	 * @param functionClazz
	 * @param <T>
	 * @return
	 */
	private <T> T throwException(Function function, Class<? extends Function> functionClazz)
	{
		throw new ClassCastException("the function " + function.getFunctionName() + " when regist is not " + "match:" + functionClazz);
	}
}
