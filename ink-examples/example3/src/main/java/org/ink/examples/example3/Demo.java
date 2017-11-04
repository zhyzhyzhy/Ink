package org.ink.examples.example3;

import org.ink.Ink;

/**
 * Created by zhuyichen on 2017/7/12.
 */

public class Demo {

//    //return the package where you bean is
//    @Override
//    public String[] beansPackage() {
//        return new String[]{"example2.org.ink.examples.example3.service", "example2.org.ink.examples.example3.web"};
//    }
//
//    //if enable jwt authentication
//    //you need to override these to method
//    @Override
//    public boolean anthenticationOpen() {
//        return true;
//    }
//
//    //return a key for jwt encode
//    @Override
//    public String SecurityKey() {
//        return "loveee";
//    }


    public static void main(String[] args) {
        new Ink(8091, Demo.class).start();
    }

}
