package com.cc.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cc.core.R;

public class WebProgressView extends View {

    private Paint paint;
    private int progress;

    public WebProgressView(Context context) {
        this(context, null);
    }

    public WebProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.WebProgressView, defStyleAttr, 0);
        int color = a.getColor(R.styleable.WebProgressView_progress_color, Color.GRAY);
        a.recycle();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (progress >= 100) {
            if (getVisibility() != View.GONE) {
                setVisibility(View.GONE);
            }
        } else {
            if (getVisibility() != View.VISIBLE) {
                setVisibility(View.VISIBLE);
            }
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float measuredWidth = getMeasuredWidth();
        float strokeWidth = getMeasuredHeight();
        float oldStrokeWidth = paint.getStrokeWidth();
        if (strokeWidth != oldStrokeWidth) {
            paint.setStrokeWidth(strokeWidth);
        }

        float factor = progress;
        float end = measuredWidth * factor/100f;
        canvas.drawLine(0, 0, end, 0, paint);
    }
}
