package top.starrysea.rina.util.string;

public class StringUtil {

    public static boolean isNotBlank(String str) {
        return str != null && !"".equals(str);
    }

    public static boolean isBlank(String str) {
        return !isNotBlank(str);
    }
}
