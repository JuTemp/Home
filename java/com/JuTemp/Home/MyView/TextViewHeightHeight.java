package com.JuTemp.Home.MyView;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class TextViewHeightHeight extends AppCompatTextView
{
	public TextViewHeightHeight(Context context)
	{
        super(context);
    }

    public TextViewHeightHeight(Context context, AttributeSet attrs)
	{
        super(context, attrs);
    }

    public TextViewHeightHeight(Context context, AttributeSet attrs, int defStyleAttr)
	{
        super(context, attrs, defStyleAttr);
    }
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

}
