package org.formidable.guoscript.util;

public class ColorUtil {

    public static String translateColor(String str){
        if (str == null){
            return null;
        }
        return str.replace("&", "§");
    }

}
