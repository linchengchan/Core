package com.cc.core.activity;

import android.support.v4.app.Fragment;

public interface ISingleActivity {

    void addFragmentToBackStack(Fragment fragment);

    void popFragment(Fragment f);
}
