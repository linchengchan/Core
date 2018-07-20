package com.cc.core.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * recycler view简单分割线的装饰器
 */

public class SimpleItemDecoration extends RecyclerView.ItemDecoration {

    private static final boolean DEBUG = false;
    private static final String TAG = SimpleItemDecoration.class.getSimpleName();

    private int mDrawDistance;
    private int mDrawColor;

    private int startDrawColor;
    private int startDrawDistance;

    private int endDrawColor;
    private int endDrawDistance;

    private Rect mPadding;
    private Rect mPaddingColor;
    private int mOrientation = -1;


    private static final int DIVIDER_MIDDLE = 0;
    private static final int DIVIDER_START = DIVIDER_MIDDLE + 1;
    private static final int DIVIDER_END = DIVIDER_START + 1;
    private static final int DIVIDER_ALL = DIVIDER_END + 1;

    private int mDividerType = DIVIDER_MIDDLE;

    public SimpleItemDecoration(int drawDistance, @ColorInt int drawColor, Rect padding, Rect paddingColor) {
        if (padding != null) {
            if (padding.left == 0 &&
                    padding.right == 0 &&
                    padding.top == 0 &&
                    padding.bottom == 0) {
                padding = null;
            } else if (paddingColor == null) {
                paddingColor = new Rect(drawColor, drawColor, drawColor, drawColor);
            }
        }
        if (drawDistance < 0) {
            drawDistance = 0;
        }
        mDrawDistance = drawDistance;
        mDrawColor = drawColor;
        mPadding = padding;
        mPaddingColor = paddingColor;
    }

    public SimpleItemDecoration(int drawDistance, @ColorInt int drawColor) {
        this(drawDistance, drawColor, null, null);
    }

    public void setAllDividerType(int startDistance, int endDrawDistance, int startColor, int endDrawColor) {
        mDividerType = DIVIDER_ALL;
        startDrawDistance = startDistance;
        this.endDrawDistance = endDrawDistance;
        startDrawColor = startColor;
        this.endDrawColor = endDrawColor;
    }

    public void setStartDividerType(int startDistance, int startColor) {
        mDividerType = DIVIDER_START;
        startDrawDistance = startDistance;
        startDrawColor = startColor;
    }

    public void setEndDividerType(int endDrawDistance, int endDrawColor) {
        mDividerType = DIVIDER_END;
        this.endDrawDistance = endDrawDistance;
        this.endDrawColor = endDrawColor;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (mDrawColor == Color.TRANSPARENT) {
            return;
        }
        if (mOrientation == RecyclerView.VERTICAL) {
            if (mDividerType == DIVIDER_START) {
                verticalDrawStart(c, parent, state);
            } else if (mDividerType == DIVIDER_END) {
                verticalDrawEnd(c, parent, state);
            } else if (mDividerType == DIVIDER_ALL) {
                verticalDrawAll(c, parent, state);
            } else {
                verticalDrawMiddle(c, parent, state);
            }

        } else {
            if (mDividerType == DIVIDER_START) {
                horizontalDrawStart(c, parent, state);
            } else if (mDividerType == DIVIDER_END) {
                horizontalDrawEnd(c, parent, state);
            } else if (mDividerType == DIVIDER_ALL) {
                horizontalDrawAll(c, parent, state);
            } else {
                horizontalDrawMiddle(c, parent, state);
            }

        }
    }

