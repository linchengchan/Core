package com.cc.core.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ETFragmentCoordinatorLayout extends CoordinatorLayout implements ETFragmentView {

    private InputMethodManager im;

    public ETFragmentCoordinatorLayout(@NonNull Context context) {
        this(context, null);
    }

    public ETFragmentCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ETFragmentCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        FragmentRootView.initETFragmentView(this);
    }

    @Override
    public void setInputMethodManager(InputMethodManager im) {
        this.im = im;
    }

    @Override
    public InputMethodManager getInputMethodManager() {
        return im;
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }
}
