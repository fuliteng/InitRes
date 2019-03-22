package com.sino.initres;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: fuliteng
 * Date:  2019-1-11
 * Email:fuliteng@aliyun.com
 * Description:  Res
 * Logical relationship :
 */
public class Res {


    public final static List<String> list;
    static {
        list=new ArrayList<>();
        list.add("drawable");
        list.add("mipmap");
        list.add("string");
        list.add("color");
    }

    public final static class Type {
         public static   int DRAWABLES = 1;
         public static   int MIPMAPS = 2;
         public static   int STRINGS = 3;
         public static   int COLORS = 4;
         public static   int DEFAULTS=0;
    }

    public final static class Result {
         public static    int SUCC = 1;
         public static    int FAIL = 2;
    }

    public static interface ResInitListener {
           void onResInitResult(int type, int result, String msg);

           void onLoadDone();
    }

}
