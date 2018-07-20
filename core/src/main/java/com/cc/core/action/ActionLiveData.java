package com.cc.core.action;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;


import com.cc.core.Core;

import java.util.ArrayList;
import java.util.List;

public class ActionLiveData extends LiveData<Action> {

    private List<String> actionKeys;

    @Override
    public void postValue(Action value) {
        super.postValue(value);
    }

    @Override
    public void setValue(Action value) {
        super.setValue(value);
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<Action> observer) {
        super.observe(owner, new AutoClearObserverWrap(this, observer));
    }

    private static class AutoClearObserverWrap implements Observer<Action> {

        private final Observer<Action> realObserver;
        private final ActionLiveData actionLiveData;

        private AutoClearObserverWrap(ActionLiveData actionLiveData, @NonNull Observer<Action> realObserver) {
            this.actionLiveData = actionLiveData;
            this.realObserver = realObserver;
        }

        @Override
        public boolean equals(Object obj) {
            if (Core.debug()) {
                Log.d(getClass().getSimpleName(), "equals()-> ");
            }
            if (obj == this) {
                return true;
            }
            if (obj != null && obj instanceof AutoClearObserverWrap) {
                AutoClearObserverWrap wrap = (AutoClearObserverWrap) obj;
                return wrap.realObserver.equals(realObserver);
            }
            return false;
        }

        @Override
        public void onChanged(@Nullable Action action) {
            realObserver.onChanged(action);
            //一个事件发出去被接受后应该自动清空
            if (actionLiveData.getValue() != null) {
                if (Core.debug()) {
                    Log.d(getClass().getSimpleName(), "onChanged()-> clear value");
                }
                actionLiveData.setValue(null);
            }
        }
    }

    public void addKey(String key) {

        if (TextUtils.isEmpty(key)) {
            return;
        }
        if (actionKeys == null) {
            actionKeys = new ArrayList<>();
        }
        if (!actionKeys.contains(key)) {
            actionKeys.add(key);
        }
    }

    public boolean hit(@NonNull Action action) {

        String key = action.getKey();

        if (Action.NO_NEED_KEY.equals(key)) {
            return true;
        }
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        if (actionKeys == null || actionKeys.isEmpty()) {
            return false;
        }
        return actionKeys.remove(key);
    }

}
