package com.yunnex.canteen.takeout.mng;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunnex.canteen.R;

/**
 * Created by lion on 16/1/6.
 */
public class TakeOutUIUtils
{
	private static ImageLoader         mImageLoader;
	private static DisplayImageOptions mDisplayImageOptions;

	/**
	 * 使用ImageLoader显示图片资源
	 *
	 * @param context
	 * @param imageUri  ：图片资源地址
	 * @param imageView ：图片UI控件
	 * @return 图片资源显示是否成功
	 */
	public static boolean displayImageView(Context context, String imageUri, ImageView imageView)
	{
		if (mImageLoader == null)
		{
			mImageLoader = ImageLoader.getInstance();
		}
		if (mDisplayImageOptions == null)
		{
			mDisplayImageOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.def_dish_loading).showImageForEmptyUri(R.drawable.def_dish_loading).showImageOnFail(R.drawable.def_dish_loading).cacheInMemory(true).cacheOnDisc(true).considerExifParams(true).bitmapConfig(Bitmap.Config.ARGB_8888).build();
		}

		if (TextUtils.isEmpty(imageUri) != true && imageView != null)
		{
			Uri uri = Uri.parse(imageUri);
			if (uri != null && uri.getScheme() != null)
			{
				mImageLoader.displayImage(imageUri, imageView, mDisplayImageOptions);
				return true;
			}
			else
			{
				imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.def_dish_loading));
				return false;
			}
		}
		else
		{
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.def_dish_loading));
			return false;
		}
	}
}
