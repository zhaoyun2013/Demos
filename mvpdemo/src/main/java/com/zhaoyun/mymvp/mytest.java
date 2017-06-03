package com.zhaoyun.mymvp;

import java.io.UnsupportedEncodingException;

/**
 * Created by zhaoyun on 17-3-3.
 */

public class mytest {
    public static void main(String[] args) {

        String result = "æ°é»1";
        try {
            String str =new String(result.getBytes("iso-8859-1"), "utf-8");
            System.out.print(str);
            str =new String(result.getBytes("gbk"), "utf-8");
            System.out.print(str);
            str =new String(result.getBytes(), "utf-8");
            System.out.print(str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
