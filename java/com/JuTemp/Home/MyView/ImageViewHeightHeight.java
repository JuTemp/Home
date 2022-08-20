package com.JuTemp.Home.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class ImageViewHeightHeight extends androidx.appcompat.widget.AppCompatImageView
{
	public ImageViewHeightHeight(Context context)
	{
        super(context);
    }

    public ImageViewHeightHeight(Context context, AttributeSet attrs)
	{
        super(context, attrs);
    }

    public ImageViewHeightHeight(Context context, AttributeSet attrs, int defStyleAttr)
	{
        super(context, attrs, defStyleAttr);
    }
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

}
