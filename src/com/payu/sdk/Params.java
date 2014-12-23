package com.payu.sdk;

import java.util.HashMap;

/**
 * Created by amit on 23/6/14.
 */
public class Params extends HashMap<String, String> {

    public String get(String key) {
        String value = super.get(key);
        if (value == null) {
            value = "";
        }
        return value;
//        return "";
    }

}
