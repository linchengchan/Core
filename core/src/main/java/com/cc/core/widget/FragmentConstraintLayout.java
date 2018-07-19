package com.cc.core.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

public class FragmentConstraintLayout extends ConstraintLayout {

    public FragmentConstraintLayout(Context context) {
        this(context, null);
    }

    public FragmentConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FragmentConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        FragmentRootView.initFragmentView(this);
    }
}
