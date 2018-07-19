package com.cc.core.web;


import android.app.Activity;
import android.webkit.JavascriptInterface;

public class CommonJsInterface {

    private final Activity activity;

    public CommonJsInterface(Activity activity) {
        this.activity = activity;
    }

    /**
     * 日租页，点自营公寓
     *
     * @param array 房源信息 第1个参数为Id,第2个参数为图片地址
     */
    @JavascriptInterface
    public void openPremises(String[] array) {

        if (array != null && array.length >= 2) {

            int hotelId = -1;
            try {
                hotelId = Integer.valueOf(array[0]);
            } catch (Exception ignore) {
            }

            if (hotelId > 0) {
            }
        }
    }


}
