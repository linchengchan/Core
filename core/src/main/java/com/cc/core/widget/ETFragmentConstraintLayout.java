package com.cc.core.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ETFragmentConstraintLayout extends ConstraintLayout implements ETFragmentView {

    private InputMethodManager im;

    public ETFragmentConstraintLayout(Context context) {
        this(context, null);
    }

    public ETFragmentConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ETFragmentConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
