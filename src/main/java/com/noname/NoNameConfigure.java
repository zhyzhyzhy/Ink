package com.noname;

/**
 * Created by zhuyichen on 2017/7/12.
 */

public abstract class NoNameConfigure {

    public abstract String[] beansPackage();

    //jwt的key
    public String SecurityKey() {
        return "Love";
    }

    public boolean anthenticationOpen() {
        return false;
    }
}
