package com.cc.core.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

public class FragmentCoordinatorLayout extends CoordinatorLayout {

    public FragmentCoordinatorLayout(@NonNull Context context) {
        this(context, null);
    }

    public FragmentCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FragmentCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        FragmentRootView.initFragmentView(this);
    }
}