    private void verticalDrawMiddle(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (DEBUG) {
            Log.d(TAG, "verticalDrawMiddle()-> ");
        }
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int count = parent.getChildCount();

        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();

            int position = lp.getViewAdapterPosition();
            if (position == 0) {
                continue;
            }
            verticalRealDrawStart(c, child, left, right);
        }
    }

    private void verticalDrawStart(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (DEBUG) {
            Log.d(TAG, "verticalDrawStart()-> ");
        }
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int count = parent.getChildCount();

        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            verticalRealDrawStart(c, child, left, right);
        }
    }


    private void verticalDrawEnd(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (DEBUG) {
            Log.d(TAG, "verticalDrawEnd()-> ");
        }
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int count = parent.getChildCount();
        int itemCount = parent.getAdapter().getItemCount();


        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            verticalRealDrawEnd(c, child, itemCount, left, right);
        }
    }

    private void verticalDrawAll(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (DEBUG) {
            Log.d(TAG, "verticalDrawEnd()-> ");
        }
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int count = parent.getChildCount();
        int itemCount = parent.getAdapter().getItemCount();

        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();

            int position = lp.getViewAdapterPosition();

            verticalRealDrawStart(c, child, left, right);
            boolean lastPosition = position == itemCount - 1;
            if (lastPosition) {
                verticalRealDrawEnd(c, child, itemCount, left, right);
            }

        }
    }

    private void verticalRealDrawStart(Canvas c, View child, int left, int right) {

        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();

        int position = lp.getViewAdapterPosition();

        int drawDistance = position == 0 ? startDrawDistance : mDrawDistance;

        int top = child.getTop() - lp.topMargin - drawDistance;

        int afterLeft = left;
        int afterRight = right;
        if (position != 0 && mPadding != null) {
            afterLeft += mPadding.left;
            afterRight -= mPadding.right;
            //draw  padding left
            realDraw(c, left, top, afterLeft, top + drawDistance, mPaddingColor.left);

            //draw padding right
            realDraw(c, afterRight, top, right, top + drawDistance, mPaddingColor.right);
        }

        int drawColor = position == 0 ? startDrawColor : mDrawColor;
        if (drawColor == Color.TRANSPARENT) {
            return;
        }
        //draw middle
        realDraw(c, afterLeft, top, afterRight, top + drawDistance, drawColor);
    }

    private void verticalRealDrawEnd(Canvas c, View child, int itemCount, int left, int right) {

        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();

        int position = lp.getViewAdapterPosition();

        boolean lastPosition = position == itemCount - 1;

        int drawDistance = lastPosition ? endDrawDistance : mDrawDistance;

        int top = child.getBottom() + lp.bottomMargin;

        int afterLeft = left;
        int afterRight = right;
        if (!lastPosition && mPadding != null) {
            afterLeft += mPadding.left;
            afterRight -= mPadding.right;
            //draw  padding left
            realDraw(c, left, top, afterLeft, top + drawDistance, mPaddingColor.left);

            //draw padding right
            realDraw(c, afterRight, top, right, top + drawDistance, mPaddingColor.right);
        }

        int drawColor = lastPosition ? endDrawColor : mDrawColor;
        if (drawColor == Color.TRANSPARENT) {
            return;
        }
        //draw middle
        realDraw(c, afterLeft, top, afterRight, top + drawDistance, drawColor);
    }

    private void realDraw(Canvas c, int left, int top, int right, int bottom, int color) {
        if (color == Color.TRANSPARENT) {
            return;
        }
        c.save();
        c.clipRect(left, top, right, bottom);
        c.drawColor(color);
        c.restore();
    }


    private void horizontalDrawMiddle(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = lp.getViewAdapterPosition();
            if (position == 0) {
                continue;
            }
            horizontalRealDrawStart(c, child, top, bottom);
        }
    }

    private void horizontalDrawStart(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            horizontalRealDrawStart(c, child, top, bottom);
        }
    }

    private void horizontalDrawEnd(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int count = parent.getChildCount();
        int itemCount = parent.getAdapter().getItemCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            horizontalRealDrawEnd(c, child, itemCount, top, bottom);
        }
    }

    private void horizontalDrawAll(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int count = parent.getChildCount();
        int itemCount = parent.getAdapter().getItemCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = lp.getViewAdapterPosition();
            horizontalRealDrawStart(c, child, top, bottom);
            boolean lastPosition = position == itemCount - 1;
            if (lastPosition) {
                horizontalRealDrawEnd(c, child, itemCount, top, bottom);
            }
        }
    }

    private void horizontalRealDrawStart(Canvas c, View child, int top, int bottom) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();

        int position = lp.getViewAdapterPosition();

        int drawDistance = position == 0 ? startDrawDistance : mDrawDistance;
        int left = child.getLeft() - lp.leftMargin - drawDistance;

        int afterTop = top;
        int afterBottom = bottom;

        if (position != 0 && mPadding != null) {
            afterTop += mPadding.top;
            afterBottom -= mPadding.bottom;
            //draw  padding top
            realDraw(c, left, top, left + drawDistance, afterTop, mPaddingColor.top);
            //draw padding bottom
            realDraw(c, left, afterBottom, left + drawDistance, bottom, mPaddingColor.bottom);
        }

        int drawColor = position == 0 ? startDrawColor : mDrawColor;
        if (drawColor == Color.TRANSPARENT) {
            return;
        }
        //draw middle
        realDraw(c, left, afterTop, left + drawDistance, afterBottom, drawColor);
    }

    private void horizontalRealDrawEnd(Canvas c, View child, int itemCount, int top, int bottom) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();

        int position = lp.getViewAdapterPosition();

        boolean lastPosition = position == itemCount - 1;
        int drawDistance = lastPosition ? endDrawDistance : mDrawDistance;
        int left = child.getRight() + lp.rightMargin;

        int afterTop = top;
        int afterBottom = bottom;

        if (!lastPosition && mPadding != null) {
            afterTop += mPadding.top;
            afterBottom -= mPadding.bottom;
            //draw  padding top
            realDraw(c, left, top, left + drawDistance, afterTop, mPaddingColor.top);
            //draw padding bottom
            realDraw(c, left, afterBottom, left + drawDistance, bottom, mPaddingColor.bottom);
        }

        int drawColor = lastPosition ? endDrawColor : mDrawColor;
        if (drawColor == Color.TRANSPARENT) {
            return;
        }
        //draw middle
        realDraw(c, left, afterTop, left + drawDistance, afterBottom, drawColor);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        validLayoutManager(parent);

        int position = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        if (DEBUG) {
            Log.d(TAG, "getItemOffsets()-> position: " + position);
        }

        if (mDividerType == DIVIDER_ALL) {
            itemOffsetAll(outRect, position, itemCount);
        } else if (mDividerType == DIVIDER_START) {
            itemOffsetStart(outRect, position);
        } else if (mDividerType == DIVIDER_END) {
            itemOffsetEnd(outRect, position, itemCount);
        } else {
            itemOffsetMiddle(outRect, position);
        }

    }


    private void itemOffsetMiddle(Rect outRect, int position) {
        if (position == 0) {
            return;
        }

        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        if (mOrientation == RecyclerView.VERTICAL) {
            top = mDrawDistance;
        } else {
            left = mDrawDistance;
        }
        outRect.set(left, top, right, bottom);
    }

    private void itemOffsetStart(Rect outRect, int position) {

        int distance = position == 0 ? startDrawDistance : mDrawDistance;

        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        if (mOrientation == RecyclerView.VERTICAL) {
            top = distance;
        } else {
            left = distance;
        }

        outRect.set(left, top, right, bottom);
    }

    private void itemOffsetEnd(Rect outRect, int position, int itemCount) {

        int distance = position == itemCount - 1 ? endDrawDistance : mDrawDistance;

        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        if (mOrientation == RecyclerView.VERTICAL) {
            bottom = distance;
        } else {
            right = distance;
        }

        outRect.set(left, top, right, bottom);
    }

    private void itemOffsetAll(Rect outRect, int position, int itemCount) {

        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        if (position == 0) {
            if (mOrientation == RecyclerView.VERTICAL) {
                top = startDrawDistance;
                bottom = mDrawDistance;
            } else {
                left = startDrawDistance;
                right = mDrawDistance;
            }

        } else if (position == itemCount - 1) {
            if (mOrientation == RecyclerView.VERTICAL) {
                bottom = endDrawDistance;
            } else {
                right = endDrawDistance;
            }
        } else {
            if (mOrientation == RecyclerView.VERTICAL) {
                bottom = mDrawDistance;
            } else {
                right = mDrawDistance;
            }
        }
        outRect.set(left, top, right, bottom);
    }

    private void validLayoutManager(RecyclerView recyclerView) {

        if (mOrientation == LinearLayoutManager.VERTICAL
                || mOrientation == LinearLayoutManager.HORIZONTAL) {
            return;
        }

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            mOrientation = linearLayoutManager.getOrientation();
        } else {
            throw new IllegalStateException("don't use this item decoration except LinearLayoutManager");
        }
    }

}
