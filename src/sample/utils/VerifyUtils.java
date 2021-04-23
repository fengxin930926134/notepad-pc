package sample.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具类
 */
public class VerifyUtils {

    /** 是时间 */
    public static final String IS_TIME = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
    /** 是周期 */
    public static final String IS_CYCLE = "^[0-9]{1,}$";

    public static boolean verify(String content, String regular) {
        if (content == null) {
            return false;
        }
        Pattern pat = Pattern.compile(regular);
        Matcher mat = pat.matcher(content);
        return mat.matches();
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
        System.out.println(verify(null, IS_CYCLE));
    }
}
