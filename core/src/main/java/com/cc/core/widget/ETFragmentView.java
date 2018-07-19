package com.cc.core.widget;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public interface ETFragmentView {

    void setInputMethodManager(InputMethodManager im);

    InputMethodManager getInputMethodManager();

    @NonNull
    View getView();
}
