package com.cc.core.action;

public class Action {

    private static int sEventGenerator;

    public static final String NO_NEED_KEY = "no_need_key";

    public final int event;

    public Action(int event) {
        this.event = event;
    }

    public String getKey() {
        return NO_NEED_KEY;
    }

    protected static int getEventGenerator() {
        sEventGenerator++;
        return sEventGenerator;
    }

}
