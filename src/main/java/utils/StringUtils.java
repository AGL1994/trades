package utils;

public class StringUtils {

    /**
     * 判断字符串似乎否为空
     * "" null return true
     * @param str
     * @return
     */
    public static boolean isBlank(String str){
        if(str == null || str.length() < 1 || "".equals(str)){
            return true;
        }
        return false;
    }
}
