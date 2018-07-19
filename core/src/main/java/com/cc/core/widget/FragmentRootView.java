package com.cc.core.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.lvjia.core.Core;

final class FragmentRootView {

    private static void setBackgroundColor(View view) {
        Drawable background = view.getBackground();
        if (background == null) {
            view.setBackgroundColor(Color.WHITE);
        }
    }

    private static void setOnClickListener(View view) {
        view.setOnClickListener(v -> {
        });
    }


    private static void setOnHideKeyboardClickListener(ETFragmentView eTFragmentView) {
        View view = eTFragmentView.getView();
        view.setOnClickListener(v -> hideKeyboard(eTFragmentView, eTFragmentView.getView().getWindowToken()));
    }

    public static void initFragmentView(View view) {
        setBackgroundColor(view);
        setOnClickListener(view);
    }

    public static void initETFragmentView(ETFragmentView etFragmentView) {
        setBackgroundColor(etFragmentView.getView());
        setOnHideKeyboardClickListener(etFragmentView);
    }

    private static void hideKeyboard(ETFragmentView ETFragmentView, IBinder token) {
        InputMethodManager im = ETFragmentView.getInputMethodManager();
        if (token != null) {
            if (im == null) {
                im = (InputMethodManager) Core.sApp.getSystemService(Context.INPUT_METHOD_SERVICE);
                ETFragmentView.setInputMethodManager(im);
            }
            if (im != null) {
                im.hideSoftInputFromWindow(token, 0);
            }
        }
    }

}
