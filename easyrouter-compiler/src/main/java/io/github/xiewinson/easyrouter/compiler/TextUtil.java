package io.github.xiewinson.easyrouter.compiler;

/**
 * Created by winson on 2017/11/30.
 */

public class TextUtil {

    public static String path2ClassName(String path) {
        return path2Name(path, true);
    }

    public static String path2MethodName(String path) {
        return path2Name(path, false);
    }

    private static String path2Name(String path, boolean firstCharUpperCase) {
        if (isEmpty(path)) {
            return null;
        }
        String[] strArr;
        if (path.contains("/")) {
            strArr = path.split("/");
        } else {
            strArr = new String[]{path};
        }
        if (strArr.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : strArr) {
            if (!isEmpty(s)) {
                if (first) {
                    sb.append(firstCharUpperCase ? upperCaseFirstChar(s) : lowerCaseFirstChar(s));
                    first = false;
                } else {
                    sb.append(upperCaseFirstChar(s));

                }
            }
        }
        String result = sb.toString();
        return isEmpty(result) ? null : result;
    }

    public static boolean isEmpty(String str) {
        return str != null && str.length() == 0;
    }


    public static String upperCaseFirstChar(String str) {
        char[] ch = str.toCharArray();
        ch[0] = Character.toUpperCase(ch[0]);
        return new String(ch);
    }

    public static String lowerCaseFirstChar(String str) {
        char[] ch = str.toCharArray();
        ch[0] = Character.toLowerCase(ch[0]);
        return new String(ch);
    }

}
