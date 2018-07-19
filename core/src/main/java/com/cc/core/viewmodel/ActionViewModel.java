package com.cc.core.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.lvjia.core.action.Action;
import com.lvjia.core.action.ActionLiveData;

public class ActionViewModel extends ViewModel {

    private static ArrayMap<String, ActionLiveData> sArrayMap;

    public ActionLiveData register(String clzName, String key) {

        ArrayMap<String, ActionLiveData> map = getMap();
        ActionLiveData liveData = map.get(clzName);
        if (liveData == null) {
            liveData = new ActionLiveData();
            map.put(clzName, liveData);
        }
        liveData.addKey(key);
        return liveData;
    }

    public void sendAction(String clzName, @NonNull Action action) {
        sendAction(getMap().get(clzName), action);
    }

    private void sendAction(ActionLiveData liveData, @NonNull Action action) {
        if (liveData != null) {
            if (liveData.hit(action)) {
                liveData.setValue(action);
            }
        }
    }

    private void sendAction(String[] clzNameList, @NonNull Action action) {
        if (clzNameList == null || clzNameList.length == 0) {
            return;
        }

        ArrayMap<String, ActionLiveData> map = getMap();
        for (String clzName : clzNameList) {
            ActionLiveData liveData = map.get(clzName);
            sendAction(liveData, action);
        }
    }

    private static ArrayMap<String, ActionLiveData> getMap() {
        if (sArrayMap == null) {
            sArrayMap = new ArrayMap<>();
        }
        return sArrayMap;
    }

}
